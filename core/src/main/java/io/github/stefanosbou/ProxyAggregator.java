package io.github.stefanosbou;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.github.stefanosbou.util.DbHelper;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.stefanosbou.verticles.ProxyAggregatorVerticle.EB_PROXY_AGGREGATOR_SERVICE_ADDRESS;

public class ProxyAggregator {

   private static ProxyAggregator proxyAggregator = null;
   private final JsonObject config;
   private final Vertx vertx;
   private final ProxyAggregatorService proxyAggregatorService;
   private final SQLClient client;

   private ProxyAggregator(Vertx vertx, JsonObject config, Boolean enableCrawling) {
      this.vertx = vertx;
      this.config = config;
      this.proxyAggregatorService = ProxyAggregatorService.createProxy(vertx, EB_PROXY_AGGREGATOR_SERVICE_ADDRESS);
      this.client = DbHelper.client(vertx, config);
      Proxy.setVertx(vertx, DbHelper.client(vertx, config)); // init static vertx instance inner job

      if (enableCrawling) {
         deployCrawlers(this.config.getJsonArray("websites"));
      }
   }

   public static ProxyAggregator init(Vertx vertx) {
      JsonArray array = new JsonArray(Arrays.asList(Arrays.stream(ProxySites.values()).map(Enum::name)
         .collect(Collectors.toList()).toArray()));

      if (proxyAggregator == null) {
         proxyAggregator = new ProxyAggregator(vertx, new JsonObject().put("websites", array), true);
      }
      return proxyAggregator;
   }

   public static ProxyAggregator init(Vertx vertx, JsonObject config, Boolean enableCrawling) {
      if (proxyAggregator == null) {
         proxyAggregator = new ProxyAggregator(vertx, config, enableCrawling);
      }
      return proxyAggregator;
   }

   private void deployCrawlers(JsonArray array) {
      for (Object crawlerName : array) {
         deployCrawlerVerticle((String)crawlerName, true);
      }
   }

   private void deployCrawlerVerticle(String crawlerName, boolean isWorker) {
      try {
         vertx.deployVerticle(ProxySites.lookup(crawlerName.toUpperCase()).getaClass(), new DeploymentOptions().setWorker(isWorker), r0 -> {
            if (r0.succeeded()) {
               System.out.println("Successfully deployed crawler for " + ProxySites.valueOf(crawlerName.toUpperCase()).getUrl());
            } else {
               // error
               System.out.println(r0.cause().getMessage());
            }
         });
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }

   public Future<Proxy> getProxy() {
      Future<Proxy> future = Future.future();
      proxyAggregatorService.getProxy(r -> {
         if (r.succeeded()) {
//            System.out.println(r.result().toJson());
            future.complete(r.result());
         } else {
            future.fail(r.cause());
         }
      });
      return future;
   }

   public Future<List<Proxy>> getProxies() {
      Future<List<Proxy>> future = Future.future();
      proxyAggregatorService.getProxies(r -> {
         if (r.succeeded()) {
//            System.out.println(r.result().toJson());
            future.complete(r.result());
         } else {
            future.fail(r.cause());
         }
      });
      return future;
   }

//
//   public Future<Optional<Proxy>> getProxy(long id) {
//         Future<Optional<Proxy>> future = Future.future();
//         proxyAggregatorService.getProxy(id, r -> {
//            if (r.succeeded()) {
//               future.complete(Optional.ofNullable(r.result()));
//            } else {
//               future.fail(r.cause());
//            }
//         });
//         return future;
//   }
}


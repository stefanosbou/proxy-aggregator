package io.github.stefanosbou;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.github.stefanosbou.util.DbHelper;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

import static io.github.stefanosbou.verticles.ProxyAggregatorVerticle.EB_PROXY_AGGREGATOR_SERVICE_ADDRESS;

public class ProxyAggregator {

   private final JsonObject config;
   private final Vertx vertx;
   private final ProxyAggregatorService proxyAggregatorService;
   private final SQLClient client;

   public ProxyAggregator(Vertx vertx, JsonObject config) {
      this.vertx = vertx;
      this.config = config;
      this.proxyAggregatorService = ProxyAggregatorService.createProxy(vertx, EB_PROXY_AGGREGATOR_SERVICE_ADDRESS);
      this.client = DbHelper.client(vertx, config);
      Proxy.setVertx(vertx, DbHelper.client(vertx, config)); // init static vertx instance inner job
   }

   private void deployCrawlerVerticle(String crawlerName, boolean isWorker) {
      vertx.deployVerticle(ProxySites.valueOf(crawlerName.toUpperCase()).getaClass(), new DeploymentOptions().setWorker(isWorker), r0 -> {
         if (r0.succeeded()) {
            System.out.println("Successfully deployed crawler for " + ProxySites.valueOf(crawlerName.toUpperCase()).getUrl());
         } else {
            // error
         }
      });
   }

   public Future<Proxy> getProxy() {
      Future<Proxy> future = Future.future();
      proxyAggregatorService.getProxy(r -> {
         if (r.succeeded()) {
            System.out.println(r.result().toJson());
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


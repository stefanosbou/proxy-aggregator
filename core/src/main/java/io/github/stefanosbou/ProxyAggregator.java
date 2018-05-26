package io.github.stefanosbou;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.github.stefanosbou.util.DbHelper;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

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
      vertx.deployVerticle(ProxySites.SSLPROXIES.getaClass(), new DeploymentOptions().setWorker(isWorker), r0 -> {
         if (r0.succeeded()) {

         }
      });
   }

}

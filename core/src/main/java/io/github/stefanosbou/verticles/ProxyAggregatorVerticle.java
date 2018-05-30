package io.github.stefanosbou.verticles;

import io.github.stefanosbou.service.ProxyAggregatorService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;


public class ProxyAggregatorVerticle extends AbstractVerticle {

   public static final String EB_PROXY_AGGREGATOR_SERVICE_ADDRESS = "vertx.proxy-aggregator.service";

   private JsonObject config;
   private ProxyAggregatorService proxyAggregatorService;

   @Override
   public void start(Future<Void> future) {
      this.config = config();

      // Create the client object
      this.proxyAggregatorService = ProxyAggregatorService.create(vertx, config);

      // create SQL client (+ init schema?)
//      RedisClient redisClient = RedisHelper.client(vertx, config);
//      redisClient.ping(pr -> { // test connection
//         if (pr.succeeded()) {
//            logger.info("Kue Verticle is running...");
//
//            // register job service
//            ProxyHelper.registerService(JobService.class, vertx, jobService, EB_JOB_SERVICE_ADDRESS);
//
//            future.complete();
//         } else {
//            logger.error("oops!", pr.cause());
//            future.fail(pr.cause());
//         }
//      });

      // Register the handler
      ProxyHelper.registerService(ProxyAggregatorService.class, vertx, proxyAggregatorService, EB_PROXY_AGGREGATOR_SERVICE_ADDRESS);

   }

}

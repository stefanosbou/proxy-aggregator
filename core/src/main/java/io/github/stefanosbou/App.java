package io.github.stefanosbou;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.github.stefanosbou.service.impl.ProxyAggregatorServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

import static io.github.stefanosbou.verticles.ProxyAggregatorVerticle.EB_PROXY_AGGREGATOR_SERVICE_ADDRESS;

public class App {
   public static void main(String[] args) {
      Vertx vertx = Vertx.vertx();

      // Create the client object
      ProxyAggregatorService proxyAggregatorService = new ProxyAggregatorServiceImpl(vertx, new JsonObject());
      // Register the handler
      ProxyHelper.registerService(ProxyAggregatorService.class, vertx, proxyAggregatorService, EB_PROXY_AGGREGATOR_SERVICE_ADDRESS);


      ProxyAggregator proxyAggregator = new ProxyAggregator(vertx, new JsonObject());

      // config will contain list of sites that will be crawled. The list will be predefined
      // as each site requires different crawling technique.

      Future<Proxy> proxyFuture = proxyAggregator.getProxy(); // return most recent available proxy
      proxyFuture.setHandler(ar -> {
         if (ar.succeeded()) {
            System.out.println(ar.result().toJson().encodePrettily());
         } else {
            System.out.println(ar.cause().getMessage());
         }
      });

//             System.out.printf("wait for answer done. Answer is: %s", pr.toJson());

//      System.out.println(proxyFuture.result().toString());
//      proxyAggregator.getProxies(); // returns list of the 10 most recent proxies
//      proxyAggregator.getProxy(id); // return proxy with 'id'
//      proxyAggregator.getProxyFromCountry(countryCode); // return most recent available proxy from 'countryCode'
//      proxyAggregator.getProxyFromSite(website); // return most recent available proxy from 'website'
//
//      proxyAggregator.checkProxy();
//      proxyAggregator.checkProxy(id);
//
//      proxyAggregator.removeProxy(id);
//      proxyAggregator.updateProxy(id);

   }
}

package io.github.stefanosbou;

import io.github.stefanosbou.model.Proxy;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

public class App {
   public static void main(String[] args) {
      Vertx vertx = Vertx.vertx();

      ProxyAggregator proxyAggregator = new ProxyAggregator(vertx, new JsonObject());

      // config will contain list of sites that will be crawled. The list will be predefined
      // as each site requires different crawling technique.

      Future<Optional<Proxy>> proxyFuture = proxyAggregator.getProxy(); // return most recent available proxy
      while (true){
         if (proxyFuture.isComplete()){
                  System.out.println(proxyFuture.result().get().toString());

         }
      }
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

package io.github.stefanosbou;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class App {
   public static void main(String[] args) {
      Vertx vertx = Vertx.vertx();

      ProxyAggregator proxyAggregator = new ProxyAggregator(vertx, new JsonObject());

      // config will contain list of sites that will be crawled. The list will be predefined
      // as each site requires different crawling technique.

      proxyAggregator.getProxy(); // return most recent available proxy
      proxyAggregator.getProxies(); // returns list of the 10 most recent proxies
      proxyAggregator.getProxy(id); // return proxy with 'id'
      proxyAggregator.getProxyFromCountry(countryCode); // return most recent available proxy from 'countryCode'
      proxyAggregator.getProxyFromSite(website); // return most recent available proxy from 'website'

      proxyAggregator.checkProxy();
      proxyAggregator.checkProxy(id);

      proxyAggregator.removeProxy(id);
      proxyAggregator.updateProxy(id);

   }
}

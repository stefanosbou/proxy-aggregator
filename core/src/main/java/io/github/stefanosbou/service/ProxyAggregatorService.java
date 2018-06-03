package io.github.stefanosbou.service;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.impl.ProxyAggregatorServiceImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.List;

@ProxyGen
public interface ProxyAggregatorService {
   /**
    * Factory method for creating a {@link ProxyAggregatorService} instance.
    *
    * @param vertx  Vertx instance
    * @param config configuration
    * @return the new {@link ProxyAggregatorService} instance
    */
   static ProxyAggregatorService create(Vertx vertx, JsonObject config) {
      return new ProxyAggregatorServiceImpl(vertx, config);
   }

   /**
    * Factory method for creating a {@link ProxyAggregatorService} service proxy.
    * This is useful for doing RPCs.
    *
    * @param vertx Vertx instance
    * @param address event bus address of RPC
    * @return the new {@link ProxyAggregatorService} service proxy
    */
   static ProxyAggregatorService createProxy(Vertx vertx, String address) {
      return ProxyHelper.createProxy(ProxyAggregatorService.class, vertx, address);

   }

   /**
    * Add proxy to backend.
    *
    * @param proxy      proxy object
    * @param handler async result handler
    */
   @Fluent
   ProxyAggregatorService addProxy(Proxy proxy, Handler<AsyncResult<Void>> handler);

   /**
    * Get the most recent available proxy from backend.
    *
    * @param handler async result handler
    */
   @Fluent
   ProxyAggregatorService getProxy(int page, int limit, String status, String country, Handler<AsyncResult<List<Proxy>>> handler);

   /**
    * Get a list of available proxy from backend.
    *
    * @param handler async result handler
    */
//   @Fluent
//   ProxyAggregatorService getProxies(Handler<AsyncResult<List<Proxy>>> handler);
   /**
    * Get the certain proxy from backend by id.
    *
    * @param id      proxy id
    * @param handler async result handler
    */
//   @Fluent
//   ProxyAggregatorService getProxy(long id, Handler<AsyncResult<Proxy>> handler);

   /**
    * Remove a proxy by id.
    *
    * @param id      proxy id
    * @param handler async result handler
    */
   @Fluent
   ProxyAggregatorService removeProxy(long id, Handler<AsyncResult<Void>> handler);

   /**
    * Judge whether a proxy with certain id exists.
    *
    * @param id      job id
    * @param handler async result handler
    */
   @Fluent
   ProxyAggregatorService existsProxy(long id, Handler<AsyncResult<Boolean>> handler);

}

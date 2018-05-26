package io.github.stefanosbou.service.impl;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ProxyAggregatorServiceImpl implements ProxyAggregatorService{

   public ProxyAggregatorServiceImpl(Vertx vertx, JsonObject config) {
   }

   @Override
   public ProxyAggregatorService addProxy(Proxy proxy, Handler<AsyncResult<Void>> handler) {
      return null;
   }

   @Override
   public ProxyAggregatorService getProxy(Handler<AsyncResult<Proxy>> handler) {
      handler.handle(Future.succeededFuture(new Proxy()));
      return this;
   }

//   @Override
//   public ProxyAggregatorService getProxy(long id, Handler<AsyncResult<Proxy>> handler) {
//      return null;
//   }

   @Override
   public ProxyAggregatorService removeProxy(long id, Handler<AsyncResult<Void>> handler) {
      return null;
   }

   @Override
   public ProxyAggregatorService existsProxy(long id, Handler<AsyncResult<Boolean>> handler) {
      return null;
   }
}

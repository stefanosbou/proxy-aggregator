package io.github.stefanosbou.service.impl;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.github.stefanosbou.util.DbHelper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

public class ProxyAggregatorServiceImpl implements ProxyAggregatorService{

   private final Vertx vertx;
   private final JsonObject config;
   private final SQLClient client;

   public ProxyAggregatorServiceImpl(Vertx vertx) {
      this(vertx, new JsonObject());

   }

   public ProxyAggregatorServiceImpl(Vertx vertx, JsonObject config) {
      this.vertx = vertx;
      this.config = config;
      this.client = DbHelper.client(vertx, config);
      Proxy.setVertx(vertx, DbHelper.client(vertx, config)); // init static vertx instance inner job

   }

   @Override
   public ProxyAggregatorService addProxy(Proxy proxy, Handler<AsyncResult<Void>> handler) {
      return null;
   }

   @Override
   public ProxyAggregatorService getProxy(Handler<AsyncResult<Proxy>> handler) {
      String sql = "SELECT * FROM table";
      client.query(sql, res -> {
         if (res.succeeded()){
//            handler.handle(Future.succeededFuture(res.result()));
            handler.handle(Future.succeededFuture(new Proxy()));
         } else {
            handler.handle(Future.failedFuture(res.cause()));
         }
      });
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

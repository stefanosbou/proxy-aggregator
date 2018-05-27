package io.github.stefanosbou.service.impl;

import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.github.stefanosbou.util.DbHelper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.UUID;

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
      initDb();
   }

   private void initDb() {
      this.client.getConnection(conn -> {
         if (conn.succeeded()) {
            SQLConnection connection = conn.result();

            String sql = "CREATE TABLE IF NOT EXISTS Proxy (\"id\" varchar(100), \"name\" varchar(100), " +
               "\"origin\" varchar(100))";
            connection.execute(sql, res -> {
               if (res.succeeded()) {
//                  createSomeData();
                  System.out.println("Successfully created table");
               } else {
                  System.out.println("Error while creating table " + res.cause().getMessage());
               }
            });
         }
      });
   }

   private void createSomeData() {
      String sql = "INSERT INTO Proxy (\"id\", \"name\", \"origin\") VALUES ?, ?, ?";
      this.client.getConnection(conn -> {
            if (conn.succeeded()) {
               SQLConnection connection = conn.result();
               connection.updateWithParams(sql, new JsonArray().add(UUID.randomUUID().toString()).add("testName").add("testOrigin"), ar -> {
                  if (ar.succeeded()) {
                     System.out.println("Successfully inserted data");
                  } else {
                     System.out.println("Error inserting data " + ar.cause().getMessage());
                  }
               });
            }
      });
   }

   @Override
   public ProxyAggregatorService addProxy(Proxy proxy, Handler<AsyncResult<Void>> handler) {
      return null;
   }

   @Override
   public ProxyAggregatorService getProxy(Handler<AsyncResult<Proxy>> handler) {
      String sql = "SELECT * FROM Proxy";
      client.getConnection(conn -> {
         if (conn.succeeded()) {
            SQLConnection connection = conn.result();
            connection.query(sql, res -> {
               if (res.succeeded()) {
                  ResultSet resultSet = res.result();
                  System.out.println(resultSet.getRows().get(0).encodePrettily());
                  handler.handle(Future.succeededFuture(new Proxy(resultSet.getRows().get(0))));
               } else {
                  System.out.println(res.cause().getMessage());
                  handler.handle(Future.failedFuture(res.cause()));
               }

            });
         }
      });
//      client.query(sql, res -> {
//         if (res.succeeded()){
////            handler.handle(Future.succeededFuture(res.result()));
//
//         } else {
//            handler.handle(Future.failedFuture(res.cause()));
//         }
//      });
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

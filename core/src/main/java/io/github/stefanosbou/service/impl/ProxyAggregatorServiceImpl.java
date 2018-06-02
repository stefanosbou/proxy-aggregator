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

import java.util.ArrayList;
import java.util.List;

public class ProxyAggregatorServiceImpl implements ProxyAggregatorService{

   private final Vertx vertx;
   private final JsonObject config;
   private final SQLClient client;

   private static final String TABLE = "proxy";

   private static final String HSQLDB_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " " +
      "(\"id\" varchar(100), " +
      "\"host\" varchar(100), " +
      "\"port\" varchar(100), " +
      "\"country_code\" varchar(100)," +
      "\"country\" varchar(100)," +
      "\"anonymity\" varchar(100)," +
      "\"google\" varchar(100)," +
      "\"https\" varchar(100)," +
      "\"status\" varchar(100)" +
      ")";

   private static final String MYSQL_CREATE_TABLE = "CREATE TABLE " + TABLE + " (\n" +
      "  `id` VARCHAR(45) NOT NULL,\n" +
      "  `host` VARCHAR(45) NULL,\n" +
      "  `port` VARCHAR(45) NULL,\n" +
      "  `country_code` VARCHAR(45) NULL,\n" +
      "  `country` VARCHAR(45) NULL,\n" +
      "  `anonymity` VARCHAR(45) NULL,\n" +
      "  `google` VARCHAR(45) NULL,\n" +
      "  `https` VARCHAR(45) NULL,\n" +
      "  `status` VARCHAR(45) NULL,\n" +
      "  `lastUpdate` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
      "  PRIMARY KEY (`id`),\n" +
      "  UNIQUE INDEX `id_UNIQUE` (`id` ASC));";

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

            connection.execute(MYSQL_CREATE_TABLE, res -> {
               if (res.succeeded()) {
//                  createSomeData();
                  System.out.println("Successfully created table");
                  connection.close();
               } else {
                  System.out.println("Error while creating table " + res.cause().getMessage());
                  connection.close();
               }
            });
         }
      });
   }

   @Override
   public ProxyAggregatorService addProxy(Proxy proxy, Handler<AsyncResult<Void>> handler) {
      final String MYSQL_INSERT_OR_UPDATE = "INSERT INTO " + TABLE + " (`id`, `host`, `port`, `country_code`, `country`, `anonymity`, `google`, `https`, `status`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) " +
         "ON DUPLICATE KEY UPDATE `status`=?, `lastUpdate`=CURRENT_TIMESTAMP";
//      String sql = "INSERT INTO Proxy (\"id\", \"host\", \"port\", \"country_code\", \"country\", \"anonymity\", \"google\", \"https\", \"status\") " +
//         "VALUES ?, ?, ?, ?, ?, ?, ?, ?, ?";
      this.client.getConnection(conn -> {
         if (conn.succeeded()) {
            SQLConnection connection = conn.result();
            connection.updateWithParams(MYSQL_INSERT_OR_UPDATE,
               new JsonArray()
                  .add(proxy.getId())
                  .add(proxy.getHost())
                  .add(proxy.getPort())
                  .add(proxy.getCountryCode())
                  .add(proxy.getCountry())
                  .add(proxy.isAnonymity())
                  .add(proxy.isGoogleEnabled())
                  .add(proxy.isHttps())
                  .add(proxy.getStatus())
                  .add(proxy.getStatus()
                  ), ar -> {
               if (ar.succeeded()) {
                  System.out.println("Successfully inserted data");
                  connection.close();
                  handler.handle(Future.succeededFuture());
               } else {
                  System.out.println("Error inserting data " + ar.cause().getMessage());
                  connection.close();
                  handler.handle(Future.failedFuture(ar.cause()));
               }
            });
         }
      });
      return this;
   }

   @Override
   public ProxyAggregatorService getProxy(Handler<AsyncResult<Proxy>> handler) {
      String sql = "SELECT * FROM " + TABLE;
      client.getConnection(conn -> {
         if (conn.succeeded()) {
            SQLConnection connection = conn.result();
            connection.query(sql, res -> {
               if (res.succeeded()) {
                  ResultSet resultSet = res.result();
//                  System.out.println(resultSet.getRows().get(0).encodePrettily());
                  connection.close();
                  handler.handle(Future.succeededFuture(new Proxy(resultSet.getRows().get(1))));
               } else {
                  System.out.println(res.cause().getMessage());
                  connection.close();
                  handler.handle(Future.failedFuture(res.cause()));
               }
            });
         }
      });
      return this;
   }

   @Override
   public ProxyAggregatorService getProxies(Handler<AsyncResult<List<Proxy>>> handler) {
      String sql = "SELECT * FROM " + TABLE + " ORDER BY lastUpdate DESC";
      client.getConnection(conn -> {
         if (conn.succeeded()) {
            SQLConnection connection = conn.result();
            connection.query(sql, res -> {
               if (res.succeeded()) {
                  ResultSet resultSet = res.result();
                  List<Proxy> proxies = new ArrayList<>();
                  for(JsonObject obj : resultSet.getRows()) {
                     proxies.add(new Proxy(obj));
                  }
                  connection.close();
                  handler.handle(Future.succeededFuture(proxies));
               } else {
                  System.out.println(res.cause().getMessage());
                  connection.close();
                  handler.handle(Future.failedFuture(res.cause()));
               }
            });
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

package io.github.stefanosbou.model;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

public class Proxy {

   private static Vertx vertx;
   private static SQLClient client;
   private static EventBus eventBus;

   public static void setVertx(Vertx v, SQLClient sqlClient) {
      vertx = v;
      client = sqlClient;
      eventBus = vertx.eventBus();
   }

   private String host;
   private int port;
   private String countryCode;
   private String country;
   private boolean anonymity;
   private boolean googleEnabled;
   private boolean https;

   public Job(JsonObject obj) {
      this.address_id = UUID.randomUUID().toString();
      _checkStatic();
   }
}

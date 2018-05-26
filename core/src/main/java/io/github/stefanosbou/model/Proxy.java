package io.github.stefanosbou.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

import java.util.UUID;

@DataObject(generateConverter = true)
public class Proxy {

   private static Vertx vertx;
   private static SQLClient client;
   private static EventBus eventBus;

   public static void setVertx(Vertx v, SQLClient sqlClient) {
      vertx = v;
      client = sqlClient;
      eventBus = vertx.eventBus();
   }

   private String id;
   private String host;
   private int port;
   private String countryCode;
   private String country;
   private boolean anonymity;
   private boolean googleEnabled;
   private boolean https;

   public Proxy() {
      this.id = UUID.randomUUID().toString();
      System.out.println(this.id);
//      _checkStatic();
   }

   public Proxy(JsonObject json) {
      ProxyConverter.fromJson(json, this);
      this.id = json.getString("id");

//      _checkStatic();

   }

   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.put("id", this.id);
      return json;
   }


   @Override
   public String toString() {
      return "This is a Proxy with id: " + this.id;
   }
}

package io.github.stefanosbou.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
   private String port;
   private String countryCode;
   private String country;
   private String anonymity;
   private String googleEnabled;
   private String https;

   MessageDigest md;
   {
      try {
         md = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      }
   }

   public Proxy() {
//      _checkStatic();
   }

   public Proxy(JsonObject json) {
      ProxyConverter.fromJson(json, this);

      // If Json has 'id' field then use this value, else generate and use MD5 hash using '<HOST><PORT>'.
      this.id              = json.getString("id",
         String.format("%032X", new BigInteger(1, md.digest((this.getHost() + this.getPort()).getBytes()))));
//      _checkStatic();
   }

   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      ProxyConverter.toJson(this, json);

      return json;
   }


   @Override
   public String toString() {
      return toJson().encodePrettily();
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }
   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getPort() {
      return port;
   }

   public void setPort(String port) {
      this.port = port;
   }

   public String getCountryCode() {
      return countryCode;
   }

   public void setCountryCode(String countryCode) {
      this.countryCode = countryCode;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String isAnonymity() {
      return anonymity;
   }

   public void setAnonymity(String anonymity) {
      this.anonymity = anonymity;
   }

   public String isGoogleEnabled() {
      return googleEnabled;
   }

   public void setGoogleEnabled(String googleEnabled) {
      this.googleEnabled = googleEnabled;
   }

   public String isHttps() {
      return https;
   }

   public void setHttps(String https) {
      this.https = https;
   }
}

package io.github.stefanosbou;

import io.vertx.core.Vertx;

public class App {
   public static void main(String[] args) {
      Vertx vertx = Vertx.vertx();

      vertx.deployVerticle(new ProxyCoreVerticle(), res -> {
         if (res.succeeded()) {
            System.out.println("[INFO] Verticle deployed successfully");
         } else {
            System.out.println("[ERROR] Verticle failed to deploy");
         }
      });
   }
}

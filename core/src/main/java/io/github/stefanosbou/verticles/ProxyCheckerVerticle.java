package io.github.stefanosbou.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.streams.Pump;

public class ProxyCheckerVerticle extends AbstractVerticle {
   public static final String EB_PROXY_CHECKER_SERVICE_ADDRESS = "vertx.proxy.checker.service";

   private static final int PORT = 9999;
   private HttpClient client;
   private static String currentIPAddress;

   @Override
   public void start(Future<Void> future) {
      EventBus eb = vertx.eventBus();

      getCurrentIPAddress(res -> {
         if(res.succeeded()) {
            currentIPAddress = res.result();
            MessageConsumer<JsonObject> consumer = eb.consumer(EB_PROXY_CHECKER_SERVICE_ADDRESS);
            consumer.handler(this::handleProxyStatusCheck);
         } else {
            System.out.println("[ERROR] " + res.cause().getMessage());
         }
      });


      vertx.createHttpServer().requestHandler(req -> {
//         req.headers().entries().stream().map((e) -> e.getKey() + ":" + e.getValue()).forEach(System.out::println);
         req.response().end(req.remoteAddress().host());
      }).listen(PORT);

      future.complete();
   }

   private void getCurrentIPAddress(Handler<AsyncResult<String>> handler) {
      client = vertx.createHttpClient(new HttpClientOptions());
      client.getNow("ifconfig.co", "/ip", response -> {
         if (response.statusCode() == 200) {
            response.bodyHandler(body -> {
               String res = body.toString().trim();
               handler.handle(Future.succeededFuture(res));
            });
         } else {
            handler.handle(Future.failedFuture(response.statusMessage()));
         }
      });
   }

   private void handleProxyStatusCheck(Message<JsonObject> message) {
      String host = message.body().getString("host");
      int port = Integer.parseInt(message.body().getString("port"));

//      boolean https = message.body().getString("https").equalsIgnoreCase("yes") ? true : false;
      client = vertx.createHttpClient(new HttpClientOptions()
            .setProxyOptions(new ProxyOptions()
               .setHost(host)
               .setPort(port))
//         .setSsl(https)
      );

      HttpClientRequest request = client.get(PORT, currentIPAddress, "/", response -> {
         if (response.statusCode() == 200) {
            response.bodyHandler(body -> {
               String res = body.toString().trim();
               System.out.println(res + " -> " + host + " " + (host.equals(res) ? "active" : "inactive"));
               JsonObject proxy = message.body();
               proxy.put("status", (host.equals(res) ? "active" : "inactive"));
               message.reply(proxy);
            });
         } else {
            System.out.println("Status code: " + response.statusCode());
            JsonObject proxy = message.body();
            proxy.put("status", "inactive");
            message.reply(proxy);
         }
      });

      request.exceptionHandler(r -> {
         System.out.println("Exception in " + this.getClass().getName()+ " : " + r.getMessage());
         JsonObject proxy = message.body();
         proxy.put("status", "inactive");
         message.reply(proxy);
      });

      request.end();


   }
}

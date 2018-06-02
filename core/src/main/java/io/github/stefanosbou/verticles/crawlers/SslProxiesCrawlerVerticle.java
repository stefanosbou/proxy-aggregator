package io.github.stefanosbou.verticles.crawlers;

import io.github.stefanosbou.ProxySites;
import io.github.stefanosbou.model.Proxy;
import io.github.stefanosbou.service.ProxyAggregatorService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

import static io.github.stefanosbou.verticles.ProxyAggregatorVerticle.EB_PROXY_AGGREGATOR_SERVICE_ADDRESS;
import static io.github.stefanosbou.verticles.ProxyCheckerVerticle.EB_PROXY_CHECKER_SERVICE_ADDRESS;

public class SslProxiesCrawlerVerticle extends AbstractVerticle {

   private static final String PROXY_WEBSITE = ProxySites.SSLPROXIES.getUrl();
   private Set<JsonObject> proxies;
   private ProxyAggregatorService service;
   private EventBus eb;

   @Override
   public void start(Future<Void> future) {
      eb = vertx.eventBus();
      service = ProxyAggregatorService.createProxy(vertx, EB_PROXY_AGGREGATOR_SERVICE_ADDRESS);

      proxies = new HashSet<>();
      crawlProxies();
      future.complete();
   }

   private void crawlProxies() {
      HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions()
         .setSsl(true));

      httpClient.requestAbs(HttpMethod.GET, PROXY_WEBSITE, response -> {
         response.bodyHandler(body -> {
            String htmlBody = body.getString(0, body.length());
            extractProxies(htmlBody);
         });
      }).end();

      vertx.setTimer(60000, id -> crawlProxies());
   }

   private void extractProxies(String htmlBody) {
      Document doc = Jsoup.parse(htmlBody);

      Element table = doc.select("table").get(0); //select the first table.
      Elements rows = table.select("tr");

      JsonObject obj;
      for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
         Element row = rows.get(i);
         Elements cols = row.select("td");

         obj = new JsonObject();
         if (!cols.isEmpty()) {
            String host = cols.get(0).text();
            String port = cols.get(1).text();
            String countryCode = cols.get(2).text();
            String country = cols.get(3).text();
            String anonymity = cols.get(4).text();
            String google = cols.get(5).text();
            String https = cols.get(6).text();

            obj.put("host", host)
               .put("port", port)
               .put("countryCode", countryCode)
               .put("country", country)
               .put("anonymity", anonymity)
               .put("googleEnabled", google)
               .put("https", https);


            eb.send(EB_PROXY_CHECKER_SERVICE_ADDRESS, obj, message -> {
//               proxies.add((JsonObject) message.result().body());

               if (message.succeeded()) {
                  Proxy proxy = new Proxy((JsonObject) message.result().body());

                  service.addProxy(proxy, res -> {
                     if (res.succeeded()) {
//                        message.result().reply(new JsonObject());
                        System.out.println("Successfully added " + message.result().isSend());
                     } else {
                        // error
//                        message.result().reply(new JsonObject());
                        System.out.println("HERE " + res.cause().getMessage());
                     }
                  });
               }
            });
         }
      }
//      System.out.println("Total proxies in the list: " + proxies.size());
   }

}

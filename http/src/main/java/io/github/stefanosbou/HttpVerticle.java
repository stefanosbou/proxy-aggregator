package io.github.stefanosbou;

import io.github.stefanosbou.model.Proxy;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HttpVerticle extends AbstractVerticle {
//   private static final Logger LOGGER = LoggerFactory.getLogger(KueHttpVerticle.class);

   private static final String HOST = "0.0.0.0";
   private static final int PORT = 8080; // Default port

   // Kue REST API
//   private static final String KUE_API_JOB_SEARCH = "/job/search/:q";
//   private static final String KUE_API_STATS = "/stats";
//   private static final String KUE_API_TYPE_STATE_STATS = "/jobs/:type/:state/stats";
   private static final String API_GET_PROXY = "/api/v1/proxy";
//   private static final String API_GET_PROXIES = "/api/proxies";
//   private static final String KUE_API_GET_JOB_TYPES = "/job/types";
//   private static final String KUE_API_JOB_RANGE = "/jobs/:from/to/:to";
//   private static final String KUE_API_JOB_TYPE_RANGE = "/jobs/:type/:state/:from/to/:to/:order";
//   private static final String KUE_API_JOB_STATE_RANGE = "/jobs/:state/:from/to/:to/:order";
//   private static final String KUE_API_JOB_RANGE_ORDER = "/jobs/:from/to/:to/:order";
//   private static final String KUE_API_CREATE_JOB = "/job";
//   private static final String KUE_API_UPDATE_JOB_STATE = "/job/:id/state/:state";
//   private static final String KUE_API_DELETE_JOB = "/job/:id";
//   private static final String KUE_API_GET_JOB_LOG = "/job/:id/log";
//   private static final String KUE_API_RESTART_JOB = "/inactive/:id";

   private ProxyAggregator proxyAggregator;

   @Override
   public void start(Future<Void> future) throws Exception {
      // init proxyAggregator
      proxyAggregator = ProxyAggregator.init(vertx);

      // create route
      final Router router = Router.router(vertx);
      router.route().handler(BodyHandler.create());

      Set<HttpMethod> allowedMethods = new HashSet<>();
      allowedMethods.add(HttpMethod.GET);
      allowedMethods.add(HttpMethod.POST);
      allowedMethods.add(HttpMethod.OPTIONS);

      router.route().handler(CorsHandler.create("*").allowedMethods(allowedMethods));

      // REST API routes
      router.get(API_GET_PROXY).handler(this::apiGetProxy);
//      router.get(API_GET_PROXIES).handler(this::apiGetProxies);
//      router.get(KUE_API_STATS).handler(this::apiStats);
//      router.get(KUE_API_TYPE_STATE_STATS).handler(this::apiTypeStateStats);
//      router.get(KUE_API_GET_JOB_TYPES).handler(this::apiJobTypes);
//      router.get(KUE_API_JOB_RANGE).handler(this::apiJobRange); // \/jobs\/([0-9]*)\.\.([0-9]*)(\/[^\/]+)?
//      router.get(KUE_API_JOB_TYPE_RANGE).handler(this::apiJobTypeRange);
//      router.get(KUE_API_JOB_STATE_RANGE).handler(this::apiJobStateRange);
//      router.get(KUE_API_JOB_RANGE_ORDER).handler(this::apiJobRange);
//      router.put(KUE_API_CREATE_JOB).handler(this::apiCreateJob);
//      router.put(KUE_API_UPDATE_JOB_STATE).handler(this::apiUpdateJobState);
//      router.get(KUE_API_GET_JOB).handler(this::apiGetJob);
//      router.get(KUE_API_GET_JOB_LOG).handler(this::apiFetchLog);
//      router.delete(KUE_API_DELETE_JOB).handler(this::apiDeleteJob);
//      router.post(KUE_API_RESTART_JOB).handler(this::apiRestartJob);


      // static resources route
      router.route().handler(StaticHandler.create());

      // create server
      vertx.createHttpServer()
         .requestHandler(router::accept)
         .listen(config().getInteger("http.port", PORT),
            config().getString("http.address", HOST), result -> {
               if (result.succeeded()) {
                  System.out.println("Proxy Aggregator http server is running on " + PORT + " port...");
                  future.complete();
               } else {
                  future.fail(result.cause());
               }
            });
   }

//   private void apiGetProxies(RoutingContext ctx) {
//      proxyAggregator.getProxies().setHandler(response -> {
//         List<Proxy> list = response.result();
//         JsonArray array = new JsonArray();
//         for(Proxy proxy : list) {
//            array.add(proxy.toJson());
//         }
//         ctx.response()
//            .putHeader("content-type", "application/json; charset=utf-8")
//            .end(array.encodePrettily());
//      });
//   }

   private void apiGetProxy(RoutingContext ctx) {
      int limit = optionalParam(ctx, "limit", 50);
      if (limit > 50) {
         limit = 50;
      }
      int page = optionalParam(ctx, "page", 0);
      String status = optionalParam(ctx, "status", "all");
      if (!status.equalsIgnoreCase("active")  &&
         !status.equalsIgnoreCase("inactive") &&
         !status.equalsIgnoreCase("all"))
      {
         status = "all";
      }
      String countryCode = optionalParam(ctx, "country", null);

      proxyAggregator.getProxy(limit, page, status, countryCode).setHandler(response -> {
         if (response.succeeded()) {
            List<Proxy> list = response.result();
            JsonArray array = new JsonArray();
            for(Proxy proxy : list) {
               array.add(proxy.toJson());
            }
            ctx.response()
               .putHeader("content-type", "application/json; charset=utf-8")
               .end(array.encodePrettily());
         } else {
            ctx.response()
               .putHeader("content-type", "application/json; charset=utf-8")
               .end(new JsonArray().encodePrettily());
         }
      });
   }

   private String optionalParam(RoutingContext ctx, String param, String defaultValue) {
      String optional = ctx.request().getParam(param);
      if (optional != null) {
         return optional;
      }
      return defaultValue;
   }

   private int optionalParam(RoutingContext ctx, String param, int defaultValue) {
      String optional = ctx.request().getParam(param);
      if (optional != null) {
         return Integer.parseInt(optional);
      }
      return defaultValue;
   }

}

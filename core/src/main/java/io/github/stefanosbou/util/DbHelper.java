package io.github.stefanosbou.util;


import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;

/**
 * Helper class for operating HSQL.
 *
 */
public final class DbHelper {

//   private static final String VERTX_KUE_REDIS_PREFIX = "vertx_kue";

   private DbHelper() {
   }

   /**
    * Factory method for creating a SQL client in Vert.x context.
    *
    * @param vertx  Vertx instance
    * @param config configuration
    * @return the new SQL client instance
    */
   public static SQLClient client(Vertx vertx, JsonObject config) {
      return JDBCClient.createShared(vertx, options(config));
   }

   /**
    * Factory method for creating a default local SQL client configuration.
    *
    * @param config configuration from Vert.x context
    * @return the new configuration instance
    */
   public static JsonObject options(JsonObject config) {
      return new JsonObject()
         .put("url", config.getString("url", "jdbc:hsqldb:file:db/test"))
//         .put("url", config.getString("url", "jdbc:mysql://localhost/test"))
         .put("driver_class", config.getString("driver_class", "org.hsqldb.jdbcDriver"))
//         .put("driver_class", config.getString("driver_class", "com.mysql.jdbc.Driver"))
//         .put("user", "root")
//         .put("password", "root")
         .put("max_pool_size", config.getInteger("max_pool_size", 30));
   }


}

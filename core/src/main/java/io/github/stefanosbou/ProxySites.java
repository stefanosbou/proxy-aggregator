package io.github.stefanosbou;

import io.github.stefanosbou.verticles.crawlers.SslProxiesCrawlerVerticle;

public enum ProxySites {
   SSLPROXIES("https://www.sslproxies.org", SslProxiesCrawlerVerticle.class),  //calls constructor with value 3
   ; // semicolon needed when fields / methods follow


   private final String url;
   private final Class aClass;

   ProxySites(String url, Class aClass) {
      this.url = url;
      this.aClass = aClass;
   }

   public String getUrl() {
      return this.url;
   }

   public Class getaClass() {
      return this.aClass;
   }


}

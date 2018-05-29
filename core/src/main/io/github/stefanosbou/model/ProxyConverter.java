/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.github.stefanosbou.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.github.stefanosbou.model.Proxy}.
 *
 * NOTE: This class has been automatically generated from the {@link io.github.stefanosbou.model.Proxy} original class using Vert.x codegen.
 */
public class ProxyConverter {

  public static void fromJson(JsonObject json, Proxy obj) {
    if (json.getValue("anonymity") instanceof String) {
      obj.setAnonymity((String)json.getValue("anonymity"));
    }
    if (json.getValue("country") instanceof String) {
      obj.setCountry((String)json.getValue("country"));
    }
    if (json.getValue("countryCode") instanceof String) {
      obj.setCountryCode((String)json.getValue("countryCode"));
    }
    if (json.getValue("googleEnabled") instanceof String) {
      obj.setGoogleEnabled((String)json.getValue("googleEnabled"));
    }
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("https") instanceof String) {
      obj.setHttps((String)json.getValue("https"));
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("port") instanceof String) {
      obj.setPort((String)json.getValue("port"));
    }
  }

  public static void toJson(Proxy obj, JsonObject json) {
    if (obj.isAnonymity() != null) {
      json.put("anonymity", obj.isAnonymity());
    }
    if (obj.getCountry() != null) {
      json.put("country", obj.getCountry());
    }
    if (obj.getCountryCode() != null) {
      json.put("countryCode", obj.getCountryCode());
    }
    if (obj.isGoogleEnabled() != null) {
      json.put("googleEnabled", obj.isGoogleEnabled());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.isHttps() != null) {
      json.put("https", obj.isHttps());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getPort() != null) {
      json.put("port", obj.getPort());
    }
  }
}
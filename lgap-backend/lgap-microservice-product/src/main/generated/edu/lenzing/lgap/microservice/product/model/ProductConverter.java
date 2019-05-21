/*
 * Copyright 2014 Red Hat, Inc.
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

package edu.lenzing.lgap.microservice.product.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link edu.lenzing.lgap.microservice.product.model.Product}.
 *
 * NOTE: This class has been automatically generated from the {@link edu.lenzing.lgap.microservice.product.model.Product} original class using Vert.x codegen.
 */
public class ProductConverter {

  public static void fromJson(JsonObject json, Product obj) {
    if (json.getValue("description") instanceof String) {
      obj.setDescription((String)json.getValue("description"));
    }
    if (json.getValue("id") instanceof Number) {
      obj.setId(((Number)json.getValue("id")).longValue());
    }
    if (json.getValue("imageUrl") instanceof String) {
      obj.setImageUrl((String)json.getValue("imageUrl"));
    }
    if (json.getValue("name") instanceof String) {
      obj.setName((String)json.getValue("name"));
    }
  }

  public static void toJson(Product obj, JsonObject json) {
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getImageUrl() != null) {
      json.put("imageUrl", obj.getImageUrl());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
  }
}
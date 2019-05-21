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

/** @module lgap-product-js/product_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JProductService = Java.type('edu.lenzing.lgap.microservice.product.service.ProductService');
var Product = Java.type('edu.lenzing.lgap.microservice.product.model.Product');

/**

 @class
*/
var ProductService = function(j_val) {

  var j_productService = j_val;
  var that = this;

  /**

   @public
   @param id {number} 
   @param handler {function} 
   @return {ProductService}
   */
  this.getProductById = function(id, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_productService["getProductById(java.lang.Long,io.vertx.core.Handler)"](utils.convParamLong(id), function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnDataObject(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {ProductService}
   */
  this.getAllProducts = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_productService["getAllProducts(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param product {Object} 
   @param handler {function} 
   @return {ProductService}
   */
  this.saveProduct = function(product, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_productService["saveProduct(edu.lenzing.lgap.microservice.product.model.Product,io.vertx.core.Handler)"](product != null ? new Product(new JsonObject(Java.asJSONCompatible(product))) : null, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnDataObject(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_productService;
};

ProductService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.product.service.ProductService");
ProductService._jtype = {
  accept: function(obj) {
    return ProductService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(ProductService.prototype, {});
    ProductService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
ProductService._create = function(jdel) {
  var obj = Object.create(ProductService.prototype, {});
  ProductService.apply(obj, arguments);
  return obj;
}
module.exports = ProductService;
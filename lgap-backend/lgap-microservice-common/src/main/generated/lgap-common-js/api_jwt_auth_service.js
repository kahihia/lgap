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

/** @module lgap-common-js/api_jwt_auth_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JApiJWTAuthService = Java.type('edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService');

/**

 @class
*/
var ApiJWTAuthService = function(j_val) {

  var j_apiJWTAuthService = j_val;
  var that = this;

  /**

   @public
   @param authInfo {Object} 
   @param handler {function} 
   @return {ApiJWTAuthService}
   */
  this.generateToken = function(authInfo, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_apiJWTAuthService["generateToken(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(authInfo), function(ar) {
      if (ar.succeeded()) {
        handler(ar.result(), null);
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
  this._jdel = j_apiJWTAuthService;
};

ApiJWTAuthService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.common.service.ApiJWTAuthService");
ApiJWTAuthService._jtype = {
  accept: function(obj) {
    return ApiJWTAuthService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(ApiJWTAuthService.prototype, {});
    ApiJWTAuthService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
ApiJWTAuthService._create = function(jdel) {
  var obj = Object.create(ApiJWTAuthService.prototype, {});
  ApiJWTAuthService.apply(obj, arguments);
  return obj;
}
module.exports = ApiJWTAuthService;
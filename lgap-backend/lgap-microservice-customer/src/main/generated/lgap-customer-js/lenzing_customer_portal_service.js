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

/** @module lgap-customer-js/lenzing_customer_portal_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JLenzingCustomerPortalService = Java.type('edu.lenzing.lgap.microservice.customer.service.LenzingCustomerPortalService');

/**

 @class
*/
var LenzingCustomerPortalService = function(j_val) {

  var j_lenzingCustomerPortalService = j_val;
  var that = this;

  /**

   @public
   @param email {string} 
   @param password {string} 
   @param handler {function} 
   @return {LenzingCustomerPortalService}
   */
  this.signIn = function(email, password, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_lenzingCustomerPortalService["signIn(java.lang.String,java.lang.String,io.vertx.core.Handler)"](email, password, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnJson(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param portalUserId {string} 
   @param portalAuthToken {string} 
   @param handler {function} 
   @return {LenzingCustomerPortalService}
   */
  this.getUserRegions = function(portalUserId, portalAuthToken, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_lenzingCustomerPortalService["getUserRegions(java.lang.String,java.lang.String,io.vertx.core.Handler)"](portalUserId, portalAuthToken, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnJson(ar.result()), null);
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
  this._jdel = j_lenzingCustomerPortalService;
};

LenzingCustomerPortalService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.customer.service.LenzingCustomerPortalService");
LenzingCustomerPortalService._jtype = {
  accept: function(obj) {
    return LenzingCustomerPortalService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(LenzingCustomerPortalService.prototype, {});
    LenzingCustomerPortalService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
LenzingCustomerPortalService._create = function(jdel) {
  var obj = Object.create(LenzingCustomerPortalService.prototype, {});
  LenzingCustomerPortalService.apply(obj, arguments);
  return obj;
}
module.exports = LenzingCustomerPortalService;
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

/** @module lgap-admin-js/admin_authentication_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAdminAuthenticationService = Java.type('edu.lenzing.lgap.microservice.admin.service.AdminAuthenticationService');

/**

 @class
*/
var AdminAuthenticationService = function(j_val) {

  var j_adminAuthenticationService = j_val;
  var that = this;

  /**

   @public
   @param userName {string} 
   @param password {string} 
   @param handler {function} 
   @return {AdminAuthenticationService}
   */
  this.authenticate = function(userName, password, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_adminAuthenticationService["authenticate(java.lang.String,java.lang.String,io.vertx.core.Handler)"](userName, password, function(ar) {
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
  this._jdel = j_adminAuthenticationService;
};

AdminAuthenticationService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.admin.service.AdminAuthenticationService");
AdminAuthenticationService._jtype = {
  accept: function(obj) {
    return AdminAuthenticationService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(AdminAuthenticationService.prototype, {});
    AdminAuthenticationService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
AdminAuthenticationService._create = function(jdel) {
  var obj = Object.create(AdminAuthenticationService.prototype, {});
  AdminAuthenticationService.apply(obj, arguments);
  return obj;
}
module.exports = AdminAuthenticationService;
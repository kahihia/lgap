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

/** @module lgap-auction-js/auction_manager_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAuctionManagerService = Java.type('edu.lenzing.lgap.microservice.auction.dprctd.service.AuctionManagerService');
var AuctionManager = Java.type('edu.lenzing.lgap.microservice.auction.model.AuctionManager');

/**

 @class
*/
var AuctionManagerService = function(j_val) {

  var j_auctionManagerService = j_val;
  var that = this;

  /**

   @public
   @param handler {function} 
   @return {AuctionManagerService}
   */
  this.loadAuctionManagers = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_auctionManagerService["loadAuctionManagers(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnListSetDataObject(ar.result()), null);
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
  this._jdel = j_auctionManagerService;
};

AuctionManagerService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.auction.dprctd.service.AuctionManagerService");
AuctionManagerService._jtype = {
  accept: function(obj) {
    return AuctionManagerService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(AuctionManagerService.prototype, {});
    AuctionManagerService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
AuctionManagerService._create = function(jdel) {
  var obj = Object.create(AuctionManagerService.prototype, {});
  AuctionManagerService.apply(obj, arguments);
  return obj;
}
module.exports = AuctionManagerService;
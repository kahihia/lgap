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

/** @module lgap-auction-js/dutch_auction_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDutchAuctionService = Java.type('edu.lenzing.lgap.microservice.auction.service.DutchAuctionService');
var DutchAuction = Java.type('edu.lenzing.lgap.microservice.auction.model.DutchAuction');

/**

 @class
*/
var DutchAuctionService = function(j_val) {

  var j_dutchAuctionService = j_val;
  var that = this;

  /**

   @public
   @param id {number} 
   @param allowedUserRegions {todo} 
   @param handler {function} 
   @return {DutchAuctionService}
   */
  this.getById = function(id, allowedUserRegions, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] === 'object' && __args[1] instanceof Array && typeof __args[2] === 'function') {
      j_dutchAuctionService["getById(java.lang.Long,io.vertx.core.json.JsonArray,io.vertx.core.Handler)"](utils.convParamLong(id), utils.convParamJsonArray(allowedUserRegions), function(ar) {
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
   @param auctionData {Object} 
   @param handler {function} 
   @return {DutchAuctionService}
   */
  this.create = function(auctionData, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_dutchAuctionService["create(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(auctionData), function(ar) {
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
   @return {DutchAuctionService}
   */
  this.startAuctionManagers = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_dutchAuctionService["startAuctionManagers(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param auction {Object} 
   @param handler {function} 
   @return {DutchAuctionService}
   */
  this.startAuction = function(auction, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_dutchAuctionService["startAuction(edu.lenzing.lgap.microservice.auction.model.DutchAuction,io.vertx.core.Handler)"](auction != null ? new DutchAuction(new JsonObject(Java.asJSONCompatible(auction))) : null, function(ar) {
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
   @param auction {Object} 
   @param handler {function} 
   @return {DutchAuctionService}
   */
  this.endAuction = function(auction, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_dutchAuctionService["endAuction(edu.lenzing.lgap.microservice.auction.model.DutchAuction,io.vertx.core.Handler)"](auction != null ? new DutchAuction(new JsonObject(Java.asJSONCompatible(auction))) : null, function(ar) {
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
   @param auction {Object} 
   @param handler {function} 
   @return {DutchAuctionService}
   */
  this.cancelAuction = function(auction, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_dutchAuctionService["cancelAuction(edu.lenzing.lgap.microservice.auction.model.DutchAuction,io.vertx.core.Handler)"](auction != null ? new DutchAuction(new JsonObject(Java.asJSONCompatible(auction))) : null, function(ar) {
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
  this._jdel = j_dutchAuctionService;
};

DutchAuctionService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.auction.service.DutchAuctionService");
DutchAuctionService._jtype = {
  accept: function(obj) {
    return DutchAuctionService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(DutchAuctionService.prototype, {});
    DutchAuctionService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
DutchAuctionService._create = function(jdel) {
  var obj = Object.create(DutchAuctionService.prototype, {});
  DutchAuctionService.apply(obj, arguments);
  return obj;
}
module.exports = DutchAuctionService;
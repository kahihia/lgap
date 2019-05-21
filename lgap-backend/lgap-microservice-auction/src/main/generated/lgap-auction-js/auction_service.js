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

/** @module lgap-auction-js/auction_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAuctionService = Java.type('edu.lenzing.lgap.microservice.auction.service.AuctionService');

/**

 @class
*/
var AuctionService = function(j_val) {

  var j_auctionService = j_val;
  var that = this;

  /**

   @public
   @param filter {Object} 
   @param allowedUserRegions {todo} 
   @param handler {function} 
   @return {AuctionService}
   */
  this.search = function(filter, allowedUserRegions, handler) {
    var __args = arguments;
    if (__args.length === 3 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'object' && __args[1] instanceof Array && typeof __args[2] === 'function') {
      j_auctionService["search(io.vertx.core.json.JsonObject,io.vertx.core.json.JsonArray,io.vertx.core.Handler)"](utils.convParamJsonObject(filter), utils.convParamJsonArray(allowedUserRegions), function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnListSetJson(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param filter {Object} 
   @param handler {function} 
   @return {AuctionService}
   */
  this.advancedSearch = function(filter, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_auctionService["advancedSearch(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(filter), function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnListSetJson(ar.result()), null);
      } else {
        handler(null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param auctionId {number} 
   @param auctionType {string} 
   @param allowedUserRegions {todo} 
   @param handler {function} 
   @return {AuctionService}
   */
  this.getAuctionByIdAndType = function(auctionId, auctionType, allowedUserRegions, handler) {
    var __args = arguments;
    if (__args.length === 4 && typeof __args[0] ==='number' && typeof __args[1] === 'string' && typeof __args[2] === 'object' && __args[2] instanceof Array && typeof __args[3] === 'function') {
      j_auctionService["getAuctionByIdAndType(java.lang.Long,java.lang.String,io.vertx.core.json.JsonArray,io.vertx.core.Handler)"](utils.convParamLong(auctionId), auctionType, utils.convParamJsonArray(allowedUserRegions), function(ar) {
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
   @param auctionId {number} 
   @param auctionType {string} 
   @param handler {function} 
   @return {AuctionService}
   */
  this.getAuctionTurnByAuctionIdAndType = function(auctionId, auctionType, handler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] === 'string' && typeof __args[2] === 'function') {
      j_auctionService["getAuctionTurnByAuctionIdAndType(java.lang.Long,java.lang.String,io.vertx.core.Handler)"](utils.convParamLong(auctionId), auctionType, function(ar) {
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
   @param auctionData {Object} 
   @param handler {function} 
   @return {AuctionService}
   */
  this.createAuction = function(auctionData, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_auctionService["createAuction(io.vertx.core.json.JsonObject,io.vertx.core.Handler)"](utils.convParamJsonObject(auctionData), function(ar) {
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
   @param auctionId {number} 
   @param handler {function} 
   @return {AuctionService}
   */
  this.cancelAuction = function(auctionId, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_auctionService["cancelAuction(java.lang.Long,io.vertx.core.Handler)"](utils.convParamLong(auctionId), function(ar) {
      if (ar.succeeded()) {
        handler(null, null);
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
  this._jdel = j_auctionService;
};

AuctionService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.auction.service.AuctionService");
AuctionService._jtype = {
  accept: function(obj) {
    return AuctionService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(AuctionService.prototype, {});
    AuctionService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
AuctionService._create = function(jdel) {
  var obj = Object.create(AuctionService.prototype, {});
  AuctionService.apply(obj, arguments);
  return obj;
}
module.exports = AuctionService;
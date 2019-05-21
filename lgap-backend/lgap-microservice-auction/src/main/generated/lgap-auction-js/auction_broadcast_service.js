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

/** @module lgap-auction-js/auction_broadcast_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAuctionBroadcastService = Java.type('edu.lenzing.lgap.microservice.auction.service.AuctionBroadcastService');
var Auction = Java.type('edu.lenzing.lgap.microservice.auction.model.Auction');

/**

 @class
*/
var AuctionBroadcastService = function(j_val) {

  var j_auctionBroadcastService = j_val;
  var that = this;

  /**

   @public
   @param auction {Object} 
   @param handler {function} 
   @return {AuctionBroadcastService}
   */
  this.auctionStarted = function(auction, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_auctionBroadcastService["auctionStarted(edu.lenzing.lgap.microservice.auction.model.Auction,io.vertx.core.Handler)"](auction != null ? new Auction(new JsonObject(Java.asJSONCompatible(auction))) : null, function(ar) {
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
   @return {AuctionBroadcastService}
   */
  this.auctionEnded = function(auction, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_auctionBroadcastService["auctionEnded(edu.lenzing.lgap.microservice.auction.model.Auction,io.vertx.core.Handler)"](auction != null ? new Auction(new JsonObject(Java.asJSONCompatible(auction))) : null, function(ar) {
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
   @return {AuctionBroadcastService}
   */
  this.auctionCanceled = function(auction, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_auctionBroadcastService["auctionCanceled(edu.lenzing.lgap.microservice.auction.model.Auction,io.vertx.core.Handler)"](auction != null ? new Auction(new JsonObject(Java.asJSONCompatible(auction))) : null, function(ar) {
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
   @param turnNumber {number} 
   @param handler {function} 
   @return {AuctionBroadcastService}
   */
  this.turnEnded = function(auction, turnNumber, handler) {
    var __args = arguments;
    if (__args.length === 3 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_auctionBroadcastService["turnEnded(edu.lenzing.lgap.microservice.auction.model.Auction,int,io.vertx.core.Handler)"](auction != null ? new Auction(new JsonObject(Java.asJSONCompatible(auction))) : null, turnNumber, function(ar) {
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
   @param turnNumber {number} 
   @param handler {function} 
   @return {AuctionBroadcastService}
   */
  this.turnStarted = function(auction, turnNumber, handler) {
    var __args = arguments;
    if (__args.length === 3 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] ==='number' && typeof __args[2] === 'function') {
      j_auctionBroadcastService["turnStarted(edu.lenzing.lgap.microservice.auction.model.Auction,int,io.vertx.core.Handler)"](auction != null ? new Auction(new JsonObject(Java.asJSONCompatible(auction))) : null, turnNumber, function(ar) {
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
  this._jdel = j_auctionBroadcastService;
};

AuctionBroadcastService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.auction.service.AuctionBroadcastService");
AuctionBroadcastService._jtype = {
  accept: function(obj) {
    return AuctionBroadcastService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(AuctionBroadcastService.prototype, {});
    AuctionBroadcastService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
AuctionBroadcastService._create = function(jdel) {
  var obj = Object.create(AuctionBroadcastService.prototype, {});
  AuctionBroadcastService.apply(obj, arguments);
  return obj;
}
module.exports = AuctionBroadcastService;
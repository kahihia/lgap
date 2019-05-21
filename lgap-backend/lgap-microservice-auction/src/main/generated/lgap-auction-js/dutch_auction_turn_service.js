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

/** @module lgap-auction-js/dutch_auction_turn_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDutchAuctionTurnService = Java.type('edu.lenzing.lgap.microservice.auction.service.DutchAuctionTurnService');
var DutchAuctionTurn = Java.type('edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn');

/**

 @class
*/
var DutchAuctionTurnService = function(j_val) {

  var j_dutchAuctionTurnService = j_val;
  var that = this;

  /**

   @public
   @param auctionId {number} 
   @param handler {function} 
   @return {DutchAuctionTurnService}
   */
  this.getTurnsByAuctionId = function(auctionId, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_dutchAuctionTurnService["getTurnsByAuctionId(java.lang.Long,io.vertx.core.Handler)"](utils.convParamLong(auctionId), function(ar) {
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
   @param auctionId {number} 
   @param handler {function} 
   @return {DutchAuctionTurnService}
   */
  this.getCurrentTurnByAuctionId = function(auctionId, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'function') {
      j_dutchAuctionTurnService["getCurrentTurnByAuctionId(java.lang.Long,io.vertx.core.Handler)"](utils.convParamLong(auctionId), function(ar) {
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
   @param turn {Object} 
   @param handler {function} 
   @return {DutchAuctionTurnService}
   */
  this.markTurnAsFinished = function(turn, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_dutchAuctionTurnService["markTurnAsFinished(edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn,io.vertx.core.Handler)"](turn != null ? new DutchAuctionTurn(new JsonObject(Java.asJSONCompatible(turn))) : null, function(ar) {
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
   @param turns {Array.<Object>} 
   @param handler {function} 
   @return {DutchAuctionTurnService}
   */
  this.markTurnsAsFinished = function(turns, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0] instanceof Array && typeof __args[1] === 'function') {
      j_dutchAuctionTurnService["markTurnsAsFinished(java.util.List,io.vertx.core.Handler)"](utils.convParamListDataObject(turns, function(json) { return new DutchAuctionTurn(json); }), function(ar) {
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
   @param turn {Object} 
   @param handler {function} 
   @return {DutchAuctionTurnService}
   */
  this.markTurnAsStarted = function(turn, handler) {
    var __args = arguments;
    if (__args.length === 2 && (typeof __args[0] === 'object' && __args[0] != null) && typeof __args[1] === 'function') {
      j_dutchAuctionTurnService["markTurnAsStarted(edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn,io.vertx.core.Handler)"](turn != null ? new DutchAuctionTurn(new JsonObject(Java.asJSONCompatible(turn))) : null, function(ar) {
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
   @param currentTurn {Object} 
   @param nextTurn {Object} 
   @param handler {function} 
   @return {DutchAuctionTurnService}
   */
  this.rollNextTurn = function(currentTurn, nextTurn, handler) {
    var __args = arguments;
    if (__args.length === 3 && (typeof __args[0] === 'object' && __args[0] != null) && (typeof __args[1] === 'object' && __args[1] != null) && typeof __args[2] === 'function') {
      j_dutchAuctionTurnService["rollNextTurn(edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn,edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn,io.vertx.core.Handler)"](currentTurn != null ? new DutchAuctionTurn(new JsonObject(Java.asJSONCompatible(currentTurn))) : null, nextTurn != null ? new DutchAuctionTurn(new JsonObject(Java.asJSONCompatible(nextTurn))) : null, function(ar) {
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
  this._jdel = j_dutchAuctionTurnService;
};

DutchAuctionTurnService._jclass = utils.getJavaClass("edu.lenzing.lgap.microservice.auction.service.DutchAuctionTurnService");
DutchAuctionTurnService._jtype = {
  accept: function(obj) {
    return DutchAuctionTurnService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(DutchAuctionTurnService.prototype, {});
    DutchAuctionTurnService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
DutchAuctionTurnService._create = function(jdel) {
  var obj = Object.create(DutchAuctionTurnService.prototype, {});
  DutchAuctionTurnService.apply(obj, arguments);
  return obj;
}
module.exports = DutchAuctionTurnService;
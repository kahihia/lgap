package edu.lenzing.lgap.microservice.auction.manager.verticle;

import com.google.inject.Inject;
import edu.lenzing.lgap.microservice.auction.manager.DutchAuctionManager;
import edu.lenzing.lgap.microservice.auction.model.AuctionPhase;
import edu.lenzing.lgap.microservice.auction.model.DutchAuction;
import edu.lenzing.lgap.microservice.auction.model.DutchAuctionTurn;
import edu.lenzing.lgap.microservice.auction.service.AuctionBroadcastService;
import edu.lenzing.lgap.microservice.auction.service.DutchAuctionService;
import edu.lenzing.lgap.microservice.auction.service.DutchAuctionTurnService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import sun.security.provider.certpath.Vertex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
public class DutchAuctionManagerVerticle extends AbstractVerticle implements DutchAuctionManager {

    private static final Logger LOG = LoggerFactory.getLogger(DutchAuctionManagerVerticle.class);

    @Inject
    private Vertx vertx;

    @Inject
    private DutchAuctionService auctionService;

    @Inject
    private DutchAuctionTurnService turnService;

    @Inject
    private AuctionBroadcastService broadcastService;

    private final Map<Long, DutchAuctionTurn> turnEndTimers;
    private List<DutchAuctionTurn> upcomingTurns;

    private DutchAuction auction;
    private DutchAuctionTurn currentTurn;

    private boolean running;

    public DutchAuctionManagerVerticle() {

        turnEndTimers = new HashMap<>();
        upcomingTurns = new LinkedList<>();
        running = false;

    }

    @Override
    public void manage(final DutchAuction auction) {

        if (this.auction != null) {
            // TODO: throw exception
            LOG.error("Overwriting managed auction.");
        }

        this.upcomingTurns = auction.getTurns().stream()
                .sorted(Comparator.comparingInt(DutchAuctionTurn::getTurnNumber))
                .filter(turn -> !turn.getFinished())
                .collect(Collectors.toList());

        this.auction = auction;

        final String currentTurnEventBusAddress = String.format("%s-%d", DutchAuctionManager.CURRENT_TURN_SERVICE_ADDRESS, auction.getId());

        this.vertx.eventBus().consumer(currentTurnEventBusAddress, message -> {
            final String msg = message.body().toString();
            switch (msg) {
                case "current-turn": {
                    JsonObject payload = null;
                    if (this.currentTurn != null) {
                        payload = this.currentTurn.toJson();
                    }
                    message.reply(payload);
                } break;
                case "end":
                    message.reply("ok");
                    this.endAuction();
                    break;
                default:
                    LOG.warn(String.format("Unknown message received: '%s'", msg));
                    break;
            }
        });

    }

    @Override
    public void start(final Future<Void> future) {

        if (auction == null) {
            final String message = "No auction is set to be managed";
            LOG.error(message);
            future.fail(message);
            return;
        }

        if (running) {
            final String message = "Auction manager is already running";
            LOG.error(message);
            future.fail(message);
            return;
        } else {
            running = true;
        }

        final Long start = auction.getStartTimestamp();
        final Long end = auction.getEndTimestamp();

        // This auction ends before it starts
        //  * report inconsistent data
        //  * exit
        if (end <= start) {
            final String message = String.format("Auction [%d] end date [%d] is sooner then the start date [%d]. Cancelling auction.", auction.getId(), end, start);
            LOG.error(message);
            future.fail(message);
            cancelAuction();
            return;
        }

        // This auction is not in a managed phase
        //  * report inconsistent data
        //  * exit
        if (auction.getAuctionPhase() != AuctionPhase.PREVIEW && auction.getAuctionPhase() != AuctionPhase.RUNNING) {
            final String message = String.format("Auction [%d] is not in a manageable phase [%s]. Skipping this auction.", auction.getId(), auction.getAuctionPhase().toString());
            LOG.error(message);
            future.fail(message);
            return;
        }

        final Long time = System.currentTimeMillis();

        // The auction should be running already
        //  * verify auction phase
        if (time >= start) {

            // This auction is running
            //  * find the current turn
            //  * execute the current turn
            //  * idle
            if (auction.getAuctionPhase() == AuctionPhase.RUNNING) {
                LOG.warn(String.format("Auction [%d] has been started already, continuing from the appropriate time", auction.getId()));
                future.complete();
                this.continueAuction();
            }

            // The auction should have been started already
            //  * report inconsistent data
            //  * set the auction phase to running
            //  * identify the turn that should be running now
            //  * mark the previous turns as finished
            //  * execute the current turn
            //  * idle
            else if (auction.getAuctionPhase() == AuctionPhase.PREVIEW) {
                LOG.warn(String.format("Auction [%d] should have been started already, but it is in PREVIEW phase. Starting auction from late preview.", auction.getId()));
                future.complete();
                this.startAuctionFromLatePreview();
            }

            else {
                final String message = String.format("Auction [%d] is not in a manageable phase [%s]", auction.getId(), auction.getAuctionPhase().toString());
                LOG.warn(message);
                future.fail(message);
                return;
            }

        }

        // This auction should be in preview mode
        //  * verify auction state
        else if (time < start) {

            // This auction is in preview mode
            //  * set timer to the start date
            //  * idle
            if (auction.getAuctionPhase() == AuctionPhase.PREVIEW) {
                LOG.info(String.format("Auction [%d] is in PREVIEW phase. Setting timer for the next phase.", auction.getId()));
                future.complete();
                this.startAuctionPreview();
            }

            // This auction is not in preview mode
            //  * report inconsistent data
            //  * exit
            else {
                final String message = String.format("Auction [%d] should be in PREVIEW phase. Resetting phase to PREVIEW.", auction.getId());
                LOG.warn(message);
                future.complete();
                this.startAuctionPreview();
                return;
            }

        }

    }

    private void startAuctionPreview() {

        LOG.info(String.format("Starting Auction [%d] preview", auction.getId()));

        setAuctionPreviewEndTimer();

    }

    private Future<Void> startAuction() {

        LOG.info(String.format("Starting Auction [%d]", auction.getId()));

        final Future<Void> future = Future.future();

        auctionService.startAuction(auction, handler -> {
            if (handler.succeeded()) {
                this.auction = handler.result();
                LOG.info(String.format("Auction [%d] started", auction.getId()));
                this.startUpcomingTurn();
                broadcastService.auctionStarted(auction, broadcast -> {});
                future.complete();
            } else {
                LOG.error(String.format("Failed to start Auction [%d]", auction.getId()), handler.cause());
                future.fail(handler.cause());
            }
        });

        return future;

    }

    private void startAuctionFromLatePreview() {

        LOG.info(String.format("Starting Auction [%d] from late preview", auction.getId()));

        eliminateLateTurns()
                .compose(eliminated -> startAuction())
                .setHandler(handler -> {
                    if (handler.succeeded()) {
                        LOG.info(String.format("Auction [%d] started from late preview", auction.getId()));
                    } else {
                        LOG.error(String.format("Failed to start auction [%d] from late preview", auction.getId()), handler.cause());
                    }
                });

    }

    private void continueAuction() {

        eliminateLateTurns().setHandler(handler -> {
            if (handler.succeeded()) {
                this.startUpcomingTurn();
            } else {
                LOG.error(String.format("Failed to continue Auction [%d]", auction.getId()), handler.cause());
            }
        });

    }

    private void endAuction() {

        LOG.info(String.format("Ending Auction [%d]", auction.getId()));

        auctionService.endAuction(auction, handler -> {
            if (handler.succeeded()) {
                this.auction = handler.result();
                LOG.info(String.format("Auction [%d] ended.", auction.getId()));
                turnEndTimers.keySet().forEach(timerId -> vertx.cancelTimer(timerId));
                broadcastService.auctionEnded(auction, broadcast -> {});
            } else {
                LOG.error(String.format("Failed to cancel end [%d]", auction.getId()), handler.cause());
            }
        });

    }

    private void cancelAuction() {

        LOG.info(String.format("Cancelling Auction [%d]", auction.getId()));

        auctionService.cancelAuction(auction, handler -> {
            if (handler.succeeded()) {
                this.auction = handler.result();
                LOG.info(String.format("Auction [%d] cancelled.", auction.getId()));
                broadcastService.auctionCanceled(auction, broadcast -> {});
            } else {
                LOG.error(String.format("Failed to cancel Auction [%d]", auction.getId()), handler.cause());
            }
        });

    }

    private Future<Void> eliminateLateTurns() {

        LOG.info(String.format("Eliminating late turns of Auction [%d]", auction.getId()));

        final List<DutchAuctionTurn> markAsFinished = new ArrayList<>();

        final long time = System.currentTimeMillis();

        for (final DutchAuctionTurn turn : upcomingTurns) {
            if (turn.getEndTimestamp() <= time) {
                markAsFinished.add(turn);
            }
        }

        LOG.info(String.format("Auction [%d] has %d late turns", auction.getId(), markAsFinished.size()));

        final Future<Void> future = Future.future();

        if (!markAsFinished.isEmpty()) {
            turnService.markTurnsAsFinished(markAsFinished, handler -> {
                if (handler.succeeded()) {
                    LOG.info(String.format("%d late turns of Auction [%d] eliminated", handler.result().size(), auction.getId()));
                    upcomingTurns.removeAll(markAsFinished);
                    future.complete();
                } else {
                    LOG.error(String.format("Failed to eliminate late turns of Auction [%d]", auction.getId()), handler.cause());
                    future.fail(handler.cause());
                }
            });
        } else {
            LOG.info(String.format("No late turns of Auction [%d] were eliminated", auction.getId()));
            future.complete();
        }

        return future;

    }

    private long setAuctionPreviewEndTimer() {

        final long duration = auction.getStartTimestamp() - System.currentTimeMillis();

        LOG.info(String.format("Starting Auction [%d] preview timer with duration: %d", auction.getId(), duration));

        return vertx.setTimer(duration, timerId -> {
            LOG.info(String.format("Auction [%d] preview ended", auction.getId()));
            this.startAuction();
        });

    }

    /* *****************************************************************************************************************
     *  AUCTION TURNS
     * ****************************************************************************************************************/

    private void startUpcomingTurn() {

        LOG.info("Starting upcoming turn.");

        if (upcomingTurns.isEmpty()) {

            LOG.info(String.format("No more turns left. Ending Auction [%d]", auction.getId()));
            this.endAuction();

        } else {

            final DutchAuctionTurn turn = upcomingTurns.remove(0);

            this.currentTurn = turn;

            LOG.info(String.format("Setting end timer for turn #%d [%d] in Auction [%d]", turn.getTurnNumber(), turn.getId(), auction.getId()));

            final long timerId = this.setTurnEndTimer(turn);
            turnEndTimers.put(timerId, turn);

        }

    }

    private long setTurnEndTimer(final DutchAuctionTurn turn) {

        final long duration = turn.getEndTimestamp() - System.currentTimeMillis();

        LOG.info(String.format("Starting timer for turn #%d [%d] with duration %d ms in Auction [%d]", turn.getTurnNumber(), turn.getId(), duration, auction.getId()));

        return vertx.setTimer(duration, timerId -> {
            final DutchAuctionTurn endedTurn = turnEndTimers.remove(timerId);
            LOG.info(String.format("Turn #%d [%d] of Auction [%d] ended", endedTurn.getTurnNumber(), endedTurn.getId(), auction.getId()));
            this.startUpcomingTurn();
        });

    }

    @Override
    public DutchAuctionTurn getCurrentTurn() {
        return this.currentTurn;
    }

}

package edu.lenzing.lgap.microservice.auction.model;

import edu.lenzing.lgap.microservice.auction.model.converter.DutchAuctionTurnConverter;
import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject
public class DutchAuctionTurn extends AbstractModel {

    private DutchAuction auction;

    private Integer turnNumber;
    private Long    turnDuration;
    private Boolean started;
    private Date    startDate;
    private Boolean finished;
    private Date    endDate;

    public DutchAuctionTurn() {}

    public DutchAuctionTurn(final int turnNumber, final long turnDuration) {
        this.turnNumber = turnNumber;
        this.turnDuration = turnDuration;
        this.started = false;
        this.finished = false;
    }

    public DutchAuctionTurn(final JsonObject json) {
        DutchAuctionTurnConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        return DutchAuctionTurnConverter.toJson(this);
    }

    public DutchAuction getAuction() {
        return auction;
    }

    public void setAuction(DutchAuction auction) {
        this.auction = auction;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
    }

    public Long getTurnDuration() {
        return turnDuration;
    }

    public void setTurnDuration(Long turnDuration) {
        this.turnDuration = turnDuration;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return The start date as timestamp
     */
    public Long getStartTimestamp() {
        return (getStartDate() == null) ? null : getStartDate().getTime();
    }

    public void setStartTimestamp(final Long timestamp) {
        if (timestamp != null) {
            this.setStartDate(new Date(timestamp));
        }
    }

    /**
     * @return The end date as timestamp
     */
    public Long getEndTimestamp() {
        return (getEndDate() == null) ? null : getEndDate().getTime();
    }

    public void setEndTimestamp(final Long timestamp) {
        if (timestamp != null) {
            this.setEndDate(new Date(timestamp));
        }
    }
}

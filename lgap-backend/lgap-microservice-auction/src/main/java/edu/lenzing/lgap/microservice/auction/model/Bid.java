package edu.lenzing.lgap.microservice.auction.model;

import edu.lenzing.lgap.microservice.auction.model.converter.BidConverter;
import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject
public class Bid extends AbstractModel {

    private Long    auctionId;
    private Long    extCustomerId;
    private Double  bid;
    private Date    bidTime;

    public Bid() {}

    public Bid(final JsonObject json) {
        BidConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        return BidConverter.toJson(this);
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public Long getExtCustomerId() {
        return extCustomerId;
    }

    public void setExtCustomerId(Long extCustomerId) {
        this.extCustomerId = extCustomerId;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    /**
     * @return The bid date as timestamp
     */
    public Long getBidTimestamp() {
        return (getBidTime() == null) ? null : getBidTime().getTime();
    }

    public void setBidTimestamp(final Long timestamp) {
        this.setBidTime(new Date(timestamp));
    }

}

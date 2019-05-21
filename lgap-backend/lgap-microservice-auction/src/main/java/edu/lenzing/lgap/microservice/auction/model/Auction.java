package edu.lenzing.lgap.microservice.auction.model;

import edu.lenzing.lgap.microservice.auction.model.converter.AuctionConverter;
import edu.lenzing.lgap.microservice.common.model.AbstractModel;
import edu.lenzing.lgap.microservice.common.model.UserRegion;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Date;
import java.util.Set;

/**
 * @author Csaba Ilonka <Ilonka.Csaba@isi-hagenberg.at>
 */
@DataObject
public class Auction extends AbstractModel {

    private String           name;
    private String           description;
    private Date             startDate;
    private Date             endDate;
    private Double           reservePrice;
    private AuctionType      auctionType;
    private AuctionPhase     auctionPhase;
    private Set<UserRegion>  regions;

    public Auction() {}

    public Auction(final JsonObject json) {
        AuctionConverter.fromJson(json, this);
    }

    @Override
    public JsonObject toJson() {
        return AuctionConverter.toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(Double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(AuctionType auctionType) {
        this.auctionType = auctionType;
    }

    public AuctionPhase getAuctionPhase() {
        return auctionPhase;
    }

    public void setAuctionPhase(AuctionPhase auctionPhase) {
        this.auctionPhase = auctionPhase;
    }

    public Set<UserRegion> getRegions() {
        return regions;
    }

    public void setRegions(Set<UserRegion> regions) {
        this.regions = regions;
    }

    /**
     * @return The start date as timestamp
     */
    public Long getStartTimestamp() {
        return (getStartDate() == null) ? null : getStartDate().getTime();
    }

    public void setStartTimestamp(final Long timestamp) {
        this.setStartDate(new Date(timestamp));
    }

    /**
     * @return The end date as timestamp
     */
    public Long getEndTimestamp() {
        return (getEndDate() == null) ? null : getEndDate().getTime();
    }

    public void setEndTimestamp(final Long tiemstamp) {
        this.setEndDate(new Date(tiemstamp));
    }
}

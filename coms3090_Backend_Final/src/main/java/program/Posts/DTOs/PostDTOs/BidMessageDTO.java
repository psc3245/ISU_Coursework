package program.Posts.DTOs.PostDTOs;

import program.Posts.PostTypes.AuctionPost;

public class BidMessageDTO {
    private Integer bidId;
    private Integer auctionId;
    private Integer userId;
    private Float bidAmount;
    public BidMessageDTO(Integer bidId, Integer auctionId, Integer userId, Float bidAmount) {
        this.bidId = bidId;
        this.auctionId = auctionId;
        this.userId = userId;
        this.bidAmount = bidAmount;
    }
    public BidMessageDTO() {
    }
    public Integer getBidId() {
        return bidId;
    }
    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }
    public Integer getAuctionId() {
        return auctionId;
    }
    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Float getBidAmount() {
        return bidAmount;
    }
    public void setBidAmount(Float bidAmount) {
        this.bidAmount = bidAmount;
    }


}

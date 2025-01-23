package program.Posts.DTOs.UpdateRequests;

import java.time.LocalDateTime;

import program.Posts.PostTypes.PostTypes;

public class AuctionUpdateRequest extends PostUpdateRequest {
    private Float startingBid;
    private LocalDateTime auctionEndTime;
    public AuctionUpdateRequest() {}

    public AuctionUpdateRequest(PostTypes type, String title, String description, Float startingBid,
            LocalDateTime auctionEndTime) {
        super(type, title, description);
        this.startingBid = startingBid;
        this.auctionEndTime = auctionEndTime;
    }
    public Float getStartingBid() {
        return startingBid;
    }
    public void setStartingBid(Float startingBid) {
        this.startingBid = startingBid;
    }
    public LocalDateTime getAuctionEndTime() {
        return auctionEndTime;
    }
    public void setAuctionEndTime(LocalDateTime auctionEndTime) {
        this.auctionEndTime = auctionEndTime;
    }
}

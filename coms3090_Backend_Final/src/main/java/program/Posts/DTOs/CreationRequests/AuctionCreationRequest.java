package program.Posts.DTOs.CreationRequests;

import java.time.LocalDateTime;
import java.util.Date;

import program.Posts.PostTypes.PostTypes;

public class AuctionCreationRequest extends PostCreationRequest{
    private Float startingBid;
    private LocalDateTime auctionEndTime;

    public AuctionCreationRequest() {}

    public AuctionCreationRequest(PostTypes type, String title, String description, Float startingBid,
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

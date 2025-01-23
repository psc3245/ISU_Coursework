package program.Posts.PostTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bids")
public class BidMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidId;

    @ManyToOne
    @JoinColumn(name = "auction_post_id")  // FK to link bids to an AuctionPost
    private AuctionPost auctionPost;

    private Integer userId;
    private Float bidAmount;
    public BidMessage(AuctionPost auctionPost, Integer userId, Float bidAmount) {
        this.auctionPost = auctionPost;
        this.userId = userId;
        this.bidAmount = bidAmount;
    }
    public BidMessage() {
    }
    public AuctionPost getAuctionPost() {
        return auctionPost;
    }
    public void setAuctionPost(AuctionPost auctionId) {
        this.auctionPost = auctionId;
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
    public Integer getBidId() {
        return bidId;
    }
    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }
    
}
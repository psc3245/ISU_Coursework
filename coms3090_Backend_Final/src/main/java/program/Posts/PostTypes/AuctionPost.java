package program.Posts.PostTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import program.Posts.Post;
import program.Users.User;

/*
 * TODO: Automatically closing auction websocket when completed
 * Accepting bids only if the bid is above currentHighestBid
 * Reworking Creation and update requests
 * Auction specific http requests, used for getting general information about an auction without actually joining it
 * 
 * OnMessage: check if higher, if higher broadcast to everyone a new highest number
 * OnClose: remover user from auctionPost
 * OnOpen: add user to auctionPost
 * OnError: throw error I guess
 * 
 * 
 */
@Entity
@DiscriminatorValue("AUCTION")
public class AuctionPost extends Post {

    private Float startingBid;
    private LocalDateTime auctionEndTime;

    private Float currentHighestBid;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_in_auction",
        joinColumns = @JoinColumn(name = "postId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> usersInAuction;
    @OneToMany(mappedBy = "auctionPost", cascade = CascadeType.ALL, orphanRemoval = true,fetch =  FetchType.EAGER)
    private List<BidMessage> bidHistory = new ArrayList<>(); 

    public AuctionPost(Integer postId, PostTypes type, String title, String description) {
        super(postId, type, title, description);
    }
    public AuctionPost(){}

    public Float getStartingBid() { return startingBid; }

    public void setStartingBid(Float startingBid) { this.startingBid = startingBid; }

    public LocalDateTime getAuctionEndTime() { return auctionEndTime; }

    public void setAuctionEndTime(LocalDateTime auctionEndTime) { this.auctionEndTime = auctionEndTime; }

    public Float getCurrentHighestBid() {
        return currentHighestBid;
    }
    public void setCurrentHighestBid(Float currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }
    public List<User> getUsersInAuction() {
        return usersInAuction;
    }
    public void setUsersInAuction(List<User> usersInAuction) {
        this.usersInAuction = usersInAuction;
    }
    public List<BidMessage> getBidHistory() {
        return bidHistory;
    }
    public void setBidHistory(List<BidMessage> bidHistory) {
        this.bidHistory = bidHistory;
    }
}
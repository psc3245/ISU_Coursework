package program.Posts.DTOs.PostDTOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import program.Users.dto.UserDTO;
import java.util.Date;

public class AuctionPostDTO extends PostDTO {
    private Float startingBid;
    private LocalDateTime auctionEndTime;
    private Float currentHighestBid;
    private List<UserDTO> usersInAuction;
    private List<BidMessageDTO> bidHistory = new ArrayList<>();

    public AuctionPostDTO(Integer postId, String title, String description, Float startingBid, LocalDateTime auctionEndTime) {
        super(postId, title, description);
        this.startingBid = startingBid;
        this.auctionEndTime = auctionEndTime;
    }

    public AuctionPostDTO() {}

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

    public Float getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(Float currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public List<UserDTO> getUsersInAuction() {
        return usersInAuction;
    }

    public void setUsersInAuction(List<UserDTO> usersInAuction) {
        this.usersInAuction = usersInAuction;
    }

    public List<BidMessageDTO> getBidHistory() {
        return bidHistory;
    }

    public void setBidHistory(List<BidMessageDTO> bidHistory) {
        this.bidHistory = bidHistory;
    }
    
}

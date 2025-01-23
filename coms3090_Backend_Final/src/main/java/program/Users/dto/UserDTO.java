package program.Users.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import program.Groups.Group;
import program.Posts.Post;
import program.Reviews.DTOs.ReviewDTO;
import program.Users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;

    private String university;

    private String email;

    private List<Integer> postIds;

    private List<Integer> ownedGroupIds;

    private List<Integer> memberGroupIds;

    private Set<Integer> following = new HashSet<>();

    private Set<Integer> followers = new HashSet<>();

    private List<ReviewDTO> userReviews;

    private Set<Integer> likedPostIds = new HashSet<>();

    private int followingCount;
    private int followerCount;

    // Getters and Setters

    public int getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for university
    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostIds(List<Integer> postIds) {
        this.postIds = postIds;
    }

    public void setMemberGroupIds(List<Integer> memberGroupIds) {
        this.memberGroupIds = memberGroupIds;
    }

    public void setOwnedGroupIds(List<Integer> ownedGroupIds) {
        this.ownedGroupIds = ownedGroupIds;
    }

    public List<Integer> getPostIds() { return this.postIds; }

    public List<Integer> getOwnedGroupIds() { return this.ownedGroupIds; }

    public List<Integer> getMemberGroupIds() { return this.memberGroupIds; }

    public Set<Integer> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Integer> following) {
        this.following = following;
    }

    public Set<Integer> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Integer> followers) {
        this.followers = followers;
    }


    public static UserDTO turnToDTO(User u) {
        UserDTO toReturn = new UserDTO();
        toReturn.setUserId(u.getUserId());
        toReturn.setUsername(u.getUsername());
        toReturn.setUniversity(u.getUniversity());
        toReturn.setEmail(u.getEmail());

        List<Integer> postIds = new ArrayList<>();
        for (Post p : u.getPosts()) {
            postIds.add(p.getPostId());
        }
        toReturn.setPostIds(postIds);

        List<Integer> memberGroupIds = new ArrayList<>();
        for (Group g : u.getMemberGroups()) {
            memberGroupIds.add(g.getGroupId());
        }
        toReturn.setMemberGroupIds(memberGroupIds);

        List<Integer> ownedGroupIds = new ArrayList<>();
        for (Group g : u.getOwnedGroups()) {
            ownedGroupIds.add(g.getGroupId());
        }
        toReturn.setOwnedGroupIds(ownedGroupIds);

        Set<Integer> followerIds = new HashSet<>();
        for (User other : u.getFollowers()) {
            followerIds.add(other.getUserId());
        }
        toReturn.setFollowers(followerIds);

        Set<Integer> followingIds = new HashSet<>();
        for (User other : u.getFollowing()) {
            followingIds.add(other.getUserId());
        }
        toReturn.setFollowers(followingIds);

        Set<Integer> likedPostIds = new HashSet<>();
        for (Post p : u.getLikedPosts()) {
            likedPostIds.add(p.getPostId());
        }
        toReturn.setLikedPostIds(likedPostIds);

        toReturn.setFollowerCount(u.getFollowerCount());
        toReturn.setFollowingCount(u.getFollowingCount());

        toReturn.setUserReviews(ReviewDTO.convertReviewsToReviewsDTO(u.getOthersReviews()));

        return toReturn;
    }


    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public Set<Integer> getLikedPostIds() {
        return likedPostIds;
    }

    public void setLikedPostIds(Set<Integer> likedPostIds) {
        this.likedPostIds = likedPostIds;
    }
    public List<ReviewDTO> getUserReviews() {
        return userReviews;
    }
    public void setUserReviews(List<ReviewDTO> reviewDTOs) {
        this.userReviews = reviewDTOs;
    }
}

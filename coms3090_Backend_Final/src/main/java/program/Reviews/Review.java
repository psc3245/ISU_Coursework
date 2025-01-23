package program.Reviews;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import program.Users.User;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;


    private String review;
    private String title;

    @Min(value = 0, message = "Ratings should be between 1 and 10")
    @Max(value = 10, message = "Ratings should be between 1 and 10")
    private Double rating;
    @ManyToOne
    @JoinColumn(name = "reviewer_id") // Column for the reviewer relationship
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewed_id") // Column for the reviewed relationship
    private User reviewed;

    @ManyToMany
    @JoinTable(
            name = "user_likes_reviews",
            joinColumns = @JoinColumn(name = "review_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> reviewLikedBy = new HashSet<>();

    
    public Review(String review, String title,
            @Min(value = 0, message = "Ratings should be between 1 and 10") @Max(value = 10, message = "Ratings should be between 1 and 10") Double rating,
            User reviewer, User reviewed) {
        this.review = review;
        this.title = title;
        this.rating = rating;
        this.reviewer = reviewer;
        this.reviewed = reviewed;
    }
    public Review() {
    }
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public User getReviewer() {
        return reviewer;
    }
    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }
    public User getReviewed() {
        return reviewed;
    }
    public void setReviewed(User reviewed) {
        this.reviewed = reviewed;
    }
    public Integer getReviewId() {
        return reviewId;
    }
    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }
    
}

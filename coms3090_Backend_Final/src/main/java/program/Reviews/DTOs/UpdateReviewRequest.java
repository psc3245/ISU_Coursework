package program.Reviews.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateReviewRequest {
    private Integer reviewId;
    private String review;
    private String title;

    @Min(value = 0, message = "Ratings should be between 1 and 10")
    @Max(value = 10, message = "Ratings should be between 1 and 10")
    private Double rating;


    public UpdateReviewRequest(Integer reviewId, String review, String title,
            @Min(value = 0, message = "Ratings should be between 1 and 10") @Max(value = 10, message = "Ratings should be between 1 and 10") Double rating) {
        this.reviewId = reviewId;
        this.review = review;
        this.title = title;
        this.rating = rating;
    }

    public UpdateReviewRequest() {
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

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    
}

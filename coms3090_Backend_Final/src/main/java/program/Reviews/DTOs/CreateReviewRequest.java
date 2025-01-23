package program.Reviews.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class CreateReviewRequest {
    private String review;
    private String title;

    @Min(value = 0, message = "Ratings should be between 1 and 10")
    @Max(value = 10, message = "Ratings should be between 1 and 10")
    private Double rating;

    private Integer reviewerId;

    private Integer reviewedId;

    public CreateReviewRequest(String review, String title,
            @Min(value = 0, message = "Ratings should be between 1 and 10") @Max(value = 10, message = "Ratings should be between 1 and 10") Double rating,
            Integer reviewerId, Integer reviewedId) {
        this.review = review;
        this.title = title;
        this.rating = rating;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
    }

    public CreateReviewRequest() {
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

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Integer getReviewedId() {
        return reviewedId;
    }

    public void setReviewedId(Integer reviewedId) {
        this.reviewedId = reviewedId;
    }
}

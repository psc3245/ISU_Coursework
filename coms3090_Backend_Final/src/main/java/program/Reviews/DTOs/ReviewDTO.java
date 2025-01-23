package program.Reviews.DTOs;

import java.util.ArrayList;
import java.util.List;

import program.Reviews.Review;

public class ReviewDTO {
    private Integer reviewId;

    private String review;
    private String title;

    private Double rating;

    private Integer reviewerId;
    private Integer reviewedId;

    public ReviewDTO(Integer reviewId, String review, String title, Double rating, Integer reviewerId,
            Integer reviewedId) {
        this.reviewId = reviewId;
        this.review = review;
        this.title = title;
        this.rating = rating;
        this.reviewerId = reviewerId;
        this.reviewedId = reviewedId;
    }

    public ReviewDTO() {
    }

    public Integer getReviewId() {
        return reviewId;
    }
    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
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
    

    public static ReviewDTO convertReviewToReviewDTO(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(review.getRating());
        reviewDTO.setReview(review.getReview());
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setReviewedId(review.getReviewed().getUserId());
        reviewDTO.setReviewerId(review.getReviewer().getUserId());
        reviewDTO.setTitle(review.getTitle());
        return reviewDTO;
    }
    public static List<ReviewDTO> convertReviewsToReviewsDTO(List<Review> reviews){
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for (Review review : reviews) {
            reviewDTOs.add(convertReviewToReviewDTO(review));
        }
        return reviewDTOs;
    }
}

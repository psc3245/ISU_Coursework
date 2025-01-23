package program.Reviews.Service;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import program.Reviews.Review;
import program.Reviews.DTOs.CreateReviewRequest;
import program.Reviews.DTOs.ReviewDTO;
import program.Reviews.DTOs.UpdateReviewRequest;
import program.Reviews.Repository.ReviewRepository;
import program.Users.User;
import program.Users.UserRepository;

@Service
public class ReviewService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    /*
     * We want:
     * Delete, Get, GetAll, getAll by reviewer, create, update and i think that is it right now
     */

    public ReviewDTO getReviewById(Integer reviewId){
        var reviewO = reviewRepository.findByReviewId(reviewId);
        if(! reviewO.isPresent()){
            throw new IllegalArgumentException("The Review does not exist");
        }
        Review review =reviewO.get();
        return ReviewDTO.convertReviewToReviewDTO(review);
    }

    public List<ReviewDTO> getReviews(){
        return ReviewDTO.convertReviewsToReviewsDTO(reviewRepository.findAll());
    }

    public List<ReviewDTO> getReviewsByUserId(Integer reviewerId){
        var reviewerO = userRepository.findByUserId(reviewerId);
        if(!reviewerO.isPresent()){
            throw new IllegalArgumentException("User does not exist");
        }
        User reviewer = reviewerO.get();
        return ReviewDTO.convertReviewsToReviewsDTO(reviewer.getReviews());
    }

    public String deleteReviewByReviewId(Integer reviewId){
        var reviewO = reviewRepository.findByReviewId(reviewId);
        if(!reviewO.isPresent()){
            throw new IllegalArgumentException("Review does not exist");
        }
        reviewRepository.delete(reviewO.get());
        return "deleted";
    }

    public ReviewDTO updateReview(UpdateReviewRequest updateReviewRequest){
        
        var reviewO =  reviewRepository.findByReviewId(updateReviewRequest.getReviewId());
        if(!reviewO.isPresent()){
            throw new IllegalArgumentException("Review does not exist");
        }
        Review review = reviewO.get();
        updateIfNotNull(updateReviewRequest.getRating(), review::setRating);
        updateIfNotNull(updateReviewRequest.getReview(), review::setReview);
        updateIfNotNull(updateReviewRequest.getTitle(), review::setTitle);
        reviewRepository.save(review);
        return ReviewDTO.convertReviewToReviewDTO(review);
    }

    public ReviewDTO createReview(CreateReviewRequest createReviewRequest){
        Review review = new Review();
        review.setRating(createReviewRequest.getRating());
        review.setReview(createReviewRequest.getReview());

        var reviewedO = userRepository.findByUserId(createReviewRequest.getReviewedId());
        if(!reviewedO.isPresent()){
            throw new IllegalArgumentException("The reviewed user does not exist");
        }

        var reviewerO = userRepository.findByUserId(createReviewRequest.getReviewerId());
        if(!reviewerO.isPresent()){
            throw new IllegalArgumentException("The reviewer user does not exist");
        }
        
        review.setReviewed(reviewedO.get());
        review.setReviewer(reviewerO.get());
        review.setTitle(createReviewRequest.getTitle());
        reviewRepository.save(review);


        return ReviewDTO.convertReviewToReviewDTO(review);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> updater) {
        if (value != null) {
            updater.accept(value);
        }
    }
}

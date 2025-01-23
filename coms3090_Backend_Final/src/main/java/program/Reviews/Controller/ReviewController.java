package program.Reviews.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import program.Reviews.Review;
import program.Reviews.DTOs.CreateReviewRequest;
import program.Reviews.DTOs.UpdateReviewRequest;
import program.Reviews.Service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@Validated
public class ReviewController {
    @Autowired
    private ReviewService reviewService;



    private <T> ResponseEntity<?> handleRequest(RequestExecutor<T> executor) {
        try {
            return new ResponseEntity<>(executor.execute(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @FunctionalInterface
    interface RequestExecutor<T> {
        T execute() throws Exception;
    }
    @GetMapping()
    public ResponseEntity<?> getAllReviews(){
        return handleRequest(() -> reviewService.getReviews());
    }
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable Integer reviewId){
        return handleRequest(()->reviewService.getReviewById(reviewId));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUserId(@PathVariable Integer userId){
        return handleRequest(()->reviewService.getReviewsByUserId(userId));
    }
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReviewByReviewId(@PathVariable Integer reviewId){
        return handleRequest(()->reviewService.deleteReviewByReviewId(reviewId));
    }
    @PutMapping()
    public ResponseEntity<?> updateReview(@RequestBody @Valid UpdateReviewRequest updateReviewRequest){
        return handleRequest(()->reviewService.updateReview(updateReviewRequest));
    }
    @PostMapping()
    public ResponseEntity<?> createReview(@RequestBody @Valid CreateReviewRequest createReviewRequest){
        return handleRequest(()->reviewService.createReview(createReviewRequest));
    }
    

    

    
}

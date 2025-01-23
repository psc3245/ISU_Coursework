package program.Reviews.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import program.Reviews.Review;



public interface ReviewRepository  extends  JpaRepository<Review,Integer>{
    Optional<Review> findByReviewId(Integer id);
    

}

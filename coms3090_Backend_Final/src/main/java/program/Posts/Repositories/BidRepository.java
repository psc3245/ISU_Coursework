package program.Posts.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import program.Posts.PostTypes.BidMessage;
@Repository
public interface BidRepository extends JpaRepository<BidMessage, Long> {
    Optional<BidMessage> findByBidId(Integer bidId);
}

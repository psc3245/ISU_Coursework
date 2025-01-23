package program.Posts.Repositories;

import jakarta.transaction.Transactional;
import program.Posts.Post;
import program.Users.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findByPostId(Integer postId);
    List<Post> findAllByTitleContainingIgnoreCase(String title);
    @Transactional
    void deleteByPostId(int id);

    List<Post> findAllByUser(User user);
}

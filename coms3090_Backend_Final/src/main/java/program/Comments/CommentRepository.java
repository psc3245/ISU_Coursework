package program.Comments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import program.Posts.Post;
import program.Users.User;

public interface CommentRepository extends  JpaRepository<Comment,Integer>{
    List<Comment> findAllByCommenter(User commenter);
    List<Comment> findAllByPost(Post post);
}

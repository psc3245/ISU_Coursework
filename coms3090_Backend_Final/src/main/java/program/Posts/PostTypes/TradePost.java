package program.Posts.PostTypes;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import program.Posts.Post;

@Entity
@DiscriminatorValue("TRADE")
public class TradePost extends Post {
    public TradePost(Integer postId, PostTypes type, String title, String description) {
        super(postId, type, title, description);
    }

    public TradePost() {
    }
    
}

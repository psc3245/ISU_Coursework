package program.Posts.PostTypes;
import program.Posts.Post;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ITEM_LISTING")
public class ItemListingPost extends Post{
    private Float price;

    public ItemListingPost(Integer postId, PostTypes type, String title, String description, Float price) {
        super(postId, type, title, description);
        this.price = price;
    }

    public ItemListingPost(Float price) {
        this.price = price;
    }
    public ItemListingPost(){}

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

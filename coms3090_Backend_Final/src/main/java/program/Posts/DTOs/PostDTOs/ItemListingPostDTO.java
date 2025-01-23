package program.Posts.DTOs.PostDTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ItemListingPostDTO extends PostDTO{
    private Float price;

    public ItemListingPostDTO(int postId, String title, String description, float price) {
        super(postId, title, description);
        this.price = price;
    }

    public ItemListingPostDTO() {
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

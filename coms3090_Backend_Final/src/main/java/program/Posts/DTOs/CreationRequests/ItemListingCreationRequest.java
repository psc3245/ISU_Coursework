package program.Posts.DTOs.CreationRequests;

import program.Posts.PostTypes.PostTypes;

import java.util.Date;

public class ItemListingCreationRequest extends PostCreationRequest{
    private Float price;

    public ItemListingCreationRequest() {}

    public ItemListingCreationRequest(PostTypes type, String title, String description, Float price) {
        super(type, title, description);
        this.price = price;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    
    
}

package program.Posts.DTOs.UpdateRequests;

import program.Posts.PostTypes.PostTypes;

public class ItemListingUpdateRequest extends PostUpdateRequest{
    private Float price;

    public ItemListingUpdateRequest() {}

    public ItemListingUpdateRequest(PostTypes type, String title, String description, Float price) {
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

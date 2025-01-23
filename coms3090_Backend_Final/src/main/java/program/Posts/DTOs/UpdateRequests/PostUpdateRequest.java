package program.Posts.DTOs.UpdateRequests;

import program.Posts.PostTypes.PostTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

/*
these two annotations basically make it so that when the api parses the json passed in by the front end it will automatically 
convert it to the appropiate postUpdateRequest child class based on the type attribute
*/
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true // Ensures 'type' is available for deserialization
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuctionUpdateRequest.class, name = "AUCTION"),
    @JsonSubTypes.Type(value = ItemListingUpdateRequest.class, name = "ITEM_LISTING"),
    @JsonSubTypes.Type(value = TradeUpdateRequest.class, name = "TRADE")
})
public abstract class PostUpdateRequest {
    private PostTypes type;

    private String title;

    private String description;
    

    public PostUpdateRequest() {}

    public PostUpdateRequest(PostTypes type, String title, String description) {
        this.type = type;
        this.title = title;
        this.description = description;
    }

    public PostTypes getType() {
        return type;
    }

    public void setType(PostTypes type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

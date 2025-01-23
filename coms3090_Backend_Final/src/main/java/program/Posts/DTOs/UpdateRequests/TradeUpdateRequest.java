package program.Posts.DTOs.UpdateRequests;

import program.Posts.PostTypes.PostTypes;

public class TradeUpdateRequest extends PostUpdateRequest{
    public TradeUpdateRequest(){}
    
    public TradeUpdateRequest(PostTypes type, String title, String description) {
        super(type,title,description);
    }
}

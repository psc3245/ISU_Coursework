package program.Posts.DTOs.CreationRequests;

import program.Posts.PostTypes.PostTypes;

import java.util.Date;

public class TradeCreationRequest extends PostCreationRequest{
    public TradeCreationRequest(){}
    
    public TradeCreationRequest(PostTypes type, String title, String description) {
        super(type,title,description);
    }
}
package program.Posts.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import program.Comments.DTOs.CommentDTO;
import program.Groups.Group;
import program.Groups.GroupRepository;
import program.Posts.Comparators.PostDTODateComparator;
import program.Posts.Comparators.PostDTOLikeComparator;
import program.Posts.Comparators.PostLikeComparator;
import program.Posts.Post;
import program.Posts.Comparators.PostDateComparator;
import program.Posts.DTOs.CreationRequests.AuctionCreationRequest;
import program.Posts.DTOs.CreationRequests.ItemListingCreationRequest;
import program.Posts.DTOs.CreationRequests.PostCreationRequest;
import program.Posts.DTOs.PostDTOs.AuctionPostDTO;
import program.Posts.DTOs.PostDTOs.BidMessageDTO;
import program.Posts.DTOs.PostDTOs.ItemListingPostDTO;
import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.DTOs.PostDTOs.TradePostDTO;
import program.Posts.DTOs.UpdateRequests.AuctionUpdateRequest;
import program.Posts.DTOs.UpdateRequests.ItemListingUpdateRequest;
import program.Posts.DTOs.UpdateRequests.PostUpdateRequest;
import program.Posts.DTOs.UpdateRequests.TradeUpdateRequest;
import program.Posts.PostTypes.AuctionPost;
import program.Posts.PostTypes.BidMessage;
import program.Posts.PostTypes.ItemListingPost;
import program.Posts.PostTypes.TradePost;
import program.Posts.Repositories.PostRepository;
import program.Users.User;
import program.Users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import program.Users.Service.UserService;
import program.Users.dto.UserDTO;

@Service
//postDTO should work for all types of post, postDTO are good for broad overview of posts. Once a user clicks into the post, we need DTOs for each type so that relavant information is sent to the front end.
public class PostService{
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    
    public static PostDTO convertPostToPostDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setDescription(post.getDescription());
        postDTO.setPostId(post.getPostId());
        postDTO.setTitle(post.getTitle());
        postDTO.setType(post.getType());
        postDTO.setUserId(post.getUser().getUserId());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setDate(post.getDate());
        postDTO.setLikes(post.getLikedBy().size());
        HashSet<Integer> likedByUserIds = new HashSet<>();
        for(User u : post.getLikedBy()) {
            likedByUserIds.add(u.getUserId());
        }
        postDTO.setLikedByUserIds(likedByUserIds);
        return postDTO;
        //convert ANY post to postDTO
    }


    public PostDTO convertPostToRelevantPostDTO(Post post){
        PostDTO ret = null;
        switch(post.getType()){
            case AUCTION:
                ret = createAuctionPostDTO(post);
                break;
            case DISCUSSION:
                //ret = createAuctionDTO(post);
                break;
            case ITEM_LISTING:
                ret = createItemListingPostDTO(post);
                break;
            case TRADE:
                ret = createTradePostDTO(post);
                break;
            default:
                throw new IllegalArgumentException("You are missing a type variable");
        }
        if(ret != null){
            return ret;
        }
        throw new IllegalArgumentException("This exception is triggered by a faulty createDTO method");
        //convert a post to it's corrosponding postDTO type
    }

    private void postDTOHelper(Post post, PostDTO postDTO){
        postDTO.setDescription(post.getDescription());
        postDTO.setPostId(post.getPostId());
        postDTO.setTitle(post.getTitle());
        postDTO.setType(post.getType());
        postDTO.setUserId(post.getUser().getUserId());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setDate(post.getDate());
        postDTO.setComments(CommentDTO.CommentListToCommentDTOList(post.getComments()));
        postDTO.setLikes(post.getLikedBy().size());
        HashSet<Integer> likedByUserIds = new HashSet<>();
        for(User u : post.getLikedBy()) {
            likedByUserIds.add(u.getUserId());
        }
        postDTO.setLikedByUserIds(likedByUserIds);
    }
    private AuctionPostDTO createAuctionPostDTO(Post post){
        AuctionPostDTO postDTO = new AuctionPostDTO();
        postDTOHelper(post, postDTO);
        AuctionPost auctionPost = (AuctionPost)post;
        postDTO.setAuctionEndTime(auctionPost.getAuctionEndTime());
        postDTO.setStartingBid(auctionPost.getStartingBid());
        postDTO.setCurrentHighestBid(auctionPost.getCurrentHighestBid());
        List<BidMessageDTO> bidDTOs = new ArrayList<>();
        auctionPost.getBidHistory().forEach((bid) -> {
            bidDTOs.add(convertBidtoBidDTO(bid));
        });
        postDTO.setBidHistory(bidDTOs);
        List<UserDTO> users = auctionPost.getUsersInAuction().stream().map(user -> UserDTO.turnToDTO(user)).collect(Collectors.toList()) ;
        postDTO.setUsersInAuction(users);

        return postDTO;
    }
    private ItemListingPostDTO createItemListingPostDTO(Post post){
        ItemListingPostDTO postDTO = new ItemListingPostDTO();
        postDTOHelper(post, postDTO);
        ItemListingPost itemListingPost = (ItemListingPost)post;
        postDTO.setPrice(itemListingPost.getPrice());
        return postDTO;

    }
    private TradePostDTO createTradePostDTO(Post post){
        TradePostDTO tradePostDTO = new TradePostDTO();
        postDTOHelper(post, tradePostDTO);
        return tradePostDTO;
    }




    public Post createPost(PostCreationRequest creationRequest) throws IllegalAccessException{
        Post ret = null;
        switch (creationRequest.getType()) {
            case AUCTION:
                ret = createAuctionPost(creationRequest);
                break;
            case DISCUSSION:
                //createDiscussionPost(creationRequest); need to implement
                break;
            case ITEM_LISTING:
                ret =createItemListingPost(creationRequest);
                break;
            case TRADE:
                ret =createTradePost(creationRequest);
                break;
            default:
                throw new IllegalArgumentException("You are missing a type variable");
        }
        if(ret != null){
            postRepository.save(ret);
            return ret;
        }
        throw new IllegalArgumentException("This exception is triggered by a faulty create method");
        
    }
    
    private void postCreationHelper(Post post, PostCreationRequest postCreationRequest) throws IllegalAccessException {
        var userOptional = userRepository.findByUserId(postCreationRequest.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalAccessException("User does not exist");
        }
        post.setDescription(postCreationRequest.getDescription());
        post.setTitle(postCreationRequest.getTitle());
        post.setType(postCreationRequest.getType());
        post.setUser(userOptional.get());
        post.setDate(LocalDateTime.now());

        if (postCreationRequest.getGroupId() != null) {
            if (groupRepository.existsByGroupId(postCreationRequest.getGroupId())) {
                Group g = groupRepository.findByGroupId(postCreationRequest.getGroupId()).get();
                post.setGroup(g);
            }
        }
    }
    
    private AuctionPost createAuctionPost(PostCreationRequest creationRequest) throws IllegalAccessException {
        // TODO A lot of auction logic
        AuctionPost post = new AuctionPost();
        postCreationHelper(post, creationRequest);
        AuctionCreationRequest auctionCreationRequest = (AuctionCreationRequest)creationRequest;
        post.setAuctionEndTime(auctionCreationRequest.getAuctionEndTime());
        post.setStartingBid(auctionCreationRequest.getStartingBid());
        post.setCurrentHighestBid(auctionCreationRequest.getStartingBid());
        post.setUsersInAuction(new ArrayList<User>());
        post.setBidHistory(new ArrayList<BidMessage>());
        return post;
        
    }
    private ItemListingPost createItemListingPost(PostCreationRequest creationRequest) throws IllegalAccessException {
        ItemListingPost post = new ItemListingPost();
        postCreationHelper(post, creationRequest);
        ItemListingCreationRequest itemListingCreationRequest = (ItemListingCreationRequest)creationRequest;
        post.setPrice(itemListingCreationRequest.getPrice());
        return post;
    }
    private TradePost createTradePost(PostCreationRequest creationRequest) throws IllegalAccessException {
        TradePost post = new TradePost();
        postCreationHelper(post, creationRequest);
        return post;
    }

    //i guess do the same logic as creation except for checking for nulls?
    public PostDTO updatePost(PostUpdateRequest updateRequest, Integer postId) {
        var post = postRepository.findByPostId(postId);
        if (!post.isPresent()) {
            throw new IllegalArgumentException("Post does not exist");
        }
        Post updatePost = post.get();
        
        switch (updatePost.getType()) {
            case AUCTION:
                updateAuctionPost((AuctionPost) updatePost, (AuctionUpdateRequest) updateRequest);
                break;
            case DISCUSSION:
                // Implement discussion post update logic if needed
                break;
            case ITEM_LISTING:
                updateItemListingPost((ItemListingPost) updatePost, (ItemListingUpdateRequest) updateRequest);
                break;
            case TRADE:
                updateTradePost((TradePost) updatePost, (TradeUpdateRequest) updateRequest);
                break;
            default:
                throw new IllegalArgumentException("Invalid post type");
        }
        postRepository.save(updatePost);
        return convertPostToRelevantPostDTO(updatePost);
    }
    
    private void updatePostHelper(Post post, PostUpdateRequest updateRequest){
        if (updateRequest.getTitle() != null) {
            post.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            post.setDescription(updateRequest.getDescription());
        }
    }
    private void updateAuctionPost(AuctionPost post, AuctionUpdateRequest updateRequest) {
        updatePostHelper(post, updateRequest);
        if (updateRequest.getStartingBid() != null && updateRequest.getStartingBid() > 0) {
            post.setStartingBid(updateRequest.getStartingBid());
        }
        if (updateRequest.getAuctionEndTime() != null) {
            post.setAuctionEndTime(updateRequest.getAuctionEndTime());
        }
    }
    
    private void updateItemListingPost(ItemListingPost post, ItemListingUpdateRequest updateRequest) {
        updatePostHelper(post, updateRequest);
        if (updateRequest.getPrice() != null && updateRequest.getPrice() >0 ) {
            post.setPrice(updateRequest.getPrice());
        }
    }
    
    private void updateTradePost(TradePost post, TradeUpdateRequest updateRequest) {
        updatePostHelper(post, updateRequest);
        // Add other trade-specific fields here as needed
    }


    public BidMessageDTO convertBidtoBidDTO(BidMessage bid){
        BidMessageDTO dto = new BidMessageDTO();
        dto.setAuctionId(bid.getAuctionPost().getPostId());
        dto.setBidAmount(bid.getBidAmount());
        dto.setBidId(bid.getBidId());
        dto.setUserId(bid.getUserId());
        return dto;
    }
    public static List<Post> sortPostsByDate(List<Post> posts) {
        Collections.sort(posts, new PostDateComparator());
        return posts;
    }

    public static List<PostDTO> sortPostDTOsByDate(List<PostDTO> posts) {
        Collections.sort(posts, new PostDTODateComparator());
        return posts;
    }

    public static List<Post> sortPostsByLikes(List<Post> posts) {
        Collections.sort(posts, new PostLikeComparator());
        return posts;
    }

    public static List<PostDTO> sortPostDTOByLikes(List<PostDTO> posts) {
        Collections.sort(posts, new PostDTOLikeComparator());
        return posts;
    }

}

    

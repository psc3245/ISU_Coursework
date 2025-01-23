package program.Posts.Controllers;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import program.Posts.Post;
import program.Posts.PostTypes.AuctionPost;
import program.Posts.PostTypes.BidMessage;
import program.Posts.Repositories.BidRepository;
import program.Posts.Repositories.PostRepository;
import program.Users.User;
import program.Users.UserRepository;
import program.chatRooms.ChatSocket.ChatSocket;


@Controller
@ServerEndpoint(value = "/api/posts/auction/{postId}/{userId}")
public class AuctionWebSocketController{
    
    private static PostRepository postRepository;
    
    private static UserRepository userRepository;
    private static BidRepository bidRepository;

    private static Map<Integer, Map<Session, String>> auctionSessionMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
    public AuctionWebSocketController(){}
    @Autowired
    public void setPostRepository(PostRepository postRepo) {
        postRepository = postRepo;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepo) {
        userRepository = userRepo;
    }

    @Autowired
    public void setBidRepository(BidRepository bidRepo) {
        bidRepository = bidRepo;
    }
    

    private final Logger logger = LoggerFactory.getLogger(AuctionWebSocketController.class);

    @OnOpen
    public void OnOpen(Session session, @PathParam("postId") Integer postId, @PathParam("userId") Integer userId) throws IOException {
        logger.info("Entered into Auction " + postId);

        //Check to see if user exists
        Optional<User> u = userRepository.findByUserId(userId);
        if (u.isEmpty()) {
            logger.info("User not found");
            // This may work ??
            session.close();
            return;
        }

        // Now we know user exists, get their username
        String username = u.get().getUsername();

        // store connecting user information
        usernameSessionMap.put(username, session);
        if (auctionSessionMap.get(postId) == null){
            auctionSessionMap.put(postId, new Hashtable<Session, String>());
        }
        auctionSessionMap.get(postId).put(session, username);

        //Send bid history to the newly connected user
        sendMessageToParticularUser(username, getBidHistory(postId));

        // broadcast that new user joined
        String message = "User " + username + " has Joined the Chat";
        broadcast(postId, message);
    }
    @OnClose
    public void OnClose(Session session, @PathParam("postId") Integer postId, @PathParam("userId") Integer userId) throws IOException {
        if (!session.isOpen()) {
            System.out.println("test");
            return;
        }

        logger.info("Entered into Close");

        // remove the user connection information
        String username = getUsernameFromSession(session, postId);
        auctionSessionMap.get(postId).remove(session);
        usernameSessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " disconnected";
        broadcast(postId, message);
    }
    @OnMessage
    public void OnMessage(Session session, @PathParam("postId") Integer postId, @PathParam("userId") Integer userId, String message) throws IOException {
        // Parse the incoming message
        BidMessage bidMessage = new BidMessage();
        bidMessage.setAuctionPost((AuctionPost) postRepository.findByPostId(postId).get());
        bidMessage.setUserId(userId);
        bidMessage.setBidAmount(Float.parseFloat(message));

        // Retrieve the auction post from the repository
        Optional<Post> auctionPostOpt = postRepository.findByPostId(postId);
        if (auctionPostOpt.isEmpty()) {
            session.getBasicRemote().sendText("Auction not found");
            return;
        }

        AuctionPost auctionPost = (AuctionPost) auctionPostOpt.get();

        // Check if the bid is higher than the current highest bid
        if (bidMessage.getBidAmount() > auctionPost.getCurrentHighestBid()) {
            // Update the highest bid

            auctionPost.getBidHistory().add(bidMessage);
            auctionPost.setCurrentHighestBid(bidMessage.getBidAmount());

            // Ensure that the auctionPost is saved with the updated bid history
            postRepository.saveAndFlush(auctionPost);  // Use saveAndFlush to ensure the update is persisted

            // Now merge the BidMessage into the persistence context to avoid detached entity issues
            // bidRepository.saveAndFlush(bidMessage);  // Use saveAndFlush here

            // Broadcast the new highest bid to all users in this auction
            String broadcastMessage = userRepository.findByUserId(userId).get().getUsername() + ": " + message;
            broadcast(postId, broadcastMessage);
        } else {
            // Inform the user that the bid was too low
            session.getBasicRemote().sendText("Bid is too low. Please bid higher than the current highest bid.");
        }
    }

    @OnError
    public void OnError(Session session, Throwable throwable) throws IOException {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void broadcast(int postId, String message) {
        Map<Session, String> sessionMap = auctionSessionMap.get(postId);
        sessionMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });
    }
    private String getUsernameFromSession(Session session, int roomId) {
        // Get session map from hashtable with room id
        Map<Session, String> sessionMap = auctionSessionMap.get(roomId);
        // Ensure both the session map exists and the given session exists in the map
        if (sessionMap == null || sessionMap.get(session) == null) return null;
        // Return the username
        return sessionMap.get(session);
    }
    private String getBidHistory(Integer postId){
        StringBuilder sb = new StringBuilder();
        AuctionPost auction = (AuctionPost)postRepository.findByPostId(postId).get();
        for (BidMessage bid : auction.getBidHistory()) {
            String bidStr = userRepository.findByUserId(bid.getUserId()).get().getUsername() + ": " + bid.getBidAmount() +"\n";
            sb.append(bidStr);
        }
        return sb.toString();
    }
    private void sendMessageToParticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
}

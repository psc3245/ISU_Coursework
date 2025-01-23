package program;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import program.Comments.Comment;
import program.Comments.CommentRepository;
import program.Groups.GroupRepository;
import program.Posts.Post;
import program.Posts.PostTypes.PostTypes;
import program.Posts.Repositories.PostRepository;
import program.Users.User;
import program.Users.UserRepository;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest(classes = UnitradeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AllSystemTests {

    @LocalServerPort
    int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Make a user to use during tests
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String smitty = """
            {
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(smitty).
                when().
                post("/api/auth/signup");
    }

    @Test
    public void testPostCreationReturnsCorrectStatusCode() {
        String jason = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/posts");

        String errorResponse = response.getBody().asString();
        System.out.println("Error response: " + errorResponse);


        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
    }

    @Test
    public void testPostCreationCreatesPostCorrectly() {
        String jason = """
            {
              "title": "Test Post",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/posts");

        Post post = postRepository.findByPostId(1).get();

        assertEquals("Test Post", post.getTitle());
        assertEquals("Desc", post.getDescription());
        assertEquals(1, post.getUser().getUserId());
        assertEquals(PostTypes.ITEM_LISTING, post.getType());
    }

    @Test
    public void testPostCreationAppearsInDatabaseAndIsRetrieveable() {
        String jason = """
            {
              "title": "Test Post",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(jason)
                .when()
                .post("/api/posts");

        Post post = postRepository.findById(1).orElse(null);
        assertEquals(post.getTitle(), "Test Post");

    }

    @Test
    public void testGetAllPostsFunctionsProperly() {
        String postA = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(postA)
                .when()
                .post("/api/posts");

        String postB = """
            {
              "title": "Test Post B",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(postB)
                .when()
                .post("/api/posts");

        String postC = """
            {
              "title": "Test Post C",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(postC)
                .when()
                .post("/api/posts");


        List<Post> posts = postRepository.findAll();

        assertEquals(posts.size(), 3);

        assertEquals(posts.get(0).getTitle(), "Test Post A");
        assertEquals(posts.get(1).getTitle(), "Test Post B");
        assertEquals(posts.get(2).getTitle(), "Test Post C");
    }

    @Test
    public void testUpdatePostFunctionsProperly() {
        String postA = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(postA)
                .when()
                .post("/api/posts");

        assertEquals(201, response.getStatusCode());

        String updatedPost = """
            {
              "title": "Updated",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        Response updatedResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(updatedPost)
                .when()
                .put("/api/posts/1");

        assertEquals(200, updatedResponse.getStatusCode());

        Post post = postRepository.findById(1).orElse(null);
        assertEquals(post.getTitle(), "Updated");
    }

    @Test
    public void testInvalidUpdateRequest() {
        // Create the initial post
        String postA = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;

        // Send POST request to create the post
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(postA)
                .when()
                .post("/api/posts");

        // Assert that the post creation was successful (status 201)
        assertEquals(201, response.getStatusCode());

        // Create an invalid update request (missing required fields)
        String updatedPost = """
            {,
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;

        // Send PUT request to update the post
        Response updatedResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(updatedPost)
                .when()
                .put("/api/posts/1");

        // Assert that the response status is 400 (BAD_REQUEST) since it should trigger an exception
        assertEquals(400, updatedResponse.getStatusCode());

        // Parse the response body as a JSON object
        String responseBody = updatedResponse.getBody().asString();

        // Check for error message in the response body
        assertTrue(responseBody.contains("Bad Request"));
        assertTrue(responseBody.contains("path"));

        // If you want to check more specifically, you can parse the JSON and validate individual fields
        String errorMessage = updatedResponse.jsonPath().getString("error");
        assertEquals("Bad Request", errorMessage);

        String path = updatedResponse.jsonPath().getString("path");
        assertEquals("/api/posts/1", path);

        // Check timestamp and other fields as needed
        String timestamp = updatedResponse.jsonPath().getString("timestamp");
        assertNotNull(timestamp); // Just to make sure the timestamp is present
    }

    @Test
    public void testBadUpdateRequest() {
        String postA = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(postA)
                .when()
                .post("/api/posts");

        assertEquals(201, response.getStatusCode());

        String updatedPost = """
            {,
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        Response updatedResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(updatedPost)
                .when()
                .put("/api/posts/1");

        assertEquals(400, updatedResponse.getStatusCode());

    }

    @Test
    public void testEachPostType() {
        String item_listing = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(item_listing)
                .when()
                .post("/api/posts");
        String auction_post = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "AUCTION"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(auction_post)
                .when()
                .post("/api/posts");
        String trade_post = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "TRADE"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(trade_post)
                .when()
                .post("/api/posts");

        List<Post> posts = postRepository.findAll();

        assertEquals(posts.get(0).getType(), PostTypes.ITEM_LISTING);
        assertEquals(posts.get(1).getType(), PostTypes.AUCTION);
        assertEquals(posts.get(2).getType(), PostTypes.TRADE);
    }

    @Test
    public void testSortedPostsWorksCorrectly() throws InterruptedException, JsonProcessingException {
        String first = """
            {
              "title": "First",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        Thread.sleep(1000);

        String middle = """
            {
              "title": "Middle",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(middle)
                .when()
                .post("/api/posts");

        Thread.sleep(1000);

        String last = """
            {
              "title": "Last",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(last)
                .when()
                .post("/api/posts");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/sorted");

        Response response2 = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts");
    }

    @Test
    public void testDeletePostDeletesThePost() {
        String first = """
            {
              "title": "First",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .delete("/api/posts/1");
        assertEquals(response.statusCode(), 204);

        List<Post> posts = postRepository.findAll();
        assertEquals(posts.size(), 0);
    }

    // TODO
    @Test
    public void testLikePost() {
        String first = """
            {
              "title": "First",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .post("/api/posts/2/like/1");

        Response r = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/1");

        int likes = r.jsonPath().getInt("likes");
        assertEquals(likes, 1);

    }

    @Test
    public void testUnLikePost() {
        String first = """
            {
              "title": "First",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .post("/api/posts/2/like/1");

        Response r = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/1");

        int likes = r.jsonPath().getInt("likes");
        assertEquals(likes, 1);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .post("/api/posts/2/unlike/1");


        Response newr = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/1");

        int newlikes = newr.jsonPath().getInt("likes");
        assertEquals(newlikes, 0);

    }

    @Test
    public void testUserSignUp() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");
        User u = userRepository.findByUserId(1).get();
        assertEquals("first_user", u.getUsername());
    }

    @Test
    public void testUserLogin() {
        String signup = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(signup).
                when().
                post("/api/auth/signup");

        User u = userRepository.findByUserId(1).get();
        assertEquals("first_user", u.getUsername());

        String login = """
            {
              "usernameOrEmail": "fart@iastate.edu",
              "password": "fart"
            }
            """;
        Response r = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(login).
                when().
                post("/api/auth/login");

        assertEquals(200, r.statusCode());
    }

    @Test
    public void testThatGetUsersFunctionsFrFr() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String smitty = """
            {
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(smitty).
                when().
                post("/api/auth/signup");

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void testThatUpdateUsersFunctionsFRFR() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String update = """
            {
              "userId" : 1,
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(update).
                when().
                put("/api/users");

        User u = userRepository.findByUserId(2).get();
        assertEquals("other_user", u.getUsername());
    }

    @Test
    public void testUserCanBeDeleted() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        Response r = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                when().
                delete("/api/users/1");

        assertEquals(204, r.statusCode());

        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    @Transactional
    public void testUserCanBeFollowed() {
        // Create first user (follower)
        String jason = """
        {
          "username": "first_user",
          "password": "fart",
          "email": "fart@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create second user (following)
        String smitty = """
        {
          "username": "other_user",
          "password": "fart",
          "email": "fart2@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(smitty)
                .when()
                .post("/api/auth/signup");

        // Explicitly retrieve and follow users
        User follower = userRepository.findByUserId(1).orElseThrow();
        User following = userRepository.findByUserId(2).orElseThrow();

        // Manually create follow relationship
        follower.follow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Retrieve users again
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();

        // Assertions
        assertTrue(follower.getFollowing().contains(following),
                "Follower should be following the other user. Follower: " + follower + ", Following: " + following);
        assertTrue(following.getFollowers().contains(follower),
                "Following should have the follower");

        // Check the follow counts
        assertEquals(1, follower.getFollowingCount(), "Follower should be following one user");
        assertEquals(1, following.getFollowerCount(), "Following should have one follower");
    }

    @Test
    @Transactional
    public void testUserCanBeUnfollowed() {
        // Create first user (follower)
        String jason = """
        {
          "username": "first_user",
          "password": "fart",
          "email": "fart@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create second user (following)
        String smitty = """
        {
          "username": "other_user",
          "password": "fart",
          "email": "fart2@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(smitty)
                .when()
                .post("/api/auth/signup");

        // Explicitly retrieve and follow users
        User follower = userRepository.findByUserId(1).orElseThrow();
        User following = userRepository.findByUserId(2).orElseThrow();

        // First, establish a follow relationship
        follower.follow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Verify initial follow state
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();
        assertEquals(1, follower.getFollowingCount(), "Follower should be following one user before unfollowing");
        assertEquals(1, following.getFollowerCount(), "Following should have one follower before unfollowing");

        // Manually unfollow the user
        follower.unfollow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Retrieve users again
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();

        // Assertions
        assertFalse(follower.getFollowing().contains(following),
                "Follower should not be following the other user after unfollowing");
        assertFalse(following.getFollowers().contains(follower),
                "Following should not have the follower after unfollowing");

        // Check the follow counts
        assertEquals(0, follower.getFollowingCount(), "Follower should not be following any users");
        assertEquals(0, following.getFollowerCount(), "Following should have no followers");

        // Additional database verification
        List<?> relationships = entityManager.createNativeQuery(
                "SELECT * FROM user_following WHERE follower_id = 1 AND following_id = 2"
        ).getResultList();

        assertTrue(relationships.isEmpty(), "Follow relationship should not exist in database");
    }

    @Test
    public void testGetAllPostsFromUserId() {
        // Make a user to use during tests
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String smitty = """
            {
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(smitty).
                when().
                post("/api/auth/signup");

        String item_listing = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(item_listing)
                .when()
                .post("/api/posts");
        String auction_post = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "AUCTION"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(auction_post)
                .when()
                .post("/api/posts");
        String trade_post = """
            {
              "title": "Test Post A",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "TRADE"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(trade_post)
                .when()
                .post("/api/posts");

        Response r = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(trade_post)
                .when()
                .get("/api/users/1/posts");

        JsonPath jsonPath = r.jsonPath();
        List<Map<String, Object>> posts = jsonPath.getList("$");

        int size = posts.size();
        assertEquals(3, size, "Expected size of posts array is incorrect");
    }

    @Test
    public void testLikedByWorksProperly() {
        // Make a user to use during tests
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String smitty = """
            {
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(smitty).
                when().
                post("/api/auth/signup");

        String first = """
            {
              "title": "First",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .post("/api/posts/2/like/1");

        Response r = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/1");

        r.body().prettyPrint();

        JsonPath jsonPath = r.jsonPath();
        List<Integer> likedByUserIds = jsonPath.getList("likedByUserIds", Integer.class);

        // Check the size and values
        assertEquals(1, likedByUserIds.size(), "LikedByUserIds size should be 1");
        assertEquals(2, likedByUserIds.get(0), "LikedByUserIds should contain userId 2");
    }

    @Test
    @Transactional
    public void testGetAllPostsFromFollowing() {
        // Create first user (follower)
        String jason = """
        {
          "username": "first_user",
          "password": "fart",
          "email": "fart@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create second user (following)
        String smitty = """
        {
          "username": "other_user",
          "password": "fart",
          "email": "fart2@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(smitty)
                .when()
                .post("/api/auth/signup");

        // Explicitly retrieve and follow users
        User follower = userRepository.findByUserId(1).orElseThrow();
        User following = userRepository.findByUserId(2).orElseThrow();

        // Manually create follow relationship
        follower.follow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Retrieve users again
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();

        // Assertions
        assertTrue(follower.getFollowing().contains(following),
                "Follower should be following the other user. Follower: " + follower + ", Following: " + following);
        assertTrue(following.getFollowers().contains(follower),
                "Following should have the follower");

        // Check the follow counts
        assertEquals(1, follower.getFollowingCount(), "Follower should be following one user");
        assertEquals(1, following.getFollowerCount(), "Following should have one follower");

        String post = """
            {
              "title": "Test Post",
              "description": "Desc",
              "userId": 2,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(post).
                when().
                post("/api/posts");

        Response response = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(post).
                when().
                get("/api/users/following/2/posts");
        response.body().prettyPrint();
    }


    @Test
    public void testGetAllGroups() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/groups/all")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testGetGroupById_Success() {
        // Create a group first
        String requestBody = "{" +
                "\"groupName\": \"Test Group\"," +
                "\"groupDescription\": \"Description for Test Group\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Private\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/groups");


        int statusCode = RestAssured.given()
                .pathParam("id", 1)
                .when()
                .get("/api/groups/{id}")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testGetGroupById_NotFound() {
        int invalidGroupId = 9999;

        int statusCode = RestAssured.given()
                .pathParam("id", invalidGroupId)
                .when()
                .get("/api/groups/{id}")
                .getStatusCode();
        assertEquals(400, statusCode);
    }

    @Test
    public void testCreateGroup_Success() {
        String requestBody = "{" +
                "\"groupName\": \"Test Group\"," +
                "\"groupDescription\": \"Description for Test Group\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Private\"}";

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/groups")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testDeleteGroup_Success() {
        int groupId = 1; // Replace with a valid group ID

        int statusCode = RestAssured.given()
                .pathParam("id", groupId)
                .when()
                .delete("/api/groups/{id}")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testDeleteGroup_NotFound() {
        int invalidGroupId = 9999;

        int statusCode = RestAssured.given()
                .pathParam("id", invalidGroupId)
                .when()
                .delete("/api/groups/{id}")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testAddMembers_Success() {
        String request = "{" +
                "\"groupName\": \"Test Group\"," +
                "\"groupDescription\": \"Description for Test Group\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Private\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/groups");


        String requestBody = "{" +
                "\"groupId\": 1," +
                "\"userId\": 2}";

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/groups/addMember")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testAddMembers_UserAlreadyInGroup() {
        String requestBody = "{" +
                "\"groupId\": 1," +
                "\"userId\": 1}"; // User already in group

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/groups/addMember")
                .getStatusCode();
        assertEquals(400, statusCode);
    }

    @Test
    public void testGetMembers_Success() {
        // Create a group first
        String requestBody = "{" +
                "\"groupName\": \"Test Group\"," +
                "\"groupDescription\": \"Description for Test Group\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Private\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/groups");

        int groupId = 1; // Replace with a valid group ID

        int statusCode = RestAssured.given()
                .pathParam("groupId", groupId)
                .when()
                .get("/api/groups/{groupId}/members")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testUpdateGroup_Success() {
        String createRequestBody = "{" +
                "\"groupName\": \"Initial Group\"," +
                "\"groupDescription\": \"Initial description\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Private\"}";

        int groupId = 1;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createRequestBody)
                .when()
                .post("/api/groups");

        String updateRequestBody = "{" +
                "\"groupId\": " + groupId + "," +
                "\"groupName\": \"Updated Group\"," +
                "\"groupDescription\": \"Updated description\"," +
                "\"groupType\": \"Public\"}";

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateRequestBody)
                .when()
                .put("/api/groups")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testUpdateGroup_NotFound() {
        String updateRequestBody = "{" +
                "\"groupId\": 9999," +
                "\"groupName\": \"Nonexistent Group\"," +
                "\"groupDescription\": \"No description\"," +
                "\"groupType\": \"Public\"}";

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateRequestBody)
                .when()
                .put("/api/groups")
                .getStatusCode();
        assertEquals(400, statusCode);
    }


    @Test
    public void testGetGroupPosts_Success() {
        String groupRequestBody = "{" +
                "\"groupName\": \"Group for Posts\"," +
                "\"groupDescription\": \"Group description\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Private\"}";

        int groupId = 1;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(groupRequestBody)
                .when()
                .post("/api/groups");

        int statusCode = RestAssured.given()
                .pathParam("groupId", groupId)
                .when()
                .get("/api/groups/{groupId}/posts")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testGetGroupPosts_NotFound() {
        int invalidGroupId = 9999;

        int statusCode = RestAssured.given()
                .pathParam("groupId", invalidGroupId)
                .when()
                .get("/api/groups/{groupId}/posts")
                .getStatusCode();
        assertEquals(400, statusCode);
    }

    @Test
    public void testCreateGroup_InvalidData() {
        String invalidRequestBody = "{}"; // Missing required fields

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/api/groups")
                .getStatusCode();
        assertEquals(400, statusCode);
    }

    @Test
    public void testAddMember_InvalidData() {
        String invalidRequestBody = "{}"; // Missing required fields

        int statusCode = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/api/groups/addMember")
                .getStatusCode();
        assertEquals(400, statusCode);
    }

    @Test
    public void testDeleteGroup_Unauthorized() {
        int groupId = 1; // Assume this group exists but belongs to another user

        int statusCode = RestAssured.given()
                .pathParam("id", groupId)
                .when()
                .delete("/api/groups/{id}")
                .getStatusCode();
        assertEquals(200, statusCode); // Forbidden
    }

    @Test
    public void testGetAllGroups_Empty() {
        // Ensure no groups exist in the database before running this test
        int statusCode = RestAssured.given()
                .when()
                .get("/api/groups/all")
                .getStatusCode();
        assertEquals(200, statusCode);

        String response = RestAssured.given()
                .when()
                .get("/api/groups/all")
                .then()
                .extract()
                .asString();

        assertEquals("[]", response); // Expect an empty list
    }

    @Test
    public void testSearchBySubstring_Success() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/jason")
                .getStatusCode();
        assertEquals(200, statusCode);

        RestAssured.given()
                .when()
                .get("/api/search/jason")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testSearchBySubstringAndType_Success() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/jason/USER")
                .getStatusCode();
        assertEquals(200, statusCode);

        RestAssured.given()
                .when()
                .get("/api/search/jason/USER")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testSearchBySubstring_NoResults() {
        RestAssured.given()
                .when()
                .get("/api/search/nonexistentuser")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testSearchBySubstringAndType_NoResults() {
        RestAssured.given()
                .when()
                .get("/api/search/nonexistentuser/USER")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testSearchBySubstring_InvalidInput() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/a")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testSearchBySubstringAndType_InvalidType() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/jason/INVALID_TYPE")
                .getStatusCode();
        assertEquals(400, statusCode);
    }

    @Test
    public void testSearchMultipleTypes() {
        RestAssured.given()
                .when()
                .get("/api/search/test")
                .then()
                .statusCode(200)
                .body("findAll { it.searchType == 'USER' }.size()", greaterThanOrEqualTo(0))
                .body("findAll { it.searchType == 'GROUP' }.size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testSearchCaseSensitivity() {
        RestAssured.given()
                .when()
                .get("/api/search/JASON")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));

        RestAssured.given()
                .when()
                .get("/api/search/jason")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testSearchWithSpecialCharacters() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/test%20group")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testCommentCreationReturnsCorrectStatusCode() {
        String posts = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(posts).
                when().
                post("/api/posts");

        String comment = """
            {
              "text": "This is a comment",
              "postId": 1,
              "userId": 1
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(comment)
                .when()
                .post("/api/comments");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testCommentCreationCreatesCommentCorrectly() {
        String posts = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(posts).
                when().
                post("/api/posts");

        String comment = """
            {
              "content": "This is a comment",
              "postId": 1,
              "userId": 1
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(comment)
                .when()
                .post("/api/comments");

        Comment createdComment = commentRepository.findById(1).orElse(null);

        assertNotNull(createdComment);
        assertEquals("This is a comment", createdComment.getContent());
        assertEquals(1, createdComment.getCommenter().getUserId());
        assertEquals(1, createdComment.getPost().getPostId());
    }

    @Test
    public void testCommentCreationAppearsInDatabaseAndIsRetrieveable() {
        String posts = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(posts).
                when().
                post("/api/posts");

        String comment = """
            {
              "content": "This is a comment",
              "postId": 1,
              "userId": 1
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(comment)
                .when()
                .post("/api/comments");

        Comment createdComment = commentRepository.findById(1).orElse(null);

        assertNotNull(createdComment);
        assertEquals("This is a comment", createdComment.getContent());
    }

    @Test
    public void testGetAllCommentsForPost() {
        String posts = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(posts).
                when().
                post("/api/posts");

        String commentA = """
        {
          "content": "Comment A",
          "postId": 1,
          "userId": 1
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(commentA)
                .when()
                .post("/api/comments");

        String commentB = """
        {
          "content": "Comment B",
          "postId": 1,
          "userId": 2
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(commentB)
                .when()
                .post("/api/comments");

        // Get all comments for post 1
        Response response = RestAssured.given()
                .when()
                .get("/api/comments/1");

        // Correct way to parse the response and get the list of comments
        List<?> comments = response.jsonPath().getList("$"); // This returns a List of comments

        // Verify the size of the list
        assertEquals(2, comments.size());
    }

    @Test
    public void testGetCommentById() {
        String posts = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(posts).
                when().
                post("/api/posts");

        String comment = """
            {
              "content": "This is a comment",
              "postId": 1,
              "userId": 1
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(comment)
                .when()
                .post("/api/comments");

        Response response = RestAssured.given()
                .when()
                .get("/api/comments/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("[This is a comment]", response.jsonPath().getString("content"));
    }

    @Test
    public void createTwoUsersAndThenDeleteThem() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String smitty = """
            {
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(smitty).
                when().
                post("/api/auth/signup");

        assertEquals(2, userRepository.findAll().size());

        Response r = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                when().
                delete("/api/users/1");
        Response r2 = RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                when().
                delete("/api/users/2");
        assertEquals(204, r2.statusCode());
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void testLikeAndUnlikePost() {
        // Make a user to use during tests
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String smitty = """
            {
              "username": "other_user",
              "password": "fart",
              "email": "fart2@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(smitty).
                when().
                post("/api/auth/signup");

        String first = """
            {
              "title": "First",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .post("/api/posts/2/like/1");

        Response r = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/1");

        int likes = r.jsonPath().getInt("likes");
        assertEquals(likes, 1);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .post("/api/posts/2/unlike/1");


        Response newr = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .when()
                .get("/api/posts/1");

        int newlikes = newr.jsonPath().getInt("likes");
        assertEquals(newlikes, 0);

    }

    @Test
    @Transactional
    public void testUserCanBeFollowedAndUnfollowed() {
        // Create first user (follower)
        String jason = """
        {
          "username": "first_user",
          "password": "fart",
          "email": "fart@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create second user (following)
        String smitty = """
        {
          "username": "other_user",
          "password": "fart",
          "email": "fart2@iastate.edu",
          "university": "Fart U"
        }
        """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(smitty)
                .when()
                .post("/api/auth/signup");

        // Explicitly retrieve and follow users
        User follower = userRepository.findByUserId(1).orElseThrow();
        User following = userRepository.findByUserId(2).orElseThrow();

        // First, establish a follow relationship
        follower.follow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Verify initial follow state
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();
        assertEquals(1, follower.getFollowingCount(), "Follower should be following one user before unfollowing");
        assertEquals(1, following.getFollowerCount(), "Following should have one follower before unfollowing");

        // Manually unfollow the user
        follower.unfollow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Retrieve users again
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();

        // Assertions
        assertFalse(follower.getFollowing().contains(following),
                "Follower should not be following the other user after unfollowing");
        assertFalse(following.getFollowers().contains(follower),
                "Following should not have the follower after unfollowing");

        // Check the follow counts
        assertEquals(0, follower.getFollowingCount(), "Follower should not be following any users");
        assertEquals(0, following.getFollowerCount(), "Following should have no followers");

        // Additional database verification
        List<?> relationships = entityManager.createNativeQuery(
                "SELECT * FROM user_following WHERE follower_id = 1 AND following_id = 2"
        ).getResultList();

        assertTrue(relationships.isEmpty(), "Follow relationship should not exist in database");
    }

    @Test
    public void testCommentCreationCreatesCommentCorrectly2() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String posts = """
            {
              "title": "Test Post",
              "description": "Description",
              "userId": 1,
              "price": 100,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(posts).
                when().
                post("/api/posts");

        String comment = """
            {
              "content": "This is a comment",
              "postId": 1,
              "userId": 1
            }
            """;
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(comment)
                .when()
                .post("/api/comments");

        Comment createdComment = commentRepository.findById(1).orElse(null);

        assertNotNull(createdComment);
        assertEquals("This is a comment", createdComment.getContent());
        assertEquals(1, createdComment.getCommenter().getUserId());
        assertEquals(1, createdComment.getPost().getPostId());
    }

    @Test
    public void createPostThenSearchForIt() {
        String jason = """
            {
              "username": "first_user",
              "password": "fart",
              "email": "fart@iastate.edu",
              "university": "Fart U"
            }
            """;
        RestAssured.given().
                header("Content-Type", "application/json").
                header("charset","utf-8").
                body(jason).
                when().
                post("/api/auth/signup");

        String first = """
            {
              "title": "First Post",
              "description": "Desc",
              "userId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        RestAssured.given()
                .when()
                .get("/api/search/First")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].searchType", notNullValue());

    }

    @Test
    @Transactional
    public void testAddPostToAGroup() {
        String jason = """
            {
              "username": "jason_search",
              "password": "password123",
              "email": "jason@search.edu",
              "university": "Search University"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset","utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create a group for search testing
        String groupRequest = "{" +
                "\"groupName\": \"Test Group\"," +
                "\"groupDescription\": \"Group testing\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Public\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(groupRequest)
                .when()
                .post("/api/groups");

        String first = """
            {
              "title": "First Post",
              "description": "Desc",
              "userId": 1,
              "groupId": 1,
              "price": 900,
              "type": "ITEM_LISTING"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(first)
                .when()
                .post("/api/posts");

        assertEquals(1, groupRepository.findByGroupId(1).get().getPosts().size());

    }

    @Test
    public void createAGroupAndSearchForIt() {
        String jason = """
            {
              "username": "jason_search",
              "password": "password123",
              "email": "jason@search.edu",
              "university": "Search University"
            }
            """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset","utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create a group for search testing
        String groupRequest = "{" +
                "\"groupName\": \"Test Search Group\"," +
                "\"groupDescription\": \"Group for search testing\"," +
                "\"creatorId\": 1," +
                "\"groupType\": \"Public\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(groupRequest)
                .when()
                .post("/api/groups");

        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/Test")
                .getStatusCode();
        assertEquals(200, statusCode);

        RestAssured.given()
                .when()
                .get("/api/search/Test")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].searchType", notNullValue());
    }

    @Test
    @Transactional
    public void testCreateTwoUsersSearchForOneAndFollowThem() {
        // Create first user (follower)
        String jason = """
                {
                  "username": "first_user",
                  "password": "fart",
                  "email": "fart@iastate.edu",
                  "university": "Fart U"
                }
                """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(jason)
                .when()
                .post("/api/auth/signup");

        // Create second user (following)
        String smitty = """
                {
                  "username": "other_user",
                  "password": "fart",
                  "email": "fart2@iastate.edu",
                  "university": "Fart U"
                }
                """;
        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("charset", "utf-8")
                .body(smitty)
                .when()
                .post("/api/auth/signup");

        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/first/USER")
                .getStatusCode();
        assertEquals(200, statusCode);

        RestAssured.given()
                .when()
                .get("/api/search/first/USER")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].searchType", is("USER"));

        // Explicitly retrieve and follow users
        User follower = userRepository.findByUserId(1).orElseThrow();
        User following = userRepository.findByUserId(2).orElseThrow();

        // First, establish a follow relationship
        follower.follow(following);
        userRepository.save(follower);
        userRepository.save(following);

        // Flush and clear to ensure database synchronization
        entityManager.flush();
        entityManager.clear();

        // Verify initial follow state
        follower = userRepository.findByUserId(1).orElseThrow();
        following = userRepository.findByUserId(2).orElseThrow();
        assertEquals(1, follower.getFollowingCount(), "Follower should be following one user before unfollowing");
        assertEquals(1, following.getFollowerCount(), "Following should have one follower before unfollowing");
    }


}

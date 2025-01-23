package program;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestTemplate;
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
public class CoolSystemTests {

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
    public void testCommentCreationCreatesCommentCorrectly() {
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

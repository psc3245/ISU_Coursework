package program;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import program.Groups.Group;
import program.Posts.Post;
import program.Posts.Repositories.PostRepository;
import program.Reviews.Review;
import program.Users.User;
import program.Users.UserRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest(classes = UnitradeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerSystemTest {
    @LocalServerPort
    int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
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

        User u = userRepository.findByUserId(1).get();
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

        assertEquals(0, userRepository.findAll().size());
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
}

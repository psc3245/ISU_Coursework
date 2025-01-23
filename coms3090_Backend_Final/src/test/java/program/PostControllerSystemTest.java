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
import program.Posts.Post;
import program.Posts.PostTypes.PostTypes;
import program.Posts.Repositories.PostRepository;
import program.Users.User;
import program.Users.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest(classes = UnitradeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PostControllerSystemTest {

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

}

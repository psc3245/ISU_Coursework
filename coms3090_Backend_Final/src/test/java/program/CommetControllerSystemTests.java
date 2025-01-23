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
import program.Posts.Post;
import program.Posts.PostTypes.PostTypes;
import program.Posts.Repositories.PostRepository;
import program.UnitradeApplication;
import program.Users.User;
import program.Users.UserRepository;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest(classes = UnitradeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CommetControllerSystemTests {

    @LocalServerPort
    int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

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


}

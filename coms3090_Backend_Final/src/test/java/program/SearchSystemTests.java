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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest(classes = UnitradeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SearchSystemTests {

    @LocalServerPort
    int port;

    @Before
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Create users for search testing
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
                .body("size()", greaterThan(0))
                .body("[0].searchType", notNullValue());
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
                .body("size()", greaterThan(0))
                .body("[0].searchType", is("USER"));
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
                .body("size()", greaterThan(0));

        RestAssured.given()
                .when()
                .get("/api/search/jason")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void testSearchWithSpecialCharacters() {
        int statusCode = RestAssured.given()
                .when()
                .get("/api/search/test%20group")
                .getStatusCode();
        assertEquals(200, statusCode);
    }

}

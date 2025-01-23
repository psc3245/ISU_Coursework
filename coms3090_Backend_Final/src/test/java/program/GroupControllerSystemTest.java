package program;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
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
public class GroupControllerSystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setup() {
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


}

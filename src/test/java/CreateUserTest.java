import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserChecks;
import user.UserClient;
import user.UserGenerator;

public class CreateUserTest {
    private UserClient userClient;
    private User user;
    private UserChecks check = new UserChecks();
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        ValidatableResponse delete = userClient.delete(accessToken);
        check.deletedSuccessfully202(delete);
    }


    @Test
    public void userCanCreated() {
        user = UserGenerator.getRandomUser();

        ValidatableResponse response = userClient.create(user);
        check.createdOrLoggedSuccessfully200(response);

        accessToken = response.extract().path("accessToken");
    }


    @Test
    public void userCantCreatedWithExistingUser() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");

        response = userClient.create(user);
        check.notCreatedExistingUser403(response);
    }
}
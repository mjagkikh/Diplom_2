import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.*;

public class LoginUserTest {
    UserClient userClient;
    User user;
    Credentials creds;
    UserChecks check;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        check = new UserChecks();
    }

    @Test
    public void loginUserWithCorrectCreds200() {
        user = UserGenerator.getRandomUser();
        creds = Credentials.from(user);

        userClient.create(user);
        ValidatableResponse response = userClient.login(creds);
        accessToken = response.extract().path("accessToken");

        check.createdOrLoggedSuccessfully200(response);

        response = userClient.delete(accessToken);
        check.deletedSuccessfully202(response);
    }

    @Test
    public void loginUserWithoutEmail401() {
        creds = new Credentials(null, "P@ssw0rd");
        ValidatableResponse response = userClient.login(creds);
        check.notLoggedWithoutRequiredFields401(response);
    }

    @Test
    public void loginUserWithoutPassword401() {
        creds = new Credentials("somemail@ya.ru", null);
        ValidatableResponse response = userClient.login(creds);
        check.notLoggedWithoutRequiredFields401(response);
    }
}
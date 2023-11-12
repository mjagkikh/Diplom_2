import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserChecks;
import user.UserClient;


public class CreateUserWithoutArgsTest {
    private UserClient userClient;
    private User user;
    private UserChecks check;

    @Before
    public void setUp() {
        userClient = new UserClient();
        check = new UserChecks();
    }

    @Test
    @DisplayName("Failed creation user without password")
    public void userCantCreatedWithoutPassword() {
        user = new User("some@ya.ru", null, "Billy");

        ValidatableResponse response = userClient.create(user);
        check.notCreatedWithoutRequiredFields403(response);
    }

    @Test
    @DisplayName("Failed creation user without email")
    public void userCantCreatedWithoutEmail() {
        user = new User(null, "P@ssw0rd", "Billy");

        ValidatableResponse response = userClient.create(user);
        check.notCreatedWithoutRequiredFields403(response);
    }

    @Test
    @DisplayName("Failed creation user without name")
    public void userCantCreatedWithoutName() {
        user = new User("some@ya.ru", "P@ssw0rd", null);

        ValidatableResponse response = userClient.create(user);
        check.notCreatedWithoutRequiredFields403(response);
    }
}
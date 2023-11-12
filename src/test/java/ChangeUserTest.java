import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserChecks;
import user.UserClient;
import user.UserGenerator;

import java.net.HttpURLConnection;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;

public class ChangeUserTest {
    UserClient userClient;
    User user;
    String accessToken;
    UserChecks check;

    private static final Random rnd = new Random();
    @Before
    public void setUp() {
        userClient = new UserClient();
        check = new UserChecks();
    }

    @After
    public void tearDown() {
        ValidatableResponse response = userClient.delete(accessToken);
        check.deletedSuccessfully202(response);
    }

    @Test
    @DisplayName("Change name for authorized user")
    public void changeNameSuccess() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        String newName = user.getName()+"ChangedName";
        user.setName(newName);

        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true),"user.name", is(newName));

    }

    @Test
    @DisplayName("Change password for authorized user")
    public void changePasswordSuccess() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        String newPsw = user.getPassword()+ rnd.nextInt(10000);
        user.setPassword(newPsw);
        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true), "user.email", is(user.getEmail().toLowerCase()));
    }
    @Test
    @DisplayName("Change email for authorized user")
    public void changeEmailSuccess() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        String newEmail = rnd.nextInt(10000)+user.getEmail();
        user.setEmail(newEmail);
        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true), "user.email", is(user.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Change email for existed email for authorized user")
    public void changeEmailFailure403() {
        user = UserGenerator.getRandomUser();
        User anotherUser = UserGenerator.getRandomUser();

        ValidatableResponse response = userClient.create(user);
        ValidatableResponse responseOfAnotherUser = userClient.create(anotherUser);
        accessToken = response.extract().path("accessToken");
        String accessTokenOfAnotherUser = responseOfAnotherUser.extract().path("accessToken");
        String emailOfAnotherExistedUser = anotherUser.getEmail();
        user.setEmail(emailOfAnotherExistedUser);

        response = userClient.changeUser(user, accessToken);
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false), "message", is("User with such email already exists"));

        responseOfAnotherUser = userClient.delete(accessTokenOfAnotherUser);
        check.deletedSuccessfully202(responseOfAnotherUser);
    }

    @Test
    @DisplayName("Change name for unauthorized user")
    public void changeNameWithoutToken() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        user.setName(user.getName()+"ChangedName");
        response = userClient.changeUserWithoutToken(user);
        check.notChangedUserWithoutToken401(response);

    }

    @Test
    @DisplayName("Change email for unauthorized user")
    public void changeEmailWithoutToken() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail("newEmail"+user.getEmail());
        response = userClient.changeUserWithoutToken(user);
        check.notChangedUserWithoutToken401(response);
    }

    @Test
    @DisplayName("Change password for unauthorized user")
    public void changePasswordWithoutToken() {
        user = UserGenerator.getRandomUser();
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        user.setPassword(user.getPassword()+"newPsw");
        response = userClient.changeUserWithoutToken(user);
        check.notChangedUserWithoutToken401(response);
    }
}
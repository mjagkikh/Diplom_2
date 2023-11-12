package user;

import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.is;

public class UserChecks {
    public void createdSuccessfully200(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", is(true));
    }

    public void deletedSuccessfully202(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_ACCEPTED)
                .body("success", is(true));
    }

    public  void notCreated403(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success", is(false), "message", is("User already exists"));
    }

    public  void notCreatedWithoutRequiredFields403(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("message", is("Email, password and name are required fields"));
    }
}
package user;

import org.apache.commons.lang.RandomStringUtils;

public class UserGenerator {

    public static  User getRandomUser() {
        return new User("Test" + RandomStringUtils.randomAlphanumeric(10)+"@ya.ru", "P@ssw0rd", "Billy");
    }
}
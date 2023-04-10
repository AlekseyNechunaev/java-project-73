package hexlet.code.exception;

public final class ExceptionMessage {

    public static final String USER_EXIST_BY_EMAIL = "a user with this email exists";
    public static final String USER_NOT_FOUND = "user not found";
    public static final String WRONG_EMAIL_OR_PASSWORD = "wrong email or password";
    public static final String STATUS_NOT_FOUND = "status not found";

    public static final String STATUS_EXIST_BY_NAME = "a status with this name already exists";
    private ExceptionMessage() {

    }
}

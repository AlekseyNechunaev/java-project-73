package hexlet.code.exception;

public final class ExceptionMessage {

    public static final String USER_EXIST_BY_EMAIL = "a user with this email exists";
    public static final String RESOURCE_NOT_FOUND = "not found";
    public static final String WRONG_EMAIL_OR_PASSWORD = "wrong email or password";
    public static final String STATUS_EXIST_BY_NAME = "a status with this name already exists";
    public static final String TASK_EXIST_BY_NAME = "a task with this name already exists";
    public static final String AUTHOR_NOT_EXIST = "the author with the passed id does not exist";
    public static final String EXECUTOR_NOT_EXIST = "the executor with the passed id does not exist";
    public static final String STATUS_NOT_EXIST = "the status with the transmitted id does not exist";
    public static final String ILLEGAL_DELETE_STATUS = "you cannot delete the status associated with tasks";
    public static final String ILLEGAL_DELETE_USER = "you cannot delete a user who is associated with tasks";
    public static final String LABEL_EXIST_BY_NAME = "a label with this name already exists";
    public static final String ILLEGAL_DELETE_TASK = "you cannot delete a label who is associated with tasks";
    private ExceptionMessage() {

    }
}

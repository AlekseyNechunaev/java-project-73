package hexlet.code.exception;

import hexlet.code.dto.ErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleController {

    private static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
    private static final Map<String, String> DATA_INTEGRITY_ERROR_MESSAGES = Map.of(
            "UC_STATUSESNAME", ExceptionMessage.STATUS_EXIST_BY_NAME,
            "UC_USERSEMAIL", ExceptionMessage.USER_EXIST_BY_EMAIL,
            "UC_TASKSNAME", ExceptionMessage.TASK_EXIST_BY_NAME,
            "UC_LABELNAME", ExceptionMessage.LABEL_EXIST_BY_NAME,
            "TASKS FOREIGN KEY(STATUS_ID)", ExceptionMessage.ILLEGAL_DELETE_STATUS,
            "TASKS FOREIGN KEY(EXECUTOR_ID)", ExceptionMessage.ILLEGAL_DELETE_USER,
            "TASKS FOREIGN KEY(AUTHOR_ID)", ExceptionMessage.ILLEGAL_DELETE_USER,
            "TASKS FOREIGN KEY(LABEL_ID)", ExceptionMessage.ILLEGAL_DELETE_LABEL
    );

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto resourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(ResourceNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto resourceNotExistException(Exception e) {
        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto dataIntegrityViolationException(DataIntegrityViolationException e) {
        final String defaultErrorMessage = e.getMessage();
        if (defaultErrorMessage != null) {
            for (Map.Entry<String, String> entry : DATA_INTEGRITY_ERROR_MESSAGES.entrySet()) {
                if (defaultErrorMessage.contains(entry.getKey())) {
                    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), entry.getValue());
                }
            }
        }
        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), UNKNOWN_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<ObjectError> validationException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto authenticationException(AuthenticationException e) {
        return new ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}

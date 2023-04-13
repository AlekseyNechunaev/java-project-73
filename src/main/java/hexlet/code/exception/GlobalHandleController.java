package hexlet.code.exception;

import hexlet.code.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalHandleController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto resourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(value = {
            ResourceExistException.class,
            ResourceNotExistException.class,
            IllegalOperationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto badRequestException(Exception e) {
        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
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

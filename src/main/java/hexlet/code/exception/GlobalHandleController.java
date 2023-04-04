package hexlet.code.exception;

import hexlet.code.dto.ErrorDto;
import hexlet.code.dto.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandleController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto resourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto userExistException(UserExistException e) {
        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<ValidationErrorDto> validationException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(field -> new ValidationErrorDto(field.getField(), field.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto authenticationException(AuthenticationException e) {
        return new ErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
}

package com.artistbooking.BookArtist.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @Getter
    @AllArgsConstructor
    public static class ExceptionResponse {
        private final List<String> messages;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionResponse handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return new ExceptionResponse(
                ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList()));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)  // 403
    @ExceptionHandler(DuplicateUsernameException.class)
    public @ResponseBody
    ExceptionResponse handleDuplicateUsernameException(
            Exception ex) {
        return new ExceptionResponse(List.of("This username is already taken. Please specify " +
                "another one!"));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)  // 401
    @ExceptionHandler(AuthenticationRequiredException.class)
    public @ResponseBody
    ExceptionResponse handleAuthenticationRequiredException(
            Exception ex) {
        return new ExceptionResponse(List.of("You must be authenticated to access this " +
                "functionality!"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(IncorrectPasswordException.class)
    public @ResponseBody
    ExceptionResponse handleIncorrectPasswordException(
            Exception ex) {
        return new ExceptionResponse(List.of("Incorrect Password! Kindly input the correct password :)"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(PasswordMismatchException.class)
    public @ResponseBody
    ExceptionResponse handlePasswordMismatchException(
            Exception ex) {
        return new ExceptionResponse(List.of("Error Confirming Password! Check if the new password is same" +
                "as the confirm passsword :)"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ResourceNotFoundException.class)
    public @ResponseBody
    ExceptionResponse handleResourceNotFoundException(
            Exception ex) {
        return new ExceptionResponse(List.of("Resource not found! Seems the thing you looking for" +
                "does not exist :)"));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ResourceFoundException.class)
    public @ResponseBody
    ExceptionResponse handleResourceFoundException(
            Exception ex) {
        return new ExceptionResponse(List.of("This resource is already taken. Please specify " +
                "another one!"));
    }
}

package account.exception.handler;

import account.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity handleUserExists(HttpServletRequest request, UserExistsException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgument(HttpServletRequest request, IllegalArgumentException e) {
        //log.debug("Processing IllegalArgumentException: {}", e.getMessage());
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordLengthException.class)
    public ResponseEntity handlePasswordLength(HttpServletRequest request, PasswordLengthException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity handleSamePassword(HttpServletRequest request, SamePasswordException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolation(HttpServletRequest request, ConstraintViolationException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityConstraintViolation(HttpServletRequest request, DataIntegrityViolationException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompromisedPasswordException.class)
    public ResponseEntity handleCompromisedPassword(HttpServletRequest request, CompromisedPasswordException e) {
        return new ResponseEntity<>(
                getBodyMap("The password is in the hacker's database!", HttpStatus.BAD_REQUEST, request),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity handleUsernameNotFound(HttpServletRequest request, RuntimeException e) {
        return new ResponseEntity<>(
                getBodyMap(e.getMessage(), HttpStatus.NOT_FOUND, request),
                HttpStatus.NOT_FOUND);
    }

    /*@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDenied(HttpServletRequest request, AccessDeniedException e) {
        return new ResponseEntity<>(
                getBodyMap("Access Denied!", HttpStatus.FORBIDDEN, request),
                HttpStatus.FORBIDDEN);
    }*/

    private static Map<String, Object> getBodyMap(String message, HttpStatus httpStatus, HttpServletRequest request) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", httpStatus.value(),
                "error", httpStatus.getReasonPhrase(),
                "message", message,
                "path", request.getRequestURI()
        );
        return body;
    }


}

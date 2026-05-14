package kz.booking.common.web;

import jakarta.servlet.http.HttpServletRequest;
import kz.booking.common.api.BekbolatovZholamanApiError;
import kz.booking.common.api.BekbolatovZholamanFieldViolation;
import kz.booking.common.exception.BekbolatovZholamanBadRequestException;
import kz.booking.common.exception.BekbolatovZholamanForbiddenException;
import kz.booking.common.exception.BekbolatovZholamanNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class BekbolatovZholamanGlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(BekbolatovZholamanGlobalExceptionHandler.class);

    @ExceptionHandler(BekbolatovZholamanNotFoundException.class)
    public ResponseEntity<BekbolatovZholamanApiError> handleNotFound(BekbolatovZholamanNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(BekbolatovZholamanForbiddenException.class)
    public ResponseEntity<BekbolatovZholamanApiError> handleForbidden(BekbolatovZholamanForbiddenException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(BekbolatovZholamanBadRequestException.class)
    public ResponseEntity<BekbolatovZholamanApiError> handleBadRequest(BekbolatovZholamanBadRequestException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BekbolatovZholamanApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<BekbolatovZholamanFieldViolation> violations = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(e -> e instanceof FieldError)
                .map(e -> (FieldError) e)
                .map(fe -> new BekbolatovZholamanFieldViolation(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), violations);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BekbolatovZholamanApiError> handleOther(Exception ex, HttpServletRequest req) {
        log.error("Unhandled error on {} {}", req.getMethod(), req.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", req.getRequestURI(), null);
    }

    private ResponseEntity<BekbolatovZholamanApiError> build(HttpStatus status, String message, String path, List<BekbolatovZholamanFieldViolation> violations) {
        BekbolatovZholamanApiError body = new BekbolatovZholamanApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                violations
        );
        return ResponseEntity.status(status).body(body);
    }
}


package com.pokemon.pokidex.Exceptions;

import com.pokemon.pokidex.Dtos.ApiResponse;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PokedexException.class)
    public ResponseEntity<ApiResponse<Void>> handlePokedex(PokedexException ex) {
        if (ex.getStatus().is5xxServerError()) {
            log.error("{}: {}", ex.getCode(), ex.getMessage(), ex);
        } else {
            log.debug("{}: {}", ex.getCode(), ex.getMessage());
        }
        return ResponseEntity.status(ex.getStatus()).body(ApiResponse.fail(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("Something went wrong", "INTERNAL_ERROR"));
    }

    /** @RequestParam constraint violations (@Min, @Size...) */
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String msg = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, msg.isBlank() ? "Invalid request" : msg, "VALIDATION_ERROR", headers);
    }

    /** Everything else Spring MVC throws (type mismatch, 404 route, 405...) funnels through here. */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String message = (body instanceof ProblemDetail pd && pd.getDetail() != null)
                ? pd.getDetail() : "Request failed";
        HttpStatus resolved = HttpStatus.resolve(statusCode.value());
        String code = resolved != null ? resolved.name() : "ERROR";
        return build(statusCode, message, code, headers);
    }

    private ResponseEntity<Object> build(HttpStatusCode status, String message, String code, HttpHeaders headers) {
        return ResponseEntity.status(status).headers(headers).body(ApiResponse.fail(message, code));
    }
}
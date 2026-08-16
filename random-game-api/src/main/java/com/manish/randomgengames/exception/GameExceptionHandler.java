package com.manish.randomgengames.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameExceptionHandler {

    /**
     * Turns an invalid game request into an HTTP 400 response.
     *
     * @param exception error containing the reason for the invalid request
     * @return response details for the client
     */
    @ExceptionHandler(InvalidGameRequestException.class)
    public ProblemDetail handleInvalidRequest(InvalidGameRequestException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Turns a missing or completed round into an HTTP 404 response.
     *
     * @param exception error containing the missing-round message
     * @return response details for the client
     */
    @ExceptionHandler(RoundNotFoundException.class)
    public ProblemDetail handleRoundNotFound(RoundNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Turns an unreadable request body into an HTTP 400 response.
     *
     * @return response details for the client
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableRequest() {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "The request body is missing or contains an invalid value"
        );
    }
}

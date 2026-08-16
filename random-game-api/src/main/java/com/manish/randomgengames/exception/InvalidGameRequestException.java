package com.manish.randomgengames.exception;

public class InvalidGameRequestException extends RuntimeException {

    /**
     * Creates an error that explains why a game request is invalid.
     *
     * @param message reason the request is invalid
     */
    public InvalidGameRequestException(String message) {
        super(message);
    }
}

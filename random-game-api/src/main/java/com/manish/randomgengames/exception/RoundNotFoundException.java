package com.manish.randomgengames.exception;

public class RoundNotFoundException extends RuntimeException {

    /**
     * Creates an error for a round that is missing or already completed.
     *
     * @param message reason the round cannot be used
     */
    public RoundNotFoundException(String message) {
        super(message);
    }
}

package com.manish.randomgengames.dto.number;

/**
 * Returns the information needed to play a new number-game round.
 *
 * @param roundId unique ID used when submitting a guess
 * @param number number the player must remember
 * @param digitCount number of digits in the number
 * @param displayTimeMs number of milliseconds to show the number
 */
public record NumberRoundResponse(
        String roundId,
        String number,
        int digitCount,
        long displayTimeMs
) {
}

package com.manish.randomgengames.dto.number;

/**
 * Returns the result of a number-game guess.
 *
 * @param correct whether the guessed number was correct
 * @param correctNumber number that was shown in the round
 * @param nextDigitCount number of digits to use in the next round
 */
public record NumberGuessResponse(boolean correct, String correctNumber, int nextDigitCount) {
}

package com.manish.randomgengames.model;

/**
 * Stores the correct number and its length for one number-game round.
 *
 * @param number number the player must remember
 * @param digitCount number of digits in the number
 */
public record NumberRound(String number, int digitCount) {
}

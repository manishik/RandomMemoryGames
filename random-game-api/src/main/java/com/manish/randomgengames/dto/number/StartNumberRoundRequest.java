package com.manish.randomgengames.dto.number;

/**
 * Carries the number of digits used to start a number-game round.
 *
 * @param digitCount number of digits requested
 */
public record StartNumberRoundRequest(int digitCount) {
}

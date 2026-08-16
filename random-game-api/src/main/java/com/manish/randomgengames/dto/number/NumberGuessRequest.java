package com.manish.randomgengames.dto.number;

/**
 * Carries a number-game round ID and the player's guess into the API.
 *
 * @param roundId ID of the round being answered
 * @param guess number entered by the player
 */
public record NumberGuessRequest(String roundId, String guess) {
}

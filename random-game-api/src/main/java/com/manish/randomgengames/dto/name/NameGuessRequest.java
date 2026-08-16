package com.manish.randomgengames.dto.name;

import java.util.List;

/**
 * Carries a name-game round ID and the player's guesses into the API.
 *
 * @param roundId ID of the round being answered
 * @param guesses names entered by the player
 */
public record NameGuessRequest(String roundId, List<String> guesses) {
}

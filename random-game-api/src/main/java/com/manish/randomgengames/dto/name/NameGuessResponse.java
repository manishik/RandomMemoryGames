package com.manish.randomgengames.dto.name;

import java.util.List;

/**
 * Returns the result of a name-game guess.
 *
 * @param correct whether every guessed name was correct and in order
 * @param correctNames names that were shown in the round
 * @param nextNameCount number of names to use in the next round
 */
public record NameGuessResponse(boolean correct, List<String> correctNames, int nextNameCount) {
}

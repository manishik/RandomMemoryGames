package com.manish.randomgengames.dto.name;

import com.manish.randomgengames.model.NameMode;

import java.util.List;

/**
 * Returns the information needed to play a new name-game round.
 *
 * @param roundId unique ID used when submitting guesses
 * @param names names the player must remember
 * @param nameCount number of names in the round
 * @param nameMode type of names used in the round
 */
public record NameRoundResponse(
        String roundId,
        List<String> names,
        int nameCount,
        NameMode nameMode
) {
}

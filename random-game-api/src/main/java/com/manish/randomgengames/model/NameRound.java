package com.manish.randomgengames.model;

import java.util.List;

/**
 * Stores the correct names and settings for one name-game round.
 *
 * @param names names the player must remember
 * @param nameCount number of names in the round
 * @param nameMode type of names used in the round
 */
public record NameRound(List<String> names, int nameCount, NameMode nameMode) {

    // Copies the list so the saved names cannot be changed later.
    public NameRound {
        names = List.copyOf(names);
    }
}

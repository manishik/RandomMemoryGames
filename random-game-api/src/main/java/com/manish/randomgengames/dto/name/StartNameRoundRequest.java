package com.manish.randomgengames.dto.name;

import com.manish.randomgengames.model.NameMode;

/**
 * Carries the settings used to start a name-game round.
 *
 * @param nameCount number of names requested
 * @param nameMode type of names requested
 */
public record StartNameRoundRequest(int nameCount, NameMode nameMode) {
}

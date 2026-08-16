package com.manish.randomgengames.dto.name;

import java.util.List;

public record NameGuessRequest(String roundId, List<String> guesses) {
}

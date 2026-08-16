package com.manish.randomgengames.dto.name;

import java.util.List;

public record NameGuessResponse(boolean correct, List<String> correctNames, int nextNameCount) {
}

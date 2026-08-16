package com.manish.randomgengames.dto.number;

public record NumberRoundResponse(
        String roundId,
        String number,
        int digitCount,
        long displayTimeMs
) {
}

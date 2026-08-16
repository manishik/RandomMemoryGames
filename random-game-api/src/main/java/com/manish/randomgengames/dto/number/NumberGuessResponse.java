package com.manish.randomgengames.dto.number;

public record NumberGuessResponse(boolean correct, String correctNumber, int nextDigitCount) {
}

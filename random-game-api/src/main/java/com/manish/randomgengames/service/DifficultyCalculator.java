package com.manish.randomgengames.service;

final class DifficultyCalculator {

    private DifficultyCalculator() {
    }

    static int calculateNextLevel(int currentLevel, int maximumLevel, boolean correct) {
        return correct
                ? Math.min(maximumLevel, currentLevel + 1)
                : Math.max(1, currentLevel - 1);
    }
}

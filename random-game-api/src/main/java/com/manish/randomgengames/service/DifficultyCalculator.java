package com.manish.randomgengames.service;

// Calculates whether the next round should be easier or harder.
final class DifficultyCalculator {

    // Prevents this helper class from being created as an object.
    private DifficultyCalculator() {
    }

    /**
     * Raises the level after a correct answer or lowers it after a wrong answer.
     *
     * @param currentLevel difficulty used in the completed round
     * @param maximumLevel highest allowed difficulty
     * @param correct whether the player's answer was correct
     * @return difficulty to use in the next round
     */
    static int calculateNextLevel(int currentLevel, int maximumLevel, boolean correct) {
        if (correct) {
            return Math.min(maximumLevel, currentLevel + 1);
        } else {
            return Math.max(1, currentLevel - 1);
        }
    }
}

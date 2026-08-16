package com.manish.randomgengames.dao;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRoundDao<T> {

    // Keeps each round in memory under its unique ID.
    private final Map<String, T> rounds = new ConcurrentHashMap<>();

    /**
     * Gives a round a unique ID and stores it in memory.
     *
     * @param round round to store
     * @return the new round ID
     */
    public String save(T round) {
        // Creates a different ID for the new round.
        String roundId = UUID.randomUUID().toString();
        rounds.put(roundId, round);
        return roundId;
    }

    /**
     * Finds and removes a round so it can only be completed once.
     *
     * @param roundId ID of the round to remove
     * @return the round, or an empty value when it does not exist
     */
    public Optional<T> remove(String roundId) {
        return Optional.ofNullable(rounds.remove(roundId));
    }
}

package com.manish.randomgengames.dao;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRoundDao<T> {

    private final Map<String, T> rounds = new ConcurrentHashMap<>();

    public String save(T round) {
        String roundId = UUID.randomUUID().toString();
        rounds.put(roundId, round);
        return roundId;
    }

    public Optional<T> remove(String roundId) {
        return Optional.ofNullable(rounds.remove(roundId));
    }
}

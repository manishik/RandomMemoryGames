package com.manish.randomgengames.service;

import com.manish.randomgengames.dao.NameCatalogDao;
import com.manish.randomgengames.dao.NameRoundDao;
import com.manish.randomgengames.dto.name.NameGuessRequest;
import com.manish.randomgengames.dto.name.NameGuessResponse;
import com.manish.randomgengames.dto.name.NameRoundResponse;
import com.manish.randomgengames.dto.name.StartNameRoundRequest;
import com.manish.randomgengames.exception.InvalidGameRequestException;
import com.manish.randomgengames.exception.RoundNotFoundException;
import com.manish.randomgengames.model.NameMode;
import com.manish.randomgengames.model.NameRound;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Service
public class NameGameService {

    private static final int MAX_NAMES = 20;

    private final NameCatalogDao catalogDao;
    private final NameRoundDao roundDao;

    public NameGameService(NameCatalogDao catalogDao, NameRoundDao roundDao) {
        this.catalogDao = catalogDao;
        this.roundDao = roundDao;
    }

    public NameRoundResponse startRound(StartNameRoundRequest request) {
        if (request.nameCount() < 1 || request.nameCount() > MAX_NAMES) {
            throw new InvalidGameRequestException("nameCount must be between 1 and " + MAX_NAMES);
        }
        if (request.nameMode() == null) {
            throw new InvalidGameRequestException("nameMode is required");
        }

        List<String> names = generateNames(request.nameCount(), request.nameMode());
        NameRound round = new NameRound(names, request.nameCount(), request.nameMode());
        String roundId = roundDao.save(round);

        return new NameRoundResponse(roundId, names, request.nameCount(), request.nameMode());
    }

    public NameGuessResponse checkGuess(NameGuessRequest request) {
        if (request.roundId() == null || request.guesses() == null) {
            throw new InvalidGameRequestException("A round ID and guesses are required");
        }

        NameRound round = roundDao.remove(request.roundId())
                .orElseThrow(() -> new RoundNotFoundException("Round not found or already completed"));

        List<String> normalizedGuesses = request.guesses().stream()
                .map(this::normalize)
                .toList();
        List<String> normalizedNames = round.names().stream()
                .map(this::normalize)
                .toList();
        boolean correct = normalizedNames.equals(normalizedGuesses);
        int nextNameCount = DifficultyCalculator.calculateNextLevel(
                round.nameCount(),
                MAX_NAMES,
                correct
        );

        return new NameGuessResponse(correct, round.names(), nextNameCount);
    }

    private List<String> generateNames(int nameCount, NameMode nameMode) {
        List<String> firstNames = shuffledCopy(catalogDao.findAllFirstNames());
        List<String> lastNames = shuffledCopy(catalogDao.findAllLastNames());

        return switch (nameMode) {
            case FIRST -> List.copyOf(firstNames.subList(0, nameCount));
            case LAST -> List.copyOf(lastNames.subList(0, nameCount));
            case FULL -> IntStream.range(0, nameCount)
                    .mapToObj(index -> firstNames.get(index) + " " + lastNames.get(index))
                    .toList();
        };
    }

    private List<String> shuffledCopy(List<String> names) {
        List<String> shuffled = new ArrayList<>(names);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}

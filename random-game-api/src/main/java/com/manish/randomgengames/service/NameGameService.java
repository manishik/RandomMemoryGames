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
// Runs the logic for starting and checking name-game rounds.
public class NameGameService {

    // Sets the largest number of names a round can use.
    private static final int MAX_NAMES = 20;

    // Provides the first names and last names used in rounds.
    private final NameCatalogDao catalogDao;

    // Stores and retrieves name-game rounds.
    private final NameRoundDao roundDao;

    /**
     * Creates the service with its name catalog and round storage.
     *
     * @param catalogDao DAO that provides available names
     * @param roundDao DAO that stores and retrieves rounds
     */
    public NameGameService(NameCatalogDao catalogDao, NameRoundDao roundDao) {
        this.catalogDao = catalogDao;
        this.roundDao = roundDao;
    }

    /**
     * Validates the request, selects random names, and starts a new round.
     *
     * @param request requested name count and name mode
     * @return the new round information
     */
    public NameRoundResponse startRound(StartNameRoundRequest request) {
        if (request.nameCount() < 1 || request.nameCount() > MAX_NAMES) {
            throw new InvalidGameRequestException("nameCount must be between 1 and " + MAX_NAMES);
        }
        if (request.nameMode() == null) {
            throw new InvalidGameRequestException("nameMode is required");
        }

        // Contains the random names that the player must remember.
        List<String> names = generateNames(request.nameCount(), request.nameMode());

        // Contains all information needed to check this round later.
        NameRound round = new NameRound(names, request.nameCount(), request.nameMode());

        // Saves the round and keeps its unique ID.
        String roundId = roundDao.save(round);

        return new NameRoundResponse(roundId, names, request.nameCount(), request.nameMode());
    }

    /**
     * Checks the player's names and calculates the difficulty of the next round.
     *
     * @param request round ID and the player's guessed names
     * @return whether the guesses were correct, the answers, and the next name count
     */
    public NameGuessResponse checkGuess(NameGuessRequest request) {
        if (request.roundId() == null || request.guesses() == null) {
            throw new InvalidGameRequestException("A round ID and guesses are required");
        }

        // Gets and removes the round so it cannot be answered twice.
        NameRound round = roundDao.remove(request.roundId())
                .orElseThrow(() -> new RoundNotFoundException("Round not found or already completed"));

        // Cleans the player's guesses before comparing them.
        List<String> normalizedGuesses = request.guesses().stream()
                .map(this::normalize)
                .toList();

        // Cleans the correct names in the same way as the guesses.
        List<String> normalizedNames = round.names().stream()
                .map(this::normalize)
                .toList();

        // Checks that every cleaned guess matches the correct name in the same position.
        boolean correct = normalizedNames.equals(normalizedGuesses);

        // Increases or decreases the name count based on the answer.
        int nextNameCount = DifficultyCalculator.calculateNextLevel(
                round.nameCount(),
                MAX_NAMES,
                correct
        );

        return new NameGuessResponse(correct, round.names(), nextNameCount);
    }

    /**
     * Shuffles the catalogs and selects names in the requested format.
     *
     * @param nameCount number of names to select
     * @param nameMode whether to use first, last, or full names
     * @return randomly selected names
     */
    private List<String> generateNames(int nameCount, NameMode nameMode) {
        // Contains the first names in a random order.
        List<String> firstNames = shuffledCopy(catalogDao.findAllFirstNames());

        // Contains the last names in a random order.
        List<String> lastNames = shuffledCopy(catalogDao.findAllLastNames());

        // The index selects matching positions from both lists when building full names.
        return switch (nameMode) {
            case FIRST -> List.copyOf(firstNames.subList(0, nameCount));
            case LAST -> List.copyOf(lastNames.subList(0, nameCount));
            case FULL -> IntStream.range(0, nameCount)
                    .mapToObj(index -> firstNames.get(index) + " " + lastNames.get(index))
                    .toList();
        };
    }

    /**
     * Makes a copy of a name list and puts it in random order.
     *
     * @param names original names to copy
     * @return shuffled copy of the names
     */
    private List<String> shuffledCopy(List<String> names) {
        // Copies the list so the original catalog is not changed.
        List<String> shuffled = new ArrayList<>(names);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    /**
     * Removes case and extra-space differences before comparing a name.
     *
     * @param value name to clean
     * @return cleaned name, or empty text when the value is null
     */
    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}

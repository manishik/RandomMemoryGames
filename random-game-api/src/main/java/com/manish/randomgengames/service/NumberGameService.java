package com.manish.randomgengames.service;

import com.manish.randomgengames.dao.NumberRoundDao;
import com.manish.randomgengames.dto.number.NumberGuessRequest;
import com.manish.randomgengames.dto.number.NumberGuessResponse;
import com.manish.randomgengames.dto.number.NumberRoundResponse;
import com.manish.randomgengames.dto.number.StartNumberRoundRequest;
import com.manish.randomgengames.exception.InvalidGameRequestException;
import com.manish.randomgengames.exception.RoundNotFoundException;
import com.manish.randomgengames.model.NumberRound;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
// Runs the logic for starting and checking number-game rounds.
public class NumberGameService {

    // Sets the largest number of digits a round can use.
    private static final int MAX_DIGITS = 1_000;

    // Stores and retrieves number-game rounds.
    private final NumberRoundDao roundDao;

    /**
     * Creates the service with the DAO used to manage rounds.
     *
     * @param roundDao DAO that stores and retrieves rounds
     */
    public NumberGameService(NumberRoundDao roundDao) {
        this.roundDao = roundDao;
    }

    /**
     * Validates the request, creates a random number, and starts a new round.
     *
     * @param request requested number of digits
     * @return the new round information
     */
    public NumberRoundResponse startRound(StartNumberRoundRequest request) {
        // Gets the requested number of digits.
        int digitCount = request.digitCount();
        if (digitCount < 1 || digitCount > MAX_DIGITS) {
            throw new InvalidGameRequestException("digitCount must be between 1 and " + MAX_DIGITS);
        }

        // Creates the number that the player must remember.
        String number = generateRandomNumber(digitCount);

        // Saves the round and keeps its unique ID.
        String roundId = roundDao.save(new NumberRound(number, digitCount));

        // Shows up to six digits for three seconds and seven or more digits for six seconds.
        long displayTimeMs = digitCount > 6 ? 6_000 : 3_000;

        return new NumberRoundResponse(roundId, number, digitCount, displayTimeMs);
    }

    /**
     * Checks the player's guess and calculates the difficulty of the next round.
     *
     * @param request round ID and the player's guess
     * @return whether the guess was correct, the answer, and the next digit count
     */
    public NumberGuessResponse checkGuess(NumberGuessRequest request) {
        if (request.roundId() == null || request.guess() == null || !request.guess().matches("\\d+")) {
            throw new InvalidGameRequestException("A round ID and whole-number guess are required");
        }

        // Gets and removes the round so it cannot be answered twice.
        NumberRound round = roundDao.remove(request.roundId())
                .orElseThrow(() -> new RoundNotFoundException("Round not found or already completed"));

        // Checks whether the guess exactly matches the saved number.
        boolean correct = round.number().equals(request.guess());

        // Increases or decreases the digit count based on the answer.
        int nextDigitCount = DifficultyCalculator.calculateNextLevel(
                round.digitCount(),
                MAX_DIGITS,
                correct
        );

        return new NumberGuessResponse(correct, round.number(), nextDigitCount);
    }

    /**
     * Creates a random number with the requested number of digits.
     *
     * @param digitCount number of digits to create
     * @return the random number as text
     */
    private String generateRandomNumber(int digitCount) {
        // Builds the random number one digit at a time.
        StringBuilder number = new StringBuilder(digitCount);

        // Makes the first digit 1-9 so the number never starts with zero.
        number.append(ThreadLocalRandom.current().nextInt(1, 10));

        // Starts at index 1 because the first digit was already added.
        for (int index = 1; index < digitCount; index++) {
            number.append(ThreadLocalRandom.current().nextInt(10));
        }

        return number.toString();
    }
}

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
public class NumberGameService {

    private static final int MAX_DIGITS = 1_000;

    private final NumberRoundDao roundDao;

    public NumberGameService(NumberRoundDao roundDao) {
        this.roundDao = roundDao;
    }

    public NumberRoundResponse startRound(StartNumberRoundRequest request) {
        int digitCount = request.digitCount();
        if (digitCount < 1 || digitCount > MAX_DIGITS) {
            throw new InvalidGameRequestException("digitCount must be between 1 and " + MAX_DIGITS);
        }

        String number = generateRandomNumber(digitCount);
        String roundId = roundDao.save(new NumberRound(number, digitCount));
        long displayTimeMs = digitCount > 6 ? 6_000 : 3_000;

        return new NumberRoundResponse(roundId, number, digitCount, displayTimeMs);
    }

    public NumberGuessResponse checkGuess(NumberGuessRequest request) {
        if (request.roundId() == null || request.guess() == null || !request.guess().matches("\\d+")) {
            throw new InvalidGameRequestException("A round ID and whole-number guess are required");
        }

        NumberRound round = roundDao.remove(request.roundId())
                .orElseThrow(() -> new RoundNotFoundException("Round not found or already completed"));

        boolean correct = round.number().equals(request.guess());
        int nextDigitCount = DifficultyCalculator.calculateNextLevel(
                round.digitCount(),
                MAX_DIGITS,
                correct
        );

        return new NumberGuessResponse(correct, round.number(), nextDigitCount);
    }

    private String generateRandomNumber(int digitCount) {
        StringBuilder number = new StringBuilder(digitCount);
        number.append(ThreadLocalRandom.current().nextInt(1, 10));

        for (int index = 1; index < digitCount; index++) {
            number.append(ThreadLocalRandom.current().nextInt(10));
        }

        return number.toString();
    }
}

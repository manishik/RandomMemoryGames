package com.manish.randomgengames.service;

import com.manish.randomgengames.dao.NumberRoundDao;
import com.manish.randomgengames.dto.number.NumberGuessRequest;
import com.manish.randomgengames.dto.number.NumberGuessResponse;
import com.manish.randomgengames.dto.number.NumberRoundResponse;
import com.manish.randomgengames.dto.number.StartNumberRoundRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberGameServiceTest {

    // Runs the number-game logic being tested.
    private final NumberGameService service = new NumberGameService(new NumberRoundDao());

    // Verifies that generated numbers have the requested length and display time.
    @Test
    void createsExactLengthNumbersWithTheCorrectDisplayTime() {
        // Contains a one-digit round with the short display time.
        NumberRoundResponse oneDigitRound = startRound(1);

        // Contains a seven-digit round with the long display time.
        NumberRoundResponse sevenDigitRound = startRound(7);

        assertThat(oneDigitRound.number()).hasSize(1);
        assertThat(oneDigitRound.displayTimeMs()).isEqualTo(3_000);
        assertThat(sevenDigitRound.number()).hasSize(7);
        assertThat(sevenDigitRound.displayTimeMs()).isEqualTo(6_000);
    }

    // Verifies that a correct guess adds one digit to the next round.
    @Test
    void correctGuessRaisesTheDifficulty() {
        // Starts a two-digit round.
        NumberRoundResponse round = startRound(2);

        // Contains the result of submitting the correct number.
        NumberGuessResponse result = service.checkGuess(
                new NumberGuessRequest(round.roundId(), round.number())
        );

        assertThat(result.correct()).isTrue();
        assertThat(result.nextDigitCount()).isEqualTo(3);
    }

    // Verifies that a wrong guess cannot lower the difficulty below one digit.
    @Test
    void wrongGuessLowersTheDifficultyWithoutGoingBelowOne() {
        // Starts a round at the minimum difficulty.
        NumberRoundResponse round = startRound(1);

        // Contains the result of submitting the wrong number.
        NumberGuessResponse result = service.checkGuess(
                new NumberGuessRequest(round.roundId(), "0")
        );

        assertThat(result.correct()).isFalse();
        assertThat(result.nextDigitCount()).isEqualTo(1);
    }

    /**
     * Starts a number round for use by a test.
     *
     * @param digitCount number of digits requested
     * @return the new test round
     */
    private NumberRoundResponse startRound(int digitCount) {
        return service.startRound(new StartNumberRoundRequest(digitCount));
    }
}

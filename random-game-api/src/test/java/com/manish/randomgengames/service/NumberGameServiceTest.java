package com.manish.randomgengames.service;

import com.manish.randomgengames.dao.NumberRoundDao;
import com.manish.randomgengames.dto.number.NumberGuessRequest;
import com.manish.randomgengames.dto.number.NumberGuessResponse;
import com.manish.randomgengames.dto.number.NumberRoundResponse;
import com.manish.randomgengames.dto.number.StartNumberRoundRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NumberGameServiceTest {

    private final NumberGameService service = new NumberGameService(new NumberRoundDao());

    @Test
    void createsExactLengthNumbersWithTheCorrectDisplayTime() {
        NumberRoundResponse oneDigitRound = startRound(1);
        NumberRoundResponse sevenDigitRound = startRound(7);

        assertThat(oneDigitRound.number()).hasSize(1);
        assertThat(oneDigitRound.displayTimeMs()).isEqualTo(3_000);
        assertThat(sevenDigitRound.number()).hasSize(7);
        assertThat(sevenDigitRound.displayTimeMs()).isEqualTo(6_000);
    }

    @Test
    void correctGuessRaisesTheDifficulty() {
        NumberRoundResponse round = startRound(2);

        NumberGuessResponse result = service.checkGuess(
                new NumberGuessRequest(round.roundId(), round.number())
        );

        assertThat(result.correct()).isTrue();
        assertThat(result.nextDigitCount()).isEqualTo(3);
    }

    @Test
    void wrongGuessLowersTheDifficultyWithoutGoingBelowOne() {
        NumberRoundResponse round = startRound(1);

        NumberGuessResponse result = service.checkGuess(
                new NumberGuessRequest(round.roundId(), "0")
        );

        assertThat(result.correct()).isFalse();
        assertThat(result.nextDigitCount()).isEqualTo(1);
    }

    private NumberRoundResponse startRound(int digitCount) {
        return service.startRound(new StartNumberRoundRequest(digitCount));
    }
}

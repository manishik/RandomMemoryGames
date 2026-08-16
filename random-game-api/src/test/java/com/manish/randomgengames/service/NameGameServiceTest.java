package com.manish.randomgengames.service;

import com.manish.randomgengames.dao.NameCatalogDao;
import com.manish.randomgengames.dao.NameRoundDao;
import com.manish.randomgengames.dto.name.NameGuessRequest;
import com.manish.randomgengames.dto.name.NameGuessResponse;
import com.manish.randomgengames.dto.name.NameRoundResponse;
import com.manish.randomgengames.dto.name.StartNameRoundRequest;
import com.manish.randomgengames.model.NameMode;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NameGameServiceTest {

    // Loads the real name files for the service tests.
    private final NameCatalogDao catalogDao = new NameCatalogDao(
            new ClassPathResource("names/first-names.txt"),
            new ClassPathResource("names/last-names.txt")
    );

    // Runs the name-game logic being tested.
    private final NameGameService service = new NameGameService(catalogDao, new NameRoundDao());

    // Verifies that the catalog has the expected number of unique names.
    @Test
    void catalogContainsBetweenThreeHundredAndFiveHundredUniqueNames() {
        // Counts every first name and last name in the catalog.
        int totalNames = catalogDao.findAllFirstNames().size() + catalogDao.findAllLastNames().size();

        assertThat(totalNames).isBetween(300, 500);
        assertThat(catalogDao.findAllFirstNames()).doesNotHaveDuplicates();
        assertThat(catalogDao.findAllLastNames()).doesNotHaveDuplicates();
    }

    // Verifies that every name mode creates the requested type of names.
    @Test
    void createsFirstLastAndFullNameRounds() {
        // Contains a round that uses only first names.
        NameRoundResponse firstNames = startRound(3, NameMode.FIRST);

        // Contains a round that uses only last names.
        NameRoundResponse lastNames = startRound(3, NameMode.LAST);

        // Contains a round that combines first names and last names.
        NameRoundResponse fullNames = startRound(3, NameMode.FULL);

        assertThat(firstNames.names()).hasSize(3).doesNotHaveDuplicates().allMatch(name -> !name.contains(" "));
        assertThat(lastNames.names()).hasSize(3).doesNotHaveDuplicates().allMatch(name -> !name.contains(" "));
        assertThat(fullNames.names()).hasSize(3).doesNotHaveDuplicates().allMatch(name -> name.split(" ").length == 2);
    }

    // Verifies that case and extra spaces do not make correct guesses fail.
    @Test
    void acceptsCorrectGuessesWithoutCaseOrExtraWhitespaceDifferences() {
        // Starts a round containing two full names.
        NameRoundResponse round = startRound(2, NameMode.FULL);

        // Changes case and adds spaces to otherwise correct guesses.
        List<String> guesses = round.names().stream()
                .map(name -> "  " + name.toUpperCase() + "  ")
                .toList();

        // Contains the result of checking the changed guesses.
        NameGuessResponse result = service.checkGuess(
                new NameGuessRequest(round.roundId(), guesses)
        );

        assertThat(result.correct()).isTrue();
        assertThat(result.nextNameCount()).isEqualTo(3);
    }

    // Verifies that a wrong guess cannot lower the difficulty below one name.
    @Test
    void wrongGuessLowersTheDifficultyWithoutGoingBelowOne() {
        // Starts a round at the minimum difficulty.
        NameRoundResponse round = startRound(1, NameMode.FIRST);

        // Contains the result of submitting a wrong name.
        NameGuessResponse result = service.checkGuess(
                new NameGuessRequest(round.roundId(), List.of("NotTheName"))
        );

        assertThat(result.correct()).isFalse();
        assertThat(result.correctNames()).isEqualTo(round.names());
        assertThat(result.nextNameCount()).isEqualTo(1);
    }

    /**
     * Starts a name round for use by a test.
     *
     * @param nameCount number of names requested
     * @param nameMode type of names requested
     * @return the new test round
     */
    private NameRoundResponse startRound(int nameCount, NameMode nameMode) {
        return service.startRound(new StartNameRoundRequest(nameCount, nameMode));
    }
}

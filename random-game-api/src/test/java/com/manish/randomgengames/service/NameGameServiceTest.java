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

    private final NameCatalogDao catalogDao = new NameCatalogDao(
            new ClassPathResource("names/first-names.txt"),
            new ClassPathResource("names/last-names.txt")
    );
    private final NameGameService service = new NameGameService(catalogDao, new NameRoundDao());

    @Test
    void catalogContainsBetweenThreeHundredAndFiveHundredUniqueNames() {
        int totalNames = catalogDao.findAllFirstNames().size() + catalogDao.findAllLastNames().size();

        assertThat(totalNames).isBetween(300, 500);
        assertThat(catalogDao.findAllFirstNames()).doesNotHaveDuplicates();
        assertThat(catalogDao.findAllLastNames()).doesNotHaveDuplicates();
    }

    @Test
    void createsFirstLastAndFullNameRounds() {
        NameRoundResponse firstNames = startRound(3, NameMode.FIRST);
        NameRoundResponse lastNames = startRound(3, NameMode.LAST);
        NameRoundResponse fullNames = startRound(3, NameMode.FULL);

        assertThat(firstNames.names()).hasSize(3).doesNotHaveDuplicates().allMatch(name -> !name.contains(" "));
        assertThat(lastNames.names()).hasSize(3).doesNotHaveDuplicates().allMatch(name -> !name.contains(" "));
        assertThat(fullNames.names()).hasSize(3).doesNotHaveDuplicates().allMatch(name -> name.split(" ").length == 2);
    }

    @Test
    void acceptsCorrectGuessesWithoutCaseOrExtraWhitespaceDifferences() {
        NameRoundResponse round = startRound(2, NameMode.FULL);
        List<String> guesses = round.names().stream()
                .map(name -> "  " + name.toUpperCase() + "  ")
                .toList();

        NameGuessResponse result = service.checkGuess(
                new NameGuessRequest(round.roundId(), guesses)
        );

        assertThat(result.correct()).isTrue();
        assertThat(result.nextNameCount()).isEqualTo(3);
    }

    @Test
    void wrongGuessLowersTheDifficultyWithoutGoingBelowOne() {
        NameRoundResponse round = startRound(1, NameMode.FIRST);

        NameGuessResponse result = service.checkGuess(
                new NameGuessRequest(round.roundId(), List.of("NotTheName"))
        );

        assertThat(result.correct()).isFalse();
        assertThat(result.correctNames()).isEqualTo(round.names());
        assertThat(result.nextNameCount()).isEqualTo(1);
    }

    private NameRoundResponse startRound(int nameCount, NameMode nameMode) {
        return service.startRound(new StartNameRoundRequest(nameCount, nameMode));
    }
}

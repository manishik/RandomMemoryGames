package com.manish.randomgengames.controller;

import com.manish.randomgengames.dto.name.NameGuessRequest;
import com.manish.randomgengames.dto.name.NameGuessResponse;
import com.manish.randomgengames.dto.name.NameRoundResponse;
import com.manish.randomgengames.dto.name.StartNameRoundRequest;
import com.manish.randomgengames.service.NameGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/name-game")
@Tag(
        name = "Random Name Game",
        description = "Start name-memory rounds and check names entered in the original order"
)
public class NameGameController {

    // Runs the name-game logic for each request.
    private final NameGameService gameService;

    /**
     * Creates the controller with its name-game service.
     *
     * @param gameService service that runs name-game rounds
     */
    public NameGameController(NameGameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Receives a request and starts a new name-game round.
     *
     * @param request selected name count and name mode
     * @return the new name-game round
    */
    @PostMapping("/round")
    @Operation(
            summary = "Start a random-name round",
            description = "Creates a round with 1 to 20 random first, last, or full names. " +
                    "Returns the round ID and names for the player to remember."
    )
    public NameRoundResponse startRound(@RequestBody StartNameRoundRequest request) {
        return gameService.startRound(request);
    }

    /**
     * Receives the player's guesses and checks them.
     *
     * @param request round ID and guessed names
     * @return the result of the guesses
    */
    @PostMapping("/guess")
    @Operation(
            summary = "Check random-name guesses",
            description = "Checks every guessed name in its original order. Letter case and extra spaces are ignored. " +
                    "The round can only be checked once, and the response includes the next name count."
    )
    public NameGuessResponse checkGuess(@RequestBody NameGuessRequest request) {
        return gameService.checkGuess(request);
    }
}

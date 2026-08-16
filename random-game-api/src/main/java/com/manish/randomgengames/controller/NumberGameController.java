package com.manish.randomgengames.controller;

import com.manish.randomgengames.dto.number.NumberGuessRequest;
import com.manish.randomgengames.dto.number.NumberGuessResponse;
import com.manish.randomgengames.dto.number.NumberRoundResponse;
import com.manish.randomgengames.dto.number.StartNumberRoundRequest;
import com.manish.randomgengames.service.NumberGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/number-game")
@Tag(
        name = "Random Number Game",
        description = "Start number-memory rounds and check the number entered by the player"
)
public class NumberGameController {

    // Runs the number-game logic for each request.
    private final NumberGameService gameService;

    /**
     * Creates the controller with its number-game service.
     *
     * @param gameService service that runs number-game rounds
     */
    public NumberGameController(NumberGameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Receives a request and starts a new number-game round.
     *
     * @param request selected number of digits
     * @return the new number-game round
    */
    @PostMapping("/round")
    @Operation(
            summary = "Start a random-number round",
            description = "Creates a round with 1 to 1,000 digits and a non-zero first digit. " +
                    "The number is shown for three seconds up to six digits and six seconds from seven digits."
    )
    public NumberRoundResponse startRound(@RequestBody StartNumberRoundRequest request) {
        return gameService.startRound(request);
    }

    /**
     * Receives the player's guess and checks it.
     *
     * @param request round ID and guessed number
     * @return the result of the guess
    */
    @PostMapping("/guess")
    @Operation(
            summary = "Check a random-number guess",
            description = "Checks the entered digits against the number from the round. " +
                    "The round can only be checked once, and the response includes the next digit count."
    )
    public NumberGuessResponse checkGuess(@RequestBody NumberGuessRequest request) {
        return gameService.checkGuess(request);
    }
}

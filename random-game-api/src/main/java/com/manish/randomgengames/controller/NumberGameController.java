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
@Tag(name = "Random Number Game")
public class NumberGameController {

    private final NumberGameService gameService;

    public NumberGameController(NumberGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/round")
    @Operation(summary = "Start a random-number round")
    public NumberRoundResponse startRound(@RequestBody StartNumberRoundRequest request) {
        return gameService.startRound(request);
    }

    @PostMapping("/guess")
    @Operation(summary = "Check a random-number guess")
    public NumberGuessResponse checkGuess(@RequestBody NumberGuessRequest request) {
        return gameService.checkGuess(request);
    }
}

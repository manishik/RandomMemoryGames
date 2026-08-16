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
@Tag(name = "Random Name Game")
public class NameGameController {

    private final NameGameService gameService;

    public NameGameController(NameGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/round")
    @Operation(summary = "Start a random-name round")
    public NameRoundResponse startRound(@RequestBody StartNameRoundRequest request) {
        return gameService.startRound(request);
    }

    @PostMapping("/guess")
    @Operation(summary = "Check random-name guesses")
    public NameGuessResponse checkGuess(@RequestBody NameGuessRequest request) {
        return gameService.checkGuess(request);
    }
}

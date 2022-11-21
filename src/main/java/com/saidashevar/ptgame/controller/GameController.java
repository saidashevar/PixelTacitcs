package com.saidashevar.ptgame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.saidashevar.ptgame.controller.request.ConnectRequest;
import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.exception.game.NoMoreCardInDeckException;
import com.saidashevar.ptgame.exception.game.TooManyCardsInHandException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.GamePlay;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/game")
@AllArgsConstructor
@Slf4j
public class GameController {
	
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	 
	@PostMapping("/start")
	public ResponseEntity<Game> startGame(@RequestBody StringRequest stringRequest) {
		log.info("start game request: {}", stringRequest.getString());
		return ResponseEntity.ok(gameService.createGame(stringRequest.getString()));
	}

	@GetMapping("")
	public String game() {
		return "templates/Game.html";
	}
	
	@PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody StringRequest stringRequest) throws NotFoundException {
		log.info("connect random {}", stringRequest.getString());
        return ResponseEntity.ok(gameService.connectToRandomGame(stringRequest.getString()));
    }
	
	
	@PostMapping("/gameplay") //This is called everytime
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
	
	@PostMapping("/takecard") //This is called when player takes card from deck
    public ResponseEntity<Game> takeHand(@RequestBody ConnectRequest request) throws NotFoundException, InvalidGameException, NoMoreActionsLeftException, NoMoreCardInDeckException, TooManyCardsInHandException {
        log.info(request.getLogin() + "takes card");
        Game game = gameService.takeCardService(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
	
	@PostMapping("/loadgame") //This is called when game page first loading (may be later it will load saved games)  
    public ResponseEntity<Game> loadBoard(@RequestBody StringRequest request) throws NotFoundException, InvalidGameException {
        log.info("got game with ID: {}", request);
        Game game = gameService.loadGameService(request.getString());
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}

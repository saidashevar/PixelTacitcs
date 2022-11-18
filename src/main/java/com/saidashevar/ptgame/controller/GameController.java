package com.saidashevar.ptgame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.exception.game.NoMoreCardInDeckException;
import com.saidashevar.ptgame.exception.game.TooManyCardsInHandException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.GamePlay;
import com.saidashevar.ptgame.model.Player;
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
	public ResponseEntity<Game> startGame(@RequestBody String login) {
		log.info("start game request: {}", login);
		return ResponseEntity.ok(gameService.createGame(login));
	}

	@GetMapping("")
	public String game() {
		return "templates/Game.html";
	}
	
	@PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody String login) throws NotFoundException {
        log.info("connect random {}", login);
        return ResponseEntity.ok(gameService.connectToRandomGame(login));
    }
	
	
	@PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
        log.info("gameplay: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
	
	@PostMapping("/loadhand")
    public ResponseEntity<Player> loadHand(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException, NoMoreActionsLeftException, NoMoreCardInDeckException, TooManyCardsInHandException {
        log.info("Loading hand: {}", request);
        Game game = gameService.loadGameService(request.getGameId());
        Player player = gameService.loadHandService(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), player);
        return ResponseEntity.ok(player);
    }
	
	@PostMapping("/loadgame")
    public ResponseEntity<Game> loadBoard(@RequestBody String gameId) throws NotFoundException, InvalidGameException {
        log.info("got game with ID: {}", gameId);
        Game game = gameService.loadGameService(gameId);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}

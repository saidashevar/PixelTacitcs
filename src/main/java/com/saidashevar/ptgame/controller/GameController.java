package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saidashevar.ptgame.controller.support.Support;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/games")
@AllArgsConstructor
@Slf4j
public class GameController {

	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final Support support;

	// Rest methods
	@GetMapping
	List<Game> getGames() {
		return gameService.getGameRepository().findAll();
	}

	@PostMapping
	Game createGame(@RequestBody Game game) {
		return gameService.getGameRepository().save(game);
	}
	
	@GetMapping("/get-game") //Return object of "Game" class with given ID.
	ResponseEntity<Game> getGame(@RequestParam("id") String gameId) throws NotFoundException, InvalidGameException {
		log.info("Requested game with ID: " + gameId);
		//support.sendBoardActionsCards(gameId); seems not working
		return ResponseEntity.ok(gameService.loadGameService(gameId));
	}
	
	@GetMapping("/get-board-actions-cards")
	ResponseEntity<String> getBoardActionsCards(@RequestParam("id") String gameId) throws MessagingException, InvalidGameException {
		support.sendBoardActionsCards(gameId);
		return ResponseEntity.ok(new String("got BAC!"));
	}
	
	//
	// Game managing methods, maybe i should unite them with rest methods
	//
	
	@PostMapping("/start")
	public ResponseEntity<Game> startGame(@RequestBody Player player) throws NotFoundException {
		log.info("start game request: {}", player.getLogin());
		return ResponseEntity.ok(gameService.createGame(player.getLogin()));
	}

	@PostMapping("/connect/random")
	public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException { 
		log.info("connect random from {}", player.getLogin());
		Game game = gameService.connectToRandomGame(player.getLogin());
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getId(), //We send message that game status had changed because second player has conncted
				 							 gameService.getGame(game));
		return ResponseEntity.ok(game);
	}
}

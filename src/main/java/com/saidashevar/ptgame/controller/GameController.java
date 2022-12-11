package com.saidashevar.ptgame.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saidashevar.ptgame.controller.request.ConnectRequest;
import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.exception.game.NoMoreCardInDeckException;
import com.saidashevar.ptgame.exception.game.TooManyCardsInHandException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Hero;
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

	// Rest methods
	@GetMapping
	List<Game> getGames() {
		return gameService.getGameRepository().findAll();
	}

	@PostMapping
	Game createGame(@RequestBody Game game) {
		return gameService.getGameRepository().save(game);
	}
	
	@GetMapping("/get-game")
	ResponseEntity<Game> getGame(@RequestParam("id") String gameId) throws NotFoundException, InvalidGameException {
		return ResponseEntity.ok(gameService.loadGameService(gameId));
	}
	
	//
	// Game managing methods, maybe i should unite them with rest methods
	//
	
	
	@PostMapping("/start")
	public ResponseEntity<Game> startGame(@RequestBody Player player) {
		log.info("start game request: {}", player.getLogin());
		return ResponseEntity.ok(gameService.createGame(player));
	}

	@PostMapping("/connect/random")
	public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException { 
		log.info("connect random from {}", player.getLogin());
		return ResponseEntity.ok(gameService.connectToRandomGame(player)); 
	}
	
	@PostMapping("/loadgame") //This is called when game page first loading (may be later it will load saved games)
	public ResponseEntity<Game> loadBoard(@RequestBody StringRequest request) throws NotFoundException, InvalidGameException { 
		log.info("got game with ID: " + request.getString()); //String here is game id;
		Game game = gameService.loadGameService(request.getString());
		Set<Hero> heroes = new HashSet<>();
		game.getPlayers().forEach(p -> heroes.addAll(p.getBoard()));
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getId(), heroes); 
		return ResponseEntity.ok(game); 
	}
	
//	  @PostMapping("/placecard") //This is called everytime public
//	  ResponseEntity<Game> gamePlay(@RequestBody PlaceOperatorRequest request)
//	  throws NotFoundException, InvalidGameException, NoMoreActionsLeftException {
//	  log.info(request.getLogin() + "deploys operator"); Game game =
//	  gameService.placeCardService(request);
//	  simpMessagingTemplate.convertAndSend("/topic/game-progress/" +
//	  game.getGameId(), game); return ResponseEntity.ok(game); }
	 
}

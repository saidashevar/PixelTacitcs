package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.repository.GameRepository;
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
	 
	@Autowired
	GameRepository gameRepository;
	
	@GetMapping
	List<Game> getGames() {	return gameRepository.findAll(); }
	
	@PostMapping
	Game createGame(@RequestBody Game game) {
		return gameRepository.save(game);
	}
	
	/*
	 * @PostMapping("/start") public ResponseEntity<Game> startGame(@RequestBody
	 * StringRequest stringRequest) { log.info("start game request: {}",
	 * stringRequest.getString()); return
	 * ResponseEntity.ok(gameService.createGame(stringRequest.getString())); }
	 */

	/*
	 * @GetMapping("") public String game() { return "templates/Game.html"; }
	 */
	
	/*
	 * @PostMapping("/connect/random") public ResponseEntity<Game>
	 * connectRandom(@RequestBody StringRequest stringRequest) throws
	 * NotFoundException { log.info("connect random {}", stringRequest.getString());
	 * return
	 * ResponseEntity.ok(gameService.connectToRandomGame(stringRequest.getString()))
	 * ; }
	 */
	
	
	/*
	 * @PostMapping("/placecard") //This is called everytime public
	 * ResponseEntity<Game> gamePlay(@RequestBody PlaceOperatorRequest request)
	 * throws NotFoundException, InvalidGameException, NoMoreActionsLeftException {
	 * log.info(request.getLogin() + "deploys operator"); Game game =
	 * gameService.placeCardService(request);
	 * simpMessagingTemplate.convertAndSend("/topic/game-progress/" +
	 * game.getGameId(), game); return ResponseEntity.ok(game); }
	 * 
	 * @PostMapping("/takecard") //This is called when player takes card from deck
	 * public ResponseEntity<Game> takeHand(@RequestBody ConnectRequest request)
	 * throws NotFoundException, InvalidGameException, NoMoreActionsLeftException,
	 * NoMoreCardInDeckException, TooManyCardsInHandException {
	 * log.info(request.getLogin() + "takes card"); Game game =
	 * gameService.takeCardService(request);
	 * simpMessagingTemplate.convertAndSend("/topic/game-progress/" +
	 * game.getGameId(), game); return ResponseEntity.ok(game); }
	 */
	
	/*
	 * @PostMapping("/loadgame") //This is called when game page first loading (may
	 * be later it will load saved games) public ResponseEntity<Game>
	 * loadBoard(@RequestBody StringRequest request) throws NotFoundException,
	 * InvalidGameException { log.info("got game with ID: " + request.getString());
	 * Game game = gameService.loadGameService(request.getString());
	 * simpMessagingTemplate.convertAndSend("/topic/game-progress/" +
	 * game.getGameId(), game); return ResponseEntity.ok(game); }
	 */
}

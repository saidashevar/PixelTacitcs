package com.saidashevar.ptgame.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.controller.request.ConnectRequest;
import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.controller.support.Support;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.cards.Leader;
import com.saidashevar.ptgame.repository.PlayerRepository;
import com.saidashevar.ptgame.service.GameService;
import com.saidashevar.ptgame.service.PlayerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/players")
public class PlayerController {

	private final GameService gameService;
//	private final CardService cardService;
	private final PlayerService playerService;
//	private final SimpMessagingTemplate simpMessagingTemplate;
	private final Support support;
	
	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping
	List<Player> getPlayers() { return playerRepository.findAll(); }
	
	@GetMapping("/{login}")
	Player getPlayer(@PathVariable String login) throws NotFoundException {
		return playerService.getPlayer(login);
	}
	
	@PostMapping
	Player createPlayer(@RequestBody Player player) {
		return playerRepository.save(player); }
	
	// Game Management functions
	
	//Method returns player object, who will play game. It checks player with current login, returns it, if it exists, or creates new one. 
	@PostMapping("/checkLogin")
	ResponseEntity<Player> checkLogin(@RequestBody StringRequest request) {
		String login = request.getString();
		log.info(login + " checks his existance in database");
		return ResponseEntity.ok(playerService.checkPlayerLogin(request.getString()));
	}
	
	//Gameplay functions
	
	@PostMapping("/take-card") //This is called when player takes card from deck
	public ResponseEntity< Set<Card> > takeCard(@RequestBody ConnectRequest request) throws NotFoundException, InvalidGameException, NoMoreActionsLeftException {
		Game game = gameService.loadGameService(request.getGameId());
		Player player = playerService.takeCard(request.getLogin(), game);
		log.info(player.getLogin() + "takes card");
		//Send info about card count to both players
		support.sendBoardActionsCards(request.getGameId());
//		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
//											 gameService.getCardCount(request.getGameId())); 
		return ResponseEntity.ok(player.getHand()); 
	}
	
	@GetMapping("/get-hand")
	ResponseEntity< Set<Card> > getHand(@RequestParam("id") String gameId, @RequestParam("login") String login) throws NotFoundException, InvalidGameException {
		Game game = gameService.loadGameService(gameId);
		return ResponseEntity.ok(game.findPlayers(login)[0].getHand());
	}
	
	@GetMapping("/get-turn")
	ResponseEntity< Set<Player> > getTurn(@RequestParam("id") String gameId, @RequestParam("login") String login) throws InvalidGameException, NotFoundException {
		Game game = gameService.loadGameService(gameId);
		return ResponseEntity.ok(game.getPlayers());
	}
	
	@GetMapping("/get-leader")
	ResponseEntity<Leader> getLeader(@RequestParam("id") String gameId, @RequestParam("login") String login) throws InvalidGameException, NotFoundException {
		Game game = gameService.loadGameService(gameId);
		Leader leader = game.findPlayers(login)[0].getLeader();
		return ResponseEntity.ok(leader);
	}
	
	//This function is used to show player where he can hire his hero from hand
	@GetMapping("/get-places")
	ResponseEntity< List<Hero> > getAvailablePlaces(@RequestParam("id") String gameId, @RequestParam("login") String login) throws NotFoundException, InvalidGameException {
		Game game = gameService.loadGameService(gameId);
		return ResponseEntity.ok( playerService.getAvailablePlaces(game, login) );
	}
	
	//This function is used to show opponent's heroes that may be attacked with this hero
	@GetMapping("/get-targets")
	ResponseEntity< List<Hero> > getAvailableTargets(@RequestParam("id") String gameId, @RequestParam("login") String login) throws InvalidGameException, NotFoundException {
		Game game = gameService.loadGameService(gameId);
		Player secondPlayer = game.findPlayers(login)[1];
		List<Hero> targets = playerService.getAvailableTargets(game, secondPlayer);
		return ResponseEntity.ok(targets);
	}
}

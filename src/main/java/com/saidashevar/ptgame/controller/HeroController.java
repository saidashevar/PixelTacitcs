package com.saidashevar.ptgame.controller;

//eclipse don't want to import static automatically so i just save this here
//nothing bad happens, right?
//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS;
//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.FINISHED;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.PEACE;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.controller.request.DamageRequest;
import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.controller.support.Support;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.EffectRepository;
import com.saidashevar.ptgame.repository.GameRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;
import com.saidashevar.ptgame.service.GameService;
import com.saidashevar.ptgame.service.HeroService;
import com.saidashevar.ptgame.service.PlayerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/heroes")
public class HeroController {
	
	private final HeroService heroService;
	private final PlayerService playerService;
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final Support support;
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	EffectRepository effectRepository;
	@Autowired
	GameRepository gameRepository;
	
	@GetMapping
	List<Hero> getHeroes() { return heroRepository.findAll(); }
	
	@PostMapping
	Hero createHero(@RequestBody Hero hero) { return heroRepository.save(hero); }
	
	@GetMapping("/get-heroes")
	ResponseEntity< List<Hero> > getHeroes(@RequestParam("id") String gameId, 
										  @RequestParam("login") String login) throws NotFoundException, InvalidGameException {
		Game game = gameService.loadGameService(gameId);
		return ResponseEntity.ok(heroService.getHeroes(game));
	}
	
	@PostMapping("/hire-hero") //Returns to player his new hand. And sends board and card count to both players by socket
							   //Also should make all actions assisting making turn. (give new actions to second player if there are no one after hiring etc.)
	public ResponseEntity< Set<Card> > hireHero(@RequestBody HireHeroRequest request) throws InvalidGameException, NotFoundException, MessagingException, NoMoreActionsLeftException {
		log.info(request.getLogin() +" hires new Hero!");
		Game game = gameService.loadGameService(request.getGameId());
		Player[] players = game.findPlayers(request.getLogin());
		
		if (heroService.hireHero(game, players, request.getCoordinateX(), request.getCoordinateY(), request.getCardId())) {
			support.sendBoardActionsCards(request.getGameId());
		} else { //If something goes wrong, i hope it never happens
			simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
				gameService.message(request.getLogin() + " tries to hire hero in place where another hero stays... strange"));
		}
		return ResponseEntity.ok(playerService.getPlayer(request.getLogin()).getHand());
	}
	
	@PostMapping("/hire-leader") //returns hand after choosing leader and sends message that both players have chosen leader
	public ResponseEntity< Set<Card> > hireLeader(@RequestBody HireHeroRequest request) throws NotFoundException, InvalidGameException {
		log.info(request.getLogin() +" hires new Leader!");
		
		Game game = gameService.loadGameService(request.getGameId());
		game.nextLeaderStatus();
		
		Player player = 
		playerService.savePlayer(
			heroService.hireLeader(
				playerService.getPlayer(request.getLogin()),
				request.getCardId()
			)
		);
		
		//Now send players new info about leaders
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
												 gameService.getGame(game));
		
		//As response - new player's hand
		return ResponseEntity.ok(player.getHand());
	}
	
	@PostMapping("/damage")
	//ResponseEntity< Set<Hero> >
	public ResponseEntity<String> makeDamage(@RequestBody DamageRequest request) throws NotFoundException, MessagingException, InvalidGameException {
		log.info(request.getLogin() +" ATTACKS!");
		Game game = gameService.loadGameService(request.getGameId());
		Player[] players = game.findPlayers(request.getLogin());
		try {
			players[0].makeAction(game, players[1], effectRepository, gameRepository);
			heroService.heroAttacked(request);
//			var resp = gameService.getHeroes(request.getGameId());
//			simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(), resp);
			support.sendBoardActionsCards(request.getGameId());
			return ResponseEntity.ok("Attack successful!");
		} catch (NoMoreActionsLeftException e) {
			log.info(e.getMessage());
			return ResponseEntity.badRequest().body("You ran out of actions!"); //oh my god
		}	
	}
	
	//It is called when we move hero's corpse into pile
	//No checking that this hero is ours!
	@PostMapping("/removeCorpse") //may be i should code another request class for better understanding
	public ResponseEntity< String > removeCorpse(@RequestBody HireHeroRequest request) throws NotFoundException, InvalidGameException, NoMoreActionsLeftException {
		log.info(request.getLogin() +" removes corpse");
		Game game = gameService.loadGameService(request.getGameId());
		Player[] players = game.findPlayers(request.getLogin());
		
		if (heroService.removeHero(game, players, (long)request.getCardId())) //I am testing that evth is alright
			support.sendBoardActionsCards(request.getGameId()); // then send a message to client with new info
		
		return ResponseEntity.ok("You successfully disposed of corpse");
	}
}

package com.saidashevar.ptgame.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Hero;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
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
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	
	@GetMapping
	List<Hero> getHeroes() { return heroRepository.findAll(); }
	
	@PostMapping
	Hero createHero(@RequestBody Hero hero) { return heroRepository.save(hero); }
	
	@GetMapping("/get-heroes")
	ResponseEntity< List<Hero> > getBoard(@RequestParam("id") String gameId, 
										 @RequestParam("login") String login) {
		return ResponseEntity.ok(heroRepository.findAll().stream().filter(card -> card.getPlayer().getLogin().equals(login)).toList());
	}
	
	@PostMapping("/hire-hero") //Returns to player his new hand. And sends board and card count to both players by socket
	public ResponseEntity< Set<Card> > hireHero(@RequestBody HireHeroRequest request) throws InvalidGameException, NotFoundException {
		log.info(request.getLogin() +" hires new Hero!");
		heroService.hireHero(request);
		//Now we have to send both players board, second player must know hero his opponent hired and where.
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
											 gameService.getBoard(request.getGameId()));
		//Second one sends info about card count
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
				 							 gameService.getCardCount(request.getGameId()));
		return ResponseEntity.ok(playerService.getPlayer(request.getLogin()).getHand());
	}
}

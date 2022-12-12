package com.saidashevar.ptgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Hero;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HeroService {
	
	private final GameService gameService;
	private final PlayerService playerService;
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	PlayerRepository playerRepository;
	
	public Hero hireHero(HireHeroRequest request) throws InvalidGameException, NotFoundException { //this is not necessary to return anything
		Game game = gameService.loadGameService(request.getGameId());
		Player player = playerService.getPlayer(request.getLogin());
		Card card = cardRepository.findById(request.getCardId())
				.orElseThrow(() -> new NotFoundException("Card with id:" + request.getCardId() + " wasn't found"));
		Hero hero = new Hero(
				game.getWave(), 
				request.getCoordinateY(), 
				card,
				player
		);
		
		card.hiredBy(player);
		cardRepository.save(card);
		
		return heroRepository.save(hero);
	}
}

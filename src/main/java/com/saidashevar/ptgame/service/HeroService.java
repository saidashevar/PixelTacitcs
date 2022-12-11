package com.saidashevar.ptgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Hero;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.HeroRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HeroService {
	
	private final GameService gameService;
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	
	public Hero hireHero(HireHeroRequest request) throws InvalidGameException, NotFoundException {
		Game game = gameService.loadGameService(request.getGameId());
		Card card = cardRepository.findById(request.getCardId())
				.orElseThrow(() -> new NotFoundException("Card with id:" + request.getCardId() + " wasn't found"));
		Hero hero = new Hero(
				game.getWave(), 
				request.getCoordinateY(), 
				card,
				game.findPlayers(request.getLogin())[0]
		);
		heroRepository.save(hero);
		return hero;
	}
}

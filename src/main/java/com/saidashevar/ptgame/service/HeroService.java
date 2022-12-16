package com.saidashevar.ptgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.cards.Leader;
import com.saidashevar.ptgame.model.cards.LeaderBasis;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.LeaderBasisRepository;
import com.saidashevar.ptgame.repository.LeaderRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HeroService {
	
	//Other service dependencies should be removed
	private final GameService gameService;
	private final PlayerService playerService;
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	LeaderRepository leaderRepository;
	@Autowired
	LeaderBasisRepository leaderBasisRepository;
	
	public Hero hireHero(HireHeroRequest request) throws InvalidGameException, NotFoundException { //this is not necessary to return anything
		Game game = gameService.loadGameService(request.getGameId());
		Player player = playerService.getPlayer(request.getLogin());
		Card card = cardRepository.findById(request.getCardId())
				.orElseThrow(() -> new NotFoundException("Card with id:" + request.getCardId() + " wasn't found"));
		Hero hero = new Hero(
				card,
				game.getWave(), 
				request.getCoordinateY(), 
				player
		);
		
		return heroRepository.save(hero);
	}
	
	public Player hireLeader(Player player, int leaderId) throws NotFoundException { //this is not necessary to return anything
		LeaderBasis leaderBasis = leaderBasisRepository.findById(leaderId + 25)
				.orElseThrow(() -> new NotFoundException("Leader with that id: " + leaderId + " doesn't exist")); //I won't create LeaderService for this
		player.setLeader(new Leader(leaderBasis, player));
		player.removeLeaderCardFromHand(leaderId);
		return player;
	}
}

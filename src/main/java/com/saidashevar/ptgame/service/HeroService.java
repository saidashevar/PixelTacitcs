package com.saidashevar.ptgame.service;

import java.util.ArrayList;
import java.util.List;

//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS;
//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.PEACE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.controller.request.DamageRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.CardBasis;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.cards.Leader;
import com.saidashevar.ptgame.model.cards.LeaderBasis;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.EffectRepository;
import com.saidashevar.ptgame.repository.GameRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.LeaderBasisRepository;
import com.saidashevar.ptgame.repository.LeaderRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class HeroService {
	
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
	@Autowired
	EffectRepository effectRepository;
	@Autowired
	GameRepository gameRepository;
	
	private Leader getLeader(long id) throws NotFoundException {
		return leaderRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Leader with some id while attack wasn't found"));
	}
	
	private Hero getHero(long id) throws NotFoundException {
		return heroRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Hero with some id while attack wasn't found"));
	}
	
	//Returns all heroes on board, excluding leaders.
	public List<Hero> getHeroes(Game game) {
		List<Hero> allHeroes = new ArrayList<>();
		game.getPlayers().stream().forEach(
				p -> allHeroes.addAll(
					heroRepository.findHeroesOfPlayer(p)
				)
			);
		return allHeroes;
	}
	
//	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean hireHero(Game game, 
							Player[] players, 
							int x, int y, 
							int cardId) throws InvalidGameException, NotFoundException, NoMoreActionsLeftException { //this is not necessary to return anything
		
		if (!heroRepository.heroOnPlace(players[0].getLogin(), x, y)) {
			log.info("No hero is on this place, hero successfully hired");;
			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new NotFoundException("Card with id: " + cardId + " wasn't found"));
			players[0].removeCardFromHand(card);
			
			heroRepository.save(new Hero(card, x, y, players[0]));
			playerRepository.save(players[0].makeAction(game, players[1], effectRepository, gameRepository));// do not divide this
			playerRepository.save(players[1]);

			return true;
		} else
			log.info("On this place some hero was found!");
			return false;
	}
	
	public Player hireLeader(Player player, int leaderId) throws NotFoundException { //this is not necessary to return anything
		LeaderBasis leaderBasis = leaderBasisRepository.findById(leaderId + 25) 									//For now leaders have id of connected cards +25. It may be changed many ways, so this line with !CAUTION!
				.orElseThrow(() -> new NotFoundException("Leader with that id: " + leaderId + " doesn't exist")); 	//I won't create LeaderService for this
		player.setLeader(new Leader(leaderBasis, player));
		player.removeLeaderCardFromHand(leaderId);
		
		return player;
	}
	
	public void heroAttacked(DamageRequest request) throws NotFoundException { //totally not most optimized function
		
		//Defining attacker. I am sure there is a much better way to do that without IF statement
		CardBasis attacker;
		if (request.isAttackerIsLeader()) 
			attacker = getLeader(request.getAttackerId());
		else 
			attacker = getHero(request.getAttackerId());
		
		//Defining target and saving damage effect
		CardBasis target;
		if (request.isTargetIsLeader()) { //yeah yeah, repeating
			
			target = getLeader(request.getTargetId());
			target.saveEffect(
					effectRepository.save(
							target.takeDamage( attacker.getAttack() )
					)
			);
			
			leaderRepository.save((Leader)target);
		} else {
			target = getHero(request.getTargetId());
			target.saveEffect(
					effectRepository.save(
							target.takeDamage(attacker.getAttack())
					)
			);
			
			heroRepository.save((Hero)target);
		}
		log.info("effects saved!"); //that also waits till hero saved... i believe
	}
	
	//It is called when we drag hero's corpse into pile
	
	public boolean removeHero(Game game, Player[] players, Long heroId) throws NotFoundException, NoMoreActionsLeftException {
		//first checking and saving that player has one action
		players[0].makeAction(game, players[1], effectRepository, gameRepository);
		//then we disconnect player with this hero
		Hero hero = getHero(heroId);
		Card card = hero.getCard();
		cardRepository.saveAndFlush(card.killHero(hero));
		//I am using such nesting constructions because i don't know how to get rid of them. They work. Without them not work
		//playerRepository.save(players[0].removeCorpseOfHero(heroRepository.save(hero.removeEffects())));s
		//then we disconnect card with this hero
		//Adding hero as a card to pile and saving it
		cardRepository.saveAndFlush(card.addInPile(playerRepository.saveAndFlush(players[0].removeCorpseOfHero(heroRepository.saveAndFlush(hero.removeEffects())))));
		//Now when nothing connects this hero with this world, we remove him from board (but not from game completely, this will be in later updates)
		heroRepository.delete(hero);
		return true; //for tests
	}
}

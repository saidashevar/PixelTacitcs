package com.saidashevar.ptgame.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.EffectRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class PlayerService {
	
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	EffectRepository effectRepository;
	
	//RESTful functions
	
	public Player getPlayer(String login) throws NotFoundException {
		return playerRepository.findById(login)
				.orElseThrow(() -> new NotFoundException("Player with login; " + login + " wasn't found")); 
	}
	
	public synchronized Player savePlayer(Player player) {
		return playerRepository.save(player); // We use flush, because we often need that player in the next moment, for creating game. 
	}
	
	//Game management functions
	
	public synchronized Player checkPlayerLogin(String login) { //Check if this login exists, if not creates new one and defines his color.
		try {
			Player player = playerRepository.findById(login)
					.orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found"));
			log.info("Player with login: " + login + " was found");
			return player;
		} catch (NotFoundException e) {
			log.info("There is no player with login: " + login + ". Creating new one");
			Player player = savePlayer(new Player(login));
			return player;
		}
	}
	
	//Gameplay functions
	
	public void takeStartHand(Player player) throws NotFoundException {
		for (int i = 0; i < 6; i++) { //at start of the game we give player 6 cards.
			Card card = player.findCardToTake();
			player.takeCard(card);
		}
	} 
	
	public Player takeCard(String requester, Game game) throws NotFoundException, NoMoreActionsLeftException { //may be more compact and easer...
		Player[] players = game.findPlayers(requester);
		Card card = players[0].findCardToTake();
		players[0].takeCard(card);
		players[0].makeAction(game, players[1], effectRepository);
		playerRepository.save(players[0]);
		playerRepository.save(players[1]);
		return players[0];
	}
	
	//This function is used when player connects to new game only.
	public void takeDeckAndHand(Player player) throws NotFoundException {
		player.setDeck(new HashSet<>(cardRepository.findAll()));
		takeStartHand(player);
	}
	
	
	//Next are functions that are used during the game
	//This method is used when player should see where hiring is possible
	//Why did i separate game wave and players' waves? who knows...
	public List<Hero> getAvailablePlaces(Game game, String login) {
		List<Hero> places = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			if (!heroRepository.heroOnPlace(login, game.getWave(), i)) 
				places.add(new Hero(game.getWave(), i)); 
		}
		return places;
	}
	
	public List<Hero> getAvailableTargets(Player player) {
		List<Hero> heroesOpenedForAttack = new ArrayList<>();
		
		//Here we check first unit in each column
		//And add first heroes of each coloumn to list of available targets.
		//We think all attacks are melee now...
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				if (heroRepository.heroOnPlace(player.getLogin(), i, j)) {
					heroesOpenedForAttack.add(new Hero(i, j));
					break;
				}
				
				if (i == 0 && j == 1) { //add leader as available target, if nobody covers him
					heroesOpenedForAttack.add(new Hero(i+1, j));
					break;
				}
			}
		}
		
		return heroesOpenedForAttack;
	}
}

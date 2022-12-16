package com.saidashevar.ptgame.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.repository.CardRepository;
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
	CardRepository cardRepository;
	
	public Player getPlayer(String login) throws NotFoundException {
		return playerRepository.findById(login)
				.orElseThrow(() -> new NotFoundException("Player with login; " + login + " wasn't found")); 
	}
	
	public synchronized Player savePlayer(Player player) {
		return playerRepository.save(player); // We use flush, because we often need that player in the next moment, for creating game. 
	}
	
	public synchronized Player checkPlayerLogin(String login) {
		try {
			Player player = playerRepository.findById(login)
					.orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found"));
			log.info("Player with login: " + login + " was found");
			return player;
//			return ResponseEntity.ok(playerRepository.findAll().stream().filter(p -> p.getLogin().equals(login))
//					.findAny().orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found")));
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
	
	public Player takeCard(String requester) throws NotFoundException {
		Player player = getPlayer(requester);
		Card card = player.findCardToTake();
		player.takeCard(card);
		playerRepository.save(player);
		return player;
	}
	
	public void takeDeckAndHand(Player player) throws NotFoundException {
		player.setDeck(new HashSet<>(cardRepository.findAll()));
		takeStartHand(player);
//		cardRepository.findAll().stream().forEach(card -> { card.addInDeck(player); cardRepository.save(card);});
	}
}

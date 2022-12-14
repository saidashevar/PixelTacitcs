package com.saidashevar.ptgame.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Player;
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
		return playerRepository.save(player);
	}
	
	public synchronized ResponseEntity<Player> checkPlayerLogin(String login) {
		try {
			Player player = playerRepository.findById(login)
					.orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found"));
			log.info("Player with login: " + login + " was found");
			return ResponseEntity.ok(player);
//			return ResponseEntity.ok(playerRepository.findAll().stream().filter(p -> p.getLogin().equals(login))
//					.findAny().orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found")));
		} catch (NotFoundException e) {
			log.info("There is no player with login: " + login + ". Creating new one");
			return ResponseEntity.ok(savePlayer(new Player(login)));
		}
	}
	
	//Gameplay functions
	
	public void takeStartHand(Player player) throws NotFoundException {
		for (int i = 0; i < 6; i++) { //at start of the game we give player 6 cards.
			Card card = player.findCardToTake();
			player.takeCard(card);
			playerRepository.save(player);
		}
	} 
	
	public Player takeCard(String requester) throws NotFoundException {
		Player player = getPlayer(requester);
		Card card = player.findCardToTake();
		player.takeCard(card);
		playerRepository.save(player);
		return player;
	}
	
	public void takeDeck(Player player) throws NotFoundException {
		player.setDeck(new HashSet<>(cardRepository.findAll()));
		savePlayer(player);
//		playerRepository.save(player);
//		cardRepository.findAll().stream().forEach(card -> { card.addInDeck(player); cardRepository.save(card);});
		
//		synchronized (player) {
//			while (player.getDeck().size() <= 15)
//				try {
//					player.wait();
//				} catch (InterruptedException e) {}
//		}
//		takeStartHand(player);
	}
}

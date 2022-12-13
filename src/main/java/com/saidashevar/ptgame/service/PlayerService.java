package com.saidashevar.ptgame.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;

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
		playerRepository.save(player);
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

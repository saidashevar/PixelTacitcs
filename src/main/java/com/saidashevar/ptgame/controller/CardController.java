package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

@RestController
@RequestMapping("/cards")
public class CardController {
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping
	List<Card> getCards() {	return cardRepository.findAll(); }
	
	@PostMapping
	Card createCard(@RequestBody Card card) {
		return cardRepository.save(card);
	}
	
	@PutMapping("/{cardId}/players/{playerLogin}")
	Card connectPlayerWithCard(
			@PathVariable Long cardId,
			@PathVariable String playerLogin
			) throws NotFoundException {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new NotFoundException("card named " + cardId + " wasn't found"));
		Player player = playerRepository.findById(playerLogin)
				.orElseThrow(() -> new NotFoundException("player with login: " + playerLogin + "wasn't found"));
		card.addInDeck(player);
		return cardRepository.save(card);
	}	
}

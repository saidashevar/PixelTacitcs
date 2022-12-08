package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.repository.CardRepository;

@RestController
@RequestMapping("/cards")
public class CardController {
	
	@Autowired
	CardRepository cardRepository;
	
	@GetMapping
	List<Card> getCards() {	return cardRepository.findAll(); }
	
	@PostMapping
	Card createCard(@RequestBody Card card) {
		return cardRepository.save(card);
	}
}

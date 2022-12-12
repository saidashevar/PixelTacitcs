package com.saidashevar.ptgame.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.saidashevar.ptgame.config.response.ResponseTypes.*;
import com.saidashevar.ptgame.config.response.UniResponse;
import com.saidashevar.ptgame.controller.request.ConnectRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;
import com.saidashevar.ptgame.service.CardService;
import com.saidashevar.ptgame.service.GameService;
import com.saidashevar.ptgame.service.PlayerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cards")
public class CardController {
	
	private final CardService cardService;
	private final PlayerService playerService;
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
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
	
	@PostMapping("/take-card") //This is called when player takes card from deck
	public ResponseEntity< List<Card> > takeCard(@RequestBody ConnectRequest request) throws NotFoundException, InvalidGameException {
		Player player = cardService.cardTakenBy(request.getLogin());
		log.info(player.getLogin() + "takes card");
		//Send info about card count to both players
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
											 gameService.getCardCount(request.getGameId())); 
		return ResponseEntity.ok(player.getHand()); 
	}
	
	@PostMapping("/updateAll") //fast restoring
	void createCards() {
		cardRepository.save(new Card(2, "Cursed_Knight", 3, 6));
		cardRepository.save(new Card(2, "Diabolist", 1, 3));
		cardRepository.save(new Card(2, "Divinity", 2, 4));
		cardRepository.save(new Card(2, "Druid", 2, 5));
		cardRepository.save(new Card(2, "Chronicler", 1, 4));
		cardRepository.save(new Card(2, "Immortal", 3, 2));
		cardRepository.save(new Card(2, "Inventor", 1, 5));
		cardRepository.save(new Card(2, "Lorekeeper", 2, 4));
		cardRepository.save(new Card(2, "Magical_Knight", 2, 4));
		cardRepository.save(new Card(2, "Puppeter", 2, 4));
		cardRepository.save(new Card(2, "Relic_Hunter", 3, 7));
		cardRepository.save(new Card(2, "Sage", 0, 4));
		cardRepository.save(new Card(2, "Plague_Bearer", 3, 4));
		cardRepository.save(new Card(2, "Monster_Hunter", 3, 5));
		cardRepository.save(new Card(2, "Mastermind", 3, 6));
		cardRepository.save(new Card(2, "Planebinder", 2, 5));
		cardRepository.save(new Card(2, "Operative", 2, 5));
		cardRepository.save(new Card(2, "Necromancer", 2, 6));
		cardRepository.save(new Card(2, "Warmage", 4, 3));
		cardRepository.save(new Card(2, "Tactitian", 0, 4));
		cardRepository.save(new Card(2, "Supervillain", 0, 3));
		cardRepository.save(new Card(2, "Warlock", 2, 3));
		cardRepository.save(new Card(2, "Technologist", 3, 3));
		cardRepository.save(new Card(2, "Sniper", 6, 3));
		cardRepository.save(new Card(2, "Werewolf", 3, 5));
	}
}

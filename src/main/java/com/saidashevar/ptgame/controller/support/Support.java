package com.saidashevar.ptgame.controller.support;

import java.util.Set;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class Support {
	
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	//Next is a support function is BAC - sending both players info about board, action and card count
	//Now it is added side card position also (sword and shield)
	//It is a overwhelming sometimes, but significantly improves code readability
	public void sendBoardActionsCards(String gameId) throws MessagingException, InvalidGameException {
		Set<Player> players = gameService.loadGameService(gameId).getPlayers(); //get players here to not run this method anywhere else
		//We are sending both players board to show them current heroes and their statuses
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
												gameService.getHeroes(players, gameId));
		//Also players must know card count of each other. No one card from any game edition has any ability to hide this
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
				 								gameService.getCardCount(players, gameId));
		//And at last send info about actions count. Must be always known
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
				 								gameService.getActionsCount(players, gameId));
		//New addition in iss7 - sending side cards position
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
												gameService.getTurns(players, gameId));
	}
}

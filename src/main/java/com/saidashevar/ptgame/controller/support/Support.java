package com.saidashevar.ptgame.controller.support;

import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class Support {
	
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	//Next is a support function is BAC - sending both players info about board, action and card count
	//It is a overwhelming sometimes, but significantly improves code readability
	public void sendBoardActionsCards(String gameId) throws MessagingException, InvalidGameException {
		//We are sending both players board to show them current heroes and their statuses
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
											 gameService.getBoard(gameId));
		//Also players must know card count of each other. No one card from any game edition has any ability to hide this
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
				 							 gameService.getCardCount(gameId));
		//And at last send info about actions count. Must be always known
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + gameId,
				 							 gameService.getActionsCount(gameId));
	}
}

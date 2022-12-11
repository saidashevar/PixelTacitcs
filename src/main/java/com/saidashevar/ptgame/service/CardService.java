package com.saidashevar.ptgame.service;

import static com.saidashevar.ptgame.model.GameStatus.*;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.controller.request.ConnectRequest;
import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.exception.game.NoMoreCardInDeckException;
import com.saidashevar.ptgame.exception.game.TooManyCardsInHandException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.GamePlay;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.GameRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class CardService {
	
	@Autowired
	CardRepository cardRepository;
	//
	// Game managment functions
	//
	
	public void giveDeck(Player player) {
		cardRepository.findAll().stream().forEach(card -> {card.addInDeck(player); cardRepository.save(card);});
	}
	//
	// Gameplay methods
	//
	
	  //It's enough to send info about deck and hand here, but I think it's not
//	  critical to send all info about player. //Maybe later with Spring Security i
//	  will improve this code. public Game takeCardService(ConnectRequest request)
//	  throws NotFoundException, InvalidGameException, NoMoreActionsLeftException,
//	  NoMoreCardInDeckException, TooManyCardsInHandException { Game game =
//	  loadGameService(request.getGameId());
//	  
//	  checkForActions(game, request.getLogin()); checkCardsInDeck(game,
//	  request.getLogin()); checkIfHandIsFull(game, request.getLogin());
//	  
//	  Player player = game.getPlayers().get(request.getLogin());
//	  player.getHand().add(player.getDeck().get(0)); player.getDeck().remove(0);
//	  player.getTurn().setActionsLeft((byte)(player.getTurn().getActionsLeft()-1));
//	  GameStorage.getInstance().setGame(game); return game; }
//	  
//	  public Game placeCardService(PlaceOperatorRequest request) throws
//	  InvalidGameException, NotFoundException, NoMoreActionsLeftException { Game
//	  game = loadGameService(request.getGameId()); checkForActions(game,
//	  request.getLogin());
//	  
//	  Player player = game.getPlayers().get(request.getLogin()); Card[][] board =
//	  player.getBoard();
//	  
//	  board[game.getWave()][request.getCoordinateY()-1] =
//	  player.getHand().get(request.getCardNumber());
//	  player.getHand().remove(request.getCardNumber());
//	  player.getTurn().setActionsLeft((byte)(player.getTurn().getActionsLeft()-1));
//	  
//	  GameStorage.getInstance().setGame(game); return game; }
//	  
	  //
	  // Check methods or support methods 
	  //
	  
	  //Throwing errors when something simple happens may be a bad practice...
	  //Should check it later. should change it later! private void
//	private void checkForActions(Game game, String requester) throws NoMoreActionsLeftException { 
//		if (game.getPlayers().get(requester).getTurn().getActionsLeft() <= 0)
//		throw new NoMoreActionsLeftException(requester + "has no more actions now!"); 
//	}
	
//	private void checkCardsInDeck(Game game, String requester) throws NoMoreCardInDeckException { 
//		if (game.getPlayers().get(requester).getDeck().isEmpty())
//		throw new NoMoreCardInDeckException(requester + "had no more cards in his deck!"); 
//	}
	
//	private void checkIfHandIsFull(Game game, String requester) throws TooManyCardsInHandException { 
//		if (game.getPlayers().get(requester).getHand().size() >= 5)
//		throw new TooManyCardsInHandException(requester + "had no more cards in his deck!"); 
//	}
	  
//	private void checkToPassTheMove(Game game) {}
	  
	//
	//Getters and setters
	//
//	public GameRepository getGameRepository() {
//		return gameRepository;
//	}
//
//	public void setGameRepository(GameRepository gameRepository) {
//		this.gameRepository = gameRepository;
//	}
}

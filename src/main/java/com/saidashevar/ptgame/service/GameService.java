package com.saidashevar.ptgame.service;

import static com.saidashevar.ptgame.config.response.ResponseTypes.BOARD;
import static com.saidashevar.ptgame.config.response.ResponseTypes.CARD_COUNT;
import static com.saidashevar.ptgame.model.GameStatus.FINISHED;
import static com.saidashevar.ptgame.model.GameStatus.IN_PROGRESS;
import static com.saidashevar.ptgame.model.GameStatus.NEW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.MyDebug;
import com.saidashevar.ptgame.config.response.UniResponse;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Hero;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.GameRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GameService {
	
	//This service depends on other services only
//	private final CardService cardService;
	private final PlayerService playerService;
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	HeroRepository heroRepository;
	
	//
	// First are connection and game managing methods
	//
	
	public synchronized Game saveGame(Game game) {
		return gameRepository.save(game);
	}
	
//	public synchronized boolean checkGame(String id, Boolean i) {
//		
//	}
	
	//Here Player is checked. It certainly exists in db.
	public Game createGame(String login) throws NotFoundException {
		Player player = playerService.checkPlayerLogin(login);
		Game game = new Game(player);
		playerService.takeDeckAndHand(player);
		playerService.savePlayer(player);
		return saveGame(game);
//		Boolean i = true; //this was used for debugging, i will erase it later
//		while (i) {
//			try {
//				gameRepository.findById(game.getId());
//				i = false;
//				
//				try {
//				    Thread.sleep(2 * 1000);
//				} catch (InterruptedException ie) {
//				    Thread.currentThread().interrupt();
//				}
//			}
//			catch (Exception e) {
//				i = true;
//			}
//		}
	}
	
	public Game connectToRandomGame(Player player) {
		Game game;
		try {
			game = gameRepository.findAll().stream()
					.filter(g -> g.getStatus().equals(NEW)).findFirst()
					.orElseThrow(() -> new NotFoundException("Game not found"));
			game.setStatus(IN_PROGRESS);
			game.addPlayer(player);
			gameRepository.save(game);
//			cardService.giveDeck(player);
//			cardService.giveStartHand(player);
			playerService.takeDeckAndHand(player);
			return game;
		} catch (NotFoundException e) {
			log.info("Game wasn't found");
			return null;
		} 
	} 
	
	//
	//Next are service methods that load some part of game or whole game. Now there is one method, though.
	//
	
	public Game loadGameService(String gameId) throws InvalidGameException { //This method... is strange a bit 
		Game game;
		try {
			game = gameRepository.findById(gameId)
					.orElseThrow(() -> new NotFoundException("Game not found"));
		} catch (NotFoundException e) {
			log.info("game with id: " + gameId + ", not found");
			return null;
		}
		
		if (game.getStatus().equals(FINISHED)) { 
			throw new InvalidGameException("Game is already finished"); 
		}
		
		return game; 
	}
	
	// Next methods prepare information about game for both players (board, card count)
	
	public UniResponse< Set<Hero> > getBoard(String gameId) throws InvalidGameException {
		Set<Hero> set = new HashSet<>();
		loadGameService(gameId).getPlayers().stream().forEach(p -> set.addAll(p.getBoard()));
		return new UniResponse< Set<Hero> >(BOARD, set);
	}
	
	public UniResponse< Map<String, Integer> > getCardCount(String gameId) throws InvalidGameException {
		Map<String, Integer> cardCount = new HashMap<>();
		loadGameService(gameId).getPlayers().stream()
			.forEach(p -> cardCount.put(p.getLogin(), p.getHand().size()));
		return new UniResponse< Map<String, Integer> >(CARD_COUNT, cardCount);
	}
	
	public String readSet (Set<Card> hand) {
		MyDebug myDebug = new MyDebug(); 
		hand.stream().forEach(c -> myDebug.string += c.getId().toString() + " ");
		return myDebug.string;
	}
	  
	//
	//Getters and setters
	//
	
	public GameRepository getGameRepository() {
		return gameRepository;
	}

	public void setGameRepository(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
}

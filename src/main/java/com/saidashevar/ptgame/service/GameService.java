package com.saidashevar.ptgame.service;

import static com.saidashevar.ptgame.config.response.ResponseTypes.BOARD;
import static com.saidashevar.ptgame.config.response.ResponseTypes.CARD_COUNT;
import static com.saidashevar.ptgame.config.response.ResponseTypes.STATUS;
import static com.saidashevar.ptgame.config.response.ResponseTypes.MESSAGE;
import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS;
import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
import static com.saidashevar.ptgame.model.GameStatus.FINISHED;
import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;

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
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
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
	
	public Game createGame(String login) throws NotFoundException {
		Player player = playerService.checkPlayerLogin(login);
		Game game = new Game(player); //Creates new game with NO2PLAYER status and this player added.
		playerService.takeDeckAndHand(player);
		player.setRed(); //This defines player's color randomly
		playerService.savePlayer(player);
		return saveGame(game);
	}
	
	public Game connectToRandomGame(String login) throws NotFoundException {
		Player player = playerService.checkPlayerLogin(login);
		playerService.takeDeckAndHand(player);
		player.setRed(); //This defines player's color randomly
		playerService.savePlayer(player);
		
		Game game = gameRepository.findAll().stream()
				.filter(g -> g.getStatus().equals(NO2PLAYER) || g.getStatus().equals(NO2PLAYER_1LEADER_CHOSEN)).findFirst()
				.orElseThrow(() -> new NotFoundException("No new games were found"));
		game.addPlayer(player);
		
		//We need to change status for frontend correctly
		if (game.getStatus().equals(NO2PLAYER))	game.setStatus(CHOOSING_LEADERS);
		else if (game.getStatus().equals(NO2PLAYER_1LEADER_CHOSEN)) game.setStatus(CHOOSING_LEADERS_1LEADER_CHOSEN);
		
		return saveGame(game);
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
	
	public UniResponse<Game> getGame(Game game) {
		return new UniResponse<Game>(STATUS, game);
	}
	
	public UniResponse<String> message(String message) {
		return new UniResponse<String>(MESSAGE, message);
	}
	
	public String readSet (Set<Card> hand) { //don't forget to remove this
		MyDebug myDebug = new MyDebug(); 
		hand.stream().forEach(c -> myDebug.string += c.getId() + " ");
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

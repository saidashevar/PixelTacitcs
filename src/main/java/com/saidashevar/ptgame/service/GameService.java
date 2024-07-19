package com.saidashevar.ptgame.service;

import static com.saidashevar.ptgame.config.response.ResponseTypes.ACTIONS_COUNT;
import static com.saidashevar.ptgame.config.response.ResponseTypes.BOARD;
import static com.saidashevar.ptgame.config.response.ResponseTypes.CARD_COUNT;
import static com.saidashevar.ptgame.config.response.ResponseTypes.MESSAGE;
import static com.saidashevar.ptgame.config.response.ResponseTypes.STATUS;
import static com.saidashevar.ptgame.config.response.ResponseTypes.TURNS;
import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS;
import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
import static com.saidashevar.ptgame.model.GameStatus.FINISHED;
import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.MyDebug;
import com.saidashevar.ptgame.config.response.UniResponse;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.Turn;
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
	
	//Only this service depends on other services
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
	
	//This method is probably too large...
	public Game connectToRandomGame(String login) throws NotFoundException {
		Game game = gameRepository.findAll().stream()
				.filter(g -> g.getStatus().equals(NO2PLAYER) || g.getStatus().equals(NO2PLAYER_1LEADER_CHOSEN)).findFirst()
				.orElseThrow(() -> new NotFoundException("No new games were found"));
		
		Player player1 = game.getPlayer();
		Player player2 = playerService.checkPlayerLogin(login);
		
		Random rd = new Random();
		boolean color = rd.nextBoolean();
		player1.setAsFirst(color);
		player2.setAsSecond(!color);
		
		playerService.takeDeckAndHand(player2);
		playerService.savePlayer(player2);
		
		game.addPlayer(player2);
		
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
	
	// Next methods are used in gameplay
	
	
	
	// Next methods prepare information about game for both players (board, card count)
	
	public UniResponse< Set<Hero> > getHeroes(Set<Player> players, String gameId) throws InvalidGameException {
		Iterator<Player> itr = players.iterator();
		Set<Hero> set = new HashSet<>();
		while (itr.hasNext()) {
			set.addAll(itr.next().getHeroes());
		}
		return new UniResponse< Set<Hero> >(BOARD, set);
	}
	
	public UniResponse< Map<String, Integer> > getCardCount(Set<Player> players, String gameId) throws InvalidGameException {
		Map<String, Integer> cardCount = new HashMap<>();
		players.stream()
			.forEach(p -> cardCount.put(p.getLogin(), p.getHand().size()));
		return new UniResponse< Map<String, Integer> >(CARD_COUNT, cardCount);
	}
	
	public UniResponse< Map<String, Byte> > getActionsCount(Set<Player> players, String gameId) throws InvalidGameException {
		Map<String, Byte> actionsCount = new HashMap<>();
		players.stream()
			.forEach(p -> actionsCount.put(p.getLogin(), p.getTurn().getActionsLeft()));
		return new UniResponse< Map<String, Byte> >(ACTIONS_COUNT, actionsCount);
	}
	
	public UniResponse< Map<String, Turn> > getTurns(Set<Player> players, String gameId) {
		Map<String, Turn> sideCardsPlace = new HashMap<>();
		players.stream()
			.forEach(p -> sideCardsPlace.put(p.getLogin(), p.getTurn()));
		return new UniResponse< Map<String, Turn> >(TURNS, sideCardsPlace);
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
	
	//Debug function
//	private String makeStringFromSet(Set<Hero> heroes) {
//		Iterator<Hero> itr = heroes.iterator();
//		String string = "";
//		while (itr.hasNext()) {
//			string += itr.next().getId() + " ";
//		}
//		return string;
//	}
}

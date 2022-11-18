package com.saidashevar.ptgame.service;

import static com.saidashevar.ptgame.model.GameStatus.*;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.exception.game.NoMoreCardInDeckException;
import com.saidashevar.ptgame.exception.game.TooManyCardsInHandException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.GamePlay;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.storage.GameStorage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {
	
	public Game createGame(String login) {
		Game game = new Game();
		game.getPlayers().get(0).setLogin(login);
		game.setStatus(NEW);
		game.setGameId(UUID.randomUUID().toString());
		game.getLoginsAndIndexes().put(login, 0); //player who creates game, becomes first player with index 0.
		GameStorage.getInstance().setGame(game);
		return game;
	}
	
	public Game connectToRandomGame(String login) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(NEW))
                .findFirst().orElseThrow(() -> new NotFoundException("Game not found"));
        game.getPlayers().get(1).setLogin(login);
        game.setStatus(IN_PROGRESS);
        game.getLoginsAndIndexes().put(login, 1); //player who connects to existing game, becomes second player with index 1.
        game.getPlayers().get(0).setActionsLeft((byte) 2);
        GameStorage.getInstance().setGame(game);
        return game;
    }
	
	public Game gamePlay(GamePlay gamePlay) throws InvalidGameException, NotFoundException {
		Game game = loadGameService(gamePlay.getGameId());
		//next line may be too hard to understand...
        String[][] board = game.getPlayers().get(game.getLoginsAndIndexes().get( gamePlay.getRequester()) ).getBoard();
        board[gamePlay.getCoordinateX()-1][gamePlay.getCoordinateY()-1] = "New Card!!!";
        GameStorage.getInstance().setGame(game);
        return game;
	}
	
	public Game loadGameService(String gameId) throws NotFoundException, InvalidGameException {
		if(gameId.contains("\"")) gameId = gameId.substring(1, gameId.length()-1);
		if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gameId);
        
        if (game.getStatus().equals(FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }
        return game;
	}
	
	//It's enough to send info about deck and hand here, but I think it's not critical to send all info about player.
	//Maybe later with Spring Security i will improve this code.
	public Player loadHandService(GamePlay gamePlay) throws NotFoundException, InvalidGameException,
															NoMoreActionsLeftException, NoMoreCardInDeckException, 
															TooManyCardsInHandException {
		Game game = loadGameService(gamePlay.getGameId());
		
		checkForActions(game, gamePlay.getRequester());
		checkCardsInDeck(game, gamePlay.getRequester());
		checkIfHandIsFull(game, gamePlay.getRequester());
		
		Player player = game.getPlayers().get(game.getLoginsAndIndexes().get(gamePlay.getRequester()));
		player.getHand().add(player.getDeck().get(0));
		player.getDeck().remove(0);
		GameStorage.getInstance().setGame(game);
		return player;
	}
	
	//Throwing errors when something simple happens may be a bad practice...
	//Should check it later.
	private void checkForActions(Game game, String requester) throws NoMoreActionsLeftException {
		if (game.getPlayers().get(game.getLoginsAndIndexes().get(requester)).getActionsLeft() <= 0)
			throw new NoMoreActionsLeftException(requester + "has no more actions now!");
	}
	
	private void checkCardsInDeck(Game game, String requester) throws NoMoreCardInDeckException {
		if (game.getPlayers().get(game.getLoginsAndIndexes().get(requester)).getDeck().isEmpty())
			throw new NoMoreCardInDeckException(requester + "had no more cards in his deck!");
	}
	
	private void checkIfHandIsFull(Game game, String requester) throws TooManyCardsInHandException {
		if (game.getPlayers().get(game.getLoginsAndIndexes().get(requester)).getHand().size() >= 5)
			throw new TooManyCardsInHandException(requester + "had no more cards in his deck!");
	}
}

package com.saidashevar.ptgame.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.GamePlay;
import com.saidashevar.ptgame.model.GameResponse;
import com.saidashevar.ptgame.model.Player;

import static com.saidashevar.ptgame.model.GameStatus.*;
import com.saidashevar.ptgame.storage.GameStorage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {
	
	public Game createGame(Player player) {
		Game game = new Game();
		game.setPlayer1(player);
		game.setStatus(NEW);
		game.setGameId(UUID.randomUUID().toString());
		GameStorage.getInstance().setGame(game);
		return game;
	}
	
	public Game connectToRandomGame(Player player2) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(NEW))
                .findFirst().orElseThrow(() -> new NotFoundException("Game not found"));
        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }
	
	public Game gamePlay(GamePlay gamePlay) throws InvalidGameException, NotFoundException {
		if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        
        if (game.getStatus().equals(FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }
        
        String[][] board;
        if(gamePlay.getRequester().getLogin().equals(game.getPlayer1().getLogin())) {
        	board = game.getBoardPlayer1();
        } else {
        	board = game.getBoardPlayer2();
        }
      
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getCardName();

        GameStorage.getInstance().setGame(game);
        return game;
	}
	
	public GameResponse prepareResponse(GamePlay gamePlay) throws NotFoundException {
		Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
		GameResponse gameResponse;
		if (gamePlay.getRequester().getLogin().equals(game.getPlayer1().getLogin())) {
			gameResponse = new GameResponse(game.getBoardPlayer2(), game.getBoardPlayer1());
		} else if (gamePlay.getRequester().getLogin().equals(game.getPlayer2().getLogin())) {
			gameResponse = new GameResponse(game.getBoardPlayer1(), game.getBoardPlayer2());
		} else throw new NotFoundException("Something went wrong with logins, sorry");
		return gameResponse;
	}
}

package com.saidashevar.ptgame.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.GamePlay;
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
		
        Game game = loadBoardService(gamePlay.getGameId());
        
        String[][] board;
        if(gamePlay.getRequester().equals(game.getPlayer1().getLogin())) {
        	if (gamePlay.getSquad() == 1) board = game.getBoardPlayer1();
        	else board = game.getBoardPlayer2();
        } else {
        	if (gamePlay.getSquad() == 1) board = game.getBoardPlayer2();
        	else board = game.getBoardPlayer1();
        }
        board[gamePlay.getCoordinateX()-1][gamePlay.getCoordinateY()-1] = "New Card!!!";

        GameStorage.getInstance().setGame(game);
        return game;
	}
	
	public Game loadBoardService(String gameId) throws NotFoundException, InvalidGameException {
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
}

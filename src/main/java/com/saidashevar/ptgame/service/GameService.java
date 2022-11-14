package com.saidashevar.ptgame.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
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
}

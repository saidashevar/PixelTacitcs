package com.saidashevar.ptgame.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.storage.GameStorage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {
	
	public Game createGame(Player player) {
		Game game = new Game();
		game.setPlayer1(player);
		game.setGameId(UUID.randomUUID().toString());
		GameStorage.getInstance().setGame(game);
		return game;
	}
}

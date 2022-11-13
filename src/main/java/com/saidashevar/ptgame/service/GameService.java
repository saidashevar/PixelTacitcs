package com.saidashevar.ptgame.service;

import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {
	
	public Game createGame(Player player) {
		Game game = new Game();
		game.setPlayer1(player);
		return game;
	}
}

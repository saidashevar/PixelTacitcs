package com.saidashevar.ptgame.model;

import lombok.Data;

@Data
public class Game {
	
	private GameStatus status;
	private String gameId;
	private Player player1;
	private Player player2;
	private String[][] boardPlayer1 = new String[3][3]; // For now, cards will be defined as strings.
	private String[][] boardPlayer2 = new String[3][3]; // Each player has his own squad
}

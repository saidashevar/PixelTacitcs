package com.saidashevar.ptgame.model;

import lombok.Data;

@Data
public class Game {
	
	private GameStatus status;
	private String gameId;
	private Player player1;
	private Player player2;
	private String[][] boardPlayer1 = {{"Killer","Battler","Destroyer"},{"Knight","Cursed Knight", "Battle mage"},{"Opperator","Sniper","Fighter"}};
	private String[][] boardPlayer2 = {{"Killer","Battler","Destroyer"},{"Knight","Cursed Knight", "Battle mage"},{"Opperator","Sniper","Fighter"}}; 
}
//For now, cards will be defined as strings.
//Each player has his own squad
//Maybe it will be better to store boards in Player class
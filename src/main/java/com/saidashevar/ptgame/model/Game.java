package com.saidashevar.ptgame.model;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Data;

@Data
public class Game {
	
	private GameStatus status;
	private String gameId;
	private Player player1;
	private Player player2;
	private ArrayList<Card> discardPilePlayer1 = new ArrayList<>(10);
	private ArrayList<Card> discardPilePlayer2 = new ArrayList<>(10);
	private ArrayList<Card> deckPlayer1 = new ArrayList<>(25);
	private ArrayList<Card> deckPlayer2 = new ArrayList<>(25);
	private ArrayList<Card> handPlayer1 = new ArrayList<>(6);
	private ArrayList<Card> handPlayer2 = new ArrayList<>(6);
	private String[][] boardPlayer1 = {{"","",""},{"","",""},{"","",""}};
	private String[][] boardPlayer2 = {{"","",""},{"","",""},{"","",""}};
	
	{
		deckPlayer1.add(new Card("Cursed Knight", 3, 6));
		deckPlayer1.add(new Card("Diabolist", 1, 3));
		deckPlayer1.add(new Card("Divinity", 2, 4));
		deckPlayer1.add(new Card("Druid", 2, 5));
		deckPlayer1.add(new Card("Chronicler", 1, 4));
		deckPlayer1.add(new Card("Immortal", 3, 2));
		deckPlayer1.add(new Card("Inventor", 1, 5));
		deckPlayer1.add(new Card("Lorekeeper", 2, 4));
		deckPlayer1.add(new Card("Magical Knight", 2, 4));
		deckPlayer1.add(new Card("Puppeter", 2, 4));
		deckPlayer1.add(new Card("Relic Hunter", 3, 7));
		deckPlayer1.add(new Card("Sage", 0, 4));
		deckPlayer1.add(new Card("Plague Bearer", 3, 4));
		deckPlayer1.add(new Card("Monster Hunter", 3, 5));
		deckPlayer1.add(new Card("Mastermind", 3, 6));
		deckPlayer1.add(new Card("Planebinder", 2, 5));
		deckPlayer1.add(new Card("Operative", 2, 5));
		deckPlayer1.add(new Card("Necromancer", 2, 6));
		deckPlayer1.add(new Card("Warmage", 4, 3));
		deckPlayer1.add(new Card("Tactitian", 0, 4));
		deckPlayer1.add(new Card("Supervillain", 0, 3));
		deckPlayer1.add(new Card("Warlock", 2, 3));
		deckPlayer1.add(new Card("Technologist", 3, 3));
		deckPlayer1.add(new Card("Sniper", 6, 3));
		deckPlayer1.add(new Card("Werewolf", 3, 5));
		Collections.shuffle(deckPlayer1);
		
		deckPlayer2.add(new Card("Cursed Knight", 3, 6));
		deckPlayer2.add(new Card("Diabolist", 1, 3));
		deckPlayer2.add(new Card("Divinity", 2, 4));
		deckPlayer2.add(new Card("Druid", 2, 5));
		deckPlayer2.add(new Card("Chronicler", 1, 4));
		deckPlayer2.add(new Card("Immortal", 3, 2));
		deckPlayer2.add(new Card("Inventor", 1, 5));
		deckPlayer2.add(new Card("Lorekeeper", 2, 4));
		deckPlayer2.add(new Card("Magical Knight", 2, 4));
		deckPlayer2.add(new Card("Puppeter", 2, 4));
		deckPlayer2.add(new Card("Relic Hunter", 3, 7));
		deckPlayer2.add(new Card("Sage", 0, 4));
		deckPlayer2.add(new Card("Plague Bearer", 3, 4));
		deckPlayer2.add(new Card("Monster Hunter", 3, 5));
		deckPlayer2.add(new Card("Mastermind", 3, 6));
		deckPlayer2.add(new Card("Planebinder", 2, 5));
		deckPlayer2.add(new Card("Operative", 2, 5));
		deckPlayer2.add(new Card("Necromancer", 2, 6));
		deckPlayer2.add(new Card("Warmage", 4, 3));
		deckPlayer2.add(new Card("Tactitian", 0, 4));
		deckPlayer2.add(new Card("Supervillain", 0, 3));
		deckPlayer2.add(new Card("Warlock", 2, 3));
		deckPlayer2.add(new Card("Technologist", 3, 3));
		deckPlayer2.add(new Card("Sniper", 6, 3));
		deckPlayer2.add(new Card("Werewolf", 3, 5));
		Collections.shuffle(deckPlayer2);
	}
}
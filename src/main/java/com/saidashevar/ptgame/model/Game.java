package com.saidashevar.ptgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import lombok.Data;

@Data
public class Game {
	
	private GameStatus status;
	private String gameId;
	private ArrayList<Player> players = new ArrayList<>(2);
	private HashMap<String, Integer> loginsAndIndexes = new HashMap<>(2);
	
	{
		players.add(new Player());
		ArrayList<Card> deck1 = players.get(0).getDeck();
		deck1.add(new Card("Cursed Knight", 3, 6));
		deck1.add(new Card("Diabolist", 1, 3));
		deck1.add(new Card("Divinity", 2, 4));
		deck1.add(new Card("Druid", 2, 5));
		deck1.add(new Card("Chronicler", 1, 4));
		deck1.add(new Card("Immortal", 3, 2));
		deck1.add(new Card("Inventor", 1, 5));
		deck1.add(new Card("Lorekeeper", 2, 4));
		deck1.add(new Card("Magical Knight", 2, 4));
		deck1.add(new Card("Puppeter", 2, 4));
		deck1.add(new Card("Relic Hunter", 3, 7));
		deck1.add(new Card("Sage", 0, 4));
		deck1.add(new Card("Plague Bearer", 3, 4));
		deck1.add(new Card("Monster Hunter", 3, 5));
		deck1.add(new Card("Mastermind", 3, 6));
		deck1.add(new Card("Planebinder", 2, 5));
		deck1.add(new Card("Operative", 2, 5));
		deck1.add(new Card("Necromancer", 2, 6));
		deck1.add(new Card("Warmage", 4, 3));
		deck1.add(new Card("Tactitian", 0, 4));
		deck1.add(new Card("Supervillain", 0, 3));
		deck1.add(new Card("Warlock", 2, 3));
		deck1.add(new Card("Technologist", 3, 3));
		deck1.add(new Card("Sniper", 6, 3));
		deck1.add(new Card("Werewolf", 3, 5));
		Collections.shuffle(deck1);
		
		players.add(new Player());
		ArrayList<Card> deck2 = players.get(1).getDeck();
		deck2.add(new Card("Cursed Knight", 3, 6));
		deck2.add(new Card("Diabolist", 1, 3));
		deck2.add(new Card("Divinity", 2, 4));
		deck2.add(new Card("Druid", 2, 5));
		deck2.add(new Card("Chronicler", 1, 4));
		deck2.add(new Card("Immortal", 3, 2));
		deck2.add(new Card("Inventor", 1, 5));
		deck2.add(new Card("Lorekeeper", 2, 4));
		deck2.add(new Card("Magical Knight", 2, 4));
		deck2.add(new Card("Puppeter", 2, 4));
		deck2.add(new Card("Relic Hunter", 3, 7));
		deck2.add(new Card("Sage", 0, 4));
		deck2.add(new Card("Plague Bearer", 3, 4));
		deck2.add(new Card("Monster Hunter", 3, 5));
		deck2.add(new Card("Mastermind", 3, 6));
		deck2.add(new Card("Planebinder", 2, 5));
		deck2.add(new Card("Operative", 2, 5));
		deck2.add(new Card("Necromancer", 2, 6));
		deck2.add(new Card("Warmage", 4, 3));
		deck2.add(new Card("Tactitian", 0, 4));
		deck2.add(new Card("Supervillain", 0, 3));
		deck2.add(new Card("Warlock", 2, 3));
		deck2.add(new Card("Technologist", 3, 3));
		deck2.add(new Card("Sniper", 6, 3));
		deck2.add(new Card("Werewolf", 3, 5));
		Collections.shuffle(deck2);
	}
}
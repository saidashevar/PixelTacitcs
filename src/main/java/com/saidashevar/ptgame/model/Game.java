package com.saidashevar.ptgame.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Game {
	
	private GameStatus status;
	private String gameId;
	private Player player1;
	private Player player2;
	private ArrayList<Card> discardPilePlayer1 = new ArrayList<>(10);
	private ArrayList<Card> discardPilePlayer2 = new ArrayList<>(10);
	private ArrayList<Card> deckPlayer1 = new ArrayList<>(21);
	private ArrayList<Card> deckPlayer2 = new ArrayList<>(21);
	private ArrayList<Card> handPlayer1 = new ArrayList<>(6);
	private ArrayList<Card> handPlayer2 = new ArrayList<>(6);
	private String[][] boardPlayer1 = {{"","",""},{"","",""},{"","",""}};
	private String[][] boardPlayer2 = {{"","",""},{"","",""},{"","",""}}; 
}
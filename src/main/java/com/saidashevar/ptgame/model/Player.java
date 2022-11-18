package com.saidashevar.ptgame.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Player {
	private String login;
	private byte actionsLeft;
	private ArrayList<Card> discardPile = new ArrayList<>(10);
	private ArrayList<Card> deck = new ArrayList<>(25);
	private ArrayList<Card> hand = new ArrayList<>(6);
	private String[][] board = {{"","",""},{"","",""},{"","",""}};
}

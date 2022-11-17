package com.saidashevar.ptgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
	
	private String name;
	private int attack;
	private int health;
}

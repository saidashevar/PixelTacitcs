package com.saidashevar.ptgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
	
	private String name;
	private int attack;
	private int maxHealth;
}

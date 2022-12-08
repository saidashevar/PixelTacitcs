package com.saidashevar.ptgame.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Card {
	
	@Id
	private String name;
	
	private int attack;
	private int maxHealth;
	
	public Card(String name, int attack, int maxHealth) {
		super();
		this.name = name;
		this.attack = attack;
		this.maxHealth = maxHealth;
	}

	public String getName() {
		return name;
	}
	
	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}

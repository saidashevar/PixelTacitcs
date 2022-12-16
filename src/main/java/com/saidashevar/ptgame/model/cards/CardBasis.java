package com.saidashevar.ptgame.model.cards;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CardBasis {
	
	@Id
	@GeneratedValue
	private int id;
	
	private int edition;
	private String name;
	private int attack;
	private int maxHealth;
	
	public CardBasis(int edition, String name, int attack, int maxHealth) {
		super();
		this.edition = edition;
		this.name = name;
		this.attack = attack;
		this.maxHealth = maxHealth;
	}
	
	public CardBasis() {}
	
	public int getId() {
		return id;
	}

	public int getEdition() {
		return edition;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAttack() {
		return attack;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
}

package com.saidashevar.ptgame.model.cards;

import com.saidashevar.ptgame.model.effects.EffectBasic;
import com.saidashevar.ptgame.model.effects.EffectSimple;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CardBasis {
	
	@Id
	@GeneratedValue
	private int id;
	
	private int edition;
	private String name;
	private int attack;
	private int maxHealth;
	
	//Functions
	public EffectSimple takeDamage(int attack) { return null; }
	
	public void saveEffect(EffectBasic effect) {} //useless???
	
	//Constructors
	public CardBasis(int edition, String name, int attack, int maxHealth) {
		super();
		this.edition = edition;
		this.name = name;
		this.attack = attack;
		this.maxHealth = maxHealth;
	}
	
	public CardBasis() {}
	
	//Getters and setters
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

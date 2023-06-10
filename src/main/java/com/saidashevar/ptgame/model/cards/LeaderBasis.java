package com.saidashevar.ptgame.model.cards;

import com.saidashevar.ptgame.model.effects.EffectSimple;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leaders")
public class LeaderBasis extends CardBasis {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Override
	public EffectSimple takeDamage(int attackValue) { return null; }
	
	//Constructors
	public LeaderBasis() {}
	
	public LeaderBasis(int edition, String name, int attack, int maxHealth) {
		super(edition, name, attack, maxHealth);
	}
}

package com.saidashevar.ptgame.model.effects;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.cards.Leader;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

@Entity
public class EffectSimple extends EffectBasic {
	
	private int value;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "effects", fetch = FetchType.LAZY)
	private Set<Hero> onHeroes = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "effects", fetch = FetchType.LAZY)
	private Set<Leader> onLeaders = new HashSet<>();
	
	//Constructors
	public EffectSimple() {
		super();
	}
	
	public EffectSimple(String name) {
		super(name);
	}
	
	//now there is one effect - ("damaged", x);
	public EffectSimple(String name, int value) {
		super(name);
		this.value = value;
	}
	
	//Getters and setters
	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public void setValue(int value) {
		this.value = value;
	}
}

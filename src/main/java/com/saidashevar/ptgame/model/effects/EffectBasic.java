package com.saidashevar.ptgame.model.effects;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.model.cards.Hero;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

	// This is parent of all effects. Effects of this class have no changing values, they are 

@Entity
@Table(name = "effects")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EffectBasic {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "effects", fetch = FetchType.LAZY)
	private Set<Hero> heroes = new HashSet<>();
	
	public EffectBasic () {}

	public EffectBasic(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

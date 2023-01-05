package com.saidashevar.ptgame.model.effects;

import jakarta.persistence.Entity;

@Entity
public class EffectSimple extends EffectBasic {
	
	private int value;
	
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

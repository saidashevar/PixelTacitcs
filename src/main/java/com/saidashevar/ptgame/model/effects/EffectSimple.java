package com.saidashevar.ptgame.model.effects;

public class EffectSimple extends EffectBasic {
	
	private int value;
	
	
	
	//Constructors
	public EffectSimple() {
		super();
	}
	
	public EffectSimple(String name) {
		super(name);
	}
	
	public EffectSimple(String name, int value) {
		super(name);
		this.value = value;
	}
	
	//Getters and setters
	public int getValue() {
		return value;
	}
}

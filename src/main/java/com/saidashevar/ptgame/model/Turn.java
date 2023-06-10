package com.saidashevar.ptgame.model;

import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "turns")
public class Turn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	private int wave = 0;
	private boolean Attacking = false; // Player who attacks, makes his move first
	private byte actionsLeft = 2;
	
	//Gameplay functions
	//Here second player is used to add him actions
	public void makeAction(Player player) throws NoMoreActionsLeftException {
		if (actionsLeft >= 1)
			actionsLeft--;
		else throw new NoMoreActionsLeftException("Player has no more actions!");
		if (actionsLeft == 0)
			player.addActions((byte)2);
	}
	
	public void addActions(byte x) {
		actionsLeft = (byte) (actionsLeft + x); //weird
	}
	
	//Getters and setters
	public int getWave() {
		return wave;
	}
	public void setWave(int wave) {
		this.wave = wave;
	}
	public boolean isAttacking() {
		return Attacking;
	}
	public void setAttacking(boolean attacking) {
		Attacking = attacking;
	}
	public byte getActionsLeft() {
		return actionsLeft;
	}
	public void setActionsLeft(int actionsLeft) {
		this.actionsLeft = (byte)actionsLeft;
	}
}

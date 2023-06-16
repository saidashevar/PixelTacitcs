package com.saidashevar.ptgame.model;

import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.repository.EffectRepository;

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
	public void makeAction(Game game, Player player, EffectRepository effectRepository) throws NoMoreActionsLeftException {
		if (actionsLeft >= 1) actionsLeft--;
		else throw new NoMoreActionsLeftException("Player has no more actions!");
		
		checkRoundOrWaveEnd(game, player, effectRepository);
	}
	
	private void checkRoundOrWaveEnd(Game game, Player player, EffectRepository effectRepository) { //Describes changes in players' turns, when round ends
		if (actionsLeft == 0) { //always see not to do (if in if)... but i have done
			if (Attacking) {
				player.addActions((byte)2); // just adding another player two actions
			} else {
				game.nextWave(effectRepository);
				if (wave == 2) {
					Attacking = true;
					wave = 0;
					actionsLeft += 2;
					player.endRound();
				} else {
					wave++;
					player.endWave();
				}
			}
		}
	}
	
	public void endWave() {
		addActions((byte)2); //in future there will be situations when there must be added 3 or more actions... but in the future!
		wave++;
	}
	public void endRound() {
		addActions((byte)2);
		wave = 0;
		Attacking = false;
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

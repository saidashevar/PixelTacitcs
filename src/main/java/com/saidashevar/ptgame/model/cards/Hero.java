package com.saidashevar.ptgame.model.cards;

import java.util.HashSet;
import java.util.Set;

import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.effects.EffectBasic;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players_heroes")
public class Hero extends CardBasis {
	
	@Column(name = "coord_x", nullable = true) //Caution! Here is matrix system is used. x means row, y - column.
	private int coordX;
	
	@Column(name = "coord_y", nullable = true)
	private int coordY;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "heroes_effects",
			joinColumns = @JoinColumn(name = "hero_id"),
			inverseJoinColumns = @JoinColumn(name = "effect_id"))
	private Set<EffectBasic> effects = new HashSet<>();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "player_login", referencedColumnName = "login")
	private Player player;
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	
	//Next are constructors. Almost all of them are used somewhere. 
	public Hero() {}
	
	public Hero(int edition, String name, int attack, int maxHealth) {
		super(edition, name, attack, maxHealth);
	}
	
	public Hero(Card card) {
		super(
			card.getEdition(),
			card.getName(),
			card.getAttack(),
			card.getMaxHealth());
	}
	
	public Hero(int coordX, int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public Hero (Card card, int coordX, int coordY) {
		this(card);
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public Hero(Card card ,int coordX, int coordY, Player player) {
		this(card, coordX, coordY);
		this.player = player;
	}
	
	public Hero(int coordX, int coordY, Player player) {
		this(coordX, coordY);
		this.player = player;
	}
	
	public Hero(Card card, Hero heroWithoutCard) {
		this(card);
		this.coordX = heroWithoutCard.getCoordX();
		this.coordY = heroWithoutCard.getCoordY();
		this.player = heroWithoutCard.getPlayer();
	}
}
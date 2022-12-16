package com.saidashevar.ptgame.model.cards;

import com.saidashevar.ptgame.model.Player;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players_heroes")
public class Hero extends CardBasis {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
	
	@Column(name = "coord_x", nullable = true) //Caution! Here is matrix system is used. x means row, y - column.
	private int coordX;
	
	@Column(name = "coord_y", nullable = true)
	private int coordY;

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "card", referencedColumnName = "id", nullable = true)
//	private Card card;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "player_login", referencedColumnName = "login")
	private Player player; 
	
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
	
	public Hero(Card card ,int coordX, int coordY, Player player) {
		this(card);
		this.coordX = coordX;
		this.coordY = coordY;
		this.player = player;
	}
}
package com.saidashevar.ptgame.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players_heroes")
public class Hero {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "coord_x", nullable = true) //Caution! Here is matrix system is used. x means row, y - column.
	private int coordX;
	
	@Column(name = "coord_y", nullable = true)
	private int coordY;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "card", referencedColumnName = "id", nullable = true)
	private Card card;
	
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
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	
	public Hero() {}
	
	public Hero(int x, int y) {
		this.coordX = x;
		this.coordY = y;
	}
	
	public Hero(int coordX, int coordY, Card card, Player player) {
		super();
		this.coordX = coordX;
		this.coordY = coordY;
		this.card = card;
		this.player = player;
	}
}
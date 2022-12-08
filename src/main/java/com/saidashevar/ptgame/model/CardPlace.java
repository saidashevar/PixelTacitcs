package com.saidashevar.ptgame.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "card_places")
public class CardPlace {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int coordX;
	private int coordY;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card", referencedColumnName = "id", nullable = true)
	private Card card;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "player_login", referencedColumnName = "login")
	private Player player; 
	
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
	
	public CardPlace() {}
	
	public CardPlace(int x, int y) {
		this.coordX = x;
		this.coordY = y;
	}
}

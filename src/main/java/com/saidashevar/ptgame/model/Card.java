package com.saidashevar.ptgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int edition;
	private String name;
	private int attack;
	private int maxHealth;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "players_decks",
			joinColumns = @JoinColumn(name = "card_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private List<Player> inDecks = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "players_hands",
			joinColumns = @JoinColumn(name = "card_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private List<Player> inHands = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "players_piles",
			joinColumns = @JoinColumn(name = "card_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private List<Player> inPiles = new ArrayList<>();
	
	public void takenBy(Player player) {
		inDecks.remove(player);
		inHands.add(player);
	}

	public Card(int edition, String name, int attack, int maxHealth) {
		super();
		this.edition = edition;
		this.name = name;
		this.attack = attack;
		this.maxHealth = maxHealth;
	}
	
	public Card() {}
	
	public Long getId() {
		return id;
	}
	
	public int getEdition() {
		return edition;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAttack() {
		return attack;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void addInDeck(Player player) {
		inDecks.add(player);
	}
	
	public void addInHand(Player player) {
		inHands.add(player);
	}
	
	public void addInPile(Player player) {
		inPiles.add(player);
	}
	
	public List<Player> getInDecks() {
		return inDecks;
	}
	
	public List<Player> getInHands() {
		return inHands;
	}
	
	public List<Player> getInPiles() {
		return inPiles;
	}
}

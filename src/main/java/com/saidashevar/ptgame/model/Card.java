package com.saidashevar.ptgame.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@ManyToMany
	@JoinTable(
			name = "players_decks",
			joinColumns = @JoinColumn(name = "card_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private Set<Player> inDecks = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
			name = "players_hands",
			joinColumns = @JoinColumn(name = "card_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private Set<Player> inHands = new HashSet<>();
	
	@ManyToMany
	@JoinTable(
			name = "players_piles",
			joinColumns = @JoinColumn(name = "card_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private Set<Player> inPiles = new HashSet<>();

	public Card(int edition, String name, int attack, int maxHealth) {
		super();
		this.edition = edition;
		this.name = name;
		this.attack = attack;
		this.maxHealth = maxHealth;
	}
	
	public Card() {}
	
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
	
	public Set<Player> getInDecks() {
		return inDecks;
	}
	
	public Set<Player> getInHands() {
		return inHands;
	}
	
	public Set<Player> getInPiles() {
		return inPiles;
	}
}

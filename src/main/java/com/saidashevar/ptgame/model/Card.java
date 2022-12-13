package com.saidashevar.ptgame.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@ManyToMany(mappedBy = "deck", fetch = FetchType.LAZY)
	private Set<Player> inDecks = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "hand", fetch = FetchType.LAZY)
	private Set<Player> inHands = new LinkedHashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "pile", fetch = FetchType.LAZY)
	private Set<Player> inPiles = new LinkedHashSet<>();
	
	public void hiredBy(Player player) {
		inHands.remove(player);
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

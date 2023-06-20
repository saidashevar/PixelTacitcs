package com.saidashevar.ptgame.model.cards;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.model.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cards")
public class Card extends CardBasis implements Comparable<Card>{
	
	@JsonIgnore
	@ManyToMany(mappedBy = "deck", fetch = FetchType.LAZY)
	private Set<Player> inDecks = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "hand", fetch = FetchType.LAZY)
	private Set<Player> inHands = new TreeSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "pile", fetch = FetchType.LAZY)
	private Set<Player> inPiles = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
	private Set<Hero> asHeroes = new HashSet<>();
	
	public void addInDeck(Player player) {
		inDecks.add(player);
	}
	
	public void addInHand(Player player) {
		inHands.add(player);
	}
	
	public Card addInPile(Player player) {
		inPiles.add(player);
		return this;
	}
	
	public Card addAsHero(Hero hero) {
		asHeroes.add(hero);
		return this;
	}
	
	public Card killHero(Hero hero) {
		asHeroes.remove(hero);
		return this;
	}

	public Card(int edition, String name, int attack, int maxHealth) {
		super(edition, name, attack, maxHealth);
	}
	
	public Card() {}
	
	public int getId() {
		return super.getId();
	}
	
	public int getEdition() {
		return super.getEdition();
	}
	
	public String getName() {
		return super.getName();
	}
	
	public int getAttack() {
		return super.getAttack();
	}

	public int getMaxHealth() {
		return super.getMaxHealth();
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

	@Override 
	public int compareTo(Card card) {
		return card.getId() - super.getId(); //oh my god
	}
}

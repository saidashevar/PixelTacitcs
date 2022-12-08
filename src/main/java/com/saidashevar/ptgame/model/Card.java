package com.saidashevar.ptgame.model;

import java.util.HashSet;
import java.util.Set;

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
			name = "players_cards",
			joinColumns = @JoinColumn(name = "card_name"),
			inverseJoinColumns = @JoinColumn(name = "player_login")
			)
	private Set<Player> playedByPlayers = new HashSet<>();
	
	public void connectWithPlayer(Player player) {
		playedByPlayers.add(player);
	}

	public Card(String name, int attack, int maxHealth) {
		super();
		this.name = name;
		this.attack = attack;
		this.maxHealth = maxHealth;
	}
	
	public Card() {}
	
	public Set<Player> getPlayedByPlayers() {
		return playedByPlayers;
	}

	public String getName() {
		return name;
	}
	
	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	
	public int getEdition() {
		return edition;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

}

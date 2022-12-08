package com.saidashevar.ptgame.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class Player {
	
	@Id
	private String login;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "turn", referencedColumnName = "id")
	private Turn turn = new Turn();
	
	@ManyToMany
	@JoinTable(
			name = "games_players",
			joinColumns = @JoinColumn(name = "player_login"),
			inverseJoinColumns = @JoinColumn(name = "game_id")
			)
	private Set<Game> playedGames = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<CardPlace> board = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "inDecks")
	private Set<Card> deck = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "inHands")
	private Set<Card> hand = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "inPiles")
	private Set<Card> pile = new HashSet<>();
	
	public String getLogin() {
		return login;
	}
	
	public Set<Card> getDeck() {
		return deck;
	}
	
	public Set<Card> getHand() {
		return hand;
	}
	
	public Set<Card> getPile() {
		return pile;
	}
	
	public Player(String login) {
		super();
		this.login = login;
	}
	
	public Player() {}
	
	{
		board.add(new CardPlace(0, 0));
		board.add(new CardPlace(0, 1));
		board.add(new CardPlace(0, 2));
		
		board.add(new CardPlace(1, 0));
		board.add(new CardPlace(1, 1));
		board.add(new CardPlace(1, 2));
		
		board.add(new CardPlace(2, 0));
		board.add(new CardPlace(2, 1));
		board.add(new CardPlace(2, 2));
	}
//	private Card[][] board = new Card[3][3];
	
	//for some time, while there is no database, cards are added here
//	{
//		deck.add(new Card("Cursed_Knight", 3, 6));
//		deck.add(new Card("Diabolist", 1, 3));
//		deck.add(new Card("Divinity", 2, 4));
//		deck.add(new Card("Druid", 2, 5));
//		deck.add(new Card("Chronicler", 1, 4));
//		deck.add(new Card("Immortal", 3, 2));
//		deck.add(new Card("Inventor", 1, 5));
//		deck.add(new Card("Lorekeeper", 2, 4));
//		deck.add(new Card("Magical_Knight", 2, 4));
//		deck.add(new Card("Puppeter", 2, 4));
//		deck.add(new Card("Relic_Hunter", 3, 7));
//		deck.add(new Card("Sage", 0, 4));
//		deck.add(new Card("Plague_Bearer", 3, 4));
//		deck.add(new Card("Monster_Hunter", 3, 5));
//		deck.add(new Card("Mastermind", 3, 6));
//		deck.add(new Card("Planebinder", 2, 5));
//		deck.add(new Card("Operative", 2, 5));
//		deck.add(new Card("Necromancer", 2, 6));
//		deck.add(new Card("Warmage", 4, 3));
//		deck.add(new Card("Tactitian", 0, 4));
//		deck.add(new Card("Supervillain", 0, 3));
//		deck.add(new Card("Warlock", 2, 3));
//		deck.add(new Card("Technologist", 3, 3));
//		deck.add(new Card("Sniper", 6, 3));
//		deck.add(new Card("Werewolf", 3, 5));
//	}
}

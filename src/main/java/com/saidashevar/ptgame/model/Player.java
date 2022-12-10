package com.saidashevar.ptgame.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.exception.NotFoundException;

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
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "players_games",
			joinColumns = @JoinColumn(name = "player_login"),
			inverseJoinColumns = @JoinColumn(name = "game_id")
			)
	private Set<Game> playedGames = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private Set<Hero> board = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "inDecks")
	private Set<Card> deck = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "inHands")
	private Set<Card> hand = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "inPiles")
	private Set<Card> pile = new HashSet<>();
	
	//gameplay functions
	public void takeCard() throws NotFoundException {
		Card taken = deck.stream().findAny().orElseThrow(() -> new NotFoundException("There are no more cards in "+login+"'s deck!")); 
		deck.remove(taken);
		hand.add(taken);
	}
	
	//game management funcitons
	public void addGame(Game game) {
		playedGames.add(game);
	}
	
	//Getters and setters
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
	
	public Set<Hero> getBoard() {
		return board;
	}

	public void setBoard(Set<Hero> board) {
		this.board = board;
	}

	public Turn getTurn() {
		return turn;
	}

	public Set<Game> getPlayedGames() {
		return playedGames;
	}

	//Constructors
	public Player(String login) {
		super();
		this.login = login;
	}
	
	public Player() {}
	
	//for some time, while there is no database, cards are added here
//	{
//		deck.add(new Card(2, "Cursed_Knight", 3, 6));
//		deck.add(new Card(2, "Diabolist", 1, 3));
//		deck.add(new Card(2, "Divinity", 2, 4));
//		deck.add(new Card(2, "Druid", 2, 5));
//		deck.add(new Card(2, "Chronicler", 1, 4));
//		deck.add(new Card(2, "Immortal", 3, 2));
//		deck.add(new Card(2, "Inventor", 1, 5));
//		deck.add(new Card(2, "Lorekeeper", 2, 4));
//		deck.add(new Card(2, "Magical_Knight", 2, 4));
//		deck.add(new Card(2, "Puppeter", 2, 4));
//		deck.add(new Card(2, "Relic_Hunter", 3, 7));
//		deck.add(new Card(2, "Sage", 0, 4));
//		deck.add(new Card(2, "Plague_Bearer", 3, 4));
//		deck.add(new Card(2, "Monster_Hunter", 3, 5));
//		deck.add(new Card(2, "Mastermind", 3, 6));
//		deck.add(new Card(2, "Planebinder", 2, 5));
//		deck.add(new Card(2, "Operative", 2, 5));
//		deck.add(new Card(2, "Necromancer", 2, 6));
//		deck.add(new Card(2, "Warmage", 4, 3));
//		deck.add(new Card(2, "Tactitian", 0, 4));
//		deck.add(new Card(2, "Supervillain", 0, 3));
//		deck.add(new Card(2, "Warlock", 2, 3));
//		deck.add(new Card(2, "Technologist", 3, 3));
//		deck.add(new Card(2, "Sniper", 6, 3));
//		deck.add(new Card(2, "Werewolf", 3, 5));
//	}
}

package com.saidashevar.ptgame.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.cards.Leader;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	
	private Boolean isRed; //this Boolean just shows cards' shirts color. Red or blue.
	
	@JsonIgnore //It must be hidden before 2-nd round
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "leader", referencedColumnName = "id")
	private Leader leader;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "turn", referencedColumnName = "id")
	private Turn turn = new Turn();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "players", fetch = FetchType.LAZY)
	private Set<Game> playedGames = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "players_decks",
			joinColumns = @JoinColumn(name = "player_login"),
			inverseJoinColumns = @JoinColumn(name = "card_id"))
	private Set<Card> deck = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "players_hands",
			joinColumns = @JoinColumn(name = "player_login"),
			inverseJoinColumns = @JoinColumn(name = "card_id"))
	private Set<Card> hand = new TreeSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
	private Set<Hero> board = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "players_piles",
			joinColumns = @JoinColumn(name = "player_login"),
			inverseJoinColumns = @JoinColumn(name = "card_id"))
	private Set<Card> pile = new LinkedHashSet<>();
	
	//gameplay functions
	public void makeAction() throws NoMoreActionsLeftException {
		turn.makeAction();
	}
	
	public void takeCard(Card card) {
		deck.remove(card);
		hand.add(card);
	}
	
	public void removeCardFromHand(Card card) {
		hand.remove(card);
	}
	
	public Card findCardToTake() throws NotFoundException {
//		Random rand = new Random();
		return deck.stream().findAny().orElseThrow(() -> new NotFoundException("There are no more cards in "+login+"'s deck!")); //old way without random
//		int size = rand.nextInt(deck.size());
//		if (size == 0) throw new NotFoundException("There are no more cards in "+login+"'s deck!");
//		else return deck.get(rand.nextInt(deck.size()));
	}
	
	public void removeLeaderCardFromHand(int id) throws NotFoundException { //Just removes card wtih leader from hand
		Card futureLeader = hand.stream().filter(card -> card.getId() == id).findAny()
			.orElseThrow(() -> new NotFoundException(this.login + " tries to make leader somebody else"));
		hand.remove(futureLeader);
	}
	
	public void setColor(Player player) {
		setRed(!player.isRed());
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

	public void setDeck(Set<Card> deck) {
		this.deck = deck;
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

	public Leader getLeader() {
		return leader;
	}

	public void setLeader(Leader leader) {
		this.leader = leader;
	}

	public boolean isRed() {
		return isRed;
	}

	public void setRed() {
		Random rd = new Random();
		isRed = rd.nextBoolean();
	}
	
	public void setRed(boolean isRed) {
		this.isRed = isRed;
	}

	//Constructors
	public Player() {}
	
	public Player(String login) {
		super();
		this.login = login;
	}
	
	public Player(String login, boolean isRed) {
		this(login);
		this.isRed = isRed;
	}
}

package com.saidashevar.ptgame.model;

import static com.saidashevar.ptgame.model.GameStatus.NEW;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.exception.NotFoundException;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {
	
	@Id
	private String id;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "games_players",
			joinColumns = @JoinColumn(name = "game_id"),
			inverseJoinColumns = @JoinColumn(name = "player_login"))
	private Set<Player> players = new HashSet<>();
	
	private int wave = 0;
	private GameStatus status = NEW;
	
	public Player[] findPlayers(String login) throws NotFoundException { // Returns player with requested login first. In very strange way without playerRepository. mmay be very bad
		Player player1 = players.stream().filter(p -> p.getLogin().equals(login)).findAny()
				.orElseThrow(() -> new NotFoundException("Player with login " + login + " is not playing that game"));
		Player player2 = players.stream().filter(p -> !p.getLogin().equals(login)).findAny().orElse(null);
//				.orElseThrow(() -> new NotFoundException("Opponent of " + login + " is out of universe"));
		Player[] players = {player1, player2}; //new Player[2];
		return players;
	}
	
//	public Player findOtherPlayerByLogin(String login) throws NotFoundException {
//		return players.stream().filter(p -> !p.getLogin().equals(login)).findAny()
//				.orElseThrow(() -> new NotFoundException("Opponent of " + login + "out of universe"));
//	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	public Set<Player> getPlayers() {
		return players;
	}

	public Game() { //Empty constructor is not empty
		this.id = UUID.randomUUID().toString();
	}
}
package com.saidashevar.ptgame.model;

import static com.saidashevar.ptgame.model.GameStatus.*;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidashevar.ptgame.exception.NotFoundException;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {
	
	@Id
	private String id;
	
	@ManyToMany(mappedBy = "playedGames")
	private Set<Player> players = new HashSet<>();
	
	private int wave = 0;
	private GameStatus status = NEW;
	
	public Player[] findPlayers(String login) throws NotFoundException { // Returns player with requested login first
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

	public Game() {}
}
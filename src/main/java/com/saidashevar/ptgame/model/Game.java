package com.saidashevar.ptgame.model;

import static com.saidashevar.ptgame.model.GameStatus.*;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
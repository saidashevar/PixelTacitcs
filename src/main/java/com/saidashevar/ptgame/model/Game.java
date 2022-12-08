package com.saidashevar.ptgame.model;

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
	
	//private GameStatus status;
	@Id
	private String id;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "playedGames")
	private Set<Player> players = new HashSet<>();
	
	//private HashMap<String, Player> players = new HashMap<>(2);
	//private String[] logins = new String[2]; //I need to have access to logins somehow. Need better collections understanding.
	private int wave = 0;
	//There must be better solution than keeping hashmap with logins and array with them, but i don't see it now
	
	public String getId() {
		return id;
	}
	
	public Game() {}
}
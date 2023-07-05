package com.saidashevar.ptgame.model;

import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;
import static com.saidashevar.ptgame.model.GameStatus.PEACE;
import static com.saidashevar.ptgame.model.GameStatus.MID_GAME;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.effects.EffectSimple;
import com.saidashevar.ptgame.repository.EffectRepository;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	private GameStatus status;
	
		//Functions
	public Player[] findPlayers(String login) throws NotFoundException { // Returns player with requested login first. In very strange way without playerRepository. may be very bad
		Player player1 = players.stream().filter(p -> p.getLogin().equals(login)).findAny()
				.orElseThrow(() -> new NotFoundException("Player with login " + login + " is not playing that game"));
		Player player2 = players.stream().filter(p -> !p.getLogin().equals(login)).findAny().orElse(null);
//				.orElseThrow(() -> new NotFoundException("Opponent of " + login + " is out of universe"));
		Player[] players = {player1, player2}; //new Player[2];
		return players;
	}
	
	public boolean checkLeaders() {
		if (this.getPlayers().size() == 2) {
			Iterator<Player> itr = this.getPlayers().iterator();
			while (itr.hasNext() ) if(itr.next().getLeader() == null) return false; //idk how to unite these 
		} else return false;
		return true;
	}
	
	public void nextLeaderStatus() {
		switch (this.getStatus()) {
		case NO2PLAYER:
			this.setStatus(NO2PLAYER_1LEADER_CHOSEN);
			break;
		case CHOOSING_LEADERS:
			this.setStatus(CHOOSING_LEADERS_1LEADER_CHOSEN);
			break;
		case CHOOSING_LEADERS_1LEADER_CHOSEN:
			this.setStatus(PEACE);
			break;
		case PEACE:
			this.setStatus(MID_GAME);
			break;
		case MID_GAME: //here is nothing...
			break;
		default:
			log.info("something went wrong with setting game status");
			break;
		}
	}
	
	public Player getPlayer() throws NotFoundException {
		return players.stream().findAny()
				.orElseThrow(() -> new NotFoundException("no players in that game"));
	}
	
//	public Player findOtherPlayerByLogin(String login) throws NotFoundException {
//		return players.stream().filter(p -> !p.getLogin().equals(login)).findAny()
//				.orElseThrow(() -> new NotFoundException("Opponent of " + login + "out of universe"));
//	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public Game nextWave(EffectRepository effectRepository) { //check losses is here!
		//Next paragraph check losses
		Iterator<Player> itr = this.getPlayers().iterator();
		while (itr.hasNext()) {
			Iterator<Hero> itr2 = itr.next().getBoard().iterator();
			while (itr2.hasNext()) {
				var hero = itr2.next();
				if (hero.getEffectValue("damaged") >= hero.getMaxHealth())
					hero.saveEffect(effectRepository.save(new EffectSimple("defeated", 1)));
			}
		}
		//switching to midgame... works!
		nextLeaderStatus();
		//Next one changes waves
		if (wave == 2) wave = 0;
		else wave++;
		return this;
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

	public Game() { //Empty constructor is not empty, wow
		super();
		id = UUID.randomUUID().toString();
		status = NO2PLAYER;
	}
	
	public Game(Player player) {
		this();
		players.add(player);
	}
}
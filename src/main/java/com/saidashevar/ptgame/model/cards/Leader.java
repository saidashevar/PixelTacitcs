package com.saidashevar.ptgame.model.cards;

import com.saidashevar.ptgame.model.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "players_leaders")
public class Leader extends LeaderBasis { // Leader is also hero, maybe this class is useless.
	
	//private Set<Effect> effects = new HashSet<>();
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private int id;
	
	public Leader() {}
	
	public Leader(LeaderBasis leader, Player player) { //Maybe constructor with card would work too
		super(
			leader.getEdition(),
			leader.getName(),
			leader.getAttack(),
			leader.getMaxHealth());
	}
}

package com.saidashevar.ptgame.model.cards;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.effects.EffectBasic;
import com.saidashevar.ptgame.model.effects.EffectSimple;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "players_leaders")
public class Leader extends LeaderBasis { // Leader is also hero, maybe this class is useless.
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "leaders_effects",
			joinColumns = @JoinColumn(name = "leader_id"),
			inverseJoinColumns = @JoinColumn(name = "effect_id"))
	private Set<EffectBasic> effects = new HashSet<>();
	
	@Override
	public EffectSimple takeDamage(int attackValue) {
		Iterator<EffectBasic> itr = effects.iterator();
		while ( itr.hasNext() ) {
			EffectBasic effect = itr.next(); 
			if(effect.getName().equals("damaged")) {
				effect.setValue(effect.getValue() + attackValue);
				return (EffectSimple)effect;
			}
		} 
		var damageEffect = new EffectSimple("damaged", attackValue);
		effects.add(damageEffect);
		return damageEffect;
	}
	
	public void saveEffect(EffectBasic effect) {
		effects.add(effect);
	}
	
	//Constructors
	public Leader() {}
	
	public Leader(LeaderBasis leader, Player player) { //Maybe constructor with card would work too
		super(
			leader.getEdition(),
			leader.getName(),
			leader.getAttack(),
			leader.getMaxHealth());
	}
}

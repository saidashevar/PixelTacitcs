package com.saidashevar.ptgame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Hero;

public interface HeroRepository extends JpaRepository<Hero, Long> {
	
	@Query("SELECT h FROM Hero h where h.player = ?1")
	List<Hero> findHeroesOfPlayer(Player player);
	
	//This was taken from the internet, but it works and must be fast. Text blocks would be useful...
	//This query returns true if some hero of player on coordinates (x,y) were found
	@Query(value = "SELECT CASE WHEN EXISTS " + 
				   "(SELECT 1 FROM players_heroes WHERE player_login = :login AND coord_x = :x AND coord_y = :y)" + 
				   "THEN 'true' ELSE 'false' END",
				   nativeQuery = true)
	boolean heroOnPlace(@Param("login") String login,
						@Param("x") int x,
						@Param("y") int y);
	
}
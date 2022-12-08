package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.PlayerRepository;

@RestController
@RequestMapping("/players")
public class PlayerController {

	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping
	List<Player> getPlayers() { return playerRepository.findAll(); }
	
	@PostMapping
	Player createPlayer(@RequestBody Player player) { return playerRepository.save(player); }
}

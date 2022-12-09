package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/players")
public class PlayerController {

	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping
	List<Player> getPlayers() { return playerRepository.findAll(); }
	
	@PostMapping
	Player createPlayer(@RequestBody Player player) {
		return playerRepository.save(player); }
	
	//Method returns player object, who will play game. It checks player with current login, returns it, if it exists, or creates new one. 
	@PostMapping("/checkLogin")
	ResponseEntity<Player> checkLogin(@RequestBody StringRequest request) {
		String login = request.getString();
		log.info(login + " checks his existance in database");
		try {
			Player player = playerRepository.findById(login)
					.orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found"));
			log.info("Player with login: " + login + " was found");
			return ResponseEntity.ok(player);
//			return ResponseEntity.ok(playerRepository.findAll().stream().filter(p -> p.getLogin().equals(login))
//					.findAny().orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found")));
		} catch (NotFoundException e) {
			log.info("There is no player with login: " + login + ". Creating new one");
			return ResponseEntity.ok(playerRepository.save(new Player(login)));
		}
//		findAll().stream().filter(p -> p.getLogin().equals(login))
	}
}

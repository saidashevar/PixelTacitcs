package com.saidashevar.ptgame.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/game")
@AllArgsConstructor
@Slf4j
public class GameController {
	
	private final GameService gameService;
	
	@PostMapping("/start")
	public ResponseEntity<Game> startGame(@RequestBody Player player) {
		log.info("start game request: {}", player);
		return ResponseEntity.ok(gameService.createGame(player));
	}

}

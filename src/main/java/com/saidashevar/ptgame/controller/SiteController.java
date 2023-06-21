package com.saidashevar.ptgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/game")
@AllArgsConstructor
public class SiteController {
	
	@GetMapping
	public String game(@RequestParam("id") String gameId) { 
		return "templates/Game.html";
	}
}

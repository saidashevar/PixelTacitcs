package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.model.Hero;
import com.saidashevar.ptgame.repository.HeroRepository;

@RestController
@RequestMapping("/heroes")
public class HeroController {
	
	@Autowired
	HeroRepository heroRepository;
	
	@GetMapping
	List<Hero> getHeroes() { return heroRepository.findAll(); }
	
	@PostMapping
	Hero createHero(@RequestBody Hero hero) {
		return heroRepository.save(hero); }
}

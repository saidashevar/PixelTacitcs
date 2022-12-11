package com.saidashevar.ptgame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.repository.HeroRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HeroService {
	
	@Autowired
	private final HeroRepository heroRepository;
	
	
}

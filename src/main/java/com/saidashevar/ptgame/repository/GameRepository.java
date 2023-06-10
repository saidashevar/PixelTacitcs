package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saidashevar.ptgame.model.Game;

public interface GameRepository extends JpaRepository<Game, String>{}
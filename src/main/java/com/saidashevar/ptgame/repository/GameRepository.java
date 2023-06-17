package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.saidashevar.ptgame.model.Game;

@Transactional(readOnly = true)
public interface GameRepository extends JpaRepository<Game, String>{}
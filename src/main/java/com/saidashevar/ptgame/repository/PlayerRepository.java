package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saidashevar.ptgame.model.Player;

public interface PlayerRepository extends JpaRepository<Player, String>{}
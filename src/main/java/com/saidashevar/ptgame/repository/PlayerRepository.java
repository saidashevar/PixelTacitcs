package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.saidashevar.ptgame.model.Player;

@Transactional(readOnly = true)
public interface PlayerRepository extends JpaRepository<Player, String>{}
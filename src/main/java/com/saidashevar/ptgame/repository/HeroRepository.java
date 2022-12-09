package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saidashevar.ptgame.model.Hero;

public interface HeroRepository extends JpaRepository<Hero, Long> {}
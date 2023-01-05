package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saidashevar.ptgame.model.effects.EffectBasic;

public interface EffectRepository extends JpaRepository<EffectBasic, Integer> {}
package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.saidashevar.ptgame.model.effects.EffectBasic;

@Transactional(readOnly = true)
public interface EffectRepository extends JpaRepository<EffectBasic, Integer> {}
package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.saidashevar.ptgame.model.cards.Leader;

@Transactional(readOnly = true)
public interface LeaderRepository extends JpaRepository<Leader, Long> {}
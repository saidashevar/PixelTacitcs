package com.saidashevar.ptgame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saidashevar.ptgame.model.cards.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {}
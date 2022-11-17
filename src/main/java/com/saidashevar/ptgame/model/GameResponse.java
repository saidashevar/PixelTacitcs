package com.saidashevar.ptgame.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameResponse {
	private String[][] upperBoard;
	private String[][] lowerBoard;
}

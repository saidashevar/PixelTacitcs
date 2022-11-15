package com.saidashevar.ptgame.model;

import lombok.Data;

@Data
public class GamePlay {
	
	private Player requester;
	private String cardName;
    private Integer coordinateX;
    private Integer coordinateY;
    private String gameId;
}

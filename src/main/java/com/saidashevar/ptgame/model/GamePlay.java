package com.saidashevar.ptgame.model;

import lombok.Data;

@Data
public class GamePlay {
	
	private String gameId;
	private String requester;
	private Integer squad; //1 - player's chosen his own squad, 2 - opponent's.
    private Integer coordinateX;
    private Integer coordinateY;
}

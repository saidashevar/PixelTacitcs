package com.saidashevar.ptgame.controller.request;

import lombok.Data;

@Data
public class HireHeroRequest {
	private String gameId;
    private String login;
    private int coordinateX; //it is choosing between vanguard, flank, rear guard.
    private int coordinateY; //it is choosing column
    private int cardId;
}

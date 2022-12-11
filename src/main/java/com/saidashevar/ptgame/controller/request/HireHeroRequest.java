package com.saidashevar.ptgame.controller.request;

import lombok.Data;

@Data
public class HireHeroRequest {
	private String gameId;
    private String login;
    private int coordinateY;
    private Long cardId;
}

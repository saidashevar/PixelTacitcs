package com.saidashevar.ptgame.controller.request;

import lombok.Data;

@Data
public class PlaceOperatorRequest {
	private String gameId;
    private String login;
    private int coordinateY;
    private int cardNumber;
}

package com.saidashevar.ptgame.config.response;

import lombok.Data;

@Data
public class UniResponse<T> {
	
	private String type;
	private T info;
	
	public UniResponse(String string, T cardCount) {
		this.type = string;
		this.info = cardCount;
	}
}

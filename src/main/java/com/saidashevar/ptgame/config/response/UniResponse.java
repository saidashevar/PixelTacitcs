package com.saidashevar.ptgame.config.response;

import lombok.Data;

@Data
public class UniResponse<T> {
	
	private ResponseTypes type;
	private T info;
	
	public UniResponse(ResponseTypes type, T cardCount) {
		this.type = type;
		this.info = cardCount;
	}
}

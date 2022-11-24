package com.saidashevar.ptgame.model;

import lombok.Data;

@Data
public class Turn {
	private int wave = 0;
	private boolean Attacking; // Player who attacks, makes his move first
	private byte actionsLeft = 10; //For experiments only
}

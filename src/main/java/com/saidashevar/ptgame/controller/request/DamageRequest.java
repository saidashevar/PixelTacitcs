package com.saidashevar.ptgame.controller.request;

import com.saidashevar.ptgame.model.effects.DamageType;

import lombok.Data;

@Data
public class DamageRequest {
	
	private String gameId;
	private String login;
	
	private DamageType type;
	private boolean attackerIsLeader;
	private long attackerId;
	private boolean targetIsLeader;
	private long targetId;
	
}

package com.saidashevar.ptgame.controller.request;

import com.saidashevar.ptgame.model.effects.DamageType;

import lombok.Data;

@Data
public class DamageRequest {
	DamageType type;
}

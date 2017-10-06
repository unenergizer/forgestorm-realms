package com.forgestorm.realms;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/*********************************************************************************
 *
 * OWNER: Robert Andrew Brown & Joseph Rugh
 * PROGRAMMER: Robert Andrew Brown & Joseph Rugh
 * PROJECT: forgestorm-realms
 * DATE: 7/4/2017
 * _______________________________________________________________________________
 *
 * Copyright © 2017 ForgeStorm.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code and/or source may be
 * reproduced, distributed, or transmitted in any form or by any means,
 * including photocopying, recording, or other electronic or mechanical methods,
 * without the prior written permission of the owner.
 */

@Getter
@Setter
class PlayerRealmData {
	
	private UUID player;
	private UUID realmOwner;
	private Location joinLocation;
	private int timeTillJoin;
	private int invincibilityTimeLeft;
	
	PlayerRealmData(Player player, UUID realmOwner, Location joinLocation) {
		this.player = player.getUniqueId();
		this.realmOwner = realmOwner;
		this.joinLocation = joinLocation;

        timeTillJoin = 5;
        invincibilityTimeLeft = 15;
    }
}
package com.forgestorm.realms;

import com.forgestorm.realms.commands.RealmCommands;
import com.forgestorm.realms.world.WorldManager;
import com.forgestorm.spigotcore.SpigotCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
public class Realms extends JavaPlugin {

    private SpigotCore spigotCore = (SpigotCore) Bukkit.getServer().getPluginManager().getPlugin("FS-SpigotCore");
    private RealmManager realmManager;

    /**
     * Enables player made realms.
     */
    @Override
    public void onEnable() {
        WorldManager.getInstance().setup(this);
        realmManager = new RealmManager(this);

        // Start Bukkit Tasks
        realmManager.runTaskTimer(this, 0, 20);

        // Register Commands
        getCommand("realm").setExecutor(new RealmCommands(this));
    }

    /**
     * This will disable player made realms.
     */
    @Override
    public void onDisable() {
        realmManager.disable();
    }
}

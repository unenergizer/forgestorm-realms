package com.forgestorm.realms;

import com.forgestorm.realms.commands.RealmCommands;
import com.forgestorm.realms.remotefilemanager.AsyncRealmDownloadFTP;
import com.forgestorm.realms.remotefilemanager.AsyncRealmUploadFTP;
import com.forgestorm.realms.remotefilemanager.SyncWorldLoader;
import com.forgestorm.realms.remotefilemanager.SyncWorldUnloader;
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
    private SyncWorldLoader syncWorldLoader;
    private SyncWorldUnloader syncWorldUnloader;
    private AsyncRealmDownloadFTP asyncRealmDownloadFTP;
    private AsyncRealmUploadFTP asyncRealmUploadFTP;

    /**
     * Enables player made realms.
     */
    @Override
    public void onEnable() {
        realmManager = new RealmManager(this);
        syncWorldLoader = new SyncWorldLoader();
        syncWorldUnloader = new SyncWorldUnloader(this);
        asyncRealmDownloadFTP = new AsyncRealmDownloadFTP(this);
        asyncRealmUploadFTP = new AsyncRealmUploadFTP(this);

        // Start Bukkit Tasks
        realmManager.runTaskTimer(this, 0, 20);
        syncWorldLoader.runTaskTimer(this, 0, 5);
        syncWorldUnloader.runTaskTimer(this, 0, 5);
        asyncRealmDownloadFTP.runTaskTimerAsynchronously(this, 0, 20);
        asyncRealmUploadFTP.runTaskTimerAsynchronously(this, 0, 20);

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

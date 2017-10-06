package com.forgestorm.realms.world;

import com.forgestorm.realms.Realms;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*********************************************************************************
 *
 * OWNER: Robert Andrew Brown & Joseph Rugh
 * PROGRAMMER: Robert Andrew Brown & Joseph Rugh
 * PROJECT: forgestorm-minigame-framework
 * DATE: 6/3/2017
 * _______________________________________________________________________________
 *
 * Copyright © 2017 ForgeStorm.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code and/or source may be 
 * reproduced, distributed, or transmitted in any form or by any means, 
 * including photocopying, recording, or other electronic or mechanical methods, 
 * without the prior written permission of the owner.
 */

public class WorldManager extends BukkitRunnable {

    private static WorldManager instance = null;
    private static boolean isSetup = false;

    private final AsyncFileManager asyncFileManager = new AsyncFileManager(this);
    private final Queue<String> worldLoadQueue = new ConcurrentLinkedQueue<>();
    private final Queue<String> worldUnloadQueue = new ConcurrentLinkedQueue<>();
    private Map<String, World> loadedWorlds = new HashMap<>();

    private WorldManager() {
    }

    /**
     * Gets the current instance of GameManager.
     *
     * @return An instance of GameManager. If not created, it will create one.
     */
    public static WorldManager getInstance() {
        if (instance == null) {
            instance = new WorldManager();
            return instance;
        }
        return instance;
    }

    /**
     * This will setup the WorldManager instance.
     *
     * @param plugin Main plugin instance.
     */
    public void setup(Realms plugin) {
        if (isSetup) return;

        isSetup = true;
        runTaskTimer(plugin, 0, 20);
        asyncFileManager.runTaskTimerAsynchronously(plugin, 0, 20 * 5);
    }

//    /**
//     * Check if a given world is loaded.
//     *
//     * @param worldName The world data and name of the world to check.
//     * @return True if the world is loaded, false otherwise.
//     */
//    public boolean isWorldLoaded(String worldName) {
//        for (World world : Bukkit.getWorlds()) if (world.getName().equals(worldName.getWorldName())) return true;
//        return false;
//    }

    /**
     * Gets the requested world.
     *
     * @param worldName The world data that contains the info
     *                  needed to get the requested world.
     */
    public void copyWorld(String worldName) {
        asyncFileManager.addWorldToLoad(worldName);
    }

    /**
     * Gets a world.
     *
     * @param worldName The data that contains a world name.
     * @return A world.
     */
    public World getWorld(String worldName) {
        if (!loadedWorlds.containsKey(worldName)) throw new NullPointerException("World is not loaded!");
        return loadedWorlds.get(worldName);
    }

    /**
     * Prepares to load a world sync with the server.
     *
     * @param worldName The data that represents a world.
     */
    public void loadWorld(String worldName) {
        worldLoadQueue.add(worldName);
    }

    /**
     * Prepares to unload a world from the server.
     *
     * @param worldName Data that represents the world we want
     *                  to unload.
     */
    public void unloadWorld(String worldName) {
        worldUnloadQueue.add(worldName);
    }

    /**
     * This will load a world that is in the SyncLoadQueue.
     * We will load the world in sync with the Spigot API.
     * Async world loading is NOT supported with the Spigot API.
     */
    private void syncWorldLoad() {
        if (worldLoadQueue.isEmpty()) return;
        String worldName = worldLoadQueue.remove();

        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.createWorld();

        // Set main reference of the arena world.
        loadedWorlds.put(worldName, Bukkit.getWorld(worldName));
    }

    /**
     * This will unload the given world from the server.  We
     * choose to not save the world in the unloadWorld method
     * because when we need the world again, it will be loaded
     * from a backup.
     */
    private void syncWorldUnload() {
        if (worldUnloadQueue.isEmpty()) return;
        String worldName = worldUnloadQueue.remove();
        World world = loadedWorlds.get(worldName);

        // Unload chunks
        for (Chunk chunk : world.getLoadedChunks()) {
            chunk.unload(false);
        }

        // Unload the world
        Bukkit.unloadWorld(world, false);
        loadedWorlds.remove(worldName);

        // Prepare the directory for saving.
        asyncFileManager.addWorldToSave(worldName);
    }

    /**
     * Repeating task that will load and unload worlds as needed.
     */
    @Override
    public void run() {
        syncWorldUnload();
        syncWorldLoad();
    }
}

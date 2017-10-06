package com.forgestorm.realms.remotefilemanager;

import com.forgestorm.spigotcore.util.logger.ColorLogger;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by unene on 2/6/2017.
 */
public class SyncWorldLoader extends BukkitRunnable {

    private final int capacity = 100;
    private BlockingQueue<String> worldLoaderQueue = new ArrayBlockingQueue<>(capacity);//<String> worldName/playerUUID

    /**
     * This will add an entry to the world loading queue.
     *
     * @param worldNameToAdd The name of the world we want to load.
     * @return True if world loading was a success. False otherwise.
     */
    public boolean addWorldName(String worldNameToAdd) {
        if (worldLoaderQueue.size() < capacity) {
            ColorLogger.DARK_PURPLE.printLog("[REALM] World added to SyncWorldLoader: " + worldNameToAdd);
            worldLoaderQueue.add(worldNameToAdd);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        //Load world if queue is not empty.
        if (!worldLoaderQueue.isEmpty()) {
            String worldName = worldLoaderQueue.remove();

            ColorLogger.DARK_PURPLE.printLog("[REALM] SyncWorldLoader loading world: " + worldName);
            Bukkit.createWorld(new WorldCreator(worldName));
        }
    }
}

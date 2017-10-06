package com.forgestorm.realms.world;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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

class AsyncFileManager extends BukkitRunnable {

    private final WorldManager worldManager;
    @Getter
    private final Queue<String> loadWorldQueue = new ConcurrentLinkedQueue<>();
    private final Queue<String> saveWorldQueue = new ConcurrentLinkedQueue<>();
    private final Queue<String> deleteWorldQueue = new ConcurrentLinkedQueue<>();
    private boolean loadingWorld = false;
    private boolean savingWorld = false;
    private boolean deletingWorld = false;

    AsyncFileManager(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    /**
     * This will prepare a world directory to be copied
     * from the backup directory.
     *
     * @param worldName The name of the world to copy.
     */
    void addWorldToLoad(String worldName) {
        loadWorldQueue.add(worldName);
    }

    /**
     * This will prepare a world directory to be copied
     * from the game directory to the backup directory.
     *
     * @param worldName The name of the world to copy.
     */
    void addWorldToSave(String worldName) {
        saveWorldQueue.add(worldName);
    }

    /**
     * This will prepare a world directory to be deleted
     * from the server. If this is called, the world is
     * no longer needed and can be fully removed.
     * <p>
     * NOTE: When the world is needed later, it will be
     * restored from a backup.
     *
     * @param worldName The name of the world directory
     *                  to delete.
     */
    void addWorldToDelete(String worldName) {
        deleteWorldQueue.add(worldName);
    }

    private void loadWorlds() {
        if (!loadWorldQueue.isEmpty() && !loadingWorld) {
            // Put lock on copying worlds for now.
            loadingWorld = true;

            // Copy the world directory
            String worldName = loadWorldQueue.remove();

            // Copy the folder!
            copyFolder(
                    new File("realms" + File.separator + worldName),
                    new File(worldName));

            // Now lets load the world and create a new world data!
            worldManager.loadWorld(worldName);

            // Now unlock the copying of worlds
            loadingWorld = false;
        }
    }

    private void saveWorlds() {
        if (!saveWorldQueue.isEmpty() && !savingWorld) {
            // Put lock on saving worlds for now.
            savingWorld = true;

            // Copy the world directory
            String worldName = saveWorldQueue.remove();

            // Copy the folder!
            copyFolder(
                    new File(worldName),
                    new File("realms" + File.separator + worldName));

            // Now delete the world in the server directory
            deleteFolder(worldName);

            // Now unlock the saving of worlds
            savingWorld = false;
        }
    }

    /**
     * This will delete a world from the server directory.
     * If the world is needed later, it will be restored
     * from a backup directory.
     */
    private void deleteWorld() {
        if (!deleteWorldQueue.isEmpty() && !deletingWorld) {
            deletingWorld = true;
            deleteFolder(deleteWorldQueue.remove());
            deletingWorld = false;
        }
    }

    /**
     * The repeating task that will copy and delete worlds.
     */
    @Override
    public void run() {
        loadWorlds();
        saveWorlds();
        deleteWorld();
    }

    /**
     * Copies a world directory to another directory.
     *
     * @param src  The source destination of the folder to copy.
     * @param dest The end destination to copy the folder to.
     */
    private void copyFolder(File src, File dest) {
        if (src.isDirectory()) {

            // if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
            }

            // list all the directory contents
            String files[] = src.list();

            assert files != null;
            for (String file : files) {
                // construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // recursive copy
                copyFolder(srcFile, destFile);
            }

        } else {
            // if file, then copy it
            // Use bytes stream to support all file types
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(src);
                out = new FileOutputStream(dest);

                byte[] buffer = new byte[1024];

                int length;
                // copy the file content in bytes
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete's a file directory.
     *
     * @param worldName The data file that will be deleted.
     */
    private void deleteFolder(String worldName) {
        try {
            Files.walkFileTree(Paths.get(worldName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

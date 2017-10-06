package com.forgestorm.realms;

import com.forgestorm.spigotcore.constants.CommonSounds;
import com.forgestorm.spigotcore.constants.SpigotCoreMessages;
import com.forgestorm.spigotcore.database.PlayerProfileData;
import com.forgestorm.spigotcore.util.display.BossBarAnnouncer;
import com.forgestorm.spigotcore.util.logger.ColorLogger;
import com.forgestorm.spigotcore.util.math.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
public class RealmManager extends BukkitRunnable implements Listener {

    private final Realms plugin;
    private final String defaultInsidePortalLocation = "11/19/7";
    private final String defaultRealmTitle = "Hi! Welcome to my realm!!";
    private final int closeDownTime = 60 * 5;
    private final int maxLockTime = 60;
    private Map<PlayerRealm, Integer> removeRealms = new ConcurrentHashMap<>();
    private Map<UUID, PlayerRealm> loadedRealms = new HashMap<>();
    private Map<Player, PlayerRealmData> playerRealmData = new HashMap<>();
    private boolean isRealmCreatorLocked = false;
    private List<RealmLock> lockedPlayerRealms = new ArrayList<>();
    private ResetTimer resetTimer;

    RealmManager(Realms plugin) {
        this.plugin = plugin;
        resetTimer = new ResetTimer();
        resetTimer.runTaskTimerAsynchronously(plugin, 0, 1);

        // Register Listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * This is a conditional check to make sure their isn't a realm nearby the one that is
     * attempting to be placed. We prevent portals from being next to each other for various
     * technical reasons. For instance, we don't want portal blocks to combine and look like
     * one big portal.
     *
     * @param location The location of the attempted portal placement.
     * @return True if we can set a portal at the given location, false if another portal is too close.
     */
    private static boolean getNearbyPortals(Location location) {
        int radius = 2; // How far this location must be from other portal locations.
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if (location.getWorld().getBlockAt(x, y, z).getType().equals(Material.PORTAL)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This is called when the server is shutting down. This will
     * being the process of unloading all realm worlds and saving
     * them to disk.
     */
    void disable() {
        for (Entry<UUID, PlayerRealm> entry : loadedRealms.entrySet()) {
            unloadRealm(entry.getValue());
        }
        loadedRealms.clear();

        // Unregister Listeners
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPhysicsEvent.getHandlerList().unregister(this);
        PlayerPortalEvent.getHandlerList().unregister(this);
        PlayerInteractEntityEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        PlayerTeleportEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        ProjectileLaunchEvent.getHandlerList().unregister(this);
        WorldLoadEvent.getHandlerList().unregister(this);
    }

    /**
     * This will allow a player to reset their realm to the default build.
     *
     * @param player The realm owner who will reset their realm.
     */
    public void resetRealm(Player player) {
        PlayerProfileData playerProfileData = plugin.getSpigotCore().getProfileManager().getProfile(player);

        if (!playerProfileData.isHasRealm()) {
            player.sendMessage(ChatColor.RED + "You don't have a realm to reset.");
            CommonSounds.ACTION_FAILED.playSound(player);
            return;
        }

        if (loadedRealms.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Your realm must be closed to reset it.");
            CommonSounds.ACTION_FAILED.playSound(player);
            return;
        }

        playerProfileData.setHasRealm(false);
        playerProfileData.setRealmInsideLocation(defaultInsidePortalLocation);
        playerProfileData.setRealmTitle(defaultRealmTitle);
        player.sendMessage(ChatColor.GREEN + "Your realm has been reset. This cannot be undone.");
        CommonSounds.ACTION_SUCCESS.playSound(player);
    }

    /**
     * This being an async removal of the supplied realm.
     *
     * @param realm The realm we are working with.
     */
    private void unloadRealm(PlayerRealm realm) {
        Player player = realm.getRealmOwner();
        lockedPlayerRealms.add(new RealmLock(player));
        realm.closeRealm();
        loadedRealms.remove(player.getUniqueId());
    }

    /**
     * This will begin the process of opening a players realm, if it is possible.
     * Checks inside the method will make sure the player hasn't tried to open a
     * realm next to another one and it also makes sure the player isn't trying to
     * set their realm portal more than one time.
     *
     * @param player        The player who wants to open their realm.
     * @param blockLocation The location of the realm portal spawn.
     */
    private void loadRealm(Player player, Location blockLocation) {
        // Make sure the player isn't in realm lock status.
        if (hasRealmLock(player)) {
            int timeLeft = realmLockTimeLeft(player);
            player.sendMessage(ChatColor.RED + "You can not open your realm yet. Please wait " + timeLeft + " seconds.");
            CommonSounds.ACTION_FAILED.playSound(player);
            return;
        }

        // Make sure the player is in the main lobby world.
        if (!blockLocation.getWorld().equals(Bukkit.getWorlds().get(0))) {
            player.sendMessage(ChatColor.RED + "You must be in a lobby world to open your realm.");
            CommonSounds.ACTION_FAILED.playSound(player);
            return;
        }

        //Make sure the player isn't setting the realm close to another realm.
        if (getNearbyPortals(blockLocation)) {
            player.sendMessage(SpigotCoreMessages.REALM_PORTAL_PLACE_TOO_CLOSE.toString());
            CommonSounds.ACTION_FAILED.playSound(player);
            return;
        }

        //Make sure the user doesn't already have a realm open.
        if (loadedRealms.containsKey(player.getUniqueId())) {
            player.sendMessage(SpigotCoreMessages.REALM_PORTAL_DUPLICATE.toString());
            CommonSounds.ACTION_FAILED.playSound(player);
            return;
        }

        PlayerRealm realm = new PlayerRealm(plugin, player, blockLocation);
        loadedRealms.put(player.getUniqueId(), realm);
        realm.openRealm(player);
    }

    /**
     * This is called when a player attempts to join a realm. Various operations
     * will happen here, such as giving the player any countdown boss bar's, if
     * they exist and spawning them at the portal location inside the realm world.
     *
     * @param player   The player attempting to join the realm world.
     * @param location The location of the realm portal block they are entering. From
     *                 location we can determine which realm they are trying to join.
     */
    private void playerEnterRealm(Player player, Location location) {
        for (Entry<UUID, PlayerRealm> entry : loadedRealms.entrySet()) {
            PlayerRealm realm = entry.getValue();

            if (location.distanceSquared(realm.getPortalOutsideLocation()) <= 2) {
                PlayerRealmData pData = new PlayerRealmData(player, realm.getRealmOwner().getUniqueId(), player.getLocation());

                playerRealmData.put(player, pData);

                //TP player to inside portal location
                player.teleport(realm.getPortalInsideLocation());

                player.sendMessage("");
                player.sendMessage(ChatColor.GREEN + "You have entered " + realm.getRealmOwner().getName() + "s realm.");
                if (realm.getTitle() != null) {
                    player.sendMessage(ChatColor.YELLOW + realm.getTitle());
                }

                //Stop double jump in the loadedRealms.
                player.setAllowFlight(false);
                player.setFlying(false);

                //Setup boss bar for player, if needed.
                if (realm.getTimeTillpvpRenabled() > 0) realm.getBossBar().showBossBar(player);

                //RealmCommands owner code
                if (realm.getRealmOwner().equals(player)) {
                    //Set owner flying if enabled.
                    if (realm.getTimeTillFlyingDisabled() > 1) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                    }

                    //If the realm is on a countdown to be closed,
                    //remove it from that countdown if the owner joins the world again.
                    if (removeRealms.containsKey(realm)) {
                        removeRealms.remove(realm);
                        player.sendMessage(ChatColor.RED + "The countdown to close your realm has been canceled.");
                        player.sendMessage(ChatColor.YELLOW + "Please exit your realm and try to close it again.");
                    }
                }
            }
        }
    }

    /**
     * This is called when a player leaves a realm either by jumping out of it or
     * by leaving through the realm portal block inside the realm world.
     *
     * @param player The player who is attempting to leave the realm world.
     */
    void playerExitRealm(Player player) {
        //Remove boss bar if they have one.
        for (Entry<UUID, PlayerRealm> entry : loadedRealms.entrySet()) {
            PlayerRealm realm = entry.getValue();

            if (realm.getPlayerList().contains(player)) {
                if (realm.getBossBar().getBossBarViewers().contains(player)) realm.getBossBar().removeBossBar(player);
            }
        }
        PlayerRealmData data = playerRealmData.get(player);
        player.teleport(data.getJoinLocation());
        playerRealmData.remove(player);
        player.setPortalCooldown(3); //Set 3 second cooldown before being able to join again.
        player.setAllowFlight(true);
        player.setFlying(false);
    }

    /**
     * Checks to see if the current player has already opened their realm world.
     *
     * @param player The player who is attempting to open a realm world.
     * @return True if the player has a realm open, false otherwise.
     */
    public boolean hasOpenRealm(Player player) {
        return loadedRealms.containsKey(player.getUniqueId());
    }

    /**
     * This will get the realm associated with this player object.
     *
     * @param player The player we want to grab the realm from.
     * @return Returns the realm object for the specified player.
     */
    public PlayerRealm getPlayerRealm(Player player) {
        return loadedRealms.get(player.getUniqueId());
    }

    /**
     * This will get the realm associated with this UUID. We will have to parse
     * the UUID inorder to get a UUID object from the string.
     *
     * @param uuid The player UUID we want to grab the realm from.
     * @return Returns the realm object for the specified player.
     */
    private PlayerRealm getPlayerRealm(String uuid) {
        return loadedRealms.get(UUID.fromString(uuid));
    }

    /**
     * This is called when a player breaks their realm portal form the main world.
     * This will then begin the unloading process for the realm.
     *
     * @param player   The player who attempted to close down the realm.
     * @param location The location of the attempted block break.
     */
    private void removePlayerRealmAtLocation(Player player, Location location) {
        if (hasOpenRealm(player)) {
            UUID uuid = player.getUniqueId();
            PlayerRealm realm = loadedRealms.get(uuid);
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            int realmX = realm.getOutsideBlockBottom().getLocation().getBlockX();
            int realmY = realm.getOutsideBlockBottom().getLocation().getBlockY();
            int realmZ = realm.getOutsideBlockBottom().getLocation().getBlockZ();

            if (x == realmX && z == realmZ && y >= realmY - 2 && y <= realmY + 2) {

                if (realm.getPlayerList().size() != 0) {
                    if (removeRealms.containsKey(loadedRealms.get(uuid))) {
                        player.sendMessage(ChatColor.YELLOW + "You have players inside your realm. So it can not close yet.");
                        player.sendMessage(ChatColor.YELLOW + "Your realm will close automatically if they leave.");
                        player.sendMessage(ChatColor.YELLOW + "If the players in your realm leave, it will close automatically.");
                        CommonSounds.ACTION_FAILED.playSound(player);
                        return;
                    }
                    player.sendMessage(ChatColor.YELLOW + "Players are inside your realm. So it will close in 5 minutes.");
                    player.sendMessage(ChatColor.YELLOW + "If you enter the realm again, it will cancel this countdown.");
                    player.sendMessage(ChatColor.YELLOW + "If the players in your realm leave, it will close automatically.");
                    CommonSounds.ACTION_FAILED.playSound(player);
                    removeRealms.put(loadedRealms.get(uuid), 60 * 5);
                } else {
                    if (removeRealms.containsKey(loadedRealms.get(uuid))) return;
                    unloadRealm(realm);
                }
            }
        }
    }

    /**
     * This will begin the realm closing operations for a player. This is only
     * called when the player manually closes their realm from within the main
     * world.
     *
     * @param player The player who closed their realm.
     */
    private void removeLogoutPlayerRealm(Player player) {
        if (hasOpenRealm(player)) {
            UUID uuid = player.getUniqueId();
            removeRealms.put(loadedRealms.get(uuid), 60 * 5); // Set unload time.
        }
    }

    /**
     * Set's the join title for a users realm. This is shown to all users who
     * enter the owners realm.
     *
     * @param player The player who is setting their realm title.
     * @param title  The title that they want to show all players joining.
     */
    public void setTitle(Player player, String title) {
        if (hasOpenRealm(player)) {
            PlayerRealm realm = loadedRealms.get(player.getUniqueId());
            realm.setTitle(title);
            plugin.getSpigotCore().getProfileManager().getProfile(player).setRealmTitle(title);
        }
    }

    /**
     * This is a conditional check to see if the player is building inside a realm's dimensions.
     *
     * @param placedLocation The location of the block placement.
     * @return True if the block can be placed, false otherwise.
     */
    private boolean buildingInsideRealm(Location placedLocation) {
        int x = (int) placedLocation.getX();
        int z = (int) placedLocation.getZ();
        PlayerRealm realm = getPlayerRealm(placedLocation.getWorld().getName());
        int max = (realm.getTier() * 16) - 1; //Building starts at 0,0 and tier 1 plot ends at 15,15 (so minus 1).
        return (x >= 0 && x <= max &&
                z >= 0 && z <= max);
    }

    /**
     * A conditional check to test to see if a player can build inside a given realm.
     *
     * @param player The player who wants to build.
     * @return True if they can build, false otherwise.
     */
    private boolean canBuild(Player player) {
        //Player is in an instance
        String worldName = player.getWorld().getName();
        PlayerRealm realm = getPlayerRealm(worldName);

        if (player.equals(realm.getRealmOwner())) {
            return true;
        } else if (!worldName.equals(player.getUniqueId().toString())) {
            //If the player is not a friend, do not let them build.
            return realm.isFriend(player);
        }
        return false;
    }

    /**
     * Prevent players from opening their realm if a realm lock is in place.
     * A realm lock is used to give the WorldManager time to save their realm
     * before they can open it again.
     *
     * @param player The player who wants to open their realm.
     * @return True if the realm is not locked, false otherwise.
     */
    private boolean hasRealmLock(Player player) {
        for (RealmLock realmLock : lockedPlayerRealms) {
            if (realmLock.getPlayer() == player) return true;
        }
        return false;
    }

    private int realmLockTimeLeft(Player player) {
        for (RealmLock realmLock : lockedPlayerRealms) {
            if (realmLock.getPlayer() == player) {
                return maxLockTime - realmLock.getLockTimeLeft();
            }
        }
        return 0;
    }

    @Override
    public void run() {

        //The code to close and remove a realm.
        for (PlayerRealm realm : removeRealms.keySet()) {

            int countDown = removeRealms.get(realm);
            List<Player> players = realm.getPlayerList();

            if (countDown % 60 == 0 && countDown >= 30) {
                for (Player player : players) {
                    player.sendMessage(ChatColor.YELLOW + "Closing world in " + countDown / 60 + " minutes.");
                }
            }

            if (countDown <= 10 || countDown == 30) {
                for (Player player : players) {
                    player.sendMessage(ChatColor.RED + "Closing world in " + countDown + " seconds.");
                }
            }

            if (countDown <= 0 || players.size() == 0) {
                unloadRealm(realm); // Begin world unload
            } else {
                removeRealms.replace(realm, --countDown);
            }

        }

        //RealmCommands lock
        Iterator<RealmLock> iterator = lockedPlayerRealms.iterator();
        while (iterator.hasNext()) {
            RealmLock realmLock = iterator.next();
            int timeLeft = realmLock.getLockTimeLeft() + 1;

            if (timeLeft >= maxLockTime) {
                iterator.remove();
            } else {
                realmLock.setLockTimeLeft(timeLeft);
            }
        }

        //Orb of peace and flight countdown.
        for (PlayerRealm realms : loadedRealms.values()) {
            if (!realms.isWorldLoaded()) return;
            int timeLeftPeace = realms.getTimeTillpvpRenabled();
            int timeLeftFlight = realms.getTimeTillFlyingDisabled();
            int idleTime = realms.getIdleTime();
            int maxIdleTime = realms.getMaxIdleTime();
            Player realmOwner = realms.getRealmOwner();
            List<Player> players = realms.getPlayerList();
            BossBarAnnouncer bossBar = realms.getBossBar();

            // Idle realm countdown
            if (realms.getPlayerList().isEmpty()) {
                realms.setIdleTime(idleTime + 1);

                if (idleTime == 1)
                    ColorLogger.DARK_BLUE.printLog("[REALM] RealmCommands is idle, starting idle countdown. Time: " + idleTime);

                // If no players are inside the realm, close it after X time.
                if (idleTime >= maxIdleTime) {
                    realms.getRealmOwner().sendMessage(ChatColor.RED + "Your realm has been idle for 5 minutes. Closing it now.");
                    realms.setIdleTime(0);
                    unloadRealm(realms);
                }
            } else {
                // Reset idle time, because someone is inside the realm.
                if (idleTime > 0) {
                    realms.setIdleTime(0);
                    ColorLogger.DARK_BLUE.printLog("[REALM] The realm is no longer idle, resetting countdown.");
                }
            }

            //Orb of peace countdown.
            if (timeLeftPeace > 0) {
                realms.setTimeTillpvpRenabled(timeLeftPeace - 1);

                //Update boss bar time.
                String time = ChatColor.AQUA + "Peaceful Time Left" + ChatColor.DARK_GRAY + ": " +
                        ChatColor.YELLOW + TimeUnit.toString(timeLeftPeace);
                bossBar.setBossBarTitle(time);

                double barPercent = (timeLeftPeace * 100) / realms.getMaxTimeTillPvpRenabled();

                bossBar.setBossBarProgress(barPercent / 100); //Must be between 0-1

                for (Player player : players) {
                    if (timeLeftPeace <= 10 || timeLeftPeace == 30) {
                        player.sendMessage(ChatColor.GREEN + "Orb of peace ends in " +
                                ChatColor.RED + timeLeftPeace + ChatColor.GREEN + " seconds.");
                    }
                }

            } else if (timeLeftPeace == 0) {
                realms.setTimeTillpvpRenabled(timeLeftPeace - 1); //Set to "-1" to prevent duplicate messages.
                if (realmOwner.isOnline()) {
                    realmOwner.sendMessage(ChatColor.RED + "PVP is now enabled in your realm.");
                    realms.setMaxTimeTillPvpRenabled(0);//Set it back to zero.
                    realms.getHologram().getArmorStands().get(1).setCustomName(ChatColor.RED + "Hostile");
                }

                //Remove the bossbar countdown.
                for (Player player : players) {
                    if (bossBar.getBossBarViewers().contains(player)) bossBar.removeBossBar(player);
                }
            }

            //Orb of flight countdown.
            if (timeLeftFlight > 0) {
                realms.setTimeTillFlyingDisabled(timeLeftFlight - 1);

                for (Player player : players) {
                    if (timeLeftFlight <= 10 || timeLeftFlight == 30) {
                        player.sendMessage(ChatColor.GREEN + "Orb of flight ends in " +
                                ChatColor.RED + timeLeftFlight + ChatColor.GREEN + " seconds.");
                    }
                }

            } else if (timeLeftFlight == 0) {
                realms.setTimeTillFlyingDisabled(timeLeftFlight - 1); //Set to "-1" to prevent duplicate messages.
                if (realmOwner.isOnline()) {
                    realmOwner.sendMessage(ChatColor.RED + "Flight is now disabled in your realm.");
                }

                //Stop the player from flying.
                for (Player player : players) {
                    player.setFlying(false);
                }
            }
        }
    }

    /**
     * This will stop portal blocks from auto breaking during a block update.
     *
     * @param event A Bukkit event for BlockPhysics.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.getBlock().getType().equals(Material.PORTAL) || event.getChangedType().equals(Material.PORTAL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Let players break blocks in their loadedRealms.
        if (player.getWorld().equals(Bukkit.getWorlds().get(0))) {
            /*
            PLAYER IS IN A LOBBY WORLD.
             */

            // Let players break their portals.
            if (block.getType() == Material.PORTAL && player.getGameMode() != GameMode.CREATIVE) {
                removePlayerRealmAtLocation(player, block.getLocation());
            }
        } else if (player.getWorld().getName().length() == 36) {
            /*
            THE FOLLOWING CODE REPRESENTS ACTIONS INSIDE AN OPEN REALM.
             */

            // Prevent manual portal breaks inside loadedRealms.
            if (block.getType() == Material.PORTAL) {
                event.setCancelled(true);
                return;
            }

            // If the player is a friend in a realm, they can build.
            event.setCancelled(!canBuild(player));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDamage(BlockDamageEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();
        Player player = event.getPlayer();

        if (player.getWorld().equals(Bukkit.getWorlds().get(0))) {

            // let op's break blocks
            if (player.isOp() && player.getGameMode() == GameMode.CREATIVE && type != Material.PORTAL) {
                event.setCancelled(false);
                return;
            }

            // Let players break their portals.
            if (type == Material.PORTAL) {

                //Prevent Admin accidental deletion
                if (!player.isOp() && !player.getGameMode().equals(GameMode.CREATIVE)) {
                    event.setCancelled(true);
                }

                removePlayerRealmAtLocation(player, block.getLocation());
            }
        } else if (player.getWorld().getName().length() == 36) {
            /*
            THE FOLLOWING CODE REPRESENTS ACTIONS INSIDE AN OPEN REALM.
             */

            // Prevent manual portal breaks inside loadedRealms.
            if (event.getBlock().getType() == Material.PORTAL) {
                event.setCancelled(true);
                return;
            }

            // If the player is a friend in a realm, they can build.
            if (canBuild(player)) {
                event.setInstaBreak(true);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().equals(Bukkit.getWorlds().get(0))) return;

        boolean builtInsideRealm = buildingInsideRealm(event.getBlockPlaced().getLocation());
        boolean canBuild = canBuild(player);

        //Player can build but is trying to build outside realm tier.
        if (canBuild && !builtInsideRealm) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "You can not build outside your realm.");
            player.sendMessage(ChatColor.RED + "Upgrade your realm to build out further.");
        }

        //If the player is a friend in a realm, they can build.
        if (!canBuild || !builtInsideRealm) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Player realm / Hologram armor stand
        if (event.getEntity() instanceof ArmorStand && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (hasOpenRealm(player)) removePlayerRealmAtLocation(player, event.getEntity().getLocation());
        }
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {

        Player player = event.getPlayer();

        // Don't let NPC's teleport.
        if (player.hasMetadata("NPC")) return;

        // If player enters a Ender portal, teleport them back to spawn pad.
        if (event.getCause().equals(TeleportCause.NETHER_PORTAL)) {

            // Cancel teleportation to the NETHER
            event.setCancelled(true);

            // Is the player inside a realm or inside the main world?
            if (player.getWorld().equals(Bukkit.getWorlds().get(0))) {
                // The player is inside the main world.
                // Send the player to this player realm.
                playerEnterRealm(player, player.getLocation());
            } else {
                // Player is inside a realm.
                // Send them to the main world.
                playerExitRealm(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //Remove/unload player realm
        Player player = event.getPlayer();

        //Remove them from a players realm.
        getPlayerRealmData().remove(player);

        //Remove/unload player realm
        removeLogoutPlayerRealm(player);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        //Lets prevent the player from using an ender pearl to teleport around the world.
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();

        if (resetTimer.containsPlayer(player)) return;
        resetTimer.addPlayer(player);

        if (!(event.getRightClicked() instanceof Player)) return;
        Player clickedPlayer = (Player) event.getRightClicked();

        if (!player.getWorld().getName().equals(player.getUniqueId().toString())) return;

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (itemInMainHand == null) return;
        if (player.isSneaking() && itemInMainHand.getType().equals(Material.COMPASS)) {
            PlayerRealm realm = getPlayerRealm(player);

            if (!realm.isFriend(clickedPlayer)) {
                realm.addFriend(clickedPlayer);
            } else {
                realm.removeFriend(clickedPlayer);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        Block block = event.getClickedBlock();

        //Use reset timer to prevent accidental double click.
        if (!resetTimer.containsPlayer(player)) {
            resetTimer.addPlayer(player);

            //Player to place realm portal (only if they are in the main world).
            if (player.isSneaking() && player.getWorld().equals(Bukkit.getWorlds().get(0))) {
                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (event.getItem() != null && event.getItem().getType().equals(Material.COMPASS)) {
                        loadRealm(player, block.getLocation());
                        return;
                    }
                }
            }

            //INSIDE REALM ACTIONS
            if (player.getWorld().getName().equals(player.getUniqueId().toString())) {

                if (event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                        event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    int amount = itemStack.getAmount();

                    //Orb of peace
                    if (event.getItem() != null && event.getItem().getType().equals(Material.ENDER_PEARL)) {

                        //Consume used item.
                        if (amount > 1) {
                            itemStack.setAmount(amount - 1);
                        } else {
                            player.getInventory().setItemInMainHand(null);
                        }
                        getPlayerRealm(uuid).toggleOrbOfPeace(player);
                        return;
                    }

                    //Orb of flight
                    if (event.getItem() != null && event.getItem().getType().equals(Material.SLIME_BALL)) {
                        //Consume used item.
                        if (amount > 1) {
                            itemStack.setAmount(amount - 1);
                        } else {
                            player.getInventory().setItemInMainHand(null);
                        }
                        getPlayerRealm(uuid).toggleOrbOfFlight(player);
                        return;
                    }
                }

                //Check for realm portal move.
                if (player.isSneaking() && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (block.getType() != Material.PORTAL) {
                        if (event.getItem() != null && event.getItem().getType().equals(Material.COMPASS)) {
                            PlayerRealm realm = getPlayerRealm(player);

                            //Remove Old RealmCommands inside Portal
                            realm.getInsideBlockTop().setType(Material.AIR);
                            realm.getInsideBlockBottom().setType(Material.AIR);

                            //Set New RealmCommands inside Portal
                            realm.setPortalInsideLocation(block.getLocation().add(0, 1, 0));
                            realm.setPlayerInsidePortal(true);

                            //Save location to player profile.
                            double x = realm.getPortalInsideLocation().getX();
                            double y = realm.getPortalInsideLocation().getY();
                            double z = realm.getPortalInsideLocation().getZ();
                            plugin.getSpigotCore().getProfileManager().getProfile(player).setRealmInsideLocation(x + "/" + y + "/" + z);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        //Cancel ender pearl throwing.
        if (event.getEntityType().equals(EntityType.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        String worldName = event.getWorld().getName();

        //If the worldname length is equal to 36 we can assume it is a player realm.
        //The player loadedRealms world names are the players uuid.
        //So Player UUID = Player RealmCommands World Name (and vise versa)
        if (worldName.length() == 36) {
            //Set the inside portal blocks for this realm.
            new BukkitRunnable() {

                @Override
                public void run() {
                    cancel();
                    PlayerRealm realm = getPlayerRealm(worldName);
                    realm.setPlayerInsidePortal(false);
                    realm.setOutsideRealmPortal();
                    realm.setWorldLoaded(true);
                }

            }.runTaskLater(plugin, 20);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();

            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                // Cancel annoying void damage.
                event.setCancelled(true);

                // Wait one tick and respawn the player..
                new BukkitRunnable() {
                    public void run() {

                        player.setFallDistance(0F);
                        CommonSounds.ACTION_FAILED.playSound(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4 * 20, 100));

                        // If the player is in the main world, tp them to spawn.
                        if (player.getWorld().getName().length() == 36) {
                            // If the player was in a realm, make them leave the realm.
                            playerExitRealm(player);
                        }

                        cancel();
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }

    @Getter
    @Setter
    class RealmLock {
        private final Player player;
        private int lockTimeLeft = 0;

        RealmLock(Player player) {
            this.player = player;
        }
    }

    private class ResetTimer extends BukkitRunnable {

        private final Map<Player, Integer> countDowns = new ConcurrentHashMap<>();

        @Override
        public void run() {

            for (Player player : countDowns.keySet()) {

                int count = countDowns.get(player);

                if (count > 0) {
                    countDowns.remove(player);
                } else {
                    countDowns.replace(player, --count);
                }
            }
        }

        void addPlayer(Player player) {
            countDowns.put(player, 2);
        }

        boolean containsPlayer(Player player) {
            return countDowns.containsKey(player);
        }
    }
}

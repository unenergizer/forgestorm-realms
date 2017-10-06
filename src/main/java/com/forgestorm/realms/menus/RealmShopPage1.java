package com.forgestorm.realms.menus;

import com.forgestorm.spigotcore.SpigotCore;
import com.forgestorm.spigotcore.constants.ItemTypes;
import com.forgestorm.spigotcore.menus.Menu;
import com.forgestorm.spigotcore.menus.actions.Exit;
import com.forgestorm.spigotcore.menus.actions.PurchaseItemStack;
import com.forgestorm.spigotcore.util.item.ItemGenerator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

@SuppressWarnings("deprecation")
public class RealmShopPage1 extends Menu {

	private final SpigotCore plugin;

	public RealmShopPage1(SpigotCore plugin) {
		super(plugin);
		this.plugin = plugin;
		init("Realm Shop: Page 1", 6);
		makeMenuItems();
	}

	@Override
	protected void makeMenuItems() {
		ItemTypes type = ItemTypes.MENU;
		ItemGenerator itemGen = plugin.getItemGen();

		// Row 1
        ItemStack log0 = new ItemStack(Material.LOG, 1, (short) 0, (byte) 0);
        ItemStack log1 = new ItemStack(Material.LOG, 1, (short) 0, (byte) 1);
        ItemStack log2 = new ItemStack(Material.LOG, 1, (short) 0, (byte) 2);
        ItemStack log3 = new ItemStack(Material.LOG, 1, (short) 0, (byte) 3);
        ItemStack log2_0 = new ItemStack(Material.LOG_2, 1, (short) 0, (byte) 0);
        ItemStack log2_1 = new ItemStack(Material.LOG_2, 1, (short) 0, (byte) 1);
        ItemStack netherrack = new ItemStack(Material.NETHERRACK);
        ItemStack stone = new ItemStack(Material.STONE);
        ItemStack bedrock = new ItemStack(Material.BEDROCK);

        setBuyItem(log0, 0, new PurchaseItemStack(plugin, log0, 20), 20);
        setBuyItem(log1, 1, new PurchaseItemStack(plugin, log1, 20), 20);
        setBuyItem(log2, 2, new PurchaseItemStack(plugin, log2, 20), 20);
        setBuyItem(log3, 3, new PurchaseItemStack(plugin, log3, 20), 20);
        setBuyItem(log2_0, 4, new PurchaseItemStack(plugin, log2_0, 20), 20);
        setBuyItem(log2_1, 5, new PurchaseItemStack(plugin, log2_1, 20), 20);
        setBuyItem(netherrack, 6, new PurchaseItemStack(plugin, netherrack, 20), 20);
        setBuyItem(stone, 7, new PurchaseItemStack(plugin, stone, 20), 20);
        setBuyItem(bedrock, 8, new PurchaseItemStack(plugin, bedrock, 20), 20);

        // Row 2
        ItemStack plank0 = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 0);
        ItemStack plank1 = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 1);
        ItemStack plank2 = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 2);
        ItemStack plank3 = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 3);
        ItemStack plank2_0 = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 4);
        ItemStack plank2_1 = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 5);
        ItemStack netherBrick = new ItemStack(Material.NETHER_BRICK);
        ItemStack cobble = new ItemStack(Material.COBBLESTONE);
        ItemStack mossyCobble = new ItemStack(Material.MOSSY_COBBLESTONE);

        setBuyItem(plank0, 9, new PurchaseItemStack(plugin, plank0, 20), 20);
        setBuyItem(plank1, 10, new PurchaseItemStack(plugin, plank1, 20), 20);
        setBuyItem(plank2, 11, new PurchaseItemStack(plugin, plank2, 20), 20);
        setBuyItem(plank3, 12, new PurchaseItemStack(plugin, plank3, 20), 20);
        setBuyItem(plank2_0, 13, new PurchaseItemStack(plugin, plank2_0, 20), 20);
        setBuyItem(plank2_1, 14, new PurchaseItemStack(plugin, plank2_1, 20), 20);
        setBuyItem(netherBrick, 15, new PurchaseItemStack(plugin, netherBrick, 20), 20);
        setBuyItem(cobble, 16, new PurchaseItemStack(plugin, cobble, 20), 20);
        setBuyItem(mossyCobble, 17, new PurchaseItemStack(plugin, mossyCobble, 20), 20);

        // Row 3
        ItemStack stair0 = new ItemStack(Material.WOOD_STAIRS);
        ItemStack stair1 = new ItemStack(Material.SPRUCE_WOOD_STAIRS);
        ItemStack stair2 = new ItemStack(Material.BIRCH_WOOD_STAIRS);
        ItemStack stair3 = new ItemStack(Material.JUNGLE_WOOD_STAIRS);
        ItemStack stair2_0 = new ItemStack(Material.ACACIA_STAIRS);
        ItemStack stair2_1 = new ItemStack(Material.DARK_OAK_STAIRS);
        ItemStack netherStairs = new ItemStack(Material.NETHER_BRICK_STAIRS);
        ItemStack cobbleStairs = new ItemStack(Material.COBBLESTONE_STAIRS);
        ItemStack mossyBrick = new ItemStack(Material.SMOOTH_BRICK, 1, (short) 0, (byte) 1);

        setBuyItem(stair0, 18, new PurchaseItemStack(plugin, stair0, 20), 20);
        setBuyItem(stair1, 19, new PurchaseItemStack(plugin, stair1, 20), 20);
        setBuyItem(stair2, 20, new PurchaseItemStack(plugin, stair2, 20), 20);
        setBuyItem(stair3, 21, new PurchaseItemStack(plugin, stair3, 20), 20);
        setBuyItem(stair2_0, 22, new PurchaseItemStack(plugin, stair2_0, 20), 20);
        setBuyItem(stair2_1, 23, new PurchaseItemStack(plugin, stair2_1, 20), 20);
        setBuyItem(netherStairs, 24, new PurchaseItemStack(plugin, netherStairs, 20), 20);
        setBuyItem(cobbleStairs, 25, new PurchaseItemStack(plugin, cobbleStairs, 20), 20);
        setBuyItem(mossyBrick, 26, new PurchaseItemStack(plugin, mossyBrick, 20), 20);

        // Row 4
        ItemStack slab0 = new ItemStack(Material.WOOD_STEP, 1, (short) 0, (byte) 0);
        ItemStack slab1 = new ItemStack(Material.WOOD_STEP, 1, (short) 0, (byte) 1);
        ItemStack slab2 = new ItemStack(Material.WOOD_STEP, 1, (short) 0, (byte) 2);
        ItemStack slab3 = new ItemStack(Material.WOOD_STEP, 1, (short) 0, (byte) 3);
        ItemStack slab2_0 = new ItemStack(Material.WOOD_STEP, 1, (short) 0, (byte) 4);
        ItemStack slab2_1 = new ItemStack(Material.WOOD_STEP, 1, (short) 0, (byte) 5);
        ItemStack netherSlab = new ItemStack(Material.STEP, 1, (short) 0, (byte) 6);
        ItemStack cobbleSlab = new ItemStack(Material.STEP, 1, (short) 0, (byte) 3);
        ItemStack stoneSlab = new ItemStack(Material.STEP);

        setBuyItem(slab0, 27, new PurchaseItemStack(plugin, slab0, 20), 20);
        setBuyItem(slab1, 28, new PurchaseItemStack(plugin, slab1, 20), 20);
        setBuyItem(slab2, 29, new PurchaseItemStack(plugin, slab2, 20), 20);
        setBuyItem(slab3, 30, new PurchaseItemStack(plugin, slab3, 20), 20);
        setBuyItem(slab2_0, 31, new PurchaseItemStack(plugin, slab2_0, 20), 20);
        setBuyItem(slab2_1, 32, new PurchaseItemStack(plugin, slab2_1, 20), 20);
        setBuyItem(netherSlab, 33, new PurchaseItemStack(plugin, netherSlab, 20), 20);
        setBuyItem(cobbleSlab, 34, new PurchaseItemStack(plugin, cobbleSlab, 20), 20);
        setBuyItem(stoneSlab, 35, new PurchaseItemStack(plugin, stoneSlab, 20), 20);

        // Row 5
        ItemStack fence0 = new ItemStack(Material.FENCE, 1, (short) 0, (byte) 0);
        ItemStack fence1 = new ItemStack(Material.SPRUCE_FENCE, 1, (short) 0, (byte) 1);
        ItemStack fence2 = new ItemStack(Material.BIRCH_FENCE, 1, (short) 0, (byte) 2);
        ItemStack fence3 = new ItemStack(Material.JUNGLE_FENCE, 1, (short) 0, (byte) 3);
        ItemStack fence2_0 = new ItemStack(Material.ACACIA_FENCE);
        ItemStack fence2_1 = new ItemStack(Material.DARK_OAK_FENCE);
        ItemStack netherFence = new ItemStack(Material.NETHER_FENCE);
        ItemStack cobbleWall = new ItemStack(Material.COBBLE_WALL);
        ItemStack mossyWall = new ItemStack(Material.COBBLE_WALL, 1, (short) 0, (byte) 1);

        setBuyItem(fence0, 36, new PurchaseItemStack(plugin, fence0, 20), 20);
        setBuyItem(fence1, 37, new PurchaseItemStack(plugin, fence1, 20), 20);
        setBuyItem(fence2, 38, new PurchaseItemStack(plugin, fence2, 20), 20);
        setBuyItem(fence3, 39, new PurchaseItemStack(plugin, fence3, 20), 20);
        setBuyItem(fence2_0, 40, new PurchaseItemStack(plugin, fence2_0, 20), 20);
        setBuyItem(fence2_1, 41, new PurchaseItemStack(plugin, fence2_1, 20), 20);
        setBuyItem(netherFence, 42, new PurchaseItemStack(plugin, netherFence, 20), 20);
        setBuyItem(cobbleWall, 43, new PurchaseItemStack(plugin, cobbleWall, 20), 20);
        setBuyItem(mossyWall, 44, new PurchaseItemStack(plugin, mossyWall, 20), 20);

        // Row 6
        //ItemStack page1 = new ItemStack(Material.PAPER, 1);
        ItemStack page2 = new ItemStack(Material.PAPER, 2);
        ItemStack page3 = new ItemStack(Material.PAPER, 3);
        ItemStack page4 = new ItemStack(Material.PAPER, 4);
        ItemStack page5 = new ItemStack(Material.PAPER, 5);
        ItemStack page6 = new ItemStack(Material.PAPER, 6);
        ItemStack page7 = new ItemStack(Material.PAPER, 7);
        ItemStack page8 = new ItemStack(Material.PAPER, 8);
        ItemStack exitButton = itemGen.generateItem("exit_button", type);
        ItemStack currentPage = new ItemStack(Material.EMPTY_MAP);

        setPageItem(currentPage, 45, 1);
        setPageItem(page2, 46, RealmShopPage2.class, 2);
        setPageItem(page3, 47, RealmShopPage3.class, 3);
        setPageItem(page4, 48, RealmShopPage4.class, 4);
        setPageItem(page5, 49, RealmShopPage5.class, 5);
        setPageItem(page6, 50, RealmShopPage6.class, 6);
        setPageItem(page7, 51, RealmShopPage7.class, 7);
        setPageItem(page8, 52, RealmShopPage8.class, 8);
        setItem(exitButton, 53, new Exit(plugin));
	}
}

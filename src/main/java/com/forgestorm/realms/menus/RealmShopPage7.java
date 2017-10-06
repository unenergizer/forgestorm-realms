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
public class RealmShopPage7 extends Menu {

    private final SpigotCore plugin;

    public RealmShopPage7(SpigotCore plugin) {
        super(plugin);
        this.plugin = plugin;
        init("Realm Shop: Page 7", 6);
        makeMenuItems();
    }

    @Override
    protected void makeMenuItems() {
        ItemTypes type = ItemTypes.MENU;
        ItemGenerator itemGen = plugin.getItemGen();

        // Row 1
        ItemStack log0 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 0);
        ItemStack log1 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 1);
        ItemStack log2 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 2);
        ItemStack log3 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 3);
        ItemStack log2_0 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 4);
        ItemStack log2_1 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 5);
        ItemStack netherrack = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 6);
        ItemStack stone = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 7);
        ItemStack bedrock = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 8);

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
        ItemStack plank0 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 9);
        ItemStack plank1 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 10);
        ItemStack plank2 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 11);
        ItemStack plank3 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 12);
        ItemStack plank2_0 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 13);
        ItemStack plank2_1 = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 14);
        ItemStack netherBrick = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) 15);
        ItemStack cobble = new ItemStack(Material.GLASS);
        ItemStack mossyCobble = new ItemStack(Material.STAINED_GLASS_PANE);

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
        ItemStack stair0 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 1);
        ItemStack stair1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 2);
        ItemStack stair2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 3);
        ItemStack stair3 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 4);
        ItemStack stair2_0 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 5);
        ItemStack stair2_1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 6);
        ItemStack netherStairs = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
        ItemStack cobbleStairs = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 8);
        ItemStack mossyBrick = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 9);

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
        ItemStack slab0 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 10);
        ItemStack slab1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 11);
        ItemStack slab2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 12);
        ItemStack slab3 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 13);
        ItemStack slab2_0 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 14);
        ItemStack slab2_1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 15);
        ItemStack netherSlab = new ItemStack(Material.THIN_GLASS);

        setBuyItem(slab0, 27, new PurchaseItemStack(plugin, slab0, 20), 20);
        setBuyItem(slab1, 28, new PurchaseItemStack(plugin, slab1, 20), 20);
        setBuyItem(slab2, 29, new PurchaseItemStack(plugin, slab2, 20), 20);
        setBuyItem(slab3, 30, new PurchaseItemStack(plugin, slab3, 20), 20);
        setBuyItem(slab2_0, 31, new PurchaseItemStack(plugin, slab2_0, 20), 20);
        setBuyItem(slab2_1, 32, new PurchaseItemStack(plugin, slab2_1, 20), 20);
        setBuyItem(netherSlab, 33, new PurchaseItemStack(plugin, netherSlab, 20), 20);

        // Row 6
        ItemStack page1 = new ItemStack(Material.PAPER, 1);
        ItemStack page2 = new ItemStack(Material.PAPER, 2);
        ItemStack page3 = new ItemStack(Material.PAPER, 3);
        ItemStack page4 = new ItemStack(Material.PAPER, 4);
        ItemStack page5 = new ItemStack(Material.PAPER, 5);
        ItemStack page6 = new ItemStack(Material.PAPER, 6);
        //ItemStack page7 = new ItemStack(Material.PAPER, 7);
        ItemStack page8 = new ItemStack(Material.PAPER, 8);
        ItemStack exitButton = itemGen.generateItem("exit_button", type);
        ItemStack currentPage = new ItemStack(Material.EMPTY_MAP, 7);

        setPageItem(page1, 45, RealmShopPage1.class, 1);
        setPageItem(page2, 46, RealmShopPage2.class, 2);
        setPageItem(page3, 47, RealmShopPage3.class, 3);
        setPageItem(page4, 48, RealmShopPage4.class, 4);
        setPageItem(page5, 49, RealmShopPage5.class, 5);
        setPageItem(page6, 50, RealmShopPage6.class, 6);
        setPageItem(currentPage, 51, 7);
        setPageItem(page8, 52, RealmShopPage8.class, 8);
        setItem(exitButton, 53, new Exit(plugin));
    }
}

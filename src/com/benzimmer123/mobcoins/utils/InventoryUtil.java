package com.benzimmer123.mobcoins.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

	public static int getInventorySpace(Inventory inv) {
		int space = 0;

		for (ItemStack content : inv.getContents()) {
			if (content == null) {
				space++;
			}
		}

		return space;
	}

	public static int getAmount(Player player, ItemStack item) {
		if (item == null)
			return 0;
		int amount = 0;
		for (int i = 0; i < 36; i++) {
			ItemStack slot = player.getInventory().getItem(i);
			if (slot == null || !slot.isSimilar(item))
				continue;
			amount += slot.getAmount();
		}
		return amount;
	}

}

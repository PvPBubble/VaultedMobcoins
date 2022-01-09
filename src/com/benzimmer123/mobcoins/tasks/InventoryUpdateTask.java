package com.benzimmer123.mobcoins.tasks;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.data.CategoryData;
import com.benzimmer123.mobcoins.utils.ColourUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;

public class InventoryUpdateTask extends BukkitRunnable {

	@Override
	public void run() {
		if (MobCoins.getInstance().isRefreshing())
			return;

		Bukkit.getOnlinePlayers().stream().filter(player -> player.getOpenInventory() != null && player.getOpenInventory().getTitle()
				.equalsIgnoreCase(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString("MainMenu.title")))).forEach(player -> {
					Inventory inventory = player.getOpenInventory().getTopInventory();
					int slot = 0;

					for (ItemStack item : inventory.getContents()) {
						if (item != null) {
							String category = CategoryData.getInstance().getCategoryFromSlot(slot);
							if (category != null) {
								inventory.setItem(slot, ItemUtil.getCategoryItem(category, "MainMenu.Items.CategoryItems." + category));
							}
						}
						slot++;
					}

					player.updateInventory();
				});
	}
}

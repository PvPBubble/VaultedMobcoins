package com.benzimmer123.mobcoins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.data.CategoryData;
import com.benzimmer123.mobcoins.data.PlayerData;
import com.benzimmer123.mobcoins.enums.ClickSound;

public class InventoryClose implements Listener {

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (PlayerData.getInstance().getPlayerPage((Player) e.getPlayer()) != -1) {
			PlayerData.getInstance().removePlayerPage((Player) e.getPlayer());
		}

		if (!MobCoins.getInstance().isRefreshing() && CategoryData.getInstance().getCategoryInventoryNames().contains(e.getView().getTitle())) {
			ClickSound.SECTION_MENU_CLOSE.play(e.getPlayer());

			new BukkitRunnable() {
				public void run() {
					if (e.getPlayer() != null) {
						if (e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING || e.getPlayer().getOpenInventory()
								.getType() == InventoryType.CREATIVE) {
							MobCoins.getInstance().getGUIManager().openGUI((Player) e.getPlayer());
						}
					}
				}
			}.runTaskLater(MobCoins.getInstance(), 2L);
		}
	}

}

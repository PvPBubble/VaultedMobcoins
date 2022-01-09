package com.benzimmer123.mobcoins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.enums.ClickSound;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.utils.ItemUtil;

public class PlayerInteract implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (e.getPlayer().getItemInHand().isSimilar(ItemUtil.getItem(null, "Options.ItemToken", e.getPlayer().getItemInHand().getAmount()))) {
			e.setCancelled(true);
			int amount = e.getPlayer().getItemInHand().getAmount();

			if (MobCoins.getInstance().getConfig().getInt("MaxBalance") != -1) {
				long total = MobCoins.getInstance().getEconomyManager().getBalance(e.getPlayer().getUniqueId()) + amount;
				if (total > MobCoins.getInstance().getConfig().getInt("MaxBalance")) {
					Lang.sendMessage(e.getPlayer(), Lang.MAX_BALANCE.toString().replaceAll("%maxbalance%", MobCoins.getInstance().getConfig().getInt(
							"MaxBalance") + ""));
					return;
				}
			}

			e.getPlayer().getInventory().removeItem(ItemUtil.getItem(null, "Options.ItemToken", amount));
			MobCoins.getInstance().getEconomyManager().addBalance(e.getPlayer().getUniqueId(), amount);
			Lang.sendMessage(e.getPlayer(), Lang.DEPOSITED_MOBCOINS_MESSAGE.toString().replaceAll("%amount%", amount + ""));
			ClickSound.ADD_COINS.play(e.getPlayer());
		}
	}
}
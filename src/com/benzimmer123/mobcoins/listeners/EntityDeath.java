package com.benzimmer123.mobcoins.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.enums.DropType;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.events.MobCoinsDropEvent;
import com.benzimmer123.mobcoins.hooks.Outpost;
import com.benzimmer123.mobcoins.utils.ConfigUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;
import com.benzimmer123.mobcoins.utils.RandomUtil;

public class EntityDeath implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {

		if (ConfigUtil.getDisabledWorlds().contains(e.getEntity().getWorld().getName().toLowerCase()))
			return;

		DamageCause damageCause = e.getEntity().getLastDamageCause().getCause();

		if (!ConfigUtil.isDropEnabled(damageCause))
			return;

		int dropChance = ConfigUtil.getPercentChance(e.getEntityType());

		if (dropChance <= 0)
			return;

		if (!RandomUtil.isDropSuccessful(dropChance))
			return;

		DropType dropType = ConfigUtil.getDropType();

		int minDrops = ConfigUtil.getMinDrops(e.getEntityType());
		int maxDrops = ConfigUtil.getMaxDrops(e.getEntityType());
		int amount = RandomUtil.randInt(minDrops, maxDrops);
		
		MobCoinsDropEvent dropEvent = new MobCoinsDropEvent(e.getEntity().getKiller(), amount);
		Bukkit.getServer().getPluginManager().callEvent(dropEvent);
		
		if (MobCoins.getInstance().isOutpostHooked()) {
			if (e.getEntity().getKiller() != null && e.getEntity().getKiller().getType() == EntityType.PLAYER) {
				amount = new Outpost().getModifiedAmount(e.getEntity().getKiller(), amount);
			}
		}

		if (dropType.equals(DropType.PHYSICAL)) {
			e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), ItemUtil.getItem(null, "Options.ItemToken", amount));
			if (e.getEntity().getKiller() == null || e.getEntity().getKiller().getType() != EntityType.PLAYER)
				return;
			Lang.sendMessage(e.getEntity().getKiller(), Lang.RECEIVED_MOBCOIN_FROM_MOB_MESSAGE.toString().replaceAll("%amount%", amount + ""));
		} else if (e.getEntity().getKiller() != null) {
			if (e.getEntity().getKiller() == null || e.getEntity().getKiller().getType() != EntityType.PLAYER)
				return;
			Lang.sendMessage(e.getEntity().getKiller(), Lang.RECEIVED_MOBCOIN_FROM_MOB_MESSAGE.toString().replaceAll("%amount%", amount + ""));
			MobCoins.getInstance().getEconomyManager().addBalance(e.getEntity().getKiller().getUniqueId(), amount);
		}
	}
}
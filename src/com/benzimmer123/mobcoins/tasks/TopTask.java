package com.benzimmer123.mobcoins.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.benzimmer123.mobcoins.MobCoins;

public class TopTask extends BukkitRunnable {

	@Override
	public void run() {
		MobCoins.getInstance().getTopManager().sortEconomyList();
	}

}

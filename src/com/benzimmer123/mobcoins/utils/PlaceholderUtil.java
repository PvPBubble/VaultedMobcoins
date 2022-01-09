package com.benzimmer123.mobcoins.utils;

import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;

public class PlaceholderUtil {

	public static String getFormattedBalance(Player player) {
		double money = MobCoins.getInstance().getEconomyManager().getBalance(player.getUniqueId());

		if (money > 1000000000) {
			return String.valueOf((double) Math.round(money / 1000000) / 1000) + "B";
		} else if (money > 1000000) {
			return String.valueOf((double) Math.round(money / 10000) / 100) + "M";
		} else if (money > 1000) {
			return String.valueOf((double) Math.round(money / 100) / 10) + "k";
		}

		return money + "";
	}

}

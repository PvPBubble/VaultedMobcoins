package com.benzimmer123.mobcoins.placeholders;

import java.text.NumberFormat;

import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.utils.PlaceholderUtil;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ClipPlaceholderAPI extends PlaceholderExpansion {

	// Identifier for this expansion
	@Override
	public String getIdentifier() {
		return "vaultedcoins";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String getAuthor() {
		return "Benzimmer";
	}

	// Since we are registering this expansion from the dependency, this can be
	// null
	@Override
	public String getPlugin() {
		return null;
	}

	// Return the plugin version since this expansion is bundled with the
	// dependency
	@Override
	public String getVersion() {
		return MobCoins.getInstance().getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, String placeholder) {
		if (placeholder == null) {
			return "";
		}

		if (player == null)
			return placeholder;

		switch (placeholder) {
		case "balance_formatted":
			return PlaceholderUtil.getFormattedBalance(player);
		case "balance":
			long balance = MobCoins.getInstance().getEconomyManager().getBalance(player.getUniqueId());
			String s = NumberFormat.getIntegerInstance().format(balance);
			return s;
		}
		return null;
	}
}
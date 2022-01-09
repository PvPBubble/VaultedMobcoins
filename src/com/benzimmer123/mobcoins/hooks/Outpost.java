package com.benzimmer123.mobcoins.hooks;

import org.bukkit.entity.Player;

import com.benzimmer123.outpost.api.objects.OutpostArena;

public class Outpost {

	public int getModifiedAmount(Player player, int amount) {
		for (OutpostArena outpost : com.benzimmer123.outpost.Outpost.getInstance().getOutpostManager().getOwnedOutposts(player)) {
			if (outpost.getMultiplier("mobcoins").getModifier() == 1.0)
				return amount;

			int tokens = (int) Math.ceil((amount * outpost.getMultiplier("mobcoins").getModifier()));
			return tokens;
		}
		return amount;
	}
	
	public long getModifiedPrice(Player player, long amount) {
		for (OutpostArena outpost : com.benzimmer123.outpost.Outpost.getInstance().getOutpostManager().getOwnedOutposts(player)) {
			if (outpost.getMultiplier("pricedrop").getModifier() == 1.0)
				return amount;

			long tokens = (long) Math.ceil((amount * outpost.getMultiplier("pricedrop").getModifier()));
			return tokens;
		}
		return amount;
	}

}

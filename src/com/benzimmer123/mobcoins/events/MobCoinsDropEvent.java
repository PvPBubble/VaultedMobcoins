package com.benzimmer123.mobcoins.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MobCoinsDropEvent extends Event {

	private int amount;
	private Player player;

	public MobCoinsDropEvent(Player player, int amount) {
		this.amount = amount;
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public int getAmount() {
		return amount;
	}
	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}

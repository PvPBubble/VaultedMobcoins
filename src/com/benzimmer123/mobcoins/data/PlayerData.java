package com.benzimmer123.mobcoins.data;

import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

public class PlayerData {

	private final static PlayerData INSTANCE;
	private Map<Player, Integer> playerPages;

	static {
		INSTANCE = new PlayerData();
	}

	public PlayerData() {
		playerPages = Maps.newLinkedHashMap();
	}

	public int getPlayerPage(Player player) {
		if (playerPages.containsKey(player)) {
			return playerPages.get(player);
		}
		return -1;
	}
	
	public void addPlayerPage(Player player, int page) {
		playerPages.put(player, page);
	}

	public void removePlayerPage(Player player) {
		playerPages.remove(player);
	}

	public static PlayerData getInstance() {
		return INSTANCE;
	}
}

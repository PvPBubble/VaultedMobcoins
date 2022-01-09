package com.benzimmer123.mobcoins.data;

import java.util.LinkedHashMap;
import java.util.UUID;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.obj.TopPlayer;
import com.google.common.collect.Maps;

public class TopData {

	private final static TopData INSTANCE;
	private LinkedHashMap<String, TopPlayer> topPlayers;

	static {
		INSTANCE = new TopData();
	}

	public TopData() {
		topPlayers = Maps.newLinkedHashMap();

		for (String uuid : MobCoins.getInstance().getSettingsManager().getEconomy().getConfigurationSection("").getKeys(false)) {
			addTopPlayer(new TopPlayer(uuid, MobCoins.getInstance().getSettingsManager().getEconomy().getInt(uuid)));
		}
	}
	
	public void addTopPlayer(TopPlayer player) {
		topPlayers.put(player.getUUID().toString(), player);
	}

	public void setTopPlayers(LinkedHashMap<String, TopPlayer> result) {
		this.topPlayers = result;
	}
	
	public TopPlayer getPlayer(UUID uuid) {
		if (topPlayers.containsKey(uuid.toString())) {
			return topPlayers.get(uuid.toString());
		}
		return new TopPlayer(uuid.toString(), 0);
	}

	public LinkedHashMap<String, TopPlayer> getPlayers() {
		return topPlayers;
	}

	public static TopData getInstance() {
		return INSTANCE;
	}
}

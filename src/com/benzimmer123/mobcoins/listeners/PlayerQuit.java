package com.benzimmer123.mobcoins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.benzimmer123.mobcoins.data.PlayerData;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (PlayerData.getInstance().getPlayerPage((Player) e.getPlayer()) != -1) {
			PlayerData.getInstance().removePlayerPage((Player) e.getPlayer());
		}
	}

}

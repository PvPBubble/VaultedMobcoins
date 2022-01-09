package com.benzimmer123.mobcoins.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.benzimmer123.mobcoins.placeholders.ClipPlaceholderAPI;

public class PlaceholderManager {

	private boolean clipPlaceholderAPIManager;

	public void setupPlaceholderAPI() {
		Plugin clip = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI");
		if (clip != null) {
			clipPlaceholderAPIManager = true;
			new ClipPlaceholderAPI().register();
		}
	}

	public String parsePlaceholders(Player player, String line) {
		line = line.replace("&", "clr");

		if (isClipPlaceholderAPIHooked()) {
			line = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, line);
		}

		return line.replace("clr", "&");
	}

	public boolean isClipPlaceholderAPIHooked() {
		return clipPlaceholderAPIManager;
	}

}

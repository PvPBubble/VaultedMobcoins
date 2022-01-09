package com.benzimmer123.mobcoins;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.benzimmer123.mobcoins.cmds.rootcommand.RootCommand;
import com.benzimmer123.mobcoins.listeners.EntityDeath;
import com.benzimmer123.mobcoins.listeners.InventoryClick;
import com.benzimmer123.mobcoins.listeners.InventoryClose;
import com.benzimmer123.mobcoins.listeners.PlayerInteract;
import com.benzimmer123.mobcoins.listeners.PlayerQuit;
import com.benzimmer123.mobcoins.managers.EconomyManager;
import com.benzimmer123.mobcoins.managers.GUIManager;
import com.benzimmer123.mobcoins.managers.PlaceholderManager;
import com.benzimmer123.mobcoins.managers.SettingsManager;
import com.benzimmer123.mobcoins.managers.TopManager;
import com.benzimmer123.mobcoins.tasks.InventoryUpdateTask;
import com.benzimmer123.mobcoins.tasks.RefreshTask;
import com.benzimmer123.mobcoins.tasks.TopTask;
import com.benzimmer123.mobcoins.utils.LicenseValidation;
import com.benzimmer123.outpost.Outpost;

public class MobCoins extends JavaPlugin {

	private static MobCoins INSTANCE;
	private SettingsManager settingsManager;
	private EconomyManager economyManager;
	private PlaceholderManager placeholderManager;
	private GUIManager guiManager;
	private TopManager topManager;
	private boolean outpostHooked;
	private boolean refreshingCategories;

	@Override
	public void onEnable() {
		INSTANCE = this;

		saveDefaultConfig();
		settingsManager = new SettingsManager();
		getSettingsManager().setup(this);

		if (getConfig().getString("license") == null || getConfig().getString("license").equalsIgnoreCase("XXXX-XXXX-XXXX-XXXX")) {
			this.getServer().getPluginManager().disablePlugin(this);
			System.out.print("[VaultedMobCoins] \"license\" option in config is either null or default.");
			System.out.print("[VaultedMobCoins] Therefore the plugin will be disabled.");
		}
		if (!new LicenseValidation(getConfig().getString("license"), "https://vaulted.cc/license/verify.php", this).register()) {
			return;
		}

		economyManager = new EconomyManager();
		guiManager = new GUIManager();
		topManager = new TopManager();
		placeholderManager = new PlaceholderManager();

		getServer().getPluginManager().registerEvents(new EntityDeath(), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		getServer().getPluginManager().registerEvents(new InventoryClose(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);

		if (Bukkit.getPluginManager().isPluginEnabled("Outpost")) {
			for (String outpost : getConfig().getConfigurationSection("outpost_multipliers.mobdrops").getKeys(false)) {
				if (Outpost.getInstance().getOutpostManager().getOutpostFromString(outpost) == null)
					continue;

				double multiplier = getConfig().getDouble("outpost_multipliers.mobdrops." + outpost);
				Outpost.getInstance().getOutpostManager().getOutpostFromString(outpost).addMultiplier("mobcoins", multiplier);
			}
			for (String outpost : getConfig().getConfigurationSection("outpost_multipliers.pricedrop").getKeys(false)) {
				if (Outpost.getInstance().getOutpostManager().getOutpostFromString(outpost) == null)
					continue;

				double multiplier = getConfig().getDouble("outpost_multipliers.pricedrop." + outpost);
				Outpost.getInstance().getOutpostManager().getOutpostFromString(outpost).addMultiplier("pricedrop", multiplier);
			}
			System.out.print("[VaultedMobCoins] Successfully hooked into Outpost.");
			outpostHooked = true;
		}

		RootCommand rootCmd = new RootCommand(this);
		getCommand("mobcoins").setExecutor(rootCmd);
		getCommand("coins").setExecutor(rootCmd);
		getCommand("transfer").setExecutor(rootCmd);

		getPlaceholderManager().setupPlaceholderAPI();

		TopTask topTask = new TopTask();
		topTask.runTaskTimerAsynchronously(this, 0, getConfig().getInt("Top_sort_delay") * 20);

		InventoryUpdateTask inventoryTask = new InventoryUpdateTask();
		inventoryTask.runTaskTimer(this, 20, 20);

		RefreshTask refreshTask = new RefreshTask();
		refreshTask.runTaskTimer(this, 0, getConfig().getInt("Category_refresh_task") * 20);
	}

	@Override
	public FileConfiguration getConfig() {
		return getSettingsManager().getConfig();
	}

	public boolean isRefreshing() {
		return refreshingCategories;
	}

	public void setRefreshing(boolean refreshingCategories) {
		this.refreshingCategories = refreshingCategories;
	}

	public boolean isOutpostHooked() {
		return outpostHooked;
	}

	public TopManager getTopManager() {
		return topManager;
	}

	public GUIManager getGUIManager() {
		return guiManager;
	}

	public PlaceholderManager getPlaceholderManager() {
		return placeholderManager;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public EconomyManager getEconomyManager() {
		return economyManager;
	}

	public static MobCoins getInstance() {
		return INSTANCE;
	}

}
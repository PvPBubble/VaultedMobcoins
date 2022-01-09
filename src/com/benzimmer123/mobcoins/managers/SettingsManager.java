package com.benzimmer123.mobcoins.managers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.benzimmer123.mobcoins.enums.Lang;
import com.google.common.io.Files;

public class SettingsManager {

	public void setup(Plugin p) {
		setupConfig(p);
		setupLang(p);
		setupEconomy(p);
		setValues();
	}

	private FileConfiguration config;
	private File configFile;

	public void setupConfig(Plugin p) {
		config = new YamlConfiguration();
		configFile = new File(p.getDataFolder() + File.separator + "config.yml");
		try {
			config.loadFromString(Files.toString(configFile, Charset.forName("UTF-8")));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save config to " + config, ex);
		}
	}

	private FileConfiguration economy;
	private File economyFile;

	private void setupEconomy(Plugin p) {
		economyFile = new File(p.getDataFolder(), "economy.yml");

		if (!economyFile.exists()) {
			try {
				economyFile.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		economy = YamlConfiguration.loadConfiguration(economyFile);
	}

	public FileConfiguration getEconomy() {
		return economy;
	}

	public void saveEconomy() {
		try {
			getEconomy().save(economyFile);
		} catch (IOException ex) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save config to " + economyFile, ex);
		}
	}

	private FileConfiguration lang;
	private File langFile;

	public void setupLang(Plugin p) {
		langFile = new File(p.getDataFolder(), "lang.yml");

		if (!langFile.exists()) {
			try {
				langFile.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		lang = YamlConfiguration.loadConfiguration(langFile);
	}

	public FileConfiguration getLang() {
		return lang;
	}

	private void saveLang() {
		try {
			getLang().save(langFile);
		} catch (IOException ex) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save config to " + langFile, ex);
		}
	}

	private void setValues() {
		for (Lang lang : Lang.values()) {
			if (!getLang().isSet(lang.getPath())) {
				getLang().set(lang.getPath(), lang.getDefault());
			}
		}

		saveLang();
	}
}

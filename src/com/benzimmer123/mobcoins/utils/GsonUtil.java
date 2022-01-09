package com.benzimmer123.mobcoins.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.benzimmer123.mobcoins.MobCoins;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonUtil {

	public static void serializeTimes(Map<String, Long> refreshTimes) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = gson.toJson(refreshTimes);
		File dataFile = new File(MobCoins.getInstance().getDataFolder(), "times.json");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(dataFile);
		fileConfig.set("times", jsonString);

		try {
			fileConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static Map<String, Long> deserializeTimes() {
		Type list = new TypeToken<Map<String, Long>>() {
		}.getType();
		File dataFile = new File(MobCoins.getInstance().getDataFolder(), "times.json");
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(dataFile);
		String jsonString = fileConfig.getString("times");
		Gson gson = new Gson();
		Map<String, Long> dataObj = gson.fromJson(jsonString, list);
		return dataObj;
	}

	public static void serializeItems(Map<String, List<String>> activeItems) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonString = gson.toJson(activeItems);
		File dataFile = new File(MobCoins.getInstance().getDataFolder(), "items.json");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(dataFile);
		fileConfig.set("items", jsonString);

		try {
			fileConfig.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static Map<String, List<String>> deserializeItems() {
		Type list = new TypeToken<Map<String, List<String>>>() {
		}.getType();
		File dataFile = new File(MobCoins.getInstance().getDataFolder(), "items.json");
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(dataFile);
		String jsonString = fileConfig.getString("items");
		Gson gson = new Gson();
		Map<String, List<String>> dataObj = gson.fromJson(jsonString, list);
		return dataObj;
	}

}

package com.benzimmer123.mobcoins.utils;

import java.util.List;
import java.util.Random;

import com.benzimmer123.mobcoins.MobCoins;
import com.google.common.collect.Lists;

public class RandomUtil {

	private static final Random random = new Random();

	public static boolean isDropSuccessful(int percent) {
		return random.nextInt(100) <= percent;
	}

	public static int randInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public static List<String> getRandomItems(String category) {
		List<String> itemNumbers = Lists.newArrayList();
		for (String itemNumber : MobCoins.getInstance().getConfig().getConfigurationSection("Categories." + category + ".Items").getKeys(false)) {
			itemNumbers.add(itemNumber);
		}
		if (itemNumbers.isEmpty())
			return Lists.newArrayList();
		List<String> activeItems = Lists.newArrayList();
		int totalSize = MobCoins.getInstance().getGUIManager().getActiveSlots(category).size();
		for (int i = 0; i < totalSize; i++) {
			if (itemNumbers.isEmpty())
				return activeItems;
			int itemNumber = random.nextInt(itemNumbers.size());
			String randomItem = itemNumbers.get(itemNumber);
			activeItems.add(randomItem);
			if (!MobCoins.getInstance().getConfig().getBoolean("DuplicateItems"))
				itemNumbers.remove(itemNumber);
		}
		return activeItems;
	}

}

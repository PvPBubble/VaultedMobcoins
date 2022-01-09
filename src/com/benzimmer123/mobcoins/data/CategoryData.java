package com.benzimmer123.mobcoins.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.utils.ColourUtil;
import com.benzimmer123.mobcoins.utils.GsonUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;
import com.benzimmer123.mobcoins.utils.RandomUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CategoryData {

	private final static CategoryData INSTANCE;
	private LinkedList<String> categoryNames;
	private Map<String, String> itemNumbers;
	private Map<ItemStack, String> categorySwitchItems;
	private Map<String, String> categoryItemNames;
	private Map<Integer, String> categoryItemSlots;
	private Map<String, List<String>> activeItems;
	private Map<String, Long> categoryRefreshTimes;
	private Map<String, String> categoryInventoryNames;

	static {
		INSTANCE = new CategoryData();
	}

	public CategoryData() {
		categoryNames = Lists.newLinkedList();
		categoryItemNames = Maps.newHashMap();
		categoryInventoryNames = Maps.newHashMap();
		categoryItemSlots = Maps.newHashMap();
		itemNumbers = Maps.newHashMap();
		categorySwitchItems = Maps.newHashMap();

		for (String category : MobCoins.getInstance().getConfig().getConfigurationSection("Switch").getKeys(false)) {
			categorySwitchItems.put(ItemUtil.getItem(null, "Switch." + category, 1), category);
		}

		boolean addCategories = false;

		if (new File(MobCoins.getInstance().getDataFolder(), "times.json").exists() && new File(MobCoins.getInstance().getDataFolder(), "items.json")
				.exists()) {
			categoryRefreshTimes = GsonUtil.deserializeTimes();
			activeItems = GsonUtil.deserializeItems();
		} else {
			activeItems = Maps.newHashMap();
			categoryRefreshTimes = Maps.newHashMap();
			addCategories = true;
		}

		for (String category : MobCoins.getInstance().getConfig().getConfigurationSection("Categories").getKeys(false)) {
			if (addCategories) {
				refreshCategory(category, false);
			}
			categoryNames.add(category);
			categoryItemNames.put(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString("MainMenu.Items.CategoryItems." + category
					+ ".name")), category);
			categoryItemSlots.put(MobCoins.getInstance().getConfig().getInt("MainMenu.Items.CategoryItems." + category + ".slot"), category);
			categoryInventoryNames.put(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString("Categories." + category + ".title")),
					category);
		}

		for (String category : MobCoins.getInstance().getConfig().getConfigurationSection("Categories").getKeys(false)) {
			for (String item : MobCoins.getInstance().getConfig().getConfigurationSection("Categories." + category + ".Items").getKeys(false)) {
				itemNumbers.put(ItemUtil.getItem(null, "Categories." + category + ".Items." + item, 1).getItemMeta().getDisplayName(), item);
			}
		}
	}

	public LinkedList<String> getCategoryNames() {
		return categoryNames;
	}

	public String getItemNumber(String displayName) {
		if (itemNumbers.containsKey(displayName)) {
			return itemNumbers.get(displayName);
		}
		return null;
	}

	public String getCategorySwitch(ItemStack item) {
		if (categorySwitchItems.containsKey(item)) {
			return categorySwitchItems.get(item);
		}
		return null;
	}

	public boolean doesCategoryExist(String categoryName) {
		return activeItems.containsKey(categoryName);
	}

	public void refreshCategory(String category, boolean silent) {
		MobCoins.getInstance().setRefreshing(true);

		if (MobCoins.getInstance().getConfig().getInt("Categories." + category + ".interval") != -1) {
			long newEndTime = System.currentTimeMillis();
			long secondsAdded = MobCoins.getInstance().getConfig().getInt("Categories." + category + ".interval") * 1000;
			categoryRefreshTimes.put(category, newEndTime + secondsAdded);
		} else {
			categoryRefreshTimes.put(category, (long) -1);
		}

		activeItems.put(category, RandomUtil.getRandomItems(category));

		Bukkit.getOnlinePlayers().stream().filter(player -> player.getOpenInventory() != null && (getCategoryInventoryNames().contains(player
				.getOpenInventory().getTitle()) || player.getOpenInventory().getTitle().equalsIgnoreCase(ColourUtil.replaceString(MobCoins
						.getInstance().getConfig().getString("MainMenu.title"))))).forEach(player -> {
							player.closeInventory();
						});

		if (!silent)
			Bukkit.broadcastMessage(Lang.CATEGORY_REFRESHED_BROADCAST.toString().replaceAll("%category%", StringUtils.capitalize(category)));

		MobCoins.getInstance().setRefreshing(false);
		GsonUtil.serializeTimes(categoryRefreshTimes);
		GsonUtil.serializeItems(activeItems);
	}

	public String getCategoryFromSlot(int slot) {
		if (categoryItemSlots.containsKey(slot)) {
			return categoryItemSlots.get(slot);
		}
		return null;
	}

	public Set<String> getCategoryInventoryNames() {
		return categoryInventoryNames.keySet();
	}

	public String getCategoryFromInventoryName(String invName) {
		if (categoryInventoryNames.containsKey(invName)) {
			return categoryInventoryNames.get(invName);
		}
		return null;
	}

	public List<String> getActiveItems(String category) {
		if (activeItems.containsKey(category)) {
			return activeItems.get(category);
		}
		return Lists.newArrayList();
	}

	public String getCategory(String itemName) {
		if (categoryItemNames.containsKey(itemName)) {
			return categoryItemNames.get(itemName);
		}
		return null;
	}

	public Map<String, Long> getCategoryEndTimes() {
		return categoryRefreshTimes;
	}

	public long getEndTime(String category) {
		if (categoryRefreshTimes.containsKey(category)) {
			return categoryRefreshTimes.get(category);
		}
		if (MobCoins.getInstance().getConfig().getInt("Categories." + category + ".interval") == -1)
			return -1;
		long newEndTime = System.currentTimeMillis();
		long secondsAdded = MobCoins.getInstance().getConfig().getInt("Categories." + category + ".interval") * 1000;
		categoryRefreshTimes.put(category, newEndTime + secondsAdded);
		return newEndTime + secondsAdded;
	}

	public Map<String, String> getCategoryItems() {
		return categoryItemNames;
	}

	public static CategoryData getInstance() {
		return INSTANCE;
	}
}
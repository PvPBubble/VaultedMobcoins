package com.benzimmer123.mobcoins.managers;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.data.CategoryData;
import com.benzimmer123.mobcoins.data.PlayerData;
import com.benzimmer123.mobcoins.enums.ClickSound;
import com.benzimmer123.mobcoins.utils.ColourUtil;
import com.benzimmer123.mobcoins.utils.ConfigUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GUIManager {

	public void openGUI(Player player) {
		Inventory inventory = Bukkit.createInventory(null, MobCoins.getInstance().getConfig().getInt("MainMenu.size"), ColourUtil.replaceString(
				MobCoins.getInstance().getConfig().getString("MainMenu.title")));

		int infoSlot = MobCoins.getInstance().getConfig().getInt("MainMenu.Items.Info.slot");
		ItemStack infoItem = ItemUtil.getItem(player, "MainMenu.Items.Info", 1);

		inventory.setItem(infoSlot, infoItem);

		int balanceSlot = MobCoins.getInstance().getConfig().getInt("MainMenu.Items.Balance.slot");
		long balance = MobCoins.getInstance().getEconomyManager().getBalance(player.getUniqueId());
		ItemStack balanceItem = ItemUtil.getBalanceItem(balance);

		inventory.setItem(balanceSlot, balanceItem);

		Map<Integer, ItemStack> placeholderItems = ItemUtil.getPlaceholderItems(player, "MainMenu.Items.placeholders");

		for (int slot : placeholderItems.keySet()) {
			if (slot < inventory.getSize())
				inventory.setItem(slot, placeholderItems.get(slot));
		}

		for (String category : MobCoins.getInstance().getConfig().getConfigurationSection("MainMenu.Items.CategoryItems").getKeys(false)) {
			int slot = MobCoins.getInstance().getConfig().getInt("MainMenu.Items.CategoryItems." + category + ".slot");
			if (slot < inventory.getSize())
				inventory.setItem(slot, ItemUtil.getCategoryItem(category, "MainMenu.Items.CategoryItems." + category));
		}

		player.openInventory(inventory);

		ClickSound.MAIN_MENU_OPEN.play(player);
	}

	public void openCategory(Player player, String category, int page) {
		Inventory inventory = Bukkit.createInventory(player, MobCoins.getInstance().getConfig().getInt("Categories." + category + ".size"), ColourUtil
				.replaceString(MobCoins.getInstance().getConfig().getString("Categories." + category + ".title")));

		Map<Integer, ItemStack> placeholderItems = ItemUtil.getPlaceholderItems(player, "Categories." + category + ".placeholders");

		for (int slot : placeholderItems.keySet()) {
			inventory.setItem(slot, placeholderItems.get(slot));
		}

		Map<Integer, ItemStack> categorySwitchItems = ItemUtil.getCategorySwitchItems();

		for (int slot : categorySwitchItems.keySet()) {
			inventory.setItem(slot, categorySwitchItems.get(slot));
		}

		ItemStack backItem = ItemUtil.getItem(player, "BackButton", 1);
		List<Integer> backSlots = MobCoins.getInstance().getConfig().getIntegerList("Categories." + category + ".BackSlots");

		for (int slot : backSlots) {
			if (slot < inventory.getSize())
				inventory.setItem(slot, backItem);
		}

		ItemStack nextPage = ItemUtil.getItem(player, "Pages.NextPage", 1);
		ItemStack previousPage = ItemUtil.getItem(player, "Pages.PreviousPage", 1);

		List<Integer> nextPageSlots = MobCoins.getInstance().getConfig().getIntegerList("Categories." + category + ".PageSlots.Next");
		List<Integer> previousPageSlots = MobCoins.getInstance().getConfig().getIntegerList("Categories." + category + ".PageSlots.Previous");

		for (int slot : nextPageSlots) {
			if (slot < inventory.getSize())
				inventory.setItem(slot, nextPage);
		}

		for (int slot : previousPageSlots) {
			if (slot < inventory.getSize())
				inventory.setItem(slot, previousPage);
		}

		List<Integer> activeSlots = getActiveSlots(category, page);
		int previousAmount = ConfigUtil.getPreviousPageItemsAmount(category, page);
		List<ItemStack> activeItems = ItemUtil.getAllRandomItems(player, category, CategoryData.getInstance().getActiveItems(category));
		int size = activeItems.size();
		activeItems = activeItems.subList(previousAmount, size);
		int itemNumber = 0;

		for (int slot : activeSlots) {
			if (slot < inventory.getSize() && activeItems.size() > itemNumber) {
				inventory.setItem(slot, activeItems.get(itemNumber));
				itemNumber++;
			}
		}

		player.openInventory(inventory);
		PlayerData.getInstance().addPlayerPage(player, page);
	}

	public List<Integer> getActiveSlots(String category, int page) {
		return MobCoins.getInstance().getConfig().getIntegerList("Categories." + category + ".slots.pages." + page);
	}

	public List<Integer> getActiveSlots(String category) {
		List<Integer> activeSlots = Lists.newArrayList();

		for (String page : MobCoins.getInstance().getConfig().getConfigurationSection("Categories." + category + ".slots.pages").getKeys(false)) {
			activeSlots.addAll(MobCoins.getInstance().getConfig().getIntegerList("Categories." + category + ".slots.pages." + page));
		}

		return activeSlots;
	}

	public Map<Integer, ItemStack> getCategoryItems() {
		Map<Integer, ItemStack> itemsList = Maps.newHashMap();
		for (String category : MobCoins.getInstance().getConfig().getConfigurationSection("MainMenu.Items.CategoryItems").getKeys(false)) {
			itemsList.put(MobCoins.getInstance().getConfig().getInt("MainMenu.Items.CategoryItems." + category + ".slot"), ItemUtil.getCategoryItem(
					category, "MainMenu.Items.CategoryItems." + category));
		}
		return itemsList;
	}

}
package com.benzimmer123.mobcoins.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.compatible.XMaterial;
import com.benzimmer123.mobcoins.data.CategoryData;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.hooks.Outpost;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ItemUtil {

	@SuppressWarnings("deprecation")
	public static ItemStack getItem(Player player, String path, int amount) {
		ItemStack item;

		if (CustomHeadUtil.isSkull(MobCoins.getInstance().getConfig().getString(path + ".material"))) {
			item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
		} else if (MobCoins.getInstance().getConfig().isSet(path + ".data")) {
			item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString(path + ".material")).get().parseMaterial(),
					amount, (short) MobCoins.getInstance().getConfig().getInt(path + ".data"));
		} else {
			item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString(path + ".material")).get().parseMaterial(),
					amount);
		}

		String balance = "";

		if (player != null) {
			balance = PlaceholderUtil.getFormattedBalance(player);
		}

		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString(path + ".name").replaceAll("%mobcoins_balance%",
				balance)));

		if (MobCoins.getInstance().getConfig().isSet(path + ".lore")) {
			List<String> lore = MobCoins.getInstance().getConfig().getStringList(path + ".lore");
			if (!lore.isEmpty()) {
				List<String> replacedLores = Lists.newArrayList();
				for (String s : lore) {
					s = ColourUtil.replaceString(s.replaceAll("%mobcoins_balance%", balance));
					s = MobCoins.getInstance().getPlaceholderManager().parsePlaceholders(player, s);
					replacedLores.add(s);
				}
				itemMeta.setLore(replacedLores);
			}
		}

		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		item.setItemMeta(itemMeta);

		if (MobCoins.getInstance().getConfig().isSet(path + ".enchanted") && MobCoins.getInstance().getConfig().getBoolean(path + ".enchanted")) {
			itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		}

		item = CustomHeadUtil.getCustomHead(item, MobCoins.getInstance().getConfig().getString(path + ".skullOwner"));
		return item;
	}

	public static ItemStack getBalanceItem(long balance) {
		ItemStack item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString("MainMenu.Items.Balance.material")).get()
				.parseMaterial(), 1);
		ItemMeta itemMeta = item.getItemMeta();
		String balanceFormatted = NumberFormat.getIntegerInstance().format(balance);
		itemMeta.setDisplayName(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString("MainMenu.Items.Balance.name").replaceAll(
				"%mobcoins_balance%", balanceFormatted + "")));
		List<String> lore = MobCoins.getInstance().getConfig().getStringList("MainMenu.Items.Balance.lore");
		if (!lore.isEmpty()) {
			List<String> replacedLores = Lists.newArrayList();
			for (String s : lore) {
				replacedLores.add(ColourUtil.replaceString(s.replaceAll("%mobcoins_balance%", balanceFormatted + "")));
			}
			itemMeta.setLore(replacedLores);
		}
		itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		item.setItemMeta(itemMeta);
		if (MobCoins.getInstance().getConfig().getBoolean("MainMenu.Items.Balance.enchanted")) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getSwitchCategoryItem(String category, String path) {
		ItemStack item;

		if (MobCoins.getInstance().getConfig().isSet(path + ".data")) {
			item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString(path + ".material")).get().parseMaterial(), 1,
					(short) MobCoins.getInstance().getConfig().getInt(path + ".data"));
		} else {
			item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString(path + ".material")).get().parseMaterial(), 1);
		}

		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString(path + ".name").replaceAll("%category%",
				category)));
		List<String> lore = MobCoins.getInstance().getConfig().getStringList(path + ".lore");
		if (!lore.isEmpty()) {
			List<String> replacedLores = Lists.newArrayList();
			for (String s : lore) {
				replacedLores.add(ColourUtil.replaceString(s.replaceAll("%category%", category)));
			}
			itemMeta.setLore(replacedLores);
		}
		itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		item.setItemMeta(itemMeta);
		if (MobCoins.getInstance().getConfig().getBoolean(path + ".enchanted")) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		return item;
	}

	public static List<ItemStack> getAllRandomItems(Player player, String category, List<String> itemNumbers) {
		List<ItemStack> itemsList = Lists.newArrayList();
		for (String itemNumber : itemNumbers) {
			ItemStack item = getItem(player, "Categories." + category + ".Items." + itemNumber, MobCoins.getInstance().getConfig().getInt(
					"Categories." + category + ".Items." + itemNumber + ".amount"));
			long price = MobCoins.getInstance().getConfig().getInt("Categories." + category + ".Items." + itemNumber + ".price");
			String s = NumberFormat.getIntegerInstance().format(price);
			String pricePlaceholder = Lang.PRICE_PLACEHOLDER.toString().replaceAll("%price%", s + "");

			if (MobCoins.getInstance().isOutpostHooked()) {
				long originalPrice = price;
				price = new Outpost().getModifiedPrice(player, price);
				String s2 = NumberFormat.getIntegerInstance().format(originalPrice);
				String s3 = NumberFormat.getIntegerInstance().format(price);
				if (price != originalPrice) {
					pricePlaceholder = Lang.PRICE_DISCOUNT_PLACEHOLDER.toString().replaceAll("%originalprice%", s2).replaceAll("%price%", s3);
				}
			}

			ItemMeta itemMeta = item.getItemMeta();
			List<String> replacedLores = Lists.newArrayList();
			for (String lore : itemMeta.getLore()) {
				replacedLores.add(lore.replace("%price%", pricePlaceholder));
			}
			itemMeta.setLore(replacedLores);
			item.setItemMeta(itemMeta);
			itemsList.add(item);
		}
		return itemsList;
	}

	public static Map<Integer, ItemStack> getPlaceholderItems(Player player, String path) {
		Map<Integer, ItemStack> placeholderItems = Maps.newHashMap();

		for (String itemName : MobCoins.getInstance().getConfig().getConfigurationSection(path).getKeys(false)) {
			ItemStack item = getItem(player, path + "." + itemName, 1);
			List<Integer> slots = MobCoins.getInstance().getConfig().getIntegerList(path + "." + itemName + ".slots");
			for (int slot : slots) {
				placeholderItems.put(slot, item);
			}
		}

		return placeholderItems;
	}

	public static ItemStack getCategoryItem(String category, String path) {
		return getCategoryItem(category, path, CategoryData.getInstance().getEndTime(category));
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getCategoryItem(String category, String path, long endTime) {
		ItemStack item;

		if (CustomHeadUtil.isSkull(MobCoins.getInstance().getConfig().getString(path + ".material"))) {
			item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
		} else if (MobCoins.getInstance().getConfig().isSet(path + ".data")) {
			item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString(path + ".material")).get().parseMaterial(), 1,
					(short) MobCoins.getInstance().getConfig().getInt(path + ".data"));
		} else {
			item = new ItemStack(XMaterial.matchXMaterial(MobCoins.getInstance().getConfig().getString(path + ".material")).get().parseMaterial(), 1);
		}

		ItemMeta itemMeta = item.getItemMeta();

		String displayEndTime;

		if (endTime == -1) {
			displayEndTime = Lang.STATIC_PLACEHOLDER.toString();
		} else {
			displayEndTime = TimeUtil.getTimeAsString(endTime);
		}

		itemMeta.setDisplayName(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString(path + ".name").replaceAll("%category%",
				StringUtils.capitalize(category))));
		List<String> lore = MobCoins.getInstance().getConfig().getStringList(path + ".lore");
		if (!lore.isEmpty()) {
			List<String> replacedLores = Lists.newArrayList();
			for (String s : lore) {
				s = ColourUtil.replaceString(s.replaceAll("%category%", StringUtils.capitalize(category)).replaceAll("%mobcoins_time_" + category
						+ "%", displayEndTime));
				s = MobCoins.getInstance().getPlaceholderManager().parsePlaceholders(null, s);
				replacedLores.add(s);
			}
			itemMeta.setLore(replacedLores);
		}
		itemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		item.setItemMeta(itemMeta);
		if (MobCoins.getInstance().getConfig().getBoolean(path + ".enchanted")) {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		item = CustomHeadUtil.getCustomHead(item, MobCoins.getInstance().getConfig().getString(path + ".skullOwner"));
		return item;
	}

	public static Map<Integer, ItemStack> getCategorySwitchItems() {
		Map<Integer, ItemStack> categorySwitch = Maps.newHashMap();

		for (String category : MobCoins.getInstance().getConfig().getConfigurationSection("Switch").getKeys(false)) {
			categorySwitch.put(MobCoins.getInstance().getConfig().getInt("Switch." + category + ".slot"), getItem(null, "Switch." + category, 1));
		}

		return categorySwitch;
	}
}
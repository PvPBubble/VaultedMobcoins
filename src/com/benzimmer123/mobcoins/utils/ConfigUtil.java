package com.benzimmer123.mobcoins.utils;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.enums.DropType;

public class ConfigUtil {

	public static boolean isDropEnabled(DamageCause damageCause) {
		if (damageCause.equals(DamageCause.FALL)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.FALL");
		} else if (damageCause.equals(DamageCause.SUFFOCATION)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.SUFFOCATION");
		} else if (damageCause.equals(DamageCause.LAVA)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.LAVA");
		} else if (damageCause.equals(DamageCause.FIRE)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.FIRE");
		} else if (damageCause.equals(DamageCause.DROWNING)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.DROWNING");
		} else if (damageCause.equals(DamageCause.WITHER)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.WITHER");
		} else if (damageCause.equals(DamageCause.MAGIC)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.MAGIC");
		} else if (damageCause.equals(DamageCause.ENTITY_ATTACK)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.ENTITY_ATTACK");
		} else if (damageCause.equals(DamageCause.ENTITY_EXPLOSION)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.ENTITY_EXPLOSION");
		} else if (damageCause.equals(DamageCause.BLOCK_EXPLOSION)) {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.BLOCK_EXPLOSION");
		} else {
			return MobCoins.getInstance().getConfig().getBoolean("DropOptions.OTHER");
		}
	}

	public static int getPreviousPageItemsAmount(String category, int currentPage) {
		int amount = 0;

		for (String page : MobCoins.getInstance().getConfig().getConfigurationSection("Categories." + category + ".slots.pages").getKeys(false)) {
			int pageNo = Integer.parseInt(page);
			if (pageNo < currentPage)
				amount += MobCoins.getInstance().getConfig().getIntegerList("Categories." + category + ".slots.pages." + page).size();
		}

		return amount;
	}

	public static List<String> getHelpMenu() {
		return MobCoins.getInstance().getConfig().getStringList("MOBCOINS_HELP_LORE");
	}

	public static List<String> getAdminHelpMenu() {
		return MobCoins.getInstance().getConfig().getStringList("MOBCOINS_HELP_ADMIN_LORE");
	}

	public static int getPercentChance(EntityType entityType) {
		if (MobCoins.getInstance().getConfig().isSet("Mobs." + entityType.name() + ".CHANCE")) {
			return MobCoins.getInstance().getConfig().getInt("Mobs." + entityType.name() + ".CHANCE");
		}
		return 0;
	}

	public static int getMinDrops(EntityType entityType) {
		if (MobCoins.getInstance().getConfig().isSet("Mobs." + entityType.name() + ".MIN_DROPS")) {
			return MobCoins.getInstance().getConfig().getInt("Mobs." + entityType.name() + ".MIN_DROPS");
		}
		return 0;
	}

	public static int getMaxDrops(EntityType entityType) {
		if (MobCoins.getInstance().getConfig().isSet("Mobs." + entityType.name() + ".MAX_DROPS")) {
			return MobCoins.getInstance().getConfig().getInt("Mobs." + entityType.name() + ".MAX_DROPS");
		}
		return 0;
	}

	public static DropType getDropType() {
		if (MobCoins.getInstance().getConfig().getString("Options.drop").equalsIgnoreCase("PHYSICAL")) {
			return DropType.PHYSICAL;
		} else if (MobCoins.getInstance().getConfig().getString("Options.drop").equalsIgnoreCase("VIRTUAL")) {
			return DropType.VIRTUAL;
		}
		return DropType.PHYSICAL;
	}

	public static List<String> getDisabledWorlds() {
		return MobCoins.getInstance().getConfig().getStringList("disabled_worlds");
	}
}
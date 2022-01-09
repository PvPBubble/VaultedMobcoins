package com.benzimmer123.mobcoins.utils;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class CustomHeadUtil {

	@SuppressWarnings("deprecation")
	public static ItemStack getCustomHead(ItemStack item, String skullOwner) {
		if (!isSkull(item)) {
			return item;
		}
				
		if (skullOwner == null || skullOwner.equalsIgnoreCase("-1")) {
			return item;
		}

		final SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
		final GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", skullOwner));
		Field profileField = null;

		try {
			profileField = itemMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(itemMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			return item;
		}

		if (skullOwner.length() <= 16) {
			itemMeta.setOwner(skullOwner);
		}
		
		item.setItemMeta(itemMeta);
		return item;
	}

	public static boolean isSkull(ItemStack item) {
		return isSkull(item.getType().toString());
	}

	public static boolean isSkull(String name) {
		return name.equalsIgnoreCase("SKULL_ITEM") || name.equalsIgnoreCase("PLAYER_HEAD");
	}

}

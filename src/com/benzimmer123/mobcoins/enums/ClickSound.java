package com.benzimmer123.mobcoins.enums;

import org.bukkit.entity.HumanEntity;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.compatible.XSound;

public enum ClickSound {

	MAIN_MENU_OPEN("mainMenuOpen"),
	SECTION_MENU_OPEN("sectionMenuOpen"),
	SECTION_MENU_CLOSE("sectionMenuClose"),
	ADD_COINS("addCoins"),
	WITHDRAW_COINS("withdrawCoins"),
	SUCCESSFUL_PURCHASE("successfulPurchase"),
	ALREADY_DISPLAYING_CATEGORY("alreadyDisplayingCategory"),
	FAILED_PURCHASE("failedPurchase");

	String configName;

	ClickSound(String configName) {
		this.configName = configName;
	}

	private String getConfigName() {
		return configName;
	}

	public float getVolume() {
		return (float) MobCoins.getInstance().getConfig().getDouble("Options.Sound." + getConfigName() + ".volume");
	}

	public float getPitch() {
		return (float) MobCoins.getInstance().getConfig().getDouble("Options.Sound." + getConfigName() + ".pitch");
	}

	public String getSoundName() {
		return MobCoins.getInstance().getConfig().getString("Options.Sound." + getConfigName() + ".name");
	}

	public boolean isEnabled() {
		return MobCoins.getInstance().getConfig().getBoolean("Options.Sound." + getConfigName() + ".enabled");
	}

	public void play(HumanEntity player) {
		ClickSound clickSound = this;
		String soundName = clickSound.getSoundName();
		XSound xSound = XSound.matchXSound(soundName).get();

		if (xSound == null)
			return;

		xSound.play(player, clickSound.getVolume(), clickSound.getPitch());
	}

}
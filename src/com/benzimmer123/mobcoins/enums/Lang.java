package com.benzimmer123.mobcoins.enums;

import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.utils.ColourUtil;

public enum Lang {

	PREFIX("", false),
	MOB_TOP_TITLE("&e&l(!) &6&lMobCoins &7&l: &c&lLEADRBOARD &r&f/ &7#%page%", false),
	MOB_TOP_ENTRY("  &6&l* &r&f#%pos% &e%player% &7- &e%amount%", false),
	MOB_TOP_FOOTER("&7&o(( Type &f/mobcoins top %nextpage% &7&oto go to the next page ))", false),
	USAGE_MESSAGE("&c&l[!] &cUsage: %usage%", true),
	CATEGORY_DOES_NOT_EXIST("&c&l[!] There is no such category as %category%!", true),
	CATEGORY_REFRESHED("&e&l[!] You have successfully refreshed %category% category.", true),
	CATEGORY_REFRESHED_BROADCAST("&e&l[!] MobCoins category %category% has just refreshed.", true),
	CATEGORY_BEING_REFRESHED("&c&l[!] Please wait, a category is being refreshed.", true),
	CATEGORY_STATIC("&c&l[!] This category cannot be refreshed.", true),
	MAX_BALANCE("&c&l[!] You cannot do this because will be over the max balance ($%maxbalance%).", true),
	PLAYER_HAS_MAX_BALANCE("&c&l[!] This player cannot receive %amount% because they will be over the max balance ($%maxbalance%).", true),
	PLAYER_NOT_FOUND_MESSAGE("&c&l[!] &cCannot find the player %player%", true),
	PROFILE_NOT_FOUND_MESSAGE("&c&l[!] &cCannot find the profile %profile%", true),
	NOT_NUMERIC_MESSAGE("&c&l[!] &cYou have entered an incorrect number", true),
	GAVE_MOBCOINS_MESSAGE("&e&l[!] &eYou gave &6%amount% &emobcoins to &6%player%", true),
	RECEIVED_MOBCOINS_MESSAGE("&e&l+ ☀%amount% &6&lMOBCOIN &7(%sender%)", true),
	GAVE_MOBCOIN_ITEMS_MESSAGE("&e&l[!] &eYou gave &6%amount% &emobcoin items to &6%player%", true),
	RECEIVED_MOBCOIN_ITEMS_MESSAGE("&e&l[!] &eYou received &6%amount% &emobcoin items from &6%sender%", true),
	NOT_PLAYER_MESSAGE("&c&l[!] &cYou need to be a player to execute this command", true),
	NOT_ENOUGH_MOBCOINS_MESSAGE("&c&l[!] &cYou do not have enough mobcoins to do that", true),
	WITHDREW_MOBCOINS_MESSAGE("&e&l[!] &eYou withdrew &6%amount% &emobcoins", true),
	DEPOSITED_MOBCOINS_MESSAGE("&e&l[!] &eYou deposited &6%amount% &emobcoins", true),
	BOUGHT_REWARD_MESSAGE("&e&l[!] &eYou bought %reward% &efor &6%amount% &emobcoins", true),
	RECEIVED_MOBCOIN_FROM_MOB_MESSAGE("&e&l+ ☀%amount% &6&lMOBCOIN &7(Mob)", true),
	REDEEMED_MOBCOIN_MESSAGE("&e&l[!] &eYou redeemed &6%amount% &emobcoins", true),
	MOBCOIN_NORMAL_SHOP_UPDATED_MESSAGE("&e&l[!] &eThe normal mobcoin items in &6/mobcoins &ehave been updated", true),
	MOBCOIN_SPECIAL_SHOP_UPDATED_MESSAGE("&e&l[!] &eThe special mobcoin items in &6/mobcoins &ehave been updated", true),
	MOBCOINS_OF_PLAYER_MESSAGE("&e&l[!] &6%player% &ehas &6%amount% &emobcoins", true),
	PLAYER_NOT_ENOUGH_MOBCOINS("&c&l[!] &c%player% does not have %amount% mobcoins", true),
	TOOK_MOBCOINS_MESSAGE("&e&l[!] &eYou took &6%amount% &emobcoins from &6%player%", true),
	PLAYER_TOOK_MOBCOINS_MESSAGE("&e&l[!] &6%player% &etook &6%amount% &emobcoins from your balance", true),
	SET_MOBCOINS_MESSAGE("&e&l[!] &6%player%&e's mobcoins balance has been set to &6%amount%", true),
	YOUR_MOBCOINS_SET_MESSAGE("&e&l[!] &eYour mobcoins balance has been set to &6%amount% &eby &6%player%", true),
	INVENTORY_FULL_MESSAGE("&c&l[!] &cYour inventory is full", true),
	INVENTORY_GOT_FILLED_MESSAGE("&e&l[!] &eYour inventory got filled so you could only withdraw &6%amount% &emobcoins", true),
	CANNOT_PAY_YOURSELF_MESSAGE("&c&l[!] &cYou cannot pay yourself", true),
	PRICE_PLACEHOLDER("&a$%price%", false),
	PRICE_DISCOUNT_PLACEHOLDER("&c&m$%originalprice%&r &a$%price%", false),
	STATIC_PLACEHOLDER("&cNo Expiry", false),
	RELOAD("&a&l[!] Successfully reloaded configuration files.", true),
	NO_PERMISSION("&c&l[!] &cYou do not have permission to execute this command.", true);

	private String defaultValue;
	private boolean usePrefix;

	Lang(String defaultValue, boolean usePrefix) {
		this.defaultValue = defaultValue;
		this.usePrefix = usePrefix;
	}

	public String getDefault() {
		return this.defaultValue;
	}

	public String getPath() {
		return this.name();
	}

	public boolean usePrefix() {
		return this.usePrefix;
	}

	@Override
	public String toString() {
		boolean usePrefix = false;
		String prefix = null;

		if (MobCoins.getInstance().getSettingsManager().getLang().isSet(Lang.PREFIX.getPath())) {
			if (!MobCoins.getInstance().getSettingsManager().getLang().getString(Lang.PREFIX.getPath()).equalsIgnoreCase("") && MobCoins.getInstance()
					.getSettingsManager().getLang().getString(Lang.PREFIX.getPath()) != null && this.usePrefix()) {
				usePrefix = true;
				prefix = ColourUtil.replaceString(MobCoins.getInstance().getSettingsManager().getLang().getString(Lang.PREFIX.getPath()));
			}
		}

		if (MobCoins.getInstance().getSettingsManager().getLang().isSet(getPath())) {
			if (MobCoins.getInstance().getSettingsManager().getLang().getString(getPath()).equalsIgnoreCase("") || MobCoins.getInstance()
					.getSettingsManager().getLang().getString(getPath()) == null) {
				return "";
			}
			return usePrefix ? prefix + ColourUtil.replaceString(MobCoins.getInstance().getSettingsManager().getLang().getString(getPath()))
					: ColourUtil.replaceString(MobCoins.getInstance().getSettingsManager().getLang().getString(getPath()));
		}

		return usePrefix ? prefix + ColourUtil.replaceString(this.defaultValue) : ColourUtil.replaceString(this.defaultValue);
	}

	public static void sendMessage(CommandSender sender, String replacedString) {
		if (replacedString.equalsIgnoreCase(""))
			return;

		String[] message = replacedString.split("\\\\n");

		for (String msg : message) {
			sender.sendMessage(msg.replace("\\n", ""));
		}
	}
}
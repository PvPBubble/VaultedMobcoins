package com.benzimmer123.mobcoins.cmds.subcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.data.CategoryData;
import com.benzimmer123.mobcoins.enums.Lang;

public class Refresh extends SubCommand {

	public Refresh(MobCoins instance) {
		super(instance, true);
		addAlias("refresh");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		if (!CategoryData.getInstance().doesCategoryExist(args[0])) {
			Lang.sendMessage(sender, Lang.CATEGORY_DOES_NOT_EXIST.toString().replaceAll("%category%", args[0]));
			return false;
		}

		if (CategoryData.getInstance().getCategoryEndTimes().get(args[0]) == -1) {
			Lang.sendMessage(sender, Lang.CATEGORY_STATIC.toString());
			return false;
		}

		CategoryData.getInstance().refreshCategory(args[0], false);
		Lang.sendMessage(sender, Lang.CATEGORY_REFRESHED.toString().replaceAll("%category%", StringUtils.capitalize(args[0])));
		return false;
	}

	@Override
	public boolean validArgumentLength(int length) {
		if (length == 2) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelp() {
		return "/mobcoins refresh <category>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.REFRESH";
	}

	@Override
	public String getDescription() {
		return "Refresh a mob coins category.";
	}
}

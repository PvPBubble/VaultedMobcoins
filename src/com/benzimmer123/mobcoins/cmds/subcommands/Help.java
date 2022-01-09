package com.benzimmer123.mobcoins.cmds.subcommands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.utils.ColourUtil;
import com.benzimmer123.mobcoins.utils.ConfigUtil;

public class Help extends SubCommand {

	public Help(MobCoins instance) {
		super(instance, true);
		addAlias("help");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		List<String> helpMenu = ConfigUtil.getHelpMenu();
		List<String> adminHelpMenu = ConfigUtil.getAdminHelpMenu();
		if (sender.hasPermission("MOBCOINS.ADMINHELP")) {
			for (String s : adminHelpMenu) {
				sender.sendMessage(ColourUtil.replaceString(s));
			}
		} else {
			for (String s : helpMenu) {
				sender.sendMessage(ColourUtil.replaceString(s));
			}
		}
		return false;
	}

	@Override
	public boolean validArgumentLength(int length) {
		if (length == 1) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelp() {
		return "/mobcoins help";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.HELP";
	}

	@Override
	public String getDescription() {
		return "View the mob coins help menu.";
	}
}

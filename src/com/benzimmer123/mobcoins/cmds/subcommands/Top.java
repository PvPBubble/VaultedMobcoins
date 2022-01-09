package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class Top extends SubCommand {

	public Top(MobCoins instance) {
		super(instance, true);
		addAlias("top");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		int i;

		if (args.length > 0) {
			try {
				i = Integer.parseInt(args[0]);
			} catch (Exception e) {
				Lang.sendMessage(sender, Lang.NOT_NUMERIC_MESSAGE.toString());
				return false;
			}
		} else {
			i = 1;
		}

		MobCoins.getInstance().getTopManager().listPage(sender, i);
		return false;
	}

	@Override
	public boolean validArgumentLength(int length) {
		if (length == 1 || length == 2) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelp() {
		return "/mobcoins top [page]";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.TOP";
	}

	@Override
	public String getDescription() {
		return "View the players with the most mob coins.";
	}

}

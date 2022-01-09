package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.utils.ItemUtil;

public class GiveItem extends SubCommand {

	public GiveItem(MobCoins instance) {
		super(instance, true);
		addAlias("giveitem");
		addAlias("giveitems");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		int amount;

		if (Bukkit.getPlayer(args[0]) == null) {
			Lang.sendMessage(sender, Lang.PLAYER_NOT_FOUND_MESSAGE.toString().replaceAll("%player%", args[0]));
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);

		try {
			amount = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			Lang.sendMessage(sender, Lang.NOT_NUMERIC_MESSAGE.toString());
			return false;
		}

		if (amount < 0) {
			Lang.sendMessage(sender, Lang.NOT_NUMERIC_MESSAGE.toString());
			return false;
		}

		target.getInventory().addItem(ItemUtil.getItem(null, "Options.ItemToken", amount));
		Lang.sendMessage(sender, Lang.GAVE_MOBCOIN_ITEMS_MESSAGE.toString().replaceAll("%player%", target.getName()).replaceAll("%amount%", amount + ""));
		Lang.sendMessage(target, Lang.RECEIVED_MOBCOIN_ITEMS_MESSAGE.toString().replaceAll("%sender%", sender.getName()).replaceAll("%amount%", amount + ""));
		return false;
	}

	@Override
	public boolean validArgumentLength(int length) {
		if (length == 3) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelp() {
		return "/mobcoins giveitem <player> <amount>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.GIVEITEM";
	}

	@Override
	public String getDescription() {
		return "Give another player mob coins items.";
	}
}

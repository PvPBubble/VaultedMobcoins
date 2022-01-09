package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class Set extends SubCommand {

	public Set(MobCoins instance) {
		super(instance, true);
		addAlias("set");
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

		if (MobCoins.getInstance().getConfig().getInt("MaxBalance") != -1) {
			long total = MobCoins.getInstance().getEconomyManager().getBalance(target.getUniqueId()) + amount;
			if (total > MobCoins.getInstance().getConfig().getInt("MaxBalance")) {
				Lang.sendMessage(sender, Lang.PLAYER_HAS_MAX_BALANCE.toString().replaceAll("%amount%", amount + "").replaceAll("%maxbalance%", MobCoins.getInstance().getConfig().getInt(
						"MaxBalance") + ""));
				return false;
			}
		}

		MobCoins.getInstance().getEconomyManager().setBalance(target.getUniqueId(), amount);
		Lang.sendMessage(sender, Lang.SET_MOBCOINS_MESSAGE.toString().replaceAll("%player%", target.getName()).replaceAll("%amount%", amount + ""));
		Lang.sendMessage(target, Lang.YOUR_MOBCOINS_SET_MESSAGE.toString().replaceAll("%player%", sender.getName()).replaceAll("%amount%", amount + ""));
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
		return "/mobcoins set <player> <amount>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.SET";
	}

	@Override
	public String getDescription() {
		return "Set another players mob coins.";
	}
}

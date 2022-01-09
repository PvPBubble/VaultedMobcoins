package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class Take extends SubCommand {

	public Take(MobCoins instance) {
		super(instance, true);
		addAlias("take");
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

		long balance = MobCoins.getInstance().getEconomyManager().getBalance(target.getUniqueId());

		if (balance < amount) {
			Lang.sendMessage(sender, Lang.PLAYER_NOT_ENOUGH_MOBCOINS.toString());
			return false;
		}

		MobCoins.getInstance().getEconomyManager().removeBalance(target.getUniqueId(), amount);
		Lang.sendMessage(sender, Lang.TOOK_MOBCOINS_MESSAGE.toString().replaceAll("%player%", target.getName()).replaceAll("%amount%", amount + ""));
		Lang.sendMessage(target, Lang.PLAYER_TOOK_MOBCOINS_MESSAGE.toString().replaceAll("%player%", sender.getName()).replaceAll("%amount%", amount + ""));
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
		return "/mobcoins take <player> <amount>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.TAKE";
	}

	@Override
	public String getDescription() {
		return "Take mob coins from another player.";
	}
}

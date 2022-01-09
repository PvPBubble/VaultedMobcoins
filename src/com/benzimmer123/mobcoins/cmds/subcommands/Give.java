package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class Give extends SubCommand {

	public Give(MobCoins instance) {
		super(instance, true);
		addAlias("give");
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

		boolean silent = false;

		if (args.length == 3) {
			if (args[2].equalsIgnoreCase("-s")) {
				silent = true;
			} else {
				Lang.sendMessage(sender, Lang.USAGE_MESSAGE.toString().replaceAll("%usage%", getHelp()));
				return false;
			}
		}

		if (MobCoins.getInstance().getConfig().getInt("MaxBalance") != -1) {
			long total = MobCoins.getInstance().getEconomyManager().getBalance(target.getUniqueId()) + amount;
			if (total > MobCoins.getInstance().getConfig().getInt("MaxBalance")) {
				Lang.sendMessage(sender, Lang.PLAYER_HAS_MAX_BALANCE.toString().replaceAll("%amount%", amount + "").replaceAll("%maxbalance%",
						MobCoins.getInstance().getConfig().getInt("MaxBalance") + ""));
				return false;
			}
		}

		MobCoins.getInstance().getEconomyManager().addBalance(target.getUniqueId(), amount);
		Lang.sendMessage(sender, Lang.GAVE_MOBCOINS_MESSAGE.toString().replaceAll("%player%", target.getName()).replaceAll("%amount%", amount + ""));

		if (!silent)
			Lang.sendMessage(target, Lang.RECEIVED_MOBCOINS_MESSAGE.toString().replaceAll("%sender%", sender.getName()).replaceAll("%amount%", amount
					+ ""));

		return false;
	}

	@Override
	public boolean validArgumentLength(int length) {
		if (length == 3 || length == 4) {
			return true;
		}
		return false;
	}

	@Override
	public String getHelp() {
		return "/mobcoins give <player> <amount> [-s]";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.GIVE";
	}

	@Override
	public String getDescription() {
		return "Give another player mob coins.";
	}
}

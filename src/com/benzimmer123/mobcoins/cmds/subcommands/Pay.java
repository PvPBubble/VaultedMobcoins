package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class Pay extends SubCommand {

	public Pay(MobCoins instance) {
		super(instance, false);
		addAlias("pay");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		int amount;
		Player player = (Player) sender;

		if (args[0].equalsIgnoreCase(player.getName())) {
			Lang.sendMessage(sender, Lang.CANNOT_PAY_YOURSELF_MESSAGE.toString());
			return false;
		}

		if (Bukkit.getPlayer(args[0]) == null) {
			Lang.sendMessage(sender, Lang.PLAYER_NOT_FOUND_MESSAGE.toString().replaceAll("%player%", args[0]));
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);

		try {
			amount = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			Lang.sendMessage(player, Lang.NOT_NUMERIC_MESSAGE.toString());
			return false;
		}

		if (amount < 0) {
			Lang.sendMessage(player, Lang.NOT_NUMERIC_MESSAGE.toString());
			return false;
		}

		long balance = MobCoins.getInstance().getEconomyManager().getBalance(player.getUniqueId());

		if (balance < amount) {
			Lang.sendMessage(player, Lang.NOT_ENOUGH_MOBCOINS_MESSAGE.toString());
			return false;
		}

		if (MobCoins.getInstance().getConfig().getInt("MaxBalance") != -1) {
			long total = MobCoins.getInstance().getEconomyManager().getBalance(target.getUniqueId()) + amount;
			if (total > MobCoins.getInstance().getConfig().getInt("MaxBalance")) {
				Lang.sendMessage(sender, Lang.PLAYER_HAS_MAX_BALANCE.toString().replaceAll("%amount%", amount + "").replaceAll("%maxbalance%",
						MobCoins.getInstance().getConfig().getInt("MaxBalance") + ""));
				return false;
			}
		}

		MobCoins.getInstance().getEconomyManager().removeBalance(player.getUniqueId(), amount);
		MobCoins.getInstance().getEconomyManager().addBalance(target.getUniqueId(), amount);
		Lang.sendMessage(player, Lang.GAVE_MOBCOINS_MESSAGE.toString().replaceAll("%player%", target.getName()).replaceAll("%amount%", amount + ""));
		Lang.sendMessage(target, Lang.RECEIVED_MOBCOINS_MESSAGE.toString().replaceAll("%sender%", player.getName()).replaceAll("%amount%", amount
				+ ""));
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
		return "/mobcoins pay <player> <amount>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.PAY";
	}

	@Override
	public String getDescription() {
		return "Pay another player mob coins.";
	}
}

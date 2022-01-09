package com.benzimmer123.mobcoins.cmds.subcommands;

import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class ViewCoins extends SubCommand {

	public ViewCoins(MobCoins instance) {
		super(instance, true);
		addAlias("viewcoins");
		addAlias("viewbalance");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		if (Bukkit.getPlayer(args[0]) == null) {
			Lang.sendMessage(sender, Lang.PLAYER_NOT_FOUND_MESSAGE.toString().replaceAll("%player%", args[0]));
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);
		long balance = MobCoins.getInstance().getEconomyManager().getBalance(target.getUniqueId());
	    String s = NumberFormat.getIntegerInstance().format(balance);
		Lang.sendMessage(sender, Lang.MOBCOINS_OF_PLAYER_MESSAGE.toString().replaceAll("%amount%", s + "").replaceAll("%player%", target.getName()));
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
		return "/mobcoins viewcoins <player>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.VIEWCOINS";
	}

	@Override
	public String getDescription() {
		return "View mob coins balance of another player.";
	}
}

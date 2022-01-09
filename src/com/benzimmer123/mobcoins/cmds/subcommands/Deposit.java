package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.ClickSound;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.utils.InventoryUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;

public class Deposit extends SubCommand {

	public Deposit(MobCoins instance) {
		super(instance, false);
		addAlias("deposit");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		int amount;
		Player player = (Player) sender;
		int amountInInventory = InventoryUtil.getAmount(player, ItemUtil.getItem(null, "Options.ItemToken", 1));

		if (args[0].equalsIgnoreCase("all")) {
			amount = amountInInventory;
		} else {
			try {
				amount = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				Lang.sendMessage(player, Lang.NOT_NUMERIC_MESSAGE.toString());
				return false;
			}
		}

		if (amount < 0) {
			Lang.sendMessage(player, Lang.NOT_NUMERIC_MESSAGE.toString());
			return false;
		}

		if (amount > amountInInventory) {
			Lang.sendMessage(player, Lang.NOT_ENOUGH_MOBCOINS_MESSAGE.toString());
			return false;
		}

		if (MobCoins.getInstance().getConfig().getInt("MaxBalance") != -1) {
			long total = MobCoins.getInstance().getEconomyManager().getBalance(player.getUniqueId()) + amount;
			if (total > MobCoins.getInstance().getConfig().getInt("MaxBalance")) {
				Lang.sendMessage(player, Lang.MAX_BALANCE.toString().replaceAll("%maxbalance%", MobCoins.getInstance().getConfig().getInt(
						"MaxBalance") + ""));
				return false;
			}
		}

		player.getInventory().removeItem(ItemUtil.getItem(null, "Options.ItemToken", amount));
		MobCoins.getInstance().getEconomyManager().addBalance(player.getUniqueId(), amount);
		Lang.sendMessage(player, Lang.DEPOSITED_MOBCOINS_MESSAGE.toString().replaceAll("%amount%", amount + ""));
		ClickSound.ADD_COINS.play(player);
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
		return "/mobcoins deposit <amount>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.DEPOSIT";
	}

	@Override
	public String getDescription() {
		return "Deposit mob coins to your balance.";
	}
}

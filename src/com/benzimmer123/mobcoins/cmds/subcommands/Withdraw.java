package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.ClickSound;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.utils.InventoryUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;

public class Withdraw extends SubCommand {

	public Withdraw(MobCoins instance) {
		super(instance, false);
		addAlias("withdraw");
	}

	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		int amount;
		Player player = (Player) sender;

		try {
			amount = Integer.parseInt(args[0]);
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
		
		int stacks = (int) Math.ceil(amount / 64);
		int space = InventoryUtil.getInventorySpace(player.getInventory());

		if (space == 0) {
			Lang.sendMessage(player, Lang.INVENTORY_FULL_MESSAGE.toString());
			return false;
		}

		if (space < stacks) {
			amount = space * 64;
			Lang.sendMessage(player, Lang.INVENTORY_GOT_FILLED_MESSAGE.toString().replaceAll("%amount%", amount + ""));
		} else {
			Lang.sendMessage(player, Lang.WITHDREW_MOBCOINS_MESSAGE.toString().replaceAll("%amount%", amount + ""));			
		}
		
		player.getInventory().addItem(ItemUtil.getItem(null, "Options.ItemToken", amount));
		MobCoins.getInstance().getEconomyManager().removeBalance(player.getUniqueId(), amount);
		ClickSound.WITHDRAW_COINS.play(player);
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
		return "/mobcoins withdraw <amount>";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.WITHDRAW";
	}

	@Override
	public String getDescription() {
		return "Withdraw mob coins from your balance.";
	}
}

package com.benzimmer123.mobcoins.cmds.rootcommand;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.subcommands.Deposit;
import com.benzimmer123.mobcoins.cmds.subcommands.Give;
import com.benzimmer123.mobcoins.cmds.subcommands.GiveItem;
import com.benzimmer123.mobcoins.cmds.subcommands.Help;
import com.benzimmer123.mobcoins.cmds.subcommands.Pay;
import com.benzimmer123.mobcoins.cmds.subcommands.Refresh;
import com.benzimmer123.mobcoins.cmds.subcommands.Reload;
import com.benzimmer123.mobcoins.cmds.subcommands.Set;
import com.benzimmer123.mobcoins.cmds.subcommands.Take;
import com.benzimmer123.mobcoins.cmds.subcommands.Top;
import com.benzimmer123.mobcoins.cmds.subcommands.ViewCoins;
import com.benzimmer123.mobcoins.cmds.subcommands.Withdraw;
import com.benzimmer123.mobcoins.enums.Lang;
import com.google.common.collect.Lists;

public class RootCommand implements CommandExecutor {

	private ArrayList<SubCommand> subCommands;

	public RootCommand(MobCoins instance) {
		subCommands = Lists.newArrayList();
		subCommands.add(new Reload(instance));
		subCommands.add(new Withdraw(instance));
		subCommands.add(new Deposit(instance));
		subCommands.add(new ViewCoins(instance));
		subCommands.add(new Pay(instance));
		subCommands.add(new Give(instance));
		subCommands.add(new Take(instance));
		subCommands.add(new Set(instance));
		subCommands.add(new GiveItem(instance));
		subCommands.add(new Help(instance));
		subCommands.add(new Top(instance));
		subCommands.add(new Refresh(instance));
	}

	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if (paramArrayOfString.length >= 1) {
			SubCommand subcommand = null;
			for (SubCommand subcommand1 : this.subCommands) {
				if (subcommand1.getIdentifiers().contains(paramArrayOfString[0].toLowerCase())) {
					subcommand = subcommand1;
					break;
				}
			}
			if (subcommand != null) {
				if (!subcommand.isConsoleAllowed() && paramCommandSender instanceof ConsoleCommandSender) {
					Lang.sendMessage(paramCommandSender, Lang.NOT_PLAYER_MESSAGE.toString());
					return true;
				}

				if (!subcommand.validArgumentLength(paramArrayOfString.length)) {
					subcommand.performHelp(paramCommandSender);
					return true;
				}

				if (paramCommandSender instanceof Player) {
					Player player = (Player) paramCommandSender;
					if (passChecks(subcommand, player))
						return subcommand.performCommand((CommandSender) player, Arrays.<String> copyOfRange(paramArrayOfString, 1,
								paramArrayOfString.length));
					return true;
				}
				subcommand.performCommand(paramCommandSender, Arrays.<String> copyOfRange(paramArrayOfString, 1, paramArrayOfString.length));
				return true;
			}
		}

		if (MobCoins.getInstance().isRefreshing()) {
			Lang.sendMessage(paramCommandSender, Lang.CATEGORY_BEING_REFRESHED.toString());
			return false;
		}

		MobCoins.getInstance().getGUIManager().openGUI((Player) paramCommandSender);
		return true;
	}

	private boolean passChecks(SubCommand command, Player player) {
		boolean hasPermission = command.hasPermission(player);
		if (!hasPermission)
			Lang.sendMessage(player, Lang.NO_PERMISSION.toString());
		return hasPermission;
	}

	public int getCommandSize() {
		return subCommands.size();
	}
}

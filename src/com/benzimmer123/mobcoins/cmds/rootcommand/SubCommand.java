package com.benzimmer123.mobcoins.cmds.rootcommand;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.enums.Lang;
import com.google.common.collect.Lists;

public abstract class SubCommand {

	protected MobCoins plugin;
	private List<String> identifiers;
	private boolean consoleAllowed;

	private static final String godPermission = "MOBCOINS.*";

	public SubCommand(MobCoins plugin, boolean consoleAllowed) {
		this.identifiers = Lists.newArrayList();
		this.plugin = plugin;
		this.consoleAllowed = consoleAllowed;
	}

	public abstract boolean performCommand(CommandSender sender, String[] args);

	public abstract boolean validArgumentLength(int length);

	public abstract String getHelp();

	public abstract String getPermission();

	public abstract String getDescription();

	protected void performHelp(CommandSender sender) {
		Lang.sendMessage(sender, Lang.USAGE_MESSAGE.toString().replaceAll("%usage%", getHelp()));
	}

	public boolean hasPermission(CommandSender sender) {
		if (sender.isOp() || sender.hasPermission(getPermission()) || sender.hasPermission(godPermission))
			return true;
		return false;
	}

	public boolean isConsoleAllowed() {
		return consoleAllowed;
	}

	public void addAlias(String alias) {
		this.identifiers.add(alias);
	}

	public List<String> getIdentifiers() {
		return identifiers;
	}

}

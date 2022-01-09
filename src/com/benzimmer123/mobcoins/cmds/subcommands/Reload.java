package com.benzimmer123.mobcoins.cmds.subcommands;

import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.cmds.rootcommand.SubCommand;
import com.benzimmer123.mobcoins.enums.Lang;

public class Reload extends SubCommand{

	public Reload(MobCoins instance) {
		super(instance, true);
		addAlias("reload");
		addAlias("rl");
	}
	
	@Override
	public boolean performCommand(CommandSender sender, String[] args) {
		plugin.reloadConfig();
		plugin.getSettingsManager().setupLang(plugin);
		Lang.sendMessage(sender, Lang.RELOAD.toString());
		return false;
	}

	@Override
	public boolean validArgumentLength(int length) {
		if(length == 1){
			return true;
		}
		return false;
	}

	@Override
	public String getHelp() {
		return "/mobcoins reload";
	}

	@Override
	public String getPermission() {
		return "MOBCOINS.RELOAD";
	}

	@Override
	public String getDescription() {
		return "Reload all Mob Coins configuration files.";
	}
}

package com.benzimmer123.mobcoins.managers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.benzimmer123.mobcoins.data.TopData;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.obj.TopPlayer;

public class TopManager {

	public void sortEconomyList() {
		LinkedHashMap<String, TopPlayer> treeMap = TopData.getInstance().getPlayers();
		treeMap = treeMap.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).collect(Collectors.toMap(Map.Entry::getKey,
				Map.Entry::getValue, (oldV, newV) -> oldV, LinkedHashMap::new));
		TopData.getInstance().setTopPlayers(treeMap);
	}

	public void listPage(CommandSender sender, int pageNumber) {
		LinkedHashMap<String, TopPlayer> map = TopData.getInstance().getPlayers();
		List<TopPlayer> players = map.values().stream().collect(Collectors.toList());

		final int pageheight = 10;
		int pagecount = (players.size() / pageheight) + 1;
		if (pageNumber > pagecount)
			pageNumber = pagecount;
		else if (pageNumber < 1)
			pageNumber = 1;
		int start = (pageNumber - 1) * pageheight;
		int end = start + pageheight;
		if (end > players.size())
			end = players.size();

		Lang.sendMessage(sender, Lang.MOB_TOP_TITLE.toString().replaceAll("%page%", pageNumber + "").replaceAll("%maxpage%", pagecount + ""));

		for (TopPlayer player : players.subList(start, end)) {
			int position = players.indexOf(player) + 1;

			Lang.sendMessage(sender, Lang.MOB_TOP_ENTRY.toString().replaceAll("%pos%", position + "").replaceAll("%player%", player.getName())
					.replaceAll("%amount%", player.getAmount() + ""));
		}

		Lang.sendMessage(sender, Lang.MOB_TOP_FOOTER.toString().replaceAll("%nextpage%", ++pageNumber + ""));
	}
}

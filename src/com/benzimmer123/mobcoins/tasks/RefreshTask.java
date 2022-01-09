package com.benzimmer123.mobcoins.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.benzimmer123.mobcoins.data.CategoryData;

public class RefreshTask extends BukkitRunnable {

	public void run() {
		for (String category : CategoryData.getInstance().getCategoryEndTimes().keySet()) {
			long endTimeStamp = CategoryData.getInstance().getEndTime(category);

			if (endTimeStamp == -1)
				continue;

			if (System.currentTimeMillis() >= endTimeStamp) {
				CategoryData.getInstance().refreshCategory(category, false);
			}
		}
	}
}

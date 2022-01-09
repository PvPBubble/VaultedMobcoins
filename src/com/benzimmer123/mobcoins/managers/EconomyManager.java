package com.benzimmer123.mobcoins.managers;

import java.util.UUID;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.data.TopData;
import com.benzimmer123.mobcoins.obj.TopPlayer;

public class EconomyManager {

	public long getBalance(UUID uuid) {
		if (MobCoins.getInstance().getSettingsManager().getEconomy().isSet(uuid.toString())) {
			return MobCoins.getInstance().getSettingsManager().getEconomy().getLong(uuid.toString());
		}
		return 0;
	}

	public void addBalance(UUID uuid, long amount) {
		long balance = getBalance(uuid);
		long total = balance + amount;
		MobCoins.getInstance().getSettingsManager().getEconomy().set(uuid.toString(), total);
		MobCoins.getInstance().getSettingsManager().saveEconomy();
		TopPlayer topPlayer = TopData.getInstance().getPlayer(uuid);
		topPlayer.setAmount(total);
	}

	public void removeBalance(UUID uuid, long amount) {
		long balance = getBalance(uuid);
		long total = balance - amount;
		MobCoins.getInstance().getSettingsManager().getEconomy().set(uuid.toString(), total);
		MobCoins.getInstance().getSettingsManager().saveEconomy();
		TopPlayer topPlayer = TopData.getInstance().getPlayer(uuid);
		topPlayer.setAmount(total);
	}

	public void setBalance(UUID uuid, long amount) {
		MobCoins.getInstance().getSettingsManager().getEconomy().set(uuid.toString(), amount);
		MobCoins.getInstance().getSettingsManager().saveEconomy();
		TopPlayer topPlayer = TopData.getInstance().getPlayer(uuid);
		topPlayer.setAmount(amount);
	}

}
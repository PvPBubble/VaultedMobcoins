package com.benzimmer123.mobcoins.obj;

import java.util.UUID;

import org.bukkit.Bukkit;

public class TopPlayer implements Comparable<TopPlayer> {

	private final String sUUID;
	private String name;
	private long amount;

	public TopPlayer(String sUUID, long amount) {
		this.sUUID = sUUID;
		this.amount = amount;
		this.name = "";
		
		UUID uuid = UUID.fromString(sUUID);
		
		if (Bukkit.getPlayer(uuid) != null) {
			name = Bukkit.getPlayer(uuid).getName();
		} else if (Bukkit.getOfflinePlayer(uuid) != null) {
			name = Bukkit.getOfflinePlayer(uuid).getName();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getAmount() {
		return amount;
	}

	public String getUUID() {
		return sUUID;
	}
	
	@Override
	public int compareTo(TopPlayer team) {
		return (int) (this.getAmount() - team.getAmount());
	}
}
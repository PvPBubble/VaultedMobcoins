package com.benzimmer123.mobcoins.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.benzimmer123.mobcoins.MobCoins;
import com.benzimmer123.mobcoins.data.CategoryData;
import com.benzimmer123.mobcoins.data.PlayerData;
import com.benzimmer123.mobcoins.enums.ClickSound;
import com.benzimmer123.mobcoins.enums.Lang;
import com.benzimmer123.mobcoins.hooks.Outpost;
import com.benzimmer123.mobcoins.utils.ColourUtil;
import com.benzimmer123.mobcoins.utils.ItemUtil;

public class InventoryClick implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null)
			return;

		if (e.getView().getTitle().equalsIgnoreCase(ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString("MainMenu.title")))) {
			e.setCancelled(true);

			if (!e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getDisplayName() == null)
				return;

			ItemStack item = e.getCurrentItem();

			if (CategoryData.getInstance().getCategory(item.getItemMeta().getDisplayName()) != null) {
				MobCoins.getInstance().getGUIManager().openCategory((Player) e.getWhoClicked(), CategoryData.getInstance().getCategory(item
						.getItemMeta().getDisplayName()), 1);
				ClickSound.SECTION_MENU_OPEN.play(e.getWhoClicked());
			}
		} else if (CategoryData.getInstance().getCategoryInventoryNames().contains(e.getView().getTitle())) {
			e.setCancelled(true);

			if (!e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getDisplayName() == null)
				return;

			String category = CategoryData.getInstance().getCategoryFromInventoryName(e.getView().getTitle());
			String itemNumber = CategoryData.getInstance().getItemNumber(e.getCurrentItem().getItemMeta().getDisplayName());

			if (itemNumber == null) {
				if (e.getCurrentItem().isSimilar(ItemUtil.getItem(null, "BackButton", 1))) {
					MobCoins.getInstance().getGUIManager().openGUI((Player) e.getWhoClicked());
				} else if (e.getCurrentItem().isSimilar(ItemUtil.getItem(null, "Pages.NextPage", 1))) {
					int page = PlayerData.getInstance().getPlayerPage((Player) e.getWhoClicked()) + 1;

					if (!MobCoins.getInstance().getConfig().isSet("Categories." + category + ".slots.pages." + page)) {
						return;
					}

					MobCoins.getInstance().getGUIManager().openCategory((Player) e.getWhoClicked(), category, page);
				} else if (e.getCurrentItem().isSimilar(ItemUtil.getItem(null, "Pages.PreviousPage", 1))) {
					int page = PlayerData.getInstance().getPlayerPage((Player) e.getWhoClicked()) - 1;
					
					if (page < 1) {
						return;
					}

					MobCoins.getInstance().getGUIManager().openCategory((Player) e.getWhoClicked(), category, page);
				} else {
					String switchCategory = CategoryData.getInstance().getCategorySwitch(e.getCurrentItem());

					if (switchCategory != null) {
						if (switchCategory.equalsIgnoreCase(category)) {
							ItemStack original = e.getCurrentItem();
							e.setCurrentItem(ItemUtil.getItem((Player) e.getWhoClicked(), "Options.StatusItems.alreadyDisplayingShop", 1));
							((Player) e.getWhoClicked()).updateInventory();
							ClickSound.ALREADY_DISPLAYING_CATEGORY.play(e.getWhoClicked());
							new BukkitRunnable() {
								public void run() {
									if (e.getWhoClicked() != null && e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(e.getView()
											.getTitle())) {
										e.setCurrentItem(original);
										((Player) e.getWhoClicked()).updateInventory();
									}
								}
							}.runTaskLater(MobCoins.getInstance(), MobCoins.getInstance().getConfig().getInt("Options.StatusItems.delay") * 20);
							return;
						}
						MobCoins.getInstance().getGUIManager().openCategory((Player) e.getWhoClicked(), switchCategory, 1);
						((Player) e.getWhoClicked()).updateInventory();
					}
				}
				return;
			}

			long price = MobCoins.getInstance().getConfig().getLong("Categories." + category + ".Items." + itemNumber + ".price");

			if (MobCoins.getInstance().isOutpostHooked()) {
				price = new Outpost().getModifiedPrice((Player) e.getWhoClicked(), price);
			}

			long balance = MobCoins.getInstance().getEconomyManager().getBalance(e.getWhoClicked().getUniqueId());

			if (price > balance) {
				ItemStack original = e.getCurrentItem();
				e.setCurrentItem(ItemUtil.getItem((Player) e.getWhoClicked(), "Options.StatusItems.notEnoughCoins", 1));
				((Player) e.getWhoClicked()).updateInventory();
				ClickSound.FAILED_PURCHASE.play(e.getWhoClicked());
				new BukkitRunnable() {
					public void run() {
						if (e.getWhoClicked() != null && e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(e.getView().getTitle())) {
							e.setCurrentItem(original);
							((Player) e.getWhoClicked()).updateInventory();
						}
					}
				}.runTaskLater(MobCoins.getInstance(), MobCoins.getInstance().getConfig().getInt("Options.StatusItems.delay") * 20);
				return;
			}

			MobCoins.getInstance().getEconomyManager().setBalance(e.getWhoClicked().getUniqueId(), balance - price);
			List<String> runCmds = MobCoins.getInstance().getConfig().getStringList("Categories." + category + ".Items." + itemNumber + ".commands");

			for (String cmd : runCmds) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replaceAll("%player%", e.getWhoClicked().getName()));
			}

			String reward = ColourUtil.replaceString(MobCoins.getInstance().getConfig().getString("Categories." + category + ".Items." + itemNumber
					+ ".name"));
			Lang.sendMessage(e.getWhoClicked(), Lang.BOUGHT_REWARD_MESSAGE.toString().replaceAll("%amount%", price + "").replaceAll("%reward%",
					reward));
			ClickSound.SUCCESSFUL_PURCHASE.play(e.getWhoClicked());
		}
	}
}
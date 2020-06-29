package net.faiden.skywars.manager.cages;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import net.faiden.skywars.SkyWars;
import net.golema.api.builder.virtual.VirtualMenu;
import net.golema.database.support.builder.items.ItemBuilder;

public class CagesMenu extends VirtualMenu {

	private static Map<Player, Inventory> menuInventoryMap = new HashMap<Player, Inventory>();
	
	/**
	 * Constructeur du CagesMenu.
	 * 
	 * @param player
	 */
	public CagesMenu(Player player) {
		super(player, "Choisir une plate-forme", 6);
		
		/**
		 * Items des Cages
		 */
		for(CagesInfo cagesInfo : CagesInfo.values()) {
			this.menuInventory.addItem(new ItemBuilder().type(cagesInfo.getMaterial()).name(cagesInfo.getName()).data((byte) cagesInfo.getData()).build());
		}
		
		menuInventoryMap.put(player, menuInventory);
		open();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory() == null) return;
		if(event.getClickedInventory() == null) return;
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType().equals(Material.AIR)) return;
		if(!(event.getInventory().equals(menuInventoryMap.get(player))))
			return;
		if(event.getInventory().equals(menuInventoryMap.get((Player) event.getWhoClicked()))) {
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			CagesInfo cagesInfo = CagesInfo.getCageByName(event.getCurrentItem().getItemMeta().getDisplayName());
			player.sendMessage(SkyWars.instance.prefixGame + ChatColor.GREEN + " Plate-forme s�lectionn�e: " + ChatColor.GOLD + cagesInfo.getName());
			SkyWars.getGamePlayer(player).setCagesInfo(cagesInfo);
			player.closeInventory();
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getInventory() == null)
			return;
		if(menuInventoryMap.get(player) == null)
			return;
		if(menuInventoryMap.get(player).equals(event.getInventory()))
			menuInventoryMap.remove(player);
	}
}
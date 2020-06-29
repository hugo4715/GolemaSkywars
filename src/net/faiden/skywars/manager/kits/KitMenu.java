package net.faiden.skywars.manager.kits;

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

public class KitMenu extends VirtualMenu {

	private static Map<Player, Inventory> playerInventoryMap = new HashMap<Player, Inventory>();
	
	/**
	 * Constructeur du KitMenu.
	 * 
	 * @param player
	 */
	public KitMenu(Player player) {
		super(player, "Kits", 6);
		
		/**
		 * Kits.
		 */
		for(KitAbstract kitAbstract : KitAbstract.kitAbstractsList) {
			this.menuInventory.setItem(kitAbstract.slot, kitAbstract.getItemIcons(player));
		}
		
		// Item de cat�goris Random.
		this.menuInventory.setItem(53, new ItemBuilder().type(Material.WOOD_DOOR).name(ChatColor.WHITE + "Fermer").build());
		for(int i = 0; i != 10; i++) { this.menuInventory.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build()); }
		this.menuInventory.setItem(17, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build());
		this.menuInventory.setItem(18, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build());
		this.menuInventory.setItem(26, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build());
		this.menuInventory.setItem(27, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build());
		this.menuInventory.setItem(35, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build());
		this.menuInventory.setItem(36, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build());
		for(int i = 44; i != 53; i++) { this.menuInventory.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build()); }
		
		playerInventoryMap.put(player, menuInventory);
		open();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getInventory() == null) return;
		if(event.getClickedInventory() == null) return;
		if(event.getCurrentItem() == null) return;
		if(event.getCurrentItem().getType().equals(Material.AIR)) return;
		if(!(event.getInventory().equals(playerInventoryMap.get(player))))
			return;
		if(event.getInventory().equals(playerInventoryMap.get((Player) event.getWhoClicked()))) {
			event.setCancelled(true);
			Player player = (Player) event.getWhoClicked();
			
			// Interaction avec un Item Random.
			if(event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) { return; }
			if(event.getCurrentItem().getType().equals(Material.WOOD_DOOR)) { player.closeInventory(); return; }
			
			// Interaction sur un Kit que le Joueur ne poss�de pas.
			if(event.getCurrentItem().getType().equals(Material.INK_SACK)) {
				player.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Vous ne poss�dez pas ce kit ! " + ChatColor.YELLOW + "Achetez-le sur nos hub.");
				player.closeInventory();
				return;
			}
			
			// Selection de kits.
			KitAbstract kitSelect = KitAbstract.getKitAbstractByName(event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.WHITE + "Kit: ", ""));
			if(kitSelect != null) {
				SkyWars.getGamePlayer(player).setKitAbstract(kitSelect);
				player.sendMessage(SkyWars.instance.prefixGame + ChatColor.GREEN + " Vous avez s�lectionn� le kit: " + ChatColor.GOLD + kitSelect.name + ChatColor.GREEN + ".");
			}
			player.closeInventory();
			
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getInventory() == null)
			return;
		if(playerInventoryMap.get(player) == null)
			return;
		if(playerInventoryMap.get(player).equals(event.getInventory()))
			playerInventoryMap.remove(player);
	}
}
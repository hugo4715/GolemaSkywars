package net.faiden.skywars.manager.cages.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.cages.CagesInfo;
import net.golema.api.builder.virtual.VirtualMenu;
import net.golema.database.support.builder.items.ItemBuilder;

public class CageMenuSecondPage extends VirtualMenu {

	private static Map<Player, Inventory> playerInventoryMap = new HashMap<Player, Inventory>();
	
	/**
	 * Constructeur du Menu de la page 2.
	 * 
	 * @param player
	 */
	public CageMenuSecondPage(Player player) {
		super(player, "Plate-forme � 2", 6);
		
		/**
		 * Cages.
		 */
		for(CagesInfo cagesInfo : CagesInfo.values()) {
			if(cagesInfo.getPage() == 2) {
//				if((cagesInfo.getPermission() != null) && (!(GolemaPlayer.get(player).hasPermission(cagesInfo.getPermission())))) {
//					this.menuInventory.setItem(cagesInfo.getSlot(), new ItemBuilder()
//							.type(Material.INK_SACK)
//							.name("�fPlate-forme: �e" + cagesInfo.getName())
//							.data((byte) 8)
//							.lore("", "�8Information: ", "�6Les plates-formes sont", "�6achetables sur nos hubs.", "", ChatColor.RED + "Vous ne poss�dez pas cette plate-forme.")
//							.build());
//				} else if(SkyWars.getGamePlayer(player).getCagesInfo().equals(cagesInfo)) {
//					this.menuInventory.setItem(cagesInfo.getSlot(), new ItemBuilder()
//							.type(cagesInfo.getMaterial())
//							.name("�fPlate-forme: �e" + cagesInfo.getName())
//							.data((byte) cagesInfo.getData())
//							.lore("", "�8Information: ", "�6Les plates-formes sont", "�6achetables sur nos hubs.", "", ChatColor.AQUA + "Plate-forme d�j� s�lectionn�e.")
//							.build());
//				} else {
//					this.menuInventory.setItem(cagesInfo.getSlot(), new ItemBuilder()
//							.type(cagesInfo.getMaterial())
//							.name("�fPlate-forme: �e" + cagesInfo.getName())
//							.data((byte) cagesInfo.getData())
//							.lore("", "�8Information: ", "�6Les plates-formes sont", "�6achetables sur nos hubs.", "", ChatColor.YELLOW + "Cliquez pour s�lectionner.")
//							.build());
//				}
			}
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
		for(int i = 44; i != 53; i++) { if(i != 48 && i != 49 && i != 50) { this.menuInventory.setItem(i, new ItemBuilder().type(Material.STAINED_GLASS_PANE).data((byte) 3).name("�0").build()); } }
		
		// Syst�me de pages et Random.
		this.menuInventory.setItem(48, new ItemBuilder().type(Material.PAPER).name(ChatColor.WHITE + "Page pr�c�dente").build());
		this.menuInventory.setItem(49, new ItemBuilder().type(Material.NETHER_STAR).name("�fPlate-forme: �dAl�atoire").lore("", "�eObtenez une plate-forme al�atoire",
				"�eparmi vos plates-formes.", "", "�cR�serv� aux �f[MineVIP]�c.").build());
		this.menuInventory.setItem(50, new ItemBuilder().type(Material.PAPER).name(ChatColor.WHITE + "Page suivante").build());
		
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
			
			// Pages
			if(event.getCurrentItem().getType().equals(Material.PAPER)) {
				if(event.getSlot() == 50) {
					new CageMenuThirdPage(player);
				} else {
					new CageMenuFirstPage(player);
				}
				return;
			}

			// Femer le Menu
			if(event.getCurrentItem().getType().equals(Material.WOOD_DOOR)) {
				player.closeInventory();
				return;
			}
			
			// Choix Al�atoire
			if(event.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
				List<CagesInfo> cagesInfosList = new ArrayList<CagesInfo>();
//				for(CagesInfo cagesInfo : CagesInfo.values()) {
//					if(GolemaPlayer.get(player).hasPermission(cagesInfo.getPermission())) {
//						cagesInfosList.add(cagesInfo);
//					}
//				}
				if(cagesInfosList.isEmpty()) {
					player.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Vous n'avez pas de plate-forme disponible.");
					player.closeInventory();
					return;
				}
				CagesInfo cagesInfo = cagesInfosList.get(new Random().nextInt(cagesInfosList.size()));
				player.sendMessage(SkyWars.instance.prefixGame + ChatColor.WHITE + " Vous avez s�lectionn� la plate-forme: " + ChatColor.GOLD + cagesInfo.getName());
				SkyWars.getGamePlayer(player).setCagesInfo(cagesInfo);
				player.closeInventory();
				return;
			}
			
			// Interaction sur un Kit que le Joueur ne poss�de pas.
			if(event.getCurrentItem().getType().equals(Material.INK_SACK)) {
				player.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Vous ne poss�dez pas cette plate-forme ! " + ChatColor.YELLOW + "Achetez-la sur nos hub.");
				player.closeInventory();
				return;
			}
			
			// D�j� cette plate-forme
			if(event.getCurrentItem().getItemMeta().getLore().contains(ChatColor.AQUA + "Plate-forme d�j� s�lectionn�e.")) {
				player.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Vous avez d�j� s�lectionn�e cette plate-forme.");
				player.closeInventory();
				return;
			}
			
			// Choix
			String cageName = event.getCurrentItem().getItemMeta().getDisplayName().replace("�fPlate-forme: �e", "");
			CagesInfo cagesInfo = CagesInfo.getCageByName(cageName);
			player.sendMessage(SkyWars.instance.prefixGame + ChatColor.WHITE + " Vous avez s�lectionn� la plate-forme: " + ChatColor.GOLD + cagesInfo.getName());
			SkyWars.getGamePlayer(player).setCagesInfo(cagesInfo);
			player.closeInventory();
			return;
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
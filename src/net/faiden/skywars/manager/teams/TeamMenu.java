package net.faiden.skywars.manager.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.faiden.skywars.SkyWars;
import net.golema.api.builder.virtual.VirtualMenu;
import net.golema.database.support.builder.items.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class TeamMenu extends VirtualMenu {

	private static Map<Player, Inventory> playerInventoryMap = new HashMap<Player, Inventory>();
	
	/**
	 * Constructeur du menu TeamMenu.
	 * 
	 * @param player
	 */
	public TeamMenu(Player player) {
		super(player, "Choisir d'équipes", 2);
		TeamManager.fullAndChargeList();
		/**
		 * Bannière des équipes
		 */
		for(TeamInfo teamInfos : TeamInfo.values()) {
			ItemStack itemStack = new ItemStack(Material.BANNER, 1, (short) teamInfos.getData());
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName(teamInfos.getCharString() + "Equipe " + teamInfos.getName());
			List<String> lores = new ArrayList<>();
			lores.add("");
			
			// Lore de la PlayerList.
			int playerInTeam = 0;
			for(Player playerTeam : TeamManager.playerTeamList.get(teamInfos)) {
				lores.add(ChatColor.GRAY + "■ " + teamInfos.getChatColor() + playerTeam.getName());
				playerInTeam++;
			}
			for(int x = playerInTeam; x != SkyWars.instance.maxPlayerPerTeam; x++) { lores.add(ChatColor.DARK_GRAY + "[Emplacement Vide]");	}
			
			
			// Lore de la possibilité de Join.
			lores.add("");
			if(TeamManager.playerTeamList.get(teamInfos).size() >= SkyWars.instance.maxPlayerPerTeam) {
				lores.add(ChatColor.RED + "Cette équipe est pleine.");
			} else {
				lores.add(ChatColor.YELLOW + "Cliquer pour rejoindre.");
			}
				
			itemMeta.setLore(lores);
			itemStack.setItemMeta(itemMeta);
			this.menuInventory.addItem(itemStack);
		}
		
		this.menuInventory.setItem(17, new ItemBuilder().type(Material.DARK_OAK_DOOR_ITEM).name(ChatColor.WHITE + "Fermer").build());
		
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
			switch (event.getCurrentItem().getType()) {
			case ARMOR_STAND:
				if(TeamManager.getPlayerTeam(player) == null) {
					TeamManager.putInARandomTeam(player);
					TeamInfo teamInfo = TeamManager.getPlayerTeam(player);
					player.sendMessage(SkyWars.instance.prefixGame + ChatColor.WHITE + " Vous avez rejoins l'équipe: " 
							+ teamInfo.getChatColor() + teamInfo.getName());
					player.closeInventory();
				} else {
					player.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Vous êtes déjà dans une équipe.");
					player.closeInventory();
					return;
				}
				break;
			case BANNER:
				
				// Paramètres des Teams.
				String teamName = event.getCurrentItem().getItemMeta().getDisplayName().substring(9);
				TeamInfo teamInfo = TeamInfo.geTeamInfoByName(teamName);
				
				// Vérifier si la team est pleine!
				if(TeamManager.playerTeamList.get(teamInfo).size() >= SkyWars.instance.maxPlayerPerTeam) {
					player.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Cette équipe est déjà pleine.");
					player.closeInventory();
					return;
				}
				
				// Ajout du Joueur dans la Team.
				TeamManager.removePlayerInAllTeam(player);
				TeamManager.addPlayerInTeam(player, teamInfo);
				player.sendMessage(SkyWars.instance.prefixGame + ChatColor.WHITE + " Vous avez rejoins l'équipe: " + teamInfo.getChatColor() + teamName);
				player.setItemInHand(new ItemBuilder().type(Material.BANNER).name(player.getItemInHand().getItemMeta().getDisplayName()).data((byte) event.getCurrentItem().getDurability()).build());
				player.closeInventory();
				break;
			default:
				break;
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if(event.getInventory().equals(playerInventoryMap.get(event.getPlayer()))) {
			playerInventoryMap.remove(event.getPlayer()); 
		}
	}
}
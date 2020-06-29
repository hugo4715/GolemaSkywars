package net.faiden.skywars.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.WinManager;
import net.faiden.skywars.manager.teams.TeamManager;
import net.golema.api.builder.titles.ActionBarBuilder;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.game.skywars.stats.SkyWarsStatsType;
import net.golema.database.golemaplayer.rank.Rank;
import net.golema.database.support.GameStatus;

public class PlayerQuitListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		// Initialisation de Variables.
		Player player = event.getPlayer();
		GolemaPlayer golemaPlayer = GolemaPlayer.getGolemaPlayer(player);
		GamePlayer gamePlayer = SkyWars.getGamePlayer(player);
		event.setQuitMessage(null);
		TeamManager.removePlayerInAllTeam(player);
		gamePlayer.updatePlayerStats();
		gamePlayer.rewardMessage(); 
		
		// Param�tres suivant le Status.
		switch (GameStatus.getStatus()) {
		case LOBBY:
			Bukkit.getOnlinePlayers().forEach(playerOnline -> {
				new ActionBarBuilder(golemaPlayer.getRank().getChatColor() + golemaPlayer.getRank().getPrefix() + " " + player.getName() + ChatColor.YELLOW + " a quitté la partie, " + ChatColor.AQUA +
						(Bukkit.getOnlinePlayers().size() - 1) + " Joueur(s) " + ChatColor.YELLOW + "en partie.").sendTo(playerOnline);
			});
			break;
		case GAME:
			
			// Gestion du Drop d'un Stuff d'un Joueur � sa mort.
			for (ItemStack itemStack : player.getInventory().getContents()) {
				if ((itemStack != null) && (!(itemStack.getType().equals(Material.AIR)))) {
					Bukkit.getWorld("world").dropItemNaturally(player.getLocation(), itemStack);
				}
			}
			
			// Gestion du Drop de l'Armure d'un Joueur � sa mort.
			for (ItemStack itemStackStuff : player.getInventory().getArmorContents()) {
				if ((itemStackStuff != null) && (!(itemStackStuff.getType().equals(Material.AIR)))) {
					Bukkit.getWorld("world").dropItemNaturally(player.getLocation(), itemStackStuff);
				}
			}
			
			// G�rer le Message de mort d'un joueur qui d�connecte.
			if(!(gamePlayer.isSpectator())) {
				
				golemaPlayer.getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.DEATHS, 1);
				
				// Mode de jeu en : SOLO
				if(SkyWars.instance.maxPlayerPerTeam == 1) {
					Bukkit.broadcastMessage(SkyWars.instance.prefixGame + " " + golemaPlayer.getRank().getChatColor() + golemaPlayer.getRank().getPrefix() + getSpaceAllow(golemaPlayer.getRank())
								+ player.getName() + ChatColor.YELLOW + " est mort en se déconnectant.");
					
				// Mode de jeu en : TEAM
				} else {
					Bukkit.broadcastMessage(SkyWars.instance.prefixGame + " " + SkyWars.getGamePlayer(player).getTeamInfo().getChatColor() + "[" + SkyWars.getGamePlayer(player).getTeamInfo().getName() + "] "
								+ player.getName() + ChatColor.YELLOW + " est mort en se déconnectant.");
				}
				
				// Ajout de l'effet de mort.
				Bukkit.getWorld("world").strikeLightningEffect(player.getLocation());
			}
			break;
		default:
			break;
		}
		
		// Param�tres du Left.
		TeamManager.removePlayerInAllTeam(player);
		gamePlayer.getScoreboardSign().destroy();
		SkyWars.getGamePlayersMap().remove(player.getName());
		new WinManager();
	}
	
	/**
	 * Espace en fonction du Rank.
	 * 
	 * @param rank
	 * @return
	 */
	private String getSpaceAllow(Rank rank) { return rank.getIdentificatorName().equals(Rank.PLAYER.getIdentificatorName()) ? "" : " "; }
}
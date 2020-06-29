package net.faiden.skywars.runnables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.golema.database.support.GameStatus;

public class ScoreboardRunnable extends BukkitRunnable {

	public ScoreboardRunnable() {}
	
	@Override
	public void run() {
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			if(SkyWars.getGamePlayer(playerOnline) != null) {
				GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
				switch (GameStatus.getStatus()) {
				case LOBBY:
					gamePlayer.getScoreboardSign().setLine(8, "§7Joueurs: §f" + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Bukkit.getMaxPlayers());
					break;
				case GAME:
					// Spectateur.
					if(gamePlayer.isSpectator()) {
						gamePlayer.getScoreboardSign().setLine(6, ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + SkyWars.instance.countPlayerInGame());
						gamePlayer.getScoreboardSign().setLine(5, ChatColor.GRAY + "Equipes: " + ChatColor.WHITE + SkyWars.instance.teamInGameList.size());
						
					// Joueur en vie.	
					} else {
						if(SkyWars.instance.maxPlayerPerTeam == 1) {
							gamePlayer.getScoreboardSign().setLine(5, ChatColor.GRAY + "Kills: " + ChatColor.WHITE + gamePlayer.getKills());
							gamePlayer.getScoreboardSign().setLine(4, ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + SkyWars.instance.countPlayerInGame());
						} else {
							gamePlayer.getScoreboardSign().setLine(6, ChatColor.GRAY + "Kills: " + ChatColor.WHITE + gamePlayer.getKills());
							gamePlayer.getScoreboardSign().setLine(5, ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + SkyWars.instance.countPlayerInGame());
							gamePlayer.getScoreboardSign().setLine(4, ChatColor.GRAY + "Equipes: " + ChatColor.WHITE + SkyWars.instance.teamInGameList.size());
						}
					}
					break;
				default:
					break;
				}
			}
		}
	}
}
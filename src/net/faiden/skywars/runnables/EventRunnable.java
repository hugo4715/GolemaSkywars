package net.faiden.skywars.runnables;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.chest.IslandChest;
import net.golema.database.support.GameStatus;

public class EventRunnable extends BukkitRunnable {
	
	public int faceEvent = 1;
	public int timer = 180;
	public boolean endIsAnnounce = false;
	
	@Override
	public void run() {
		
		// S�curit� li� au status.
		if(!(GameStatus.isStatus(GameStatus.GAME))) {
			this.cancel();
			return;
		}
		
		// Premi�re face de remplissage !
		if((faceEvent == 1) || (faceEvent == 2)) {
			// Timer == 0
			if(timer == 0) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.GOLD + " Les coffres viennent de se remplir de nouveau.");
				Bukkit.broadcastMessage("");
				IslandChest.refuel(SkyWars.instance.cheatMode);
				if(faceEvent == 1) { faceEvent = 2; } else { faceEvent = 3; timer = 300; return; }
				timer = 120;
				return;
			}
			
			// Runnable Du remplissage
			for(Player playerOnline : Bukkit.getOnlinePlayers()) {
				GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
				if(!(gamePlayer.isSpectator())) {
					if(SkyWars.instance.maxPlayerPerTeam == 1) {
						gamePlayer.getScoreboardSign().setLine(7, ChatColor.GOLD + "Remplissage §7»§6 " + new SimpleDateFormat("m:ss").format(new Date(timer * 1000)));
					} else {
						gamePlayer.getScoreboardSign().setLine(8, ChatColor.GOLD + "Remplissage §7»§6 " + new SimpleDateFormat("m:ss").format(new Date(timer * 1000)));
					}
				}
			}
		} else {
			
			// Timer == 0
			if(timer == 0) {
				
				// Annonce fin de partie.
				if(!(endIsAnnounce)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.RED + " C'est l'heure de la fin de la partie.");
					Bukkit.broadcastMessage("");
					endIsAnnounce = true;
				}
				
				for(Player playerOnline : Bukkit.getOnlinePlayers()) {
					GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
					if(!(gamePlayer.isSpectator())) {
						playerOnline.setHealth(playerOnline.getHealth() - 1);
						if(SkyWars.instance.maxPlayerPerTeam == 1) {
							gamePlayer.getScoreboardSign().setLine(7, ChatColor.GOLD + "Attente victoire");
						} else {
							gamePlayer.getScoreboardSign().setLine(8, ChatColor.GOLD + "Attente victoire");
						}
					}
				}
				return;
			}
			
			// Runnable Du remplissage
			for(Player playerOnline : Bukkit.getOnlinePlayers()) {
				GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
				if(!(gamePlayer.isSpectator())) {
					if(SkyWars.instance.maxPlayerPerTeam == 1) {
						gamePlayer.getScoreboardSign().setLine(7, ChatColor.GOLD + "Fin de partie §7»§6 " + new SimpleDateFormat("m:ss").format(new Date(timer * 1000)));
					} else {
						gamePlayer.getScoreboardSign().setLine(8, ChatColor.GOLD + "Fin de partie §7»§6 " + new SimpleDateFormat("m:ss").format(new Date(timer * 1000)));
					}
				}
			}
		}
		timer--;
	}
}
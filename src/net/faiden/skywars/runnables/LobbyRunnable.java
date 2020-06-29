package net.faiden.skywars.runnables;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.GameManager;

public class LobbyRunnable extends BukkitRunnable {
	
	public static Integer lobbyTimer = 30;
	public static boolean isStarted = false;
	
	public LobbyRunnable() {}
	
	@Override
	public void run() {
		
		// S�curit� si le 'minPlayer' n'est plus respect�.
		if((Bukkit.getOnlinePlayers().size() < SkyWars.instance.minPlayers) && (!(SkyWars.instance.forceStart))) {
			Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.RED 
					+ " Il n'y a pas assez de joueurs pour démarrer la partie!");
			lobbyTimer = 120;
			isStarted = false;
			this.cancel();
			return;
		}
		
		// Acc�l�ration du Timer si le serveur se rempli.
		if((Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) && (lobbyTimer >= 20)) { lobbyTimer = 15; }
		
		// D�marrage de la Partie.
		if(lobbyTimer == 0) {
			new GameManager();
			this.cancel();
			return;
		}
		
		// Gestion de l'�volution du Timer.
		if((lobbyTimer == 120) || (lobbyTimer == 90) || (lobbyTimer == 60) || (lobbyTimer == 30) || (lobbyTimer == 15) || (lobbyTimer == 10) || ((lobbyTimer <= 5) && (lobbyTimer != 0))) {
			Bukkit.broadcastMessage(SkyWars.instance.prefixGame + " " + ChatColor.YELLOW + "Début de la partie dans " + ChatColor.GOLD + lobbyTimer + getSeconds(lobbyTimer) + ChatColor.YELLOW + ".");
			for(Player playerOnline : Bukkit.getOnlinePlayers()) { playerOnline.playSound(playerOnline.getLocation(), Sound.NOTE_PLING, 2.0f, 1.0f); } 
		}
		for(Player playerOnline : Bukkit.getOnlinePlayers()) { 
			playerOnline.setLevel(lobbyTimer);
			SkyWars.getGamePlayer(playerOnline).getScoreboardSign().setLine(3, ChatColor.GRAY + "Lancement: " + ChatColor.WHITE + new SimpleDateFormat("mm:ss").format(new Date(LobbyRunnable.lobbyTimer * 1000)));
		}
		lobbyTimer--;
	}
	
	/**
	 * R�cup�rer la String du Timer.
	 * 
	 * @param timer
	 * @return
	 */
	private String getSeconds(Integer timer) { return timer == 1 ? " seconde" : " secondes"; }
}
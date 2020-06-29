package net.faiden.skywars.runnables;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.golema.database.support.GameStatus;

public class GameRunnable extends BukkitRunnable {

	public static Integer gameTimer = 0;

	public GameRunnable() {}

	@Override
	public void run() {
		
		// Stop le timer � la fin de la Game.
		if(!(GameStatus.isStatus(GameStatus.GAME))) {
			this.cancel();
			return;
		}
		
		// Actualiser les Scoreboard pour les Spectateur
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
			if(gamePlayer.isSpectator()) {
				gamePlayer.getScoreboardSign().setLine(4, ChatColor.GRAY + "Temps: " + ChatColor.AQUA + new SimpleDateFormat("mm:ss").format(new Date(GameRunnable.gameTimer * 1000)));
			}
		}
		gameTimer++;
	}
}
package net.faiden.skywars.listeners.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.faiden.skywars.SkyWars;
import net.golema.database.support.GameStatus;
import net.golema.database.support.builder.TitleBuilder;
import net.golema.database.support.locations.LocationUtils;

public class PlayerMoveListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		switch (GameStatus.getStatus()) {
		case LOBBY:
			// T�l�portation au Hub si le Joueur s'�loigne.
			if (player.getLocation().getBlockY() < 0) {
				player.teleport(LocationUtils.createLocation(Bukkit.getWorld("world"), 1000, 67, 1000, 90.0f, 0.0f));
				new TitleBuilder(ChatColor.GOLD + "SkyWars", ChatColor.RED + "Pourquoi veux-tu t'enfuir ?").send(player);
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
				return; 
			}
			break;
		case GAME:
			
			// Bloquer les joueurs lors de la t�l�portation
			if ((!(SkyWars.instance.canMove))
					&& (!(SkyWars.getGamePlayer(player).isSpectator()))) {
				if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY()
						|| event.getFrom().getZ() != event.getTo().getZ()) {
					Location loc = event.getFrom();
					event.getPlayer().teleport(loc.setDirection(event.getTo().getDirection()));
				}
			}			
			
			// Gestion d'un Joueur qui va dans le vide en �tat Spectateur.
			if(player.getLocation().getBlockY() < 0) {
				if(SkyWars.getGamePlayer(player).isSpectator()) {
					List<Player> gamePlayerList = new ArrayList<Player>();
					for(Player playerOnline : Bukkit.getOnlinePlayers()) {
						if(!(SkyWars.getGamePlayer(playerOnline).isSpectator())) {
							gamePlayerList.add(playerOnline);
						}
					}
					player.teleport(gamePlayerList.get(new Random().nextInt(gamePlayerList.size())).getLocation());
					
				// Acc�l�ration de la mort d'un Joueur qui saute dans le vide.		
				} else { player.setHealth(0.0d); }
			}
			break;
		default:
			break;
		}
	}
}
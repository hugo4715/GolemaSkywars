package net.faiden.skywars.runnables;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.chest.IslandChest;
import net.golema.api.builder.titles.ActionBarBuilder;
import net.golema.api.utils.PlayerUtils;
import net.golema.database.support.builder.TitleBuilder;

public class LaunchRunnable extends BukkitRunnable {
	
	public static Integer timer = 5;
	public static boolean cageIsDestroy = false;
	
	@Override
	public void run() {
		
		// En attente des destructions de Cages.
		if(!(cageIsDestroy)) {
			
			if(timer == 0) {
				
				// Gestion des Coffres.
				IslandChest.refuel(SkyWars.instance.cheatMode);
				
				// Gestion de param�tres de values.
				SkyWars.instance.canMove = true;
				cageIsDestroy = true;
				timer = 5;
				
				// Suppresion des Cages et lancement de la partie.
				for(Player playerOnline : Bukkit.getOnlinePlayers()) {
					
					// Initialisation du GamePlayer
					GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
					
					// Suppresion des cages et Initilisation des joueurs.
					gamePlayer.getCages().removeCage();
					playerOnline.playSound(playerOnline.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
					playerOnline.setWalkSpeed(0.2f);
					playerOnline.setGameMode(GameMode.SURVIVAL);
					PlayerUtils.clearInventory(playerOnline);
					
					// Envoie des kits.
					if(gamePlayer.getKitAbstract() != null) {
						gamePlayer.getKitAbstract().sendKits(playerOnline);
						
					// Envois du kits par d�fauts
					} else {
						playerOnline.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
						playerOnline.getInventory().addItem(new ItemStack(Material.STONE, 32));
						playerOnline.sendMessage(SkyWars.instance.prefixGame + ChatColor.RED + "" + ChatColor.ITALIC + " Vous n'avez pas sélectionné de kit.");
					}
				}
				
				// Message d'alertes de ka destruction des Cages.
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.WHITE + " Les plates-formes sont maintenant détruites, les dégats seront actifs dans " 
											+ ChatColor.RED + timer + " secondes" + ChatColor.WHITE + ".");
				Bukkit.broadcastMessage("");
				
				// Message de lancement pour le mode >> SOLO
				if(SkyWars.instance.maxPlayerPerTeam == 1) {
					Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Les teams ne sont pas autorisées.");
				// Message de lancement pour le mode >> TEAM
				} else {
					Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.RED + " Le cross-team n'est pas autorisé.");
				}

				new TitleBuilder(ChatColor.YELLOW + "SkyWars", ChatColor.LIGHT_PURPLE + "Let's Go ! Bonne chance à tous les joueurs...").broadcast();
				new GameRunnable().runTaskTimer(SkyWars.instance, 0L, 20L);
				new EventRunnable().runTaskTimer(SkyWars.instance, 0L, 20L);

				return;
			}
			
			// Message du Timer en title.
			for(Player playerOnline : Bukkit.getOnlinePlayers()) {
				GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
				if(!(gamePlayer.isSpectator())) {
					if(SkyWars.instance.maxPlayerPerTeam == 1) {
						gamePlayer.getScoreboardSign().setLine(7, ChatColor.GOLD + "Démarrage §7»§6 " + new SimpleDateFormat("m:ss").format(new Date(timer * 1000)));
					} else {
						gamePlayer.getScoreboardSign().setLine(8, ChatColor.GOLD + "Démarrage §7»§6 " + new SimpleDateFormat("m:ss").format(new Date(timer * 1000)));
					}
				}
			}
			new TitleBuilder(ChatColor.AQUA + "" + timer, "").broadcast();
			timer--;
			return;
		}
		
		// En attente de la fin des d�g�ts de Chutes.
		if(timer == 0) {
			SkyWars.instance.damage = true;
			new ActionBarBuilder(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "?" + ChatColor.WHITE + "] Les dégâts sont désormais été activés.").sendToServer();
			this.cancel();
			return;
		}
		
		// Messages sur le Timer des d�gats.
		new ActionBarBuilder(ChatColor.WHITE + "Les dégâts seront actifs dans " + ChatColor.RED + timer + " seconde(s)" + ChatColor.WHITE + ".").sendToServer();
		timer--;
		return;
	}
}
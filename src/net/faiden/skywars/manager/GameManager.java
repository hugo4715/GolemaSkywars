package net.faiden.skywars.manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.cages.Cages;
import net.faiden.skywars.manager.teams.TeamManager;
import net.faiden.skywars.runnables.LaunchRunnable;
import net.golema.api.utils.PlayerUtils;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.game.skywars.stats.SkyWarsStatsType;
import net.golema.database.support.GameStatus;
import net.golema.database.support.boards.TeamsTagsManager;
import net.golema.database.support.builder.TitleBuilder;
import tk.hugo4715.golema.timesaver.TimeSaverAPI;
import tk.hugo4715.golema.timesaver.server.ServerStatus;

public class GameManager {
	
	/**
	 * Lancement de la partie.
	 */
	public GameManager() {
		
		// Initialisation de la partie.
		GameStatus.setStatus(GameStatus.GAME);
		TimeSaverAPI.setJoinable(false);
		TimeSaverAPI.setServerStatus(ServerStatus.INGAME);
		TeamManager.fullAndChargeList();
		SkyWars.instance.canMove = false;
		for(Location location : SkyWars.instance.getSpawnLocationList()) { loadChunks(Bukkit.getWorld("world"), location.getBlockX(), location.getBlockZ(), 20, 20); }
		 
		// Title informatif du d�marrage.
					new TitleBuilder(ChatColor.YELLOW + "SkyWars", ChatColor.AQUA + "Téléportation en cours...").broadcast();
		// Param�tres des Joueurs.
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			
			// R�cup�ration du GamePlayer et Locations.
			GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
			gamePlayer.setPlayed(true);
			GolemaPlayer.getGolemaPlayer(playerOnline).getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.GAMEPLAYED, 1);
			TeamManager.putInARandomTeam(playerOnline);
			Location locationTeam = TeamManager.getTeamLocation(TeamManager.getPlayerTeam(playerOnline));
			
			// Gestion des Cages.
			Location locationCage = new Location(Bukkit.getWorld("world"), locationTeam.getX(), (locationTeam.getY() + 6), locationTeam.getZ());
			gamePlayer.setCages(new Cages(gamePlayer.getCagesInfo(), locationCage));
			gamePlayer.getCages().createCage();
			
			// Gestion de la t�l�portation du Joueur.
			Location locationTP = new Location(Bukkit.getWorld("world"), locationCage.getX(), (locationCage.getY() + 1), locationCage.getZ());
			playerOnline.teleport(locationTP);
			
			// Initialisation du Joueur.
			PlayerUtils.clearInventory(playerOnline);
			playerOnline.setMaxHealth(20.0d);
			playerOnline.setHealth(20.0d);
			playerOnline.setFoodLevel(20);
			playerOnline.setWalkSpeed(0.0f);
			playerOnline.setFlying(false);
			playerOnline.setAllowFlight(false);
			
			// Gestion du Design du TabList.
			// Mode >> SOLO
			if(SkyWars.instance.maxPlayerPerTeam == 1) {
				TeamsTagsManager.setNameTag(playerOnline, playerOnline.getName(), "§f");
			// Mode >> TEAM
			} else {
				TeamsTagsManager.setNameTag(playerOnline, TeamManager.getPlayerTeam(playerOnline).getName(),
						TeamManager.getPlayerTeam(playerOnline).getChatColor() + "");
			}
			
			// Enregistrement des Teams.
			gamePlayer.setTeamInfo(TeamManager.getPlayerTeam(playerOnline));
			if(!SkyWars.instance.teamInGameList.contains(TeamManager.getPlayerTeam(playerOnline))) { SkyWars.instance.teamInGameList.add(TeamManager.getPlayerTeam(playerOnline)); }
			if(!(TeamManager.playerNameList.containsKey(TeamManager.getPlayerTeam(playerOnline)))) { TeamManager.playerNameList.put(TeamManager.getPlayerTeam(playerOnline), new ArrayList<String>()); }
			TeamManager.playerNameList.get(TeamManager.getPlayerTeam(playerOnline)).add(playerOnline.getName());
			
			// SCOREBOARD
			if(SkyWars.instance.maxPlayerPerTeam == 1) {
				gamePlayer.getScoreboardSign().setLine(9, "§d");
				gamePlayer.getScoreboardSign().setLine(8, ChatColor.WHITE + "Prochain événement:");
				gamePlayer.getScoreboardSign().setLine(7, ChatColor.GOLD + "Démarrage 0:05");
				gamePlayer.getScoreboardSign().setLine(6, "§c");
				gamePlayer.getScoreboardSign().setLine(5, ChatColor.GRAY + "Kills: " + ChatColor.WHITE + gamePlayer.getKills());
				gamePlayer.getScoreboardSign().setLine(4, ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + SkyWars.instance.countPlayerInGame());
			} else {
				gamePlayer.getScoreboardSign().setLine(10, "§d");
				gamePlayer.getScoreboardSign().setLine(9, ChatColor.WHITE + "Prochain événement:");
				gamePlayer.getScoreboardSign().setLine(8, ChatColor.GOLD + "Démarrage 0:05");
				gamePlayer.getScoreboardSign().setLine(7, "§c");
				gamePlayer.getScoreboardSign().setLine(6, ChatColor.GRAY + "Kills: " + ChatColor.WHITE + gamePlayer.getKills());
				gamePlayer.getScoreboardSign().setLine(5, ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + SkyWars.instance.countPlayerInGame());
				gamePlayer.getScoreboardSign().setLine(4, ChatColor.GRAY + "Equipes: " + ChatColor.WHITE + SkyWars.instance.teamInGameList.size());
			}
			gamePlayer.getScoreboardSign().setLine(3, "§b");
		}
		
		// Annonce du Lancement de la partie et d�marrage du LaunchRunnable.
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "?" + ChatColor.WHITE + "] La partie a désormais été démarrée. La destruction de vos plates-formes aura lieu dans " 
										+ ChatColor.LIGHT_PURPLE + "5 secondes" + ChatColor.WHITE + ".");
		new LaunchRunnable().runTaskTimer(SkyWars.instance, 0L, 20L);
	}
	
	// Generate the chunks around the schematic
	private static void loadChunks(World worldf, int x, int z, int sizeX, int sizeY) {
		sizeX = sizeX / 32;
		sizeY = sizeY / 32;
		sizeX++;
		sizeY++;

		Chunk chunk = worldf.getChunkAt(new org.bukkit.Location(worldf, x, 40, z));
		chunk.load(true);
		int cx = chunk.getX() - sizeX;
		int cz = chunk.getZ() - sizeY;
		while (cx < chunk.getX() + sizeX) {
			while (cz < chunk.getZ() + sizeY) {
				if (cx != chunk.getX() || cz != chunk.getZ()) {
					worldf.getChunkAt(cx, cz).load(true);
				}
				cz++;
			}
			cx++;
		}
	}
}
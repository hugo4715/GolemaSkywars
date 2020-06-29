package net.faiden.skywars.manager.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.faiden.skywars.SkyWars;

public class TeamManager {
	
	public static List<TeamInfo> teamList = new ArrayList<TeamInfo>();
	public static Map<TeamInfo, List<Player>> playerTeamList = new HashMap<TeamInfo, List<Player>>();
	public static Map<TeamInfo, List<String>> playerNameList = new HashMap<TeamInfo, List<String>>();
	public static Map<TeamInfo, Location> teamLocationList = new HashMap<TeamInfo, Location>();
	
	// CONSTRUCTOR
	public TeamManager() {}
	
	/**
	 * Ajouter un Joueur à une Team.
	 * 
	 * @param player
	 * @param teamInfo
	 */
	public static void addPlayerInTeam(Player player, TeamInfo teamInfo) {
		if(playerTeamList.get(teamInfo) == null) { playerTeamList.put(teamInfo, new ArrayList<Player>()); }
		playerTeamList.get(teamInfo).add(player);
	}
	
	/**
	 * Enlever un Joueur d'une Team.
	 * 
	 * @param player
	 * @param teamInfo
	 */
	public static void removePlayerInTeam(Player player, TeamInfo teamInfo) {
		playerTeamList.get(teamInfo).remove(player);
	}
	
	/**
	 * Supprimer le Joueur de toutes les Teams.
	 * 
	 * @param player
	 */
	public static void removePlayerInAllTeam(Player player) {
		for(TeamInfo teamInfo : TeamInfo.values()) {
			if(playerTeamList.get(teamInfo) != null) {
				if(playerTeamList.get(teamInfo).contains(player)) {
					playerTeamList.get(teamInfo).remove(player);
					if(playerTeamList.get(teamInfo).isEmpty()
							|| playerTeamList.get(teamInfo).size() == 0) {
						SkyWars.instance.teamInGameList.remove(teamInfo);
					}
				}
			}
		}
	}
	
	/**
	 * Savoir si une Team est plein.
	 * 
	 * @param teamInfo
	 * @return
	 */
	public boolean teamIsFull(TeamInfo teamInfo) {
		if(playerTeamList.get(teamInfo).size() >= SkyWars.instance.maxPlayerPerTeam) {
			return true;
		}
		return false;
	}
	
	/**
	 * Ajouter un Joueur à une Team Aléatoire.
	 * 
	 * @param player
	 */
	public static void putInARandomTeam(Player player) {
		if(!(hasPlayerTeam(player))) {
			for(TeamInfo teamInfo : TeamInfo.values()) {
				if(playerTeamList.get(teamInfo).size() >= SkyWars.instance.maxPlayerPerTeam) {
					teamList.remove(teamInfo);
				}
			}
			TeamInfo teamInfo = teamList.get(new Random().nextInt(teamList.size()));
			if(playerTeamList.get(teamInfo) == null) { playerTeamList.put(teamInfo, new ArrayList<Player>()); }
			playerTeamList.get(teamInfo).add(player);
		}
	}
	
	/**
	 * Récupérer la Team d'un Joueur.
	 * 
	 * @param player
	 * @return
	 */
	public static TeamInfo getPlayerTeam(Player player) {
		for(TeamInfo teamInfo : teamList) {
			if(playerTeamList.get(teamInfo).contains(player)) {
				return teamInfo;
			}
		}
		return null;
	}
	
	/**
	 * Récupérer la Location d'une Team.
	 * 
	 * @param teamInfo
	 * @return
	 */
	public static Location getTeamLocation(TeamInfo teamInfo) {
		return teamLocationList.get(teamInfo);
	}
	
	/**
	 * Récupérer si le Joueur est dans une Team.
	 * 
	 * @param player
	 * @param teamInfo
	 * @return
	 */
	public static boolean isPlayerInTeam(Player player, TeamInfo teamInfo) {
		return playerTeamList.get(teamInfo).contains(player);
	}
	
	/**
	 * Récupérer si le Joueur possède une Team.
	 * 
	 * @param player
	 * @return
	 */
	public static boolean hasPlayerTeam(Player player) {
		for(TeamInfo teamInfo : TeamInfo.values()) {
			if(playerTeamList.get(teamInfo).contains(player)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Récupérer la List des Teams.
	 * 
	 * @return
	 */
	public static List<TeamInfo> getTeamList() {
		return teamList;
	}
	
	/**
	 * Récupérer la Map des Player & Team.
	 * 
	 * @return
	 */
	public static Map<TeamInfo, List<Player>> getPlayerTeamList() {
		return playerTeamList;
	}
	
	/**
	 * Charger les Teams au lancement du Serveur
	 */
	public static void fullAndChargeList() {
		for(TeamInfo teamInfo : TeamInfo.values()) {
			
			// Team List
			if(!(teamList.contains(teamInfo))) {
				teamList.add(teamInfo);
			}
			
			// Team et Joueur
			if(playerTeamList.get(teamInfo) == null) {
				playerTeamList.put(teamInfo, new ArrayList<Player>());
			}
			
			// Location
			if(teamLocationList.get(teamInfo) == null) {
				Location locationRandom = (Location) SkyWars.spawnLocations.get(new Random().nextInt(SkyWars.spawnLocations.size()));
				teamLocationList.put(teamInfo, locationRandom);
				SkyWars.spawnLocations.remove(locationRandom);
			}
		}
	}
}
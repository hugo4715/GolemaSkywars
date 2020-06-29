package net.faiden.skywars; 

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.faiden.skywars.listeners.ListenerManager;
import net.faiden.skywars.manager.perks.PerkChecker;
import net.faiden.skywars.manager.teams.TeamInfo;
import net.faiden.skywars.runnables.LobbyRunnable;
import net.faiden.skywars.runnables.ScoreboardRunnable;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.game.skywars.perks.SkyWarsPerksMode;
import net.golema.database.golemaplayer.game.skywars.stats.SkyWarsStatsMode;
import net.golema.database.golemaplayer.rank.Rank;
import net.golema.database.support.GameStatus;
import net.golema.database.support.configs.FileManager;
import net.golema.database.support.configs.FileManager.Config;
import net.golema.database.support.world.WorldManager;
import tk.hugo4715.golema.timesaver.TimeSaverAPI;
import tk.hugo4715.golema.timesaver.server.GameInfos;
import tk.hugo4715.golema.timesaver.server.ServerStatus;
import tk.hugo4715.golema.timesaver.server.ServerType;

public class SkyWars extends JavaPlugin {
	
	public static List<Location> spawnLocations = new ArrayList<Location>();
	public List<MapGameInfos> mapInfosList = new ArrayList<MapGameInfos>();
	public List<TeamInfo> teamInGameList = new ArrayList<TeamInfo>();
	
	private static Map<String, GamePlayer> gamePlayersMap = new HashMap<String, GamePlayer>();
	
	public String prefixGame = ChatColor.AQUA + "" + ChatColor.BOLD + "SkyWars" + ChatColor.WHITE + "│";
	public Integer maxPlayerPerTeam = getConfig().getInt("playerPerTeam");
	public Integer minPlayers = 8;
	public boolean damage = false;
	public boolean canMove = true;
	public boolean forceStart = false;
	public boolean cheatMode = getConfig().getBoolean("cheatMode");
	
	public FileManager fileManager;
	public Config mapLocationsConfig;
	public MapGameInfos mapGameInfos;
	
	private PerkChecker perkChecker;
	
	public static SkyWars instance;
	
	@Override
	public void onLoad() {
		
		// Définition de l'instance du Plugin.
		instance = this;
		
		// Gestion de la Map et des Configurations.
		Bukkit.unloadWorld("world", false);
		for(MapGameInfos mapInfos : MapGameInfos.values()) { mapInfosList.add(mapInfos); }
		this.mapGameInfos = mapInfosList.get(new Random().nextInt(mapInfosList.size()));
		fileManager = new FileManager(instance);
		mapLocationsConfig = fileManager.getConfig("maps/" + mapGameInfos.getConfigName());
		mapLocationsConfig.copyDefaults(true).save();
		WorldManager.deleteWorld("world");
		File from = new File("maps/" + mapGameInfos.getMapName());
		File to = new File("world");
		try {
			WorldManager.copyFolder(from, to); 
		} catch (IOException e) {
			System.err.println("Erreur: Le serveur n'arrive pas à copier la Map : " + mapGameInfos.getMapName()); 
		}
		this.saveDefaultConfig();
		super.onLoad();
	}
	
	@Override
	public void onEnable() {
		
		// Initialisation des paramètres de la partie.
		this.registerLocations();
		new ListenerManager(instance).registerListeners();
		new ScoreboardRunnable().runTaskTimer(instance, 0L, 20L);
		GameStatus.setStatus(GameStatus.LOBBY);
		
		// Perks et Kits.
		this.perkChecker = new PerkChecker();
		
		// Initialisation du TimeSaver.
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				TimeSaverAPI.setServerMap(mapGameInfos.getMapName());
				TimeSaverAPI.setServerStatus(ServerStatus.ALLOW);
				TimeSaverAPI.setServerGame(getGameInfos());
				TimeSaverAPI.setServerType(ServerType.GAME);
				TimeSaverAPI.setJoinable(true);
			}
		}, 40L);
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() { 	
		super.onDisable(); 
	}

	/**
	 * Récupérer le type de partie.
	 * 
	 * @return
	 */
	public GameInfos getGameInfos() {
		if (getConfig().getInt("playerPerTeam") > 1) {
			if (cheatMode) return GameInfos.SKYWARSTEAMCHEAT;
			return GameInfos.SKYWARSTEAMNORMAL;
		} else {
			if (cheatMode) return GameInfos.SKYWARSSOLOCHEAT;
			return GameInfos.SKYWARSSOLONORMAL;
		}
	}
	
	/**
	 * Récupérer le SkyWarsStatsMode.
	 * 
	 * @return
	 */
	public SkyWarsStatsMode getSkyWarsStatsMode() {
		if (getConfig().getInt("playerPerTeam") > 1) {
			if (cheatMode) return SkyWarsStatsMode.TEAMCHEAT;
			return SkyWarsStatsMode.TEAMNORMAL;
		} else {
			if (cheatMode) return SkyWarsStatsMode.SOLOCHEAT;
			return SkyWarsStatsMode.SOLONORMAL;
		}
	}
	
	/**
	 * Récupérer le Perk Mode de la partie.
	 * 
	 * @return
	 */
	public SkyWarsPerksMode getSkyWarsPerkMode() {
		if (getConfig().getInt("playerPerTeam") > 1) return SkyWarsPerksMode.SKYWARS_TEAM;
		return SkyWarsPerksMode.SKYWARS_SOLO;
	}
	
	/**
	 * Enregister les Locations.
	 */
	public void registerLocations() {
		for (int i = 1; i <= 12; i++) {
			spawnLocations.add(new Location(Bukkit.getWorld("world"), mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".x"), mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".y"),
					mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".z")));
		}
	}
	
	/**
	 * Récupérer la liste des Spawns.
	 */
	public List<Location> getSpawnLocationList() {
		List<Location> spawnList = new ArrayList<Location>();
		for (int i = 1; i <= 12; i++) {
			spawnList.add(new Location(Bukkit.getWorld("world"), mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".x"), mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".y"),
					mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".z")));
		}
		return spawnList;
	}
	
	/**
	 * Récupérer un GamePlayer.
	 * 
	 * @param player
	 * @return
	 */
	public static GamePlayer getGamePlayer(Player player) {
		if(gamePlayersMap.get(player.getName()) == null) { gamePlayersMap.put(player.getName(), new GamePlayer(player)); }
		return gamePlayersMap.get(player.getName());
	}
	
	/**
	 * Récupérer la map des GamePlyers.
	 * 
	 * @return
	 */
	public static Map<String, GamePlayer> getGamePlayersMap() {
		return gamePlayersMap;
	}
	
	/**
	 * Compter le nombre de Joueurs en vie dans la partie.
	 * 
	 * @return
	 */
	public Integer countPlayerInGame() {
		List<Player> playerInGame = new ArrayList<Player>();
		for(Player playerOnline : Bukkit.getOnlinePlayers()) {
			if((SkyWars.getGamePlayer(playerOnline) != null)&& (!(SkyWars.getGamePlayer(playerOnline).isSpectator()))) {
				playerInGame.add(playerOnline);
			}
		}
		return playerInGame.size();
	}
	
	/**
	 * Récupérer le String qui correspond au mode de Jeu.
	 * @return
	 */
	public String getNameStringMode() {
		if(getConfig().getInt("playerPerTeam") > 1) {
			if(cheatMode) {
				return ChatColor.RED + "Team | Mode Cheat";
			} else {
				return ChatColor.RED + "Team | Mode Normal";
			}
		} else {
			if(cheatMode) {
				return ChatColor.RED + "Solo | Mode Cheat";
			} else {
				return ChatColor.RED + "Solo | Mode Normal";
			}
		}
	}
	
	public PerkChecker getPerkChecker() {
		return perkChecker;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Vérifiez qui effectué la commande.
		if(!(sender instanceof Player)) {
			System.out.println("Vous devez être un joueur pour utiliser cette commande.");
			return false;
		}
		
		// Mise en oeuvre de la commande '/start'.
		if(label.equalsIgnoreCase("start")) {
			Player player = (Player) sender;
			GolemaPlayer golemaPlayer = GolemaPlayer.getGolemaPlayer(player);
			if(golemaPlayer.getRankPower() >= Rank.YOUTUBER.getPower()) {
				if(!(LobbyRunnable.isStarted)) {
					if(Bukkit.getOnlinePlayers().size() == 1) {
						player.sendMessage(ChatColor.RED + "Erreur: Vous ne pouvez pas jouer seul.");
						return false;
					}
					this.forceStart = true;
					new LobbyRunnable().runTaskTimer(SkyWars.instance, 0L, 20L);
					LobbyRunnable.isStarted = true;
					Bukkit.broadcastMessage(prefixGame + ChatColor.YELLOW + " La partie vient d'être démarrée.");
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "Erreur: La partie à déjà été lancée.");
					return false;
				}
			} else {
				golemaPlayer.sendMessageNoPermission();
				return false;
			}
		}
		return false;
	}
}
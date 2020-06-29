package net.faiden.skywars;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import net.faiden.skywars.manager.WinManager;
import net.faiden.skywars.manager.cages.Cages;
import net.faiden.skywars.manager.cages.CagesInfo;
import net.faiden.skywars.manager.kits.KitAbstract;
import net.faiden.skywars.manager.teams.TeamInfo;
import net.faiden.skywars.manager.teams.TeamManager;
import net.faiden.skywars.runnables.GameRunnable;
import net.faiden.skywars.runnables.LobbyRunnable;
import net.golema.api.builder.board.ScoreboardSign;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.game.skywars.stats.SkyWarsStatsType;
import net.golema.database.golemaplayer.levels.LevelMechanic;
import net.golema.database.golemaplayer.levels.LevelType;
import net.golema.database.support.GameStatus;
import net.golema.database.support.boards.TeamsTagsManager;
import net.golema.database.support.utils.GolemaLogger;

public class GamePlayer {
	
	public Player player;
	private GolemaPlayer golemaPlayer;
	
	private int coinsWin;
	private int creditsWin;
	private int experience;
	
	private int gamePlayed;
	private int gameWins;
	private int kills;
	private int death;
	private int blocPlace;
	private int blocBreak; 
	private boolean played;
	
	private TeamInfo teamInfo;
	private KitAbstract kitAbstract = null;
	
	private Cages cages;
	private CagesInfo cagesInfo = CagesInfo.DEFAULT;
	
	private boolean isSpectator;
	private ScoreboardSign scoreboardSign;
	
	/**
	 * Constructeur du GamePlayer.
	 * 
	 * @param player
	 */
	public GamePlayer(Player player) {
		
		// Variables liés au Joueur.
		this.player = player;
		this.golemaPlayer = GolemaPlayer.getGolemaPlayer(player);
		
		// Statistiques de la partie (Coins).
		this.coinsWin = 0;
		this.creditsWin = 0;
		this.experience = 0;
		
		// Statistiques de la partie (Utils).
		this.gamePlayed = 0;
		this.gameWins = 0;
		this.kills = 0;
		this.death = 0;
		this.blocPlace = 0;
		this.blocBreak = 0;
		
		// Définir le mode spectateur d'un Joueur.
		if(GameStatus.isStatus(GameStatus.LOBBY)) { this.isSpectator = false; } else { this.isSpectator = true; }		
		
		// Mise en place du Scoreboard.
		this.scoreboardSign = new ScoreboardSign(player, player.getName());
		this.makeScoreboard();
		
		// Log de création.
		GolemaLogger.logDebug("[GamePlayer] Created " + player.getName() + " succes.");
	}
	
	/**
	 * Update les stats skywars.
	 */
	public void updatePlayerStats() {
		if(played) {
			golemaPlayer.getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.BLOCKSPLACES, blocPlace);
			golemaPlayer.getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.BLOCKSBREAKS, blocBreak);
		}
	} 
	
	/**
	 * Créer le scoreboard du Joueur.
	 */
	private void makeScoreboard() {
		this.scoreboardSign.setObjectiveName(ChatColor.YELLOW + "" + ChatColor.BOLD + "SkyWars");
		this.scoreboardSign.create();

		// Spectator
		if(isSpectator) {
			this.scoreboardSign.setLine(2, "§7Map: §b" + SkyWars.instance.mapGameInfos.getMapName());
			this.scoreboardSign.setLine(1, "§a");
			this.scoreboardSign.setLine(0, ChatColor.YELLOW + "play.golemamc.net");
			
		// Player	
		} else {
			this.scoreboardSign.setLine(9, "§d");
			this.scoreboardSign.setLine(8, ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Bukkit.getMaxPlayers());
			this.scoreboardSign.setLine(7, "§c");
			this.scoreboardSign.setLine(6, ChatColor.GOLD + "En attente de");
			this.scoreboardSign.setLine(5, ChatColor.GOLD + "joueurs...");
			this.scoreboardSign.setLine(4, "§b");
			this.scoreboardSign.setLine(3, ChatColor.GRAY + "Lancement: " + ChatColor.WHITE + new SimpleDateFormat("mm:ss").format(new Date(LobbyRunnable.lobbyTimer * 1000)));
			this.scoreboardSign.setLine(2, ChatColor.GRAY + "Map: " + ChatColor.WHITE + SkyWars.instance.mapGameInfos.getMapName());
			this.scoreboardSign.setLine(1, "§a");
			this.scoreboardSign.setLine(0, ChatColor.YELLOW + "play.golemamc.net");
		}
	}
	
	/**
	 * Définir le joueur au status de Spectateur.
	 */
	public void setSpectator() {
		this.isSpectator = true;
		
		// Tag de partie.
		TeamsTagsManager.setNameTag(player, "§z§7SPECTATOR", ChatColor.GRAY + "[SPEC] ");
		if(TeamManager.playerTeamList.get(teamInfo) != null) {
			if((TeamManager.playerTeamList.get(teamInfo).contains(player)) 
					&& (TeamManager.playerTeamList.get(teamInfo).size() == 1)) {
				SkyWars.instance.teamInGameList.remove(teamInfo);
				
				// Message d'élimination de l'équipe (SI != SOLO)
				if(SkyWars.instance.maxPlayerPerTeam != 1) {
					Bukkit.broadcastMessage(SkyWars.instance.prefixGame + " " + ChatColor.YELLOW + "L'équipe " + teamInfo.getChatColor() + "[" + teamInfo.getName() + "] "
							+ ChatColor.YELLOW + "a été éliminée !");
				}
				
			}
			TeamManager.playerTeamList.get(teamInfo).remove(player);
		}
		
		// Scoreboard.
		this.scoreboardSign.setLine(10, "§c");
		this.scoreboardSign.setLine(9, ChatColor.GOLD + "Vous êtes en mode");
		this.scoreboardSign.setLine(8, ChatColor.GOLD + "spectateur...");
		this.scoreboardSign.setLine(7, "§c");
		this.scoreboardSign.setLine(6, ChatColor.GRAY + "Joueurs: §f0");
		this.scoreboardSign.setLine(5, ChatColor.GRAY + "Equipes: " + ChatColor.WHITE + SkyWars.instance.teamInGameList.size());
		this.scoreboardSign.setLine(4, ChatColor.GRAY + "Temps: " + ChatColor.AQUA + new SimpleDateFormat("mm:ss").format(new Date(GameRunnable.gameTimer * 1000)));
		this.scoreboardSign.setLine(3, "§b");
		
		player.setGameMode(GameMode.SPECTATOR);
		new WinManager();
	}
	
	/*
	 * Getter du GamePlayer.
	 */
	public GolemaPlayer getGolemaPlayer() {
		return golemaPlayer;
	}
	
	public Integer getCoinsWin() {
		return coinsWin;
	}
	
	public int getCreditsWin() {
		return creditsWin;
	}
	
	public int getExperience() {
		return experience;
	}
	
	public int getGamePlayed() {
		return gamePlayed;
	}
	
	public int getGameWins() {
		return gameWins;
	}
	
	public int getKills() {
		return kills;
	}
	
	public int getDeath() {
		return death;
	}
	
	public int getBlocPlace() {
		return blocPlace;
	}
	
	public int getBlocBreak() {
		return blocBreak;
	}
	
	public TeamInfo getTeamInfo() {
		return teamInfo;
	}
	
	public KitAbstract getKitAbstract() {
		return kitAbstract;
	}
	
	public Cages getCages() {
		return cages;
	}
	
	public CagesInfo getCagesInfo() {
		return cagesInfo;
	}
	
	public boolean isSpectator() {
		return isSpectator;
	}
	
	public boolean isPlayed() {
		return played;
	}
	
	public ScoreboardSign getScoreboardSign() {
		return scoreboardSign;
	}
	
	/**
	 * Setter du GamePlayer.
	 */
	public void setGolemaPlayer(GolemaPlayer golemaPlayer) {
		this.golemaPlayer = golemaPlayer;
	}
	
	public void setCoinsWin(Integer coinsWin) {
		this.coinsWin = coinsWin;
	}
	
	public void setCreditsWin(int creditsWin) {
		this.creditsWin = creditsWin;
	}
	
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	public void setGamePlayed(int gamePlayed) {
		this.gamePlayed = gamePlayed;
	}
	
	public void setGameWins(int gameWins) {
		this.gameWins = gameWins;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void setDeath(int death) {
		this.death = death;
	}
	
	public void setBlocBreak(int blocBreak) {
		this.blocBreak = blocBreak;
	}
	
	public void setBlocPlace(int blocPlace) {
		this.blocPlace = blocPlace;
	}
	
	public void setTeamInfo(TeamInfo teamInfo) {
		this.teamInfo = teamInfo;
	}
	
	public void setKitAbstract(KitAbstract kitAbstract) {
		this.kitAbstract = kitAbstract;
	}
	
	public void setCages(Cages cages) {
		this.cages = cages;
	}
	
	public void setCagesInfo(CagesInfo cagesInfo) {
		this.cagesInfo = cagesInfo;
	}
	
	public void setScoreboardSign(ScoreboardSign scoreboardSign) {
		this.scoreboardSign = scoreboardSign;
	}
	
	public void setPlayed(boolean played) {
		this.played = played;
	}
	
	/**
	 * Message de récompenses.
	 */
	public void rewardMessage() {
		if(played) {
			player.sendMessage("");
			player.sendMessage("§6§lRécapitulatif§f│ §aTout ce que vous devez savoir:");
			player.sendMessage("");
			player.sendMessage("§f● Statistiques:");
			player.sendMessage("§7■ §bKills §7» §f" + kills);
			player.sendMessage("§7■ §bBlocs placés §7» §f" + blocPlace);
			player.sendMessage("§7■ §bBlocs cassés §7» §f" + blocBreak);
			player.sendMessage("");
			player.sendMessage("§f● Gains:");
			player.sendMessage("§7■ §e" + coinsWin + " Coins");
			player.sendMessage("§7■ §b" + creditsWin + " GolemaCrédits");
			player.sendMessage("§7■ §d" + experience + " Expériences");
			player.sendMessage("§7■ §7Progression de l'expérience ["
					+ LevelMechanic.getPlayerProgressBarLevel(golemaPlayer.getExperienceLevel(LevelType.SKYWARS_LEVEL),
							"●", net.md_5.bungee.api.ChatColor.LIGHT_PURPLE, net.md_5.bungee.api.ChatColor.WHITE)
					+ "§7]");
			player.sendMessage("");
		}
	}
}
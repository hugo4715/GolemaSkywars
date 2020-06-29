package net.faiden.skywars.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.faiden.skywars.manager.teams.TeamInfo;
import net.faiden.skywars.manager.teams.TeamManager;
import net.faiden.skywars.runnables.GameRunnable;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.currency.Currency;
import net.golema.database.golemaplayer.game.luckbox.LuckBoxType;
import net.golema.database.golemaplayer.game.skywars.stats.SkyWarsStatsType;
import net.golema.database.golemaplayer.levels.LevelType;
import net.golema.database.golemaplayer.rank.Rank;
import net.golema.database.support.GameStatus;
import net.golema.database.support.builder.TitleBuilder;
import net.golema.database.support.servers.SwitchServer;

public class WinManager {
	
	public WinManager() {
		if(GameStatus.isStatus(GameStatus.GAME)) {
			
			// Les joueurs se sont tous déconnectés.
			if(Bukkit.getOnlinePlayers().size() == 0) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				return;
			}
			
			// Détection d'une Victoire.
			if(SkyWars.instance.teamInGameList.size() == 1) {
				 
				GameStatus.setStatus(GameStatus.FINISH);
				
				// Détection de la Team gagnante.
				for(TeamInfo teamInfo : SkyWars.instance.teamInGameList) {
					Bukkit.broadcastMessage(" "); 
					String winnerMessage = "";
		            for (String winnerName : TeamManager.playerNameList.get(teamInfo)) {
		            	winnerMessage = winnerMessage + ", " + winnerName;
		            }
		            winnerMessage = winnerMessage.replaceFirst(", ", "");
		            winnerMessage.trim();
		            
		            // Message de victoire pour le mode >> SOLO
		            if(SkyWars.instance.maxPlayerPerTeam == 1) {
		            	Player playerWin = Bukkit.getPlayer(winnerMessage);
		            	if(playerWin != null) {
		            		Rank rank = GolemaPlayer.getGolemaPlayer(playerWin).getRank();	     
			            	if(rank != null) { 
			            		Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.GREEN + " Félicitations, victoire de " + rank.getChatColor() + rank.getPrefix() + getSpaceAllow(rank) 
			            					+ winnerMessage);
			            		new TitleBuilder("", ChatColor.GOLD + "Victoire de " + rank.getChatColor() + rank.getPrefix() + getSpaceAllow(rank) + winnerMessage).broadcast(); 
			            	}
		            	}
		            // Message de victoire pour le mode >> TEAM
		            } else {
		            	Bukkit.broadcastMessage(SkyWars.instance.prefixGame + ChatColor.GREEN + " Victoire de l'équipe " + teamInfo.getChatColor() + teamInfo.getName() 
            			+ ChatColor.GRAY + " (" + winnerMessage + ")");
		            	new TitleBuilder(ChatColor.GOLD + "Victoire de l'équipe " + teamInfo.getChatColor() + teamInfo.getName(), ChatColor.GRAY + " [" + winnerMessage + "]").broadcast();
		     
		            }
		            
		            // Gestion des Coins
		            List<Player> winnerPlayerList = new ArrayList<Player>();
		            for(Player playerOnline : Bukkit.getOnlinePlayers()) { if(playerOnline.getName().equalsIgnoreCase(winnerMessage)) { winnerPlayerList.add(playerOnline); } }
		            for(Player playerWinner : winnerPlayerList) {
		            
		            	GamePlayer gamePlayerWinner = SkyWars.getGamePlayer(playerWinner);
		            	GolemaPlayer golemaPlayerWinner = GolemaPlayer.getGolemaPlayer(playerWinner);
		            	playerWinner.playSound(playerWinner.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
		            	GolemaPlayer.getGolemaPlayer(playerWinner).getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.WINS, 1);
		            	
		    			// Envoie des Coins au target.
		    			int coinsReward = 10;
		    			gamePlayerWinner.setCoinsWin(gamePlayerWinner.getCoinsWin() + coinsReward);
		    			golemaPlayerWinner.addCoinsGame(Currency.GCOINS, coinsReward, "Victoire");
	    				
	    				// Envoie des Crédits au target.
	    				int creditsReward = 1;
	    				gamePlayerWinner.setCreditsWin(gamePlayerWinner.getCreditsWin() + creditsReward);
	    				golemaPlayerWinner.addCreditsGame(creditsReward, "Victoire");
	    				
	    				// Envoie de l'Experience au target.
	    				int experienceReward = 20;
	    				gamePlayerWinner.setExperience(gamePlayerWinner.getExperience() + experienceReward);
	    				golemaPlayerWinner.addExperience(LevelType.SKYWARS_LEVEL, LuckBoxType.LUCKBOX_SKYWARS, experienceReward);
		            }
		            
		            // Envoie du nouveau Scoreboard et message de Récompenses.
					for(Player playerOnline : Bukkit.getOnlinePlayers()) {
						
						// Récupérer le GamePlayer.
						GamePlayer gamePlayer = SkyWars.getGamePlayer(playerOnline);
						@SuppressWarnings("unused")
						GolemaPlayer golemaPlayer = GolemaPlayer.getGolemaPlayer(playerOnline);
						gamePlayer.updatePlayerStats();
						
						// Scoreboard
						gamePlayer.getScoreboardSign().setLine(10, "");
						gamePlayer.getScoreboardSign().setLine(9, ChatColor.GOLD + "Fin de la partie,");
	        			gamePlayer.getScoreboardSign().setLine(8, ChatColor.GOLD + "faites " + ChatColor.YELLOW + "" + ChatColor.BOLD + "/lobby" + ChatColor.GOLD + ".");
	        			gamePlayer.getScoreboardSign().setLine(7, "§c");
	        			gamePlayer.getScoreboardSign().setLine(6, ChatColor.GRAY + "Gagnant:");
	        			if(SkyWars.instance.maxPlayerPerTeam == 1) {
	        				gamePlayer.getScoreboardSign().setLine(5, ChatColor.AQUA + winnerMessage);
	        			} else { gamePlayer.getScoreboardSign().setLine(5, teamInfo.getChatColor() + "" + ChatColor.BOLD + "Equipe ".toUpperCase() + teamInfo.getName().toUpperCase()); }
						gamePlayer.getScoreboardSign().setLine(4, "§b");
						gamePlayer.getScoreboardSign().setLine(3, ChatColor.GRAY + "Durée: " + ChatColor.WHITE + new SimpleDateFormat("mm:ss").format(new Date(GameRunnable.gameTimer * 1000)));
						
						// Message de Récompenses.
						gamePlayer.rewardMessage(); 
						
					}
					
					// Fin de Game.
					Bukkit.getScheduler().runTaskLater(SkyWars.instance, new Runnable() {
						@Override
						public void run() {
							for(Player playerOnline : Bukkit.getOnlinePlayers()) {
								SwitchServer.sendPlayerToLobby(playerOnline, true);
							}
							Bukkit.getScheduler().runTaskLater(SkyWars.instance, new Runnable() {
								@Override
								public void run() {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
								}
							}, 20 * 2L);
							
						}
					}, 20 * 10L);
				}
			}
		}
	}
	
	/**
	 * Espace en fonction du Rank.
	 * 
	 * @param rank
	 * @return
	 */
	private String getSpaceAllow(Rank rank) { return rank.getIdentificatorName().equals(Rank.PLAYER.getIdentificatorName()) ? "" : " "; }
}

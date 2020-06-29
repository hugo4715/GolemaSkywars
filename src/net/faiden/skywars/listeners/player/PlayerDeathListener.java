package net.faiden.skywars.listeners.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.currency.Currency;
import net.golema.database.golemaplayer.game.skywars.stats.SkyWarsStatsType;
import net.golema.database.golemaplayer.rank.Rank;
import net.golema.database.support.GameStatus;
import net.golema.database.support.builder.JsonMessageBuilder;

public class PlayerDeathListener implements Listener {

	public Map<Player, Player> playerKillerMap = new HashMap<Player, Player>();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event) {

		// Initialisation des variables.
		Player player = event.getEntity();
		GolemaPlayer golemaPlayer = GolemaPlayer.getGolemaPlayer(player);
		GamePlayer gamePlayer = SkyWars.getGamePlayer(player);
		event.setDeathMessage(null);

		// Vérifications des événements.
		if (!(GameStatus.isStatus(GameStatus.GAME)))
			return;
		if (gamePlayer == null)
			return;
		gamePlayer.setDeath(gamePlayer.getDeath() + 1);
		gamePlayer.updatePlayerStats();

		// Message de mort par une autre Entity.
		if (playerKillerMap.get(player) != null) {

			// Variable du Killer/Shooter
			Player shooter = playerKillerMap.get(player);
			GamePlayer gamePlayerShooter = SkyWars.getGamePlayer(shooter);
			GolemaPlayer golemaPlayerShooter = GolemaPlayer.getGolemaPlayer(shooter);
			gamePlayerShooter.setKills(gamePlayerShooter.getKills() + 1);
			GolemaPlayer.getGolemaPlayer(shooter).getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.KILLS, 1);

			// Mort en mode Solo.
			if (SkyWars.instance.maxPlayerPerTeam == 1) {
				Bukkit.broadcastMessage(golemaPlayer.getRank().getChatColor() + golemaPlayer.getRank().getPrefix()
						+ getSpaceAllow(golemaPlayer.getRank()) + player.getName() + ChatColor.YELLOW
						+ " a été tué par " + GolemaPlayer.getGolemaPlayer(shooter).getRank().getChatColor()
						+ GolemaPlayer.getGolemaPlayer(shooter).getRank().getPrefix()
						+ getSpaceAllow(GolemaPlayer.getGolemaPlayer(shooter).getRank()) + shooter.getName()
						+ ChatColor.YELLOW + ".");
				playerKillerMap.remove(player);

				// Mort en mode Team.
			} else {
				Bukkit.broadcastMessage(gamePlayer.getTeamInfo().getChatColor() + "["
						+ gamePlayer.getTeamInfo().getName() + "] " + player.getName() + ChatColor.YELLOW
						+ " a été tué par " + gamePlayerShooter.getTeamInfo().getChatColor() + "["
						+ gamePlayerShooter.getTeamInfo().getName() + "] " + shooter.getName() + ChatColor.YELLOW
						+ ".");
				playerKillerMap.remove(player);
			}

			// Envoie des Coins au target.
			int coinsReward = 2;
			gamePlayerShooter.setCoinsWin(gamePlayerShooter.getCoinsWin() + coinsReward);
			golemaPlayerShooter.addCoinsGame(Currency.GCOINS, coinsReward, "Kill de " + player.getName());
			
			// Envoie des Perks au Joueur.
			SkyWars.instance.getPerkChecker().playBulldozer(golemaPlayerShooter);
			SkyWars.instance.getPerkChecker().playGoldenApple(golemaPlayerShooter);
			SkyWars.instance.getPerkChecker().playExperience(golemaPlayerShooter);
			SkyWars.instance.getPerkChecker().playSpeed(golemaPlayerShooter);
			SkyWars.instance.getPerkChecker().playRegen(golemaPlayerShooter);

		// Message quand le meurt lui même.
		} else {

			// Mort en mode Solo.
			if (SkyWars.instance.maxPlayerPerTeam == 1) {
				Bukkit.broadcastMessage(golemaPlayer.getRank().getChatColor() + golemaPlayer.getRank().getPrefix()
						+ getSpaceAllow(golemaPlayer.getRank()) + player.getName() + ChatColor.YELLOW + " est mort.");

				// Mort en mode Team.
			} else {
				Bukkit.broadcastMessage(
						gamePlayer.getTeamInfo().getChatColor() + "[" + gamePlayer.getTeamInfo().getName() + "] "
								+ player.getName() + ChatColor.YELLOW + " est mort.");

			}
		}

		// Message pour un Spectateur qui est éliminé.
		player.sendMessage("");
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "✖" + ChatColor.GRAY + "│ " + ChatColor.YELLOW
				+ "Vous avez été éliminé !");
		JsonMessageBuilder jsonMessageBuilder = new JsonMessageBuilder();
		jsonMessageBuilder.newJComp(ChatColor.AQUA + "Souhaitez-vous rejouer ? ").build(jsonMessageBuilder);
		jsonMessageBuilder.newJComp(ChatColor.GRAY + "[" + ChatColor.GREEN + "Rejouer - ➲" + ChatColor.GRAY + "]")
				.addCommandExecutor("/lobby").addHoverText(ChatColor.YELLOW + "Rejoindre une nouvelle partie.")
				.build(jsonMessageBuilder);
		jsonMessageBuilder.send(player);
		player.sendMessage("");
		GolemaPlayer.getGolemaPlayer(player).getSkyWarsPlayer().addPlayerStats(SkyWars.instance.getSkyWarsStatsMode(), SkyWarsStatsType.DEATHS, 1);
		
		// Définir le joueur en Spectator.
		Bukkit.getWorld("world").strikeLightningEffect(player.getLocation());
		gamePlayer.setSpectator();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		switch (GameStatus.getStatus()) {
		case GAME:
			if (event.getEntity() instanceof Player) {

				// Initialiser la variavle du Joueur.
				Player player = (Player) event.getEntity();

				// Dégats par un autre Joueur.
				if (event.getDamager() instanceof Player) {
					Player playerKiller = (Player) event.getDamager();
					playerKillerMap.put(player, playerKiller);
				}

				// Dégats par une boule de neige.
				if (event.getDamager() instanceof Snowball) {
					Snowball projectil = (Snowball) event.getDamager();
					Entity shooter = (Entity) projectil.getShooter();
					if (shooter instanceof Player) {
						Player shooterPlayer = (Player) shooter;
						playerKillerMap.put(player, shooterPlayer);
					}
				}

				// Dégats par une flêche.
				if (event.getDamager() instanceof Arrow) {
					Arrow projectil = (Arrow) event.getDamager();
					Entity shooter = (Entity) projectil.getShooter();
					if (shooter instanceof Player) {
						Player shooterPlayer = (Player) shooter;
						playerKillerMap.put(player, shooterPlayer);
						
						// Gestion des Perks.
						SkyWars.instance.getPerkChecker().playReturnArrow(GolemaPlayer.getGolemaPlayer(shooterPlayer));
					}
				}

				// Dégats par un oeuf.
				if (event.getDamager() instanceof Egg) {
					Egg projectil = (Egg) event.getDamager();
					Entity shooter = (Entity) projectil.getShooter();
					if (shooter instanceof Player) {
						Player shooterPlayer = (Player) shooter;
						playerKillerMap.put(player, shooterPlayer);
					}
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Espace en fonction du Rank.
	 * 
	 * @param rank
	 * @return
	 */
	private String getSpaceAllow(Rank rank) {
		return rank.getIdentificatorName().equals(Rank.PLAYER.getIdentificatorName()) ? "" : " ";
	}
}

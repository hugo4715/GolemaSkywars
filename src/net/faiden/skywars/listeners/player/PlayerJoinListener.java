package net.faiden.skywars.listeners.player;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.GameUtils;
import net.faiden.skywars.SkyWars;
import net.faiden.skywars.runnables.LobbyRunnable;
import net.golema.api.builder.titles.ActionBarBuilder;
import net.golema.api.utils.PlayerUtils;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.levels.LevelMechanic;
import net.golema.database.golemaplayer.levels.LevelType;
import net.golema.database.golemaplayer.rank.Rank;
import net.golema.database.support.GameStatus;
import net.golema.database.support.boards.TeamsTagsManager;
import net.golema.database.support.builder.TitleBuilder;
import net.golema.database.support.builder.items.ItemBuilder;

public class PlayerJoinListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {

		// Initialisation des Variables.
		Player player = event.getPlayer();
		GolemaPlayer golemaPlayer = GolemaPlayer.getGolemaPlayer(player);
		GamePlayer gamePlayer = SkyWars.getGamePlayer(player);
		int playerExperience = golemaPlayer.getExperienceLevel(LevelType.SKYWARS_LEVEL);
		event.setJoinMessage(null); 

		// Vérifier si la partie n'a pas déjà démarrée.
		if (!(GameStatus.isStatus(GameStatus.LOBBY))) {
			gamePlayer.setSpectator();

			// Message pour un Spectateur qui rejoins.
			player.sendMessage("");
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "ATTENTION" + ChatColor.GRAY + "│ "
					+ ChatColor.YELLOW + "Il vous est impossible de jouer !");
			player.sendMessage(ChatColor.AQUA + "Vous avez rejoint la partie en mode spectateur.");
			player.sendMessage("");
			return;
		}

		// Informations message sur les Statistiques.
		player.sendMessage(" ");
		golemaPlayer.sendCenteredMessage(
				ChatColor.WHITE + "[" + ChatColor.AQUA + "?" + ChatColor.WHITE + "] Informations sur vos statistiques");
		player.sendMessage(" ");
		golemaPlayer.sendCenteredMessage("§eVous possèdez " + ChatColor.GOLD + "" + ChatColor.BOLD
				+ LevelMechanic.getPlayerLevel(playerExperience) + "✯" + ChatColor.YELLOW + " sur le jeu SkyWars.");
		golemaPlayer.sendCenteredMessage(ChatColor.GRAY + "Classement : " + ChatColor.AQUA
				+ "https://stats.golemamc.net/player/" + player.getName() + "/");
		player.sendMessage(" ");

		// Paramètres liés aux Designs de la partie.
		new TitleBuilder(ChatColor.YELLOW + "SkyWars", SkyWars.instance.getNameStringMode()).send(player);
		new ActionBarBuilder(ChatColor.GRAY + "Développé par : " + ChatColor.DARK_GREEN + "Faiden").withStay(4)
				.sendTo(player);
		Bukkit.broadcastMessage(SkyWars.instance.prefixGame + " " + golemaPlayer.getRank().getChatColor()
				+ golemaPlayer.getRank().getPrefix() + getSpaceAllow(golemaPlayer.getRank()) + player.getName()
				+ ChatColor.YELLOW + " a rejoint la partie ! " + ChatColor.GREEN + "("
				+ Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")");
		TeamsTagsManager.setNameTag(player,
				LevelMechanic.getPlayerLevel(playerExperience) + new Random().nextInt(48545215) + "",
				ChatColor.GRAY + "" + LevelMechanic.getPlayerLevel(playerExperience) + "✯" + ChatColor.WHITE + "│ "
						+ golemaPlayer.getRank().getChatColor());

		// Paramètres de connexions à la partie du Joueur.
		player.setMaxHealth(20.0d);
		player.setHealth(20.0d);
		player.setFoodLevel(20);
		player.setWalkSpeed(0.2f);
		player.setLevel(LobbyRunnable.lobbyTimer);
		player.setGameMode(GameMode.ADVENTURE);
		player.teleport(new Location(Bukkit.getWorld("world"), 1000, 67, 1000, 90.0f, 0.0f));
		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);

		// Paramètres liés au Joueur lui-même.
		PlayerUtils.removeAllPotionEffect(player);
		PlayerUtils.clearInventory(player);
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		player.getInventory()
				.setItem(0,
						new ItemBuilder().type(Material.NAME_TAG).name(ChatColor.GREEN + "" + ChatColor.BOLD
								+ "Sélecteur de kits " + ChatColor.DARK_GRAY + " ▏ " + ChatColor.GRAY + " Clic-droit")
								.build());
		if (SkyWars.instance.maxPlayerPerTeam != 1) {
			player.getInventory().setItem(1, GameUtils.itemTeamSelector());
		}
		player.getInventory()
				.setItem(8,
						new ItemBuilder().type(Material.BED).name(ChatColor.RED + "" + ChatColor.BOLD
								+ "Retourner au hub " + ChatColor.DARK_GRAY + " ▏ " + ChatColor.GRAY + " Clic-droit")
								.build());

		// Lancement du Timer pour le démarrage du Jeu.
		if ((Bukkit.getOnlinePlayers().size() >= SkyWars.instance.minPlayers) && (!LobbyRunnable.isStarted)) {
			new LobbyRunnable().runTaskTimer(SkyWars.instance, 0L, 20L);
			LobbyRunnable.isStarted = true;
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
package net.faiden.skywars.manager.perks;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.faiden.skywars.SkyWars;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.golemaplayer.game.skywars.perks.SkyWarsPerksType;

public class PerkChecker {

	private String PREFIX = ChatColor.GOLD + "" + ChatColor.BOLD + "Perks " + ChatColor.GRAY + " » ";

	public PerkChecker() {}

	/**
	 * Utilisation de Récupération de Flêches.
	 * 
	 * @param golemaPlayer
	 */
	public void playReturnArrow(GolemaPlayer golemaPlayer) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.ARROW_KEEP);
		int randomApple = new Random().nextInt(100);
		switch (perkLevel) {
		case 1:
			if (randomApple <= 5) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+ 1 Flêche§f.");
			}
			break;
		case 2:
			if (randomApple <= 10) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+ 1 Flêche§f.");
			}
			break;
		case 3:
			if (randomApple <= 15) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+ 1 Flêche§f.");
			}
			break;
		case 4:
			if (randomApple <= 20) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+ 1 Flêche§f.");
			}
			break;
		default:
			break;
		}
	}

	/***** FIRE ARROW *****/

	/**
	 * Utilisation de Enderpearl.
	 * 
	 * @param golemaPlayer
	 * @param damage
	 * @return
	 */
	public double playFallEnderPearl(GolemaPlayer golemaPlayer, double damage) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.ENDERPEARL_LIMIT_DAMAGE);
		switch (perkLevel) {
		case 1:
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d25% §fde dégâts en moins.");
			return ((damage / 100) * 25);
		case 2:
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d50% §fde dégâts en moins.");
			return ((damage / 100) * 50);
		case 3:
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d75% §fde dégâts en moins.");
			return ((damage / 100) * 75);
		case 4:
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d100% §fde dégâts en moins.");
			return damage;
		default:
			break;
		}
		return damage;
	}

	/**
	 * Utilisation de Régénération.
	 * 
	 * @param golemaPlayer
	 */
	public void playRegen(GolemaPlayer golemaPlayer) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.REGENERATION);
		switch (perkLevel) {
		case 1:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dRégénération I §fpendant §b2 secondes§f.");
			break;
		case 2:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 4, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dRégénération I §fpendant §b4 secondes§f.");
			break;
		case 3:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 6, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dRégénération I §fpendant §b6 secondes§f.");
			break;
		case 4:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dRégénération I §fpendant §b10 secondes§f.");
			break;
		default:
			break;
		}
	}

	/**
	 * Utilisation de Speed.
	 * 
	 * @param golemaPlayer
	 */
	public void playSpeed(GolemaPlayer golemaPlayer) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.SPEED);
		switch (perkLevel) {
		case 1:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dSpeed I §fpendant §b2 secondes§f.");
			break;
		case 2:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dSpeed I §fpendant §b4 secondes§f.");
			break;
		case 3:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 6, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dSpeed I §fpendant §b6 secondes§f.");
			break;
		case 4:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0), true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dSpeed I §fpendant §b10 secondes§f.");
			break;
		default:
			break;
		}
	}

	/**
	 * Utilisation de Exeprience.
	 * 
	 * @param golemaPlayer
	 */
	public void playExperience(GolemaPlayer golemaPlayer) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.EXPERIENCE);
		switch (perkLevel) {
		case 1:
			golemaPlayer.getPlayer().setLevel(golemaPlayer.getPlayer().getLevel() + 1);
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+1 Level §f.");
			break;
		case 2:
			golemaPlayer.getPlayer().setLevel(golemaPlayer.getPlayer().getLevel() + 2);
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+2 Level §f.");
			break;
		case 3:
			golemaPlayer.getPlayer().setLevel(golemaPlayer.getPlayer().getLevel() + 3);
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+3 Level §f.");
			break;
		case 4:
			golemaPlayer.getPlayer().setLevel(golemaPlayer.getPlayer().getLevel() + 4);
			golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d+4 Level §f.");
			break;
		default:
			break;
		}
	}

	/**
	 * Utilisation de GoldenApple.
	 * 
	 * @param golemaPlayer
	 */
	public void playGoldenApple(GolemaPlayer golemaPlayer) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.GOLDEN_APPLE);
		int randomApple = new Random().nextInt(100);
		switch (perkLevel) {
		case 1:
			if (randomApple <= 10) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d1 Pommes d'or§f.");
			}
			break;
		case 2:
			if (randomApple <= 22) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d1 Pommes d'or§f.");
			}
			break;
		case 3:
			if (randomApple <= 26) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d1 Pommes d'or§f.");
			}
			break;
		case 4:
			if (randomApple <= 30) {
				golemaPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				golemaPlayer.getPlayer().sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §d1 Pommes d'or§f.");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Utilisation du Perk Bulldozer.
	 * 
	 * @param golemaPlayer
	 */
	public void playBulldozer(GolemaPlayer golemaPlayer) {
		int perkLevel = golemaPlayer.getSkyWarsPlayer().getPlayerPerks(SkyWars.instance.getSkyWarsPerkMode(),
				SkyWarsPerksType.BULLDOZE);
		switch (perkLevel) {
		case 1:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 1, 0),
					true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dForce I §fpendant §b1 seconde§f.");
			break;
		case 2:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 2, 0),
					true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dForce I §fpendant §b2 secondes§f.");
			break;
		case 3:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 3, 0),
					true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dForce I §fpendant §b3 secondes§f.");
			break;
		case 4:
			golemaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 4, 0),
					true);
			golemaPlayer.getPlayer()
					.sendMessage(PREFIX + ChatColor.WHITE + "Vous recevez §dForce I §fpendant §b4 secondes§f.");
			break;
		default:
			break;
		}
	}
}
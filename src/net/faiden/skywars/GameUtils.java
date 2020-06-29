package net.faiden.skywars;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class GameUtils {

	/**
	 * Récupérer l'item du sélecteur de Teams.
	 * 
	 * @return
	 */
	public static ItemStack itemTeamSelector() {
		ItemStack itemStack = new ItemStack(Material.BANNER);
		BannerMeta iBannerMeta = (BannerMeta) itemStack.getItemMeta();
		iBannerMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Sélecteur d'Équipe " + ChatColor.DARK_GRAY + " ▏ " + ChatColor.GRAY + " Clic-droit");
		iBannerMeta.setBaseColor(DyeColor.WHITE);
		List<Pattern> patterns = new ArrayList<Pattern>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.CROSS));
        iBannerMeta.setPatterns(patterns);
		itemStack.setItemMeta(iBannerMeta);
		return itemStack;
	}
}
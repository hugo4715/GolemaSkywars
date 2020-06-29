package net.faiden.skywars.manager.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class KitAbstract {
	
	public static List<KitAbstract> kitAbstractsList = new ArrayList<KitAbstract>();
	
	protected int slot;
	protected String permission;
	protected String name;
	
	public abstract void sendKits(Player player);
	public abstract ItemStack getItemIcons(Player player);
	
	// Enregistrer le Kit.
	public void register(KitAbstract kitAbstract) {
		kitAbstractsList.add(kitAbstract);
	}
	
	/**
	 * Récupérer un Kit.
	 * 
	 * @param name
	 * @return
	 */
	public static KitAbstract getKitAbstractByName(String name) {
		for(KitAbstract kits : kitAbstractsList) {
			if(kits.name.equalsIgnoreCase(name)) {
				return kits;
			}
		}
		return null;
	}
}

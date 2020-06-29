package net.faiden.skywars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import net.faiden.skywars.listeners.entity.EntityDamageListener;
import net.faiden.skywars.listeners.player.PlayerChatListener;
import net.faiden.skywars.listeners.player.PlayerDeathListener;
import net.faiden.skywars.listeners.player.PlayerDropItemListener;
import net.faiden.skywars.listeners.player.PlayerFoodLevelListener;
import net.faiden.skywars.listeners.player.PlayerInteractListener;
import net.faiden.skywars.listeners.player.PlayerJoinListener;
import net.faiden.skywars.listeners.player.PlayerMoveListener;
import net.faiden.skywars.listeners.player.PlayerQuitListener;
import net.faiden.skywars.listeners.world.WorldListener;
import net.golema.api.patch.KnockbackFixerListener;

public class ListenerManager {
	
	public Plugin plugin;
	public PluginManager pluginManager;

	/**
	 * Constructeur du ListenerManager.
	 * 
	 * @param plugin
	 */
	public ListenerManager(Plugin plugin) {
		this.plugin = plugin;
		this.pluginManager = Bukkit.getPluginManager();
	}
	
	// Enregistrement des Listeners du SkyWars.
	public void registerListeners() {
		
		// Gestion des �v�nements avec les Entity.
		pluginManager.registerEvents(new EntityDamageListener(), plugin);
		
		// Gestion des �v�nements avec les Player.
		pluginManager.registerEvents(new PlayerJoinListener(), plugin);
		pluginManager.registerEvents(new PlayerQuitListener(), plugin);
		pluginManager.registerEvents(new PlayerInteractListener(), plugin);
		pluginManager.registerEvents(new PlayerMoveListener(), plugin);
		pluginManager.registerEvents(new PlayerDeathListener(), plugin);
		pluginManager.registerEvents(new PlayerChatListener(), plugin);
		pluginManager.registerEvents(new PlayerFoodLevelListener(), plugin);
		pluginManager.registerEvents(new PlayerDropItemListener(), plugin);
		
		// Gestion des �v�nements avec les World.
		pluginManager.registerEvents(new WorldListener(), plugin);
		
		// Mise en place du Patch des KB.
		pluginManager.registerEvents(new KnockbackFixerListener(), plugin);
	}
}

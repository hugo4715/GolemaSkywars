package net.faiden.skywars.listeners.world;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import net.faiden.skywars.GamePlayer;
import net.faiden.skywars.SkyWars;

public class WorldListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		GamePlayer gamePlayer = SkyWars.getGamePlayer(player);
		gamePlayer.setBlocBreak(gamePlayer.getBlocBreak() + 1);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		GamePlayer gamePlayer = SkyWars.getGamePlayer(player);
		gamePlayer.setBlocPlace(gamePlayer.getBlocPlace() + 1);
	}
}
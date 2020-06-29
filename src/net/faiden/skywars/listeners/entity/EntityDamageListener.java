package net.faiden.skywars.listeners.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.faiden.skywars.SkyWars;
import net.golema.database.golemaplayer.GolemaPlayer;
import net.golema.database.support.GameStatus;

public class EntityDamageListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		switch (GameStatus.getStatus()) {
		case LOBBY:
			event.setCancelled(true); 
			break;
		case GAME:
			// Gestion des dégats durant la partie.
			if (SkyWars.instance.damage) {
				event.setCancelled(false);

				// Perks des dégats.
				if (event.getEntity() instanceof Player) {
					if ((event.getCause().equals(DamageCause.FALL)) || (event.getCause().equals(DamageCause.FALL))) {
						event.setDamage(SkyWars.instance.getPerkChecker().playFallEnderPearl(
								GolemaPlayer.getGolemaPlayer((Player) event.getEntity()), event.getDamage()));
					}
				}
				
			} else {
				event.setCancelled(true);
			}
			break;
		case FINISH:
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}
}
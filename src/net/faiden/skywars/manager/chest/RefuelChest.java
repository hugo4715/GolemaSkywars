package net.faiden.skywars.manager.chest;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.faiden.skywars.SkyWars;
import net.golema.database.support.utils.GolemaLogger;

public class RefuelChest {

	private static List<RefuelChest> islandsChests = null;

	private Location location;
	private int id;

	public RefuelChest(int x, int y, int z, int islandId) {
		location = new Location(Bukkit.getWorld("world"), x, y, z);
		id = islandId;

		reset();
	}

	public void reset() {
		location.getBlock().setType(Material.CHEST);
		Chest c = (Chest) location.getBlock().getState();
		c.getBlockInventory().clear();
	}

	public Location getLocation() {
		return location;
	}

	public int getId() {
		return id;
	}

	/**
	 * Get all created chest on the config
	 * 
	 * @return A list of new RefuelChests fetched from config
	 */
	public static List<RefuelChest> get() {

		if (islandsChests == null) {
			islandsChests = Lists.newArrayList();
			// TODO

			List<Location> chests = Lists.newArrayList();

			int chestPerIsland = 3;
			List<Location> islands = getSpawns();

			// get all chests on map
			for(World w : Bukkit.getWorlds()) {
				for (Chunk c : w.getLoadedChunks()) {
					for (BlockState b : c.getTileEntities()) {
						if (b instanceof Chest) {
							chests.add(b.getLocation());
						}
					}
				}
			}
			

			Map<Location /* island spawn */, List<Location /* chest coords */>> m = Maps.newHashMap();

			int i = 0;
			for (Location center : islands) {
				List<Location> c = Lists.newArrayList(chests.stream().filter(chest -> chest.getWorld().equals(center.getWorld())).collect(Collectors.toSet()));
				// order from cloest to center
				c.sort(new Comparator<Location>() {
					@Override
					public int compare(Location o1, Location o2) {
						if (o1 == o2 || o1.equals(o2))
							return 0;
						return o1.distanceSquared(center) > o2.distanceSquared(center) ? 1 : -1;
					}
				});
				m.put(center, Lists.newArrayList(c.subList(0, chestPerIsland)));
				System.out.println("island " + i + "  " + center.getBlockX() + "/" + center.getBlockY() + "/"
						+ center.getBlockZ());
				for (int y = 0; y < m.get(center).size(); y++) {
					Location l = m.get(center).get(y);
					System.out.println("chest " + l.getBlockX() + "/" + l.getBlockY() + "/" + l.getBlockZ());
				}

				i++;
			}

			List<Location> used = Lists.newArrayList();
			m.entrySet().forEach(en -> used.addAll(en.getValue()));

			List<Location> centerChests = Lists.newArrayList();
			for (Location loc : chests) {
				if (!used.contains(loc)) {
					centerChests.add(loc);
				}
			}

			for (Location loc : centerChests) {
				islandsChests.add(new RefuelChest(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), -1));
			}

			Iterator<Entry<Location, List<Location>>> it = m.entrySet().iterator();
			int index = 0;
			while (it.hasNext()) {
				List<Location> p = it.next().getValue();
				for (Location loc : p) {
					islandsChests.add(new RefuelChest(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), index));
				}
				index++;
			}
			GolemaLogger.log("Found " + chests.size() + " chest in map");
		}

		return islandsChests;
	}

	private static List<Location> getSpawns() {
		List<Location> spawn = Lists.newArrayList();
		for (int i = 1; i <= 12; i++) {
			spawn.add(new Location(Bukkit.getWorld("world"), SkyWars.instance.mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".x"), SkyWars.instance.mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".y"), SkyWars.instance.mapLocationsConfig.get().getDouble("LocationSpawn." + i + ".z")));
		}
		return spawn;
	}
}
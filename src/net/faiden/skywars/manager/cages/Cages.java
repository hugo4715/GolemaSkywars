package net.faiden.skywars.manager.cages;

import org.bukkit.Location;
import org.bukkit.Material;

public class Cages {
	
	public CagesInfo cagesInfo;
	public Material material;
	public short data;
	public Location location;
	
	/**
	 * Constructeur des Cages.
	 * 
	 * @param cagesInfo
	 * @param location
	 */
	public Cages(CagesInfo cagesInfo, Location location) {
		this.cagesInfo = cagesInfo;
		this.material = cagesInfo.getMaterial();
		this.data = cagesInfo.getData();
		this.location = location;
	}
	
	public CagesInfo getCagesInfo() {
		return cagesInfo;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public short getData() {
		return data;
	}
	
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Création de la Cage.
	 */
	@SuppressWarnings("deprecation")
	public void createCage() {
		location.getBlock().setType(Material.SEA_LANTERN);
		location.getBlock().getRelative(0, 0, 1).setType(material);
		location.getBlock().getRelative(0, 0, 2).setType(material);
		location.getBlock().getRelative(0, 0, -1).setType(material);
		location.getBlock().getRelative(0, 0, -2).setType(material);
		location.getBlock().getRelative(1, 0, 0).setType(material);
		location.getBlock().getRelative(-1, 0, 0).setType(material);
		location.getBlock().getRelative(1, 0, -1).setType(material);
		location.getBlock().getRelative(-1, 0, -1).setType(material);
		location.getBlock().getRelative(1, 0, 1).setType(material);
		location.getBlock().getRelative(-1, 0, 1).setType(material);
		location.getBlock().getRelative(0, 0, 1).setData((byte) data);
		location.getBlock().getRelative(0, 0, 2).setData((byte) data);
		location.getBlock().getRelative(0, 0, -1).setData((byte) data);
		location.getBlock().getRelative(0, 0, -2).setData((byte) data);
		location.getBlock().getRelative(1, 0, 0).setData((byte) data);
		location.getBlock().getRelative(-1, 0, 0).setData((byte) data);
		location.getBlock().getRelative(1, 0, -1).setData((byte) data);
		location.getBlock().getRelative(-1, 0, -1).setData((byte) data);
		location.getBlock().getRelative(1, 0, 1).setData((byte) data);
		location.getBlock().getRelative(-1, 0, 1).setData((byte) data);
		location.getBlock().getRelative(0, 0, -3).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(0, 0, 3).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(2, 0, 1).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(2, 0, 0).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(2, 0, -1).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(-2, 0, 1).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(-2, 0, 0).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(-2, 0, -1).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(-1, 0, 2).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(-1, 0, -2).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(1, 0, 2).setType(Material.COAL_BLOCK);
		location.getBlock().getRelative(1, 0, -2).setType(Material.COAL_BLOCK);
	}
	
	/**
	 * Suppresion de la Cage.
	 */
	public void removeCage() {
		location.getBlock().setType(Material.AIR);
		location.getBlock().getRelative(0, 0, 1).setType(Material.AIR);
		location.getBlock().getRelative(0, 0, 2).setType(Material.AIR);
		location.getBlock().getRelative(0, 0, -1).setType(Material.AIR);
		location.getBlock().getRelative(0, 0, -2).setType(Material.AIR);
		location.getBlock().getRelative(1, 0, 0).setType(Material.AIR);
		location.getBlock().getRelative(-1, 0, 0).setType(Material.AIR);
		location.getBlock().getRelative(1, 0, -1).setType(Material.AIR);
		location.getBlock().getRelative(-1, 0, -1).setType(Material.AIR);
		location.getBlock().getRelative(1, 0, 1).setType(Material.AIR);
		location.getBlock().getRelative(-1, 0, 1).setType(Material.AIR);
		location.getBlock().getRelative(0, 0, -3).setType(Material.AIR);
		location.getBlock().getRelative(0, 0, 3).setType(Material.AIR);
		location.getBlock().getRelative(2, 0, 1).setType(Material.AIR);
		location.getBlock().getRelative(2, 0, 0).setType(Material.AIR);
		location.getBlock().getRelative(2, 0, -1).setType(Material.AIR);
		location.getBlock().getRelative(-2, 0, 1).setType(Material.AIR);
		location.getBlock().getRelative(-2, 0, 0).setType(Material.AIR);
		location.getBlock().getRelative(-2, 0, -1).setType(Material.AIR);
		location.getBlock().getRelative(-1, 0, 2).setType(Material.AIR);
		location.getBlock().getRelative(-1, 0, -2).setType(Material.AIR);
		location.getBlock().getRelative(1, 0, 2).setType(Material.AIR);
		location.getBlock().getRelative(1, 0, -2).setType(Material.AIR);
	}
}
package net.faiden.skywars.manager.cages;

import org.bukkit.Material;

public enum CagesInfo {
	
	DEFAULT(1, "Défaut", Material.GLASS, (short) 0, null, 1, 20),
	GLASS_WHITE(2, "Blanche", Material.STAINED_GLASS, (short) 0, "skywars.cages.null", 1, 21),
	GLASS_LIGHT_GRAY(3, "Grise", Material.STAINED_GLASS, (short) 8, "skywars.cages.null", 1, 22),
	GLASS_GRAY(4, "Grise Foncée", Material.STAINED_GLASS, (short) 7, "skywars.cages.null", 1, 23),
	GLASS_BLACK(5, "Noire", Material.STAINED_GLASS, (short) 15, "skywars.cages.null", 1, 24),
	GLASS_BROWN(6, "Marron", Material.STAINED_GLASS, (short) 12, "skywars.cages.null", 1, 29),
	GLASS_ORANGE(7, "Orange", Material.STAINED_GLASS, (short) 1, "skywars.cages.null", 1, 30),
	GLASS_RED(8, "Rouge", Material.STAINED_GLASS, (short) 14, "skywars.cages.null", 1, 31),
	GLASS_YELLOW(9, "Jaune", Material.STAINED_GLASS, (short) 4, "skywars.cages.null", 1, 32),
	GLASS_PINK(10, "Rose", Material.STAINED_GLASS, (short) 6, "skywars.cages.null", 1, 33),
	GLASS_MAGENTA(11, "Magenta", Material.STAINED_GLASS, (short) 2, "skywars.cages.null", 2, 20),
	GLASS_BLUE(12, "Bleu", Material.STAINED_GLASS, (short) 9, "skywars.cages.null", 2, 21),
	GLASS_AQUA(13, "Aqua", Material.STAINED_GLASS, (short) 3, "skywars.cages.null", 2, 22),
	GLASS_PURPLE(14, "Violette", Material.STAINED_GLASS, (short) 10, "skywars.cages.null", 2, 23),
	GLASS_GREEN(15, "Verte", Material.STAINED_GLASS, (short) 5, "skywars.cages.null", 2, 24),
	GLASS_GREEN_DARK(16, "Verte Foncée", Material.STAINED_GLASS, (short) 13, "skywars.cages.null", 2, 29),
	GLASS_BEDROCK(17, "Bedrock", Material.BEDROCK, (short) 0, "skywars.cages.null", 2, 30),
	GLASS_FRUIT(18, "Fruitée", Material.MELON, (short) 0, "skywars.cages.null", 2, 31),
	GLASS_LEAVES(19, "Feuillage", Material.LEAVES, (short) 0, "skywars.cages.null", 2, 32),
	GLASS_MUSIC(20, "Jukebox", Material.JUKEBOX, (short) 0, "skywars.cages.null", 2, 33),
	
	GLASS_OCEAN(21, "Océanique", Material.PRISMARINE, (short) 0, "skywars.cages.null", 3, 20),
	GLASS_JAIL(22, "Prison", Material.IRON_FENCE, (short) 0, "skywars.cages.null", 3, 21),
	GLASS_WOOD(23, "Bois", Material.FENCE, (short) 0, "skywars.cages.null", 3, 22),
	GLASS_ORE(24, "Minerais", Material.IRON_ORE, (short) 0, "skywars.cages.null", 3, 23),
	GLASS_SLIME(25, "Slime", Material.SLIME_BLOCK, (short) 0, "skywars.cages.null", 3, 24),
	GLASS_GRASS(26, "Herbe", Material.DIRT, (short) 0, "skywars.cages.null", 3, 29),
	GLASS_EMERAUDE(27, "Emeraude", Material.EMERALD_BLOCK, (short) 0, "skywars.cages.null", 3, 30),
	GLASS_DIAMOND(28, "Diamant", Material.DIAMOND_BLOCK, (short) 0, "skywars.cages.null", 3, 31),
	GLASS_REDSTONE(29, "Redstone", Material.REDSTONE_BLOCK, (short) 0, "skywars.cages.null", 3, 32),
	GLASS_STONE(30, "Pierre", Material.COBBLE_WALL, (short) 0, "skywars.cages.null", 3, 33),
	GLASS_NETHER(31, "Nether", Material.NETHER_FENCE, (short) 0, "skywars.cages.null", 4, 20),
	GLASS_ICE(32, "Glacée", Material.ICE, (short) 0, "skywars.cages.null", 4, 21),
	GLASS_FLOWER(33, "Fleure", Material.YELLOW_FLOWER, (short) 0, "skywars.cages.null", 4, 22),
	GLASS_MULTICOLOR(34, "Arc en Ciel", Material.BEACON, (short) 0, "skywars.cages.null", 4, 23),
	;
	
	public int id;
	public String name;
	public Material material;
	public short data;
	public String permission;
	public int page;
	public int slot;
	
	/**
	 * Constructeur de l'énumeration CagesInfo.
	 * 
	 * @param id
	 * @param name
	 * @param material
	 * @param data
	 * @param permission
	 * @param page
	 * @param slot
	 */
	private CagesInfo(int id, String name, Material material, short data, String permission, int page, int slot) {
		this.id = id;
		this.name = name;
		this.material = material;
		this.data = data;
		this.permission = permission;
		this.page = page;
		this.slot = slot;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public short getData() {
		return data;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public int getPage() {
		return page;
	}
	
	public int getSlot() {
		return slot;
	}
	
	/**
	 * Récupérer une Cage par son Nom.
	 * 
	 * @param cageName
	 * @return
	 */
	public static CagesInfo getCageByName(String cageName) {
		for(CagesInfo cagesInfo : CagesInfo.values()) {
			if(cagesInfo.getName().equalsIgnoreCase(cageName)) {
				return cagesInfo;
			}
		}
		return CagesInfo.DEFAULT;
	}
}
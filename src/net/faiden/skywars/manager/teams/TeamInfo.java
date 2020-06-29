package net.faiden.skywars.manager.teams;

import org.bukkit.ChatColor;

public enum TeamInfo {
	
	YELLOW("Jaune", (short) 11, ChatColor.YELLOW, "§e"),
	ORANGE("Orange", (short) 14, ChatColor.GOLD, "§6"),
	RED("Rouge", (short) 1, ChatColor.RED, "§c"),
	LIGHT_GREEN("Vert", (short) 10, ChatColor.GREEN, "§a"),
	DARK_GREEN("Vert Foncé", (short) 2, ChatColor.DARK_GREEN, "§2"),
	PINK("Rose", (short) 9, ChatColor.LIGHT_PURPLE, "§d"),
	PURPLE("Violet", (short) 5, ChatColor.DARK_PURPLE, "§5"),
	AQUA("Aqua", (short) 12, ChatColor.AQUA, "§b"),
	BLUE("Bleu", (short) 6, ChatColor.DARK_AQUA, "§3"),
	WHITE("Blanc", (short) 15, ChatColor.WHITE, "§f"),
	LIGHT_GRAY("Gris", (short) 7, ChatColor.GRAY, "§7"),
	DARK_GRAY("Gris Fonce", (short) 8, ChatColor.DARK_GRAY, "§8");
	
	private String name;
	private short data;
	private ChatColor chatColor;
	private String charString;
	
	/**
	 * Constructeur du TeamInfos.
	 * 
	 * @param name
	 * @param data
	 * @param chatColor
	 * @param charString
	 */
	private TeamInfo(String name, short data, ChatColor chatColor, String charString) {
		this.name = name;
		this.data = data;
		this.chatColor = chatColor;
		this.charString = charString;
	}
	
	public String getName() {
		return name;
	}
	
	public short getData() {
		return data;
	}
	
	public ChatColor getChatColor() {
		return chatColor;
	}
	
	public String getCharString() {
		return charString;
	}
	
	/**
	 * Sélectionner une Team par rapport à son Nom.
	 * 
	 * @param teamName
	 * @return
	 */
	public static TeamInfo geTeamInfoByName(String teamName) {
		for(TeamInfo teamInfo : TeamInfo.values()) {
			if(teamInfo.getName().equalsIgnoreCase(teamName)) {
				return teamInfo;
			}
		}
		return null;
	}
}
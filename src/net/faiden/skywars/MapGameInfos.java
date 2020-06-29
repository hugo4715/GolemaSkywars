package net.faiden.skywars;

public enum MapGameInfos {
	
	//CANYON("Canyon", "canyon.yml"),
	AQUATIC("Aquatic", "aquatic.yml"),
	//SHED("Shed", "shed.yml"),
	//SHINSEKAI("Shinsekai", "shinsekai.yml"),
	CAMPING("Camping", "camping.yml"),
	TROPICO("Tropico", "tropico.yml"),
	MEKA("Meka", "meka.yml"),
	HAFREEPY("Hafreepy", "hafreepy.yml"),
	SUBMIRATE("Submirate", "submirate.yml"),
	ABIENEIGE("Abieneige", "abieneige.yml")
	;
	
	public String mapName;
	public String configName;
	
	/**
	 * Constructeur du MapInfos.
	 * 
	 * @param mapName
	 * @param configName
	 */
	private MapGameInfos(String mapName, String configName) {
		this.mapName = mapName;
		this.configName = configName;
	}

	/**
	 * R�cup�rer le nom de la Map.
	 * 
	 * @return
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * R�cup�rer le nom de la Config.
	 * 
	 * @return
	 */
	public String getConfigName() {
		return configName;
	}
}
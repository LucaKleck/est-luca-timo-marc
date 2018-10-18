/**  
* MapTile.java - Represents one tile of the map
* @author Luca Kleck
* @version 0.01
* @since 0.01 
* @see ObjectMap
*/
package map;

public class MapTile {
	private int xPos;
	private int yPos;
	private int type;
	/*
	 * Types:
	 * 0 = plains
	 * 1 = forests
	 * 2 = mountains
	 * 3 = bodies of water
	 */
	private String name;
	private MapTileResources mapTileResources;
//	private BuildingEffect buildingEffect;
//	private UnitEffect unitEffect;
	
	public MapTile(int xPos, int yPos, int type, String name) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.type = type;
		this.name = name;
		this.mapTileResources = new MapTileResources(type);
	}
	// TODO add constructors with UnitEffect &/or BuildingEffect
	public int getXPos() {
		return xPos;
	}
	public int getYPos() {
		return yPos;
	}
	public int getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public MapTileResources getMapTileResources() {
		return mapTileResources;
	}
	/*
	public BuildingEffect getBuildingEffect() {
		return buildingEffect
	}
	public UnitEffect getUnitEffect() {
		return unitEffect
	}
	 */
}

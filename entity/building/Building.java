package entity.building;

import java.util.ArrayList;

import abilities.Ability;
import entity.Entity;

public class Building extends Entity {
	
	private BuildingRessources ressources;
	
	public static final String WOOD_GETTER = "Lumberjack";
	public static final String FOOD_GETTER = "Farmer";
	public static final String GOLD_GETTER = "Goldminer";
	public static final String METAL_GETTER = "Metalforge";
	public static final String STONE_GETTER = "Stonemason";
	public static final String MANA_GETTER = "Manastonecollector";
	
	
	public Building(int xPos, int yPos, String name, int health, ArrayList<Ability> abilities) {
		super(xPos, yPos, name, health, abilities);
		ressources = new BuildingRessources (xPos, yPos, name);
	}
	
	private static int level;
	private static int efficiency;
	
	public int getEfficiency(int xPos, int yPos) {
		return efficiency;
	}
	public BuildingRessources getRessources() {
		return ressources;
	}
	
}
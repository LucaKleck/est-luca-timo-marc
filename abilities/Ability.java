package abilities;

import entity.Entity;

public abstract class Ability {
	// DAMAGE will be searched and replaced by the actual damage the unit will deal
	// Dev Abilities
	public static final String ABILITY_DEV_CREATE_UNIT = "devCreateUnit";
	public static final String ABILITY_DESC_DEV_CREATE_UNIT = "devCreateUnit";

	public static final String ABILITY_DEV_CREATE_BUILDER = "devCreateBuilder";
	public static final String ABILITY_DESC_DEV_CREATE_BUILDER = "devCreateBuilder";

	public static final String ABILITY_DEV_CREATE_BUILDING = "devCreateBuilding";
	public static final String ABILITY_DESC_DEV_CREATE_BUILDING = "devCreateBuilding";

	// Unit Abilities
	public static final String ABILITY_FIRE_BALL = "Fireball";
	public static final String ABILITY_DESC_FIRE_BALL = "Shoots a Fireball at the target, dealing DAMAGE damage to the target";

	public static final String ABILITY_MELEE = "Melee";
	public static final String ABILITY_DESC_MELEE = "Slashes target for DAMAGE damage";

	public static final String ABILITY_MOVE = "Move";
	public static final String ABILITY_DESC_MOVE = "Moves to targeted spot";

	public static final String ABILITY_BUILD = "Build";
	public static final String ABILITY_DESC_BUILD = "Builds at targeted spot";

	// Building Abilities
	public static final String ABILITY_COLLECT_RESOURCES = "Collect resources";
	public static final String ABILITY_DESC_COLLECT_RESOURCES = "Collects resources at the end of the round";

	public static final String ABILITY_CREATE_UNIT = "Create Unit";
	public static final String ABILITY_DESC_CREATE_UNIT = "Creates new Unit at the end of the round";
	
	public static final String ABILITY_CREATE_WARRIOR = "Create Warrior";
	public static final String ABILITY_DESC_CREATE_WARRIOR = "Creates new Warrior at the end of the round";
	
	public static final String ABILITY_CREATE_MAGE = "Create Mage";
	public static final String ABILITY_DESC_CREATE_MAGE = "Creates new Mage at the end of the round";
	
	public static final String ABILITY_CREATE_BUILDER = "Create Builder";
	public static final String ABILITY_DESC_CREATE_BUILDER = "Creates new Builder at the end of the round";

	private String name;
	private String description;
	public int maxRange = 3;

	public Ability(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public abstract void applyAbility(Entity source, Entity target);

	public boolean rangeCheck(int unitX, int unitY, int mapTileX, int mapTileY) {
		if (mapTileX == unitX && mapTileY == unitY) {
			return false;
		}
		if (mapTileX >= unitX - maxRange && mapTileX <= unitX + maxRange && mapTileY >= unitY - maxRange
				&& mapTileY <= unitY + maxRange) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return name;
	}

}

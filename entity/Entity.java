package entity;

import java.awt.Point;
import java.util.ArrayList;

import abilities.Ability;
import core.Event;
import map.ObjectMap;

public class Entity {
	private static int entityCount;
	
	private int id;
	private int xPos;
	private int yPos;
	private String name;
	private int maxHealth;
	private int currentHealth;
	private ArrayList<Ability> abilities;
	private int level = 1;
	private int maxRange = 5; // Will be calculated via the abilities in the future
	private Event event = null;
	
	public Entity(int xPos, int yPos, String name, int maxHealth, ArrayList<Ability> abilities) {
		entityCount++;
		this.id = entityCount;
		this.name = name;
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
		this.xPos = xPos;
		this.yPos = yPos;
		this.abilities = abilities;
	}
	
	public Entity(Point pointXY, String name, int maxHealth, ArrayList<Ability> abilities) {
		entityCount++;
		this.id = entityCount;
		this.name = name;
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
		this.xPos = (int) pointXY.getX();
		this.yPos = (int) pointXY.getY();
		this.abilities = abilities;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public String getName() {
		return name;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;

		if (currentHealth <= 0) {
			destroy();
		}

	}

	public int getMaxRange() {
		return maxRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public int getLevel() {
		return level;
	}
	
	public void addLevel() {
		level ++;
	}
	
	private void destroy() {

		int i = 0;

		while (ObjectMap.getEntityMap().get(i) != this) {
			i++;
		}

		ObjectMap.getEntityMap().remove(i);

	}

	@Override
	public String toString() {
		return "Entity [id=" + id + ", xPos=" + xPos + ", yPos=" + yPos + ", name=" + name + ", maxHealth=" + maxHealth
				+ ", currentHealth=" + currentHealth + ", abilities=" + abilities + ", level=" + level + ", maxRange="
				+ maxRange + ", event=" + event + "]";
	}

	public boolean hasAbility() {
		boolean has = false;
		try {
			for (int i = 0; i < abilities.size(); i++) {
				if (abilities.get(i) != null) {
					has = true;
					return has;
				}
			}
		} catch (NullPointerException nl) {
		}
		return has;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		if(this.event != null) {
			this.event.cancleEvent();
			this.event=null;
			System.gc();
		}
		this.event = event;
		//System.out.println(event.toString());
		//System.out.println(GameInfo.getEventQueue());
	}

	public static int getEntityCount() {
		return entityCount;
	}

	public static void setEntityCount(int entityCount) {
		Entity.entityCount = entityCount;
	}

	public int getId() {
		return id;
	}

}

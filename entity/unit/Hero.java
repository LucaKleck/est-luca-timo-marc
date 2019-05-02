package entity.unit;

import java.util.ArrayList;

import abilities.Ability;
import abilities.MeleeAttack;
import abilities.RangedAttack;
import core.Point2DNoFxReq;

public class Hero extends Unit {

	private static final int BASE_MAX_HEALTH = 20;
	private static final int BASE_DAMAGE = 8;
	private static final int MOVEMENT_RANGE = 4;
	
	public Hero(Point2DNoFxReq pointXY, String name, int currentHealth, int level, boolean controlable, ArrayList<Ability> abilities) {
		super(pointXY, name, BASE_MAX_HEALTH, currentHealth, level, controlable, BASE_DAMAGE, MOVEMENT_RANGE, abilities);
		abilities.add(new RangedAttack(4, BASE_DAMAGE));
		abilities.add(new MeleeAttack(2, BASE_DAMAGE + 2));
	}

	public Hero(Point2DNoFxReq pointXY, String name, int level, boolean controlable, ArrayList<Ability> abilities) {
		super(pointXY, name, BASE_MAX_HEALTH, BASE_MAX_HEALTH, level, controlable, BASE_DAMAGE, MOVEMENT_RANGE, abilities);
		abilities.add(new RangedAttack(4, BASE_DAMAGE));
		abilities.add(new MeleeAttack(2, BASE_DAMAGE + 2));
	}
	
}

package entity.unit;

import java.util.ArrayList;

import abilities.Ability;
import abilities.FireBall;
import core.Point2DNoFxReq;

public class Dragon extends Unit {

	private static final int BASE_MAX_HEALTH = 10;
	private static final int BASE_DAMAGE = 10;
	private static final int MOVEMENT_RANGE = 4;
	
	public Dragon(Point2DNoFxReq pointXY, String name, int currentHealth, int level, boolean controlable, ArrayList<Ability> abilities) {
		super(pointXY, name, BASE_MAX_HEALTH, currentHealth, level, controlable, BASE_DAMAGE, MOVEMENT_RANGE, abilities);
		abilities.add(new FireBall(BASE_DAMAGE));
	}

	public Dragon(Point2DNoFxReq pointXY, String name, int level, boolean controlable, ArrayList<Ability> abilities) {
		super(pointXY, name, BASE_MAX_HEALTH, BASE_MAX_HEALTH, level, controlable, BASE_DAMAGE, MOVEMENT_RANGE, abilities);
		abilities.add(new FireBall(BASE_DAMAGE));
	}
	
}
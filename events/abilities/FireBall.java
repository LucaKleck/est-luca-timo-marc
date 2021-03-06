package events.abilities;

import entity.Entity;

public class FireBall extends Ability {

	private static final int BASE_DAMAGE = 2;
	
	public FireBall() {
		super(Ability.ABILITY_FIRE_BALL, Ability.ABILITY_DESC_FIRE_BALL, Ability.ABILITY_TYPE_DAMAGE);
		this.setDamage(BASE_DAMAGE);
	}
	
	public FireBall(int damage) {
		super(Ability.ABILITY_FIRE_BALL, Ability.ABILITY_DESC_FIRE_BALL, Ability.ABILITY_TYPE_DAMAGE);
		this.setDamage(damage);
	}
	
	public FireBall(boolean isMage) {
		super(Ability.ABILITY_FIRE_BALL, Ability.ABILITY_DESC_FIRE_BALL, Ability.ABILITY_TYPE_DAMAGE);
		this.setDamage(BASE_DAMAGE  + 2);
	}
	
	@Override
	public void applyAbility(Entity source, Entity target) {
		target.setCurrentHealth(target.getCurrentHealth() - this.getDamage());
	}

}


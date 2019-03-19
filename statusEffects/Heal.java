package statusEffects;

public class Heal extends StatusEffect {

	public Heal(String name, int duration) {
		super(name, duration);
	}

	@Override
	public void applyEffect() {
		if(this.getDuration() > 1) {
			this.reduceDuration();
			this.getTarget().setCurrentHealth(this.getTarget().getCurrentHealth() + 1);
		} else {
			this.getTarget().setCurrentHealth(this.getTarget().getCurrentHealth() + 1);
			this.getSource().removeStatusEffect(this);
		}
	}

}

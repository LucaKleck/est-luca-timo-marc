package core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;

import core.Core;
import core.GameInfo;
import core.XMLSaveAndLoad;
import entity.Entity;
import entity.EntityFilter;
import entity.building.Building;
import entity.building.DefenseBuilding;
import entity.building.ProductionBuilding;
import entity.unit.Builder;
import entity.unit.Unit;
import entity.unit.UnitFactory;
import events.Event;
import events.abilities.Ability;
import events.abilities.CollectResources;
import events.abilities.Idle;
import events.abilities.Move;
import events.statusEffects.StatusEffect;
import frame.gamePanels.LogPanel;
import frame.gamePanels.MainGamePanel;
import frame.graphics.effects.AbilityEffect;

public class NextRoundActionListener implements ActionListener, Runnable {
	private static final ExecutorService EXS = Executors.newFixedThreadPool(1);
	private JButton j;
	private EntityFilter entityFilter;
	private UnitFactory uf;
	
	private static int eventCycleSpeed = 250;

	private int 
	collectedWood = 0,
	collectedFood = 0,
	collectedStone = 0,
	collectedGold = 0,
	collectedManaStone = 0,
	collectedMetal = 0;
	
	public NextRoundActionListener() {
		entityFilter = new EntityFilter();
		uf = new UnitFactory();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		j = ((JButton) (e.getSource()));
		j.setEnabled(false);

		EXS.execute(this);
		//Spawn an enemy wave every ten rounds
	}

	@Override
	public void run() {
		if(Core.loadSetting(Core.SETTING_QUICK_ROUNDS).matches(Core.TRUE)) eventCycleSpeed = 50;
		else eventCycleSpeed = 250;

		ArrayList<Entity> entityMap = GameInfo.getObjectMap().getEntityMap();
		ArrayList<StatusEffect> statusEffects = new ArrayList<StatusEffect>();
		ArrayList<Entity> enemyEntities = new ArrayList<Entity>();

		GameInfo.getObjectMap().getSelected().removeSelected();
		LogPanel.appendNewLine("-----------------");
		LogPanel.appendNewLine("Round "+GameInfo.getRoundInfo().getRoundNumber()+" start");
		// player events
		LogPanel.appendNewLine("-> User Events <-");
		goThroughEventList();

		for (Entity entity : entityMap) {
			if (entity.isControllable() == false) {
				enemyEntities.add(entity);
			}
			if (entity.getStatusEffects().isEmpty() == false) {
				statusEffects.addAll(entity.getStatusEffects());
			}
		}
		LogPanel.appendNewLine("-> Status Effects <-");
		for (StatusEffect statusEffect : statusEffects) {
			statusEffect.applyEffect();
			LogPanel.appendNewLine(statusEffect.toString());
		}
		
		for (Entity entity : enemyEntities) {

			Ability ability;
			
			if (entity instanceof Unit) {
				Entity bestTarget = entityFilter.getBestEntityTarget(entity);
				int rangeToTarget = entityFilter.getRangeToTarget(entity, bestTarget);
				ability = entityFilter.getBestAbility(rangeToTarget, entity);
				if (ability != null && bestTarget != null && entity instanceof Builder == false) {
					entity.setEvent(new Event(entity, bestTarget, ability, new AbilityEffect(entity, bestTarget, ability)));
				} else if (entity instanceof Builder) {
					ability = entityFilter.getBestBuilderAbility((Builder) entity);
					entity.setEvent(new Event(entity, entity, ability, new AbilityEffect(entity, bestTarget, ability)));
				} else {
					for (Ability abl : entity.getAbilities()) {
						if (abl instanceof Move) {
							ability = abl;
						}
					}
					if (ability != null) {
						((Move) ability).setMoveToPoint(entityFilter.getNextMovePoint((Unit)entity));
						entity.setEvent(new Event(entity, entity, ability, new AbilityEffect(entity, entity, ability)));
					}

				}
			} else if (entity instanceof ProductionBuilding) {
				if (entity.getName().equals(Building.PORTAL)) {
					if (GameInfo.getRoundInfo().getRoundNumber() % 5 == 0) {
						int wave = GameInfo.getRoundInfo().getRoundNumber()/ 5;
						ArrayList<Unit> units = uf.getNewPortalUnitsByWave(wave);
						for(Unit unit: units) {
							entityMap.add(unit);
						}
					}
				}
			} else if(entity instanceof DefenseBuilding) {
				Entity bestTarget = entityFilter.getBestEntityTarget(entity);
				ability = entityFilter.getBestAbility(entityFilter.getRangeToTarget(entity, bestTarget), entity);
				if (ability != null && bestTarget != null && entity instanceof Builder == false) {
					entity.setEvent(
							new Event(entity, bestTarget, ability, new AbilityEffect(entity, bestTarget, ability)));
				}
			}
		}
		LogPanel.appendNewLine("-> Enemy Events <-");
		goThroughEventList();

		// Collect resources after player and AI moved so that buildings that were
		// destroyed don't give resources
		for (Entity b : entityMap) {
			if (b instanceof Building && b.isControllable()) {
				for (Ability ab : b.getAbilities()) {
					if (ab instanceof CollectResources) {
						if (GameInfo.getRoundInfo().getNewBuildings().contains(b))
							break;
						if (b.getEvent() == null)
							b.setEvent(new Event(b, b, ab, null));
					}
				}
			}
		}
		
		GameInfo.getRoundInfo().getNewBuildings().clear();
		if (Core.getMainJFrame().getCurrentComponent() instanceof MainGamePanel) {
			((MainGamePanel) Core.getMainJFrame().getCurrentComponent()).updateUI();
			((MainGamePanel) Core.getMainJFrame().getCurrentComponent()).getMapPanel().getMapImage().update();
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		LogPanel.appendNewLine("-> Building Events <-");
		goThroughEventList();
		LogPanel.appendNewLine("Farmers collected " + collectedFood +" Food");
		LogPanel.appendNewLine("Lumberjacks collected " + collectedWood +" Wood");
		LogPanel.appendNewLine("Stonemasons collected " + collectedStone +" Stone");
		LogPanel.appendNewLine("Metalsmiths collected " + collectedMetal +" Metal");
		LogPanel.appendNewLine("Goldminers collected " + collectedGold +" Gold");
		LogPanel.appendNewLine("Mage apprentices collected " + collectedManaStone +" Manastones");
		
		collectedWood = 0;
		collectedFood = 0;
		collectedStone = 0;
		collectedGold = 0;
		collectedManaStone = 0;
		collectedMetal = 0;
		
		for (Entity e : entityMap) {
			if (e.isAutoIdle()) {
				e.setEvent(new Event(e, e, new Idle(), null));
			}
		}
		
		GameInfo.getRoundInfo().increRoundNum();
		// Update stuff
		if (Core.getMainJFrame().getCurrentComponent() instanceof MainGamePanel) {
			((MainGamePanel) Core.getMainJFrame().getCurrentComponent()).updateUI();
			((MainGamePanel) Core.getMainJFrame().getCurrentComponent()).getMapPanel().getMapImage().update();
		}
		
		if(new Boolean(Core.loadSetting(Core.SETTING_AUTO_SAVE)).booleanValue()) {
			XMLSaveAndLoad.saveGame(Core.GAME_PATH_SAVES + File.separator + "autosave_" + GameInfo.getRoundInfo().getRoundNumber()%3 + ".xml");
		}
		j.setEnabled(true);
	}

	private void goThroughEventList() {
		for (Iterator<Event> iterator = GameInfo.getRoundInfo().getEventHandler().getEventList().iterator(); iterator.hasNext();) {
			Event e = iterator.next();
			if (e.getAbility() instanceof CollectResources == false) {
				if (Core.getMainJFrame().getCurrentComponent() instanceof MainGamePanel) {
					MainGamePanel mp = (MainGamePanel) Core.getMainJFrame().getCurrentComponent();
					mp.getMapPanel().setPosition(e.getSource().getXPos(), e.getSource().getYPos());
				}
			}
			if(!(e.getAbility() instanceof Move) && !(e.getAbility() instanceof CollectResources)) {
				LogPanel.appendNewLine(e.toString());
			}
			if (e.getAbility() instanceof CollectResources) {
				int type = ((CollectResources)e.getAbility()).getResourceType();
				switch (type) {
				case CollectResources.RESOURCE_TYPE_FOOD:
					collectedFood += ((CollectResources)e.getAbility()).getResourcesToBeCollected();
					break;
				case CollectResources.RESOURCE_TYPE_GOLD:
					collectedGold += ((CollectResources)e.getAbility()).getResourcesToBeCollected();
					break;
				case CollectResources.RESOURCE_TYPE_MANA_STONE:
					collectedManaStone += ((CollectResources)e.getAbility()).getResourcesToBeCollected();
					break;
				case CollectResources.RESOURCE_TYPE_METAL:
					collectedMetal += ((CollectResources)e.getAbility()).getResourcesToBeCollected();
					break;
				case CollectResources.RESOURCE_TYPE_STONE:
					collectedStone += ((CollectResources)e.getAbility()).getResourcesToBeCollected();
					break;
				case CollectResources.RESOURCE_TYPE_WOOD:
					collectedWood += ((CollectResources)e.getAbility()).getResourcesToBeCollected();
					break;
				}
			}

			if(e.getAbility() instanceof CollectResources == false) {
				try {
					Thread.sleep(eventCycleSpeed);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			e.run();
			// due to concurrent modification issues iterator is used to remove the current
			// item from the list and a helper method (removeEventWithoutRemovingFromList)
			// is used to remove Events as intended
			e.getSource().removeEventWithoutRemovingFromList();
			iterator.remove();
			System.gc();
			if (Core.getMainJFrame().getCurrentComponent() instanceof MainGamePanel) {
				MainGamePanel mp = (MainGamePanel) Core.getMainJFrame().getCurrentComponent();
				mp.updateUI();
				mp.getMapPanel().getMapImage().update();
			}
			if(e.getAbility() instanceof CollectResources == false) {
				try {
					Thread.sleep(eventCycleSpeed);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}

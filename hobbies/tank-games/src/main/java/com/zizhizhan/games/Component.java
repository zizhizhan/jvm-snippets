package com.zizhizhan.games;

import java.awt.Rectangle;
import java.util.List;

public interface Component extends Drawable {
	
	boolean add(Component c);
	boolean remove(Component c);

	Rectangle getScope();
	List<Component> getComposites();
	
	void setScenario(Scenario scenario);	
	Scenario getScenario();
	
	Team getTeam();
}

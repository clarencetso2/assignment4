package assignment4;

import java.util.*;
public class Critter1 extends Critter {
	int direction = 0;
	public String toString() { 
		return "1"; 
	}
	
	
	public void doTimeStep() {
		walk(direction);
		direction = getRandomInt(8);
	}
	

	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 0) 
			return true;
		
		return false;
	}
	

}

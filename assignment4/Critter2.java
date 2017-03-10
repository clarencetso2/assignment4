package assignment4;

import java.util.*;
public class Critter2 extends Critter {
	int direction = 0;
	public String toString() { 
		return "1"; 
	}
	
	
	public void doTimeStep() {
		walk(direction);
		direction = (direction + 1)% 8;
	}
	

	@Override
	public boolean fight(String opponent) {
		
		return false;
	}
	

}

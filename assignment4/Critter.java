package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */


import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static Critter[][] critterGrid = new Critter [Params.world_height][Params.world_width];
	protected boolean moved = false; //checks if critter has already moved in timeStep
	protected boolean timeStep = false; // checks whether walk/run is called in timeStep or fight (false if in timeStep)
	
	
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	/**
	 * If called in doTimeStep() in Critter: Critter moves a space in a direction
	 * If called in fight() in Critter: attempts to move Critter a space in specified direction(if space open)
	 * @param direction critter will move (8 directions)
	 */
	protected final void walk(int direction) {
		//if it hasnt moved and is doing timeStep
		if(moved == false && timeStep == false){
			executeMove(1,direction);
			moved = true;
		}
		//if in fight and hasnt moved
		else if(moved == false){
			if(unOccupied(1,direction)){
				executeMove(1,direction);
				moved = true;
			}
			
		}
		energy-=Params.walk_energy_cost;

		
	}
	
	
	/**
	 * If called in doTimeStep() in Critter: Critter moves a space in a direction
	 * If called in fight() in Critter: attempts to move Critter a space in specified direction(if space open)
	 * @param direction critter will move (8 directions)
	 */
	protected final void run(int direction) {
		//if it hasnt moved and is doing timeStep
				if(moved == false && timeStep == false){
					executeMove(2,direction);
					moved = true;
				}
				//if in fight and hasnt moved
				else if(moved == false){
					if(unOccupied(2,direction)){
						executeMove(2,direction);
						moved = true;
					}
					
				}
				energy-=Params.run_energy_cost;
	}
	
	
	
	
	/**
	 * checks if critter can move into empty spot
	 * @param numSteps Number of steps to take in direction
	 * @param direction of movement
	 * @return True if the spot not occupied
	 */
	protected boolean unOccupied(int numSteps,int direction){
		int xTemp= x_coord;
		int yTemp= y_coord;
		if(direction==0 || direction==1 || direction==7){  // right directions
			xTemp += numSteps;
		}
		if(direction == 1 || direction == 2 || direction == 3){	// up directions
		
			yTemp += numSteps;
		}
		
		if(direction== 3 || direction== 4 || direction== 5){  // left directions
			xTemp -= numSteps;
		}
		
		if(direction == 5 || direction == 6 || direction == 7){	// down directions
			
			yTemp -= numSteps;
		}
		
		if(xTemp<0){  					//check for overflow
			xTemp+=Params.world_width;
		}
		if(yTemp<0){
			yTemp+=Params.world_height;	//check for overflow
		}
		x_coord=x_coord%Params.world_width;	// keeps critter on the board 
		y_coord=y_coord%Params.world_height;
		
		if(occupied(xTemp,yTemp) == false){
			return true;
		}
		else 
			return false;
	}
	
	
	/**
	 * Moves critter numsteps in a direction and wraps if needed
	 * @param numSteps The number of steps to move the Critter.
	 * @param direction The direction to move the Critter.
	 */
	protected final void executeMove(int numSteps, int direction){
		if(direction==0 || direction==1 || direction==7){  // right directions
			x_coord += numSteps;
		}
		if(direction == 1 || direction == 2 || direction == 3){	// up directions
		
			y_coord += numSteps;
		}
		
		if(direction== 3 || direction== 4 || direction== 5){  // left directions
			x_coord -= numSteps;
		}
		
		if(direction == 5 || direction == 6 || direction == 7){	// down directions
			
			y_coord -= numSteps;
		}
		
		if(x_coord<0){  					//check for neg and places onto board
			x_coord+=Params.world_width;
		}
		if(y_coord<0){
			y_coord+=Params.world_height;	//check for neg
		}
		x_coord=x_coord%Params.world_width;	// keeps critter on the board 
		y_coord=y_coord%Params.world_height;
	}
	
	
	protected final void reproduce(Critter offspring, int direction) {
		if(this.energy < Params.min_reproduce_energy){
			return;
		}
		else{
			offspring.x_coord = this.x_coord;
			offspring.y_coord = this.y_coord;
			offspring.executeMove(1, direction);
			offspring.moved = false;
			offspring.energy = this.energy/2;
			this.energy = (int) Math.ceil(((double) (this.energy))/2);
			babies.add(offspring);
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
	
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
		critterGrid=new Critter[Params.world_height][Params.world_width];
	}
	
	public static void worldTimeStep() {
		// Do timestep for all critters
		doTimeStepAllCritters();
		
		//resolve encounter
		resolveEncounters();
		
		//Invoke rest energy cost on all critters
		updateEnergy();
		
		//remove dead critters (energy < 0)
		removeDead();
		
		//add new baby critters
		addCritters();
		
		
		
		
		
		
		
	}
	
	public static void displayWorld() {
		// Complete this method.
	}
	
	
	/**
	 * Update energy of each Critter and populates a 2d grid array
	 */
	private static void updateEnergy(){
			//Update rest energy and grid
		for(Critter c:population){
			c.energy=c.getEnergy()-Params.rest_energy_cost; // make sure to do b4 dead
		}
	}
	
	
	
	/*
	 * adds new critters into population array
	 */
	public static void addCritters(){
		for(int i = 0; i < babies.size(); i++){
			population.add(babies.get(i));
		}
	}
	
	/**
	 * Remove dead critters from population
	 */
	private static void removeDead(){
		//Remove dead critters from populaiton list
		for(int i = 0; i < population.size(); i++){
			if(population.get(i).energy <= 0){
				population.remove(i);
				i--;   //shifts back everything in list
			}   
		}
		
		
	}
	
	/**
	 * Executes the doTimeStep() function for each Critter
	 */
	private static void doTimeStepAllCritters(){
		//Do time step for each critter
		for(Critter c:population){
			c.moved=false; 
			c.timeStep=true; 
			c.doTimeStep();
			c.timeStep=false; 
		}
	}
	
	
	/**
	 * Resolves encounters between critters
	 */
	private static void resolveEncounters(){
		int diceA, diceB;
		Critter temp1, temp2;
		boolean fightAB, fightBA;
		
		//check for encounters on all critters in population
		for (int i = 0; i < population.size(); i++){
			temp1=population.get(i);
			temp1.timeStep=false;
			for(int j = i + 1; j < population.size(); j++){
				temp2=population.get(j);
				temp2.timeStep=false;
				
				//If temp1 energy about to die, cant fight
				if(temp1.energy<=0){
					break;
				}
				//If temp2 about to die, temp1 doesnt need to fight
				if(temp2.energy<=0){
					continue;
				}
				//if not at same location, cant fight
				if(sameLocation(temp1,temp2) == false){
					continue;
				}
				
				//check if want to fight
				fightAB=temp1.fight(temp2.toString());
				fightBA=temp2.fight(temp1.toString());
				diceA = 0; diceB = 0;
				
				//check if at same location and both alive
				if(temp1.getEnergy() > 0 && temp2.getEnergy() > 0 && sameLocation(temp1, temp2) == true){
					if(fightAB){
						diceA = getRandomInt(temp1.getEnergy());
					}
					if(fightBA)
					{
						diceB = getRandomInt(temp2.getEnergy());
					}
					
					if(diceA >= diceB){
						temp1.energy= temp1.getEnergy() + temp2.getEnergy()/2;
						temp2.energy=0;
					}else{
						temp2.energy= temp2.getEnergy() + temp1.getEnergy()/2;
						temp1.energy=0;
					}				}
			}
		
		}
	}
	
	
	
	
	/**
	 * Checks if the critters are in the same location 
	 * @param a First Critter
	 * @param b Second Critter
	 * @return True if both critters are in the same location, false otherwise
	 */
	private static boolean sameLocation(Critter a,Critter b){
		if(a.x_coord==b.x_coord && b.y_coord==a.y_coord){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the current critter's location is occupied
	 * @param a Critter
	 * @return True if the space the critter is in is occupied
	 */
	public static boolean occupied(Critter a){
		for(Critter c:population){
			if(c.energy>0 && sameLocation(a,c)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the current x and y coordinate are free
	 * @param x X coordinate or column to check
	 * @param y Y coordinate or row to check
	 * @return True if the (x,y) coordinate is occupied
	 */
	private static boolean occupied(int x, int y){
		for(Critter c:population){
			if(c.energy>0 && c.x_coord==x && c.y_coord==y){
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	
	
		
}

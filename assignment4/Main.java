package assignment4;
/* CRITTERS Main.java
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

import java.util.Scanner;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
        int inputFlag = 1;
        String[] commands = {"quit","show","step","seed","make","stats"};
        System.out.print("critters>");
        while (inputFlag == 1) {
    		String inputLine = kb.nextLine();
    		StringTokenizer input = new StringTokenizer(inputLine);
    		boolean exception = true;
    		
    		if(!input.hasMoreElements()) {
    			System.out.println("invalid command: " + inputLine);
    			continue;
    		}
    		
    		String command = input.nextToken();
    		
    		for(int i = 0; i < 6; i++) {
    			if(command.equals(commands[i])) {
    				exception = false;
    			}
    		}
    		
    		if(exception == true) {
    			System.out.println("invalid command: " + inputLine);
    			exception = false;
    		}
    		
    		else if(command.equals("quit") && input.hasMoreTokens() == false) {
    			inputFlag = 0;
    		}
    		
    		else if(command.equals("show") && input.hasMoreTokens() == false) {
    			Critter.displayWorld();
    		}
    		
    		else if(command.equals("step") && input.hasMoreTokens() == false) {
    			Critter.worldTimeStep();
    		}
    		
    		else if(command.equals("step") && input.hasMoreTokens() == true) {
    			try {
	    			String num = input.nextToken();
					int number = Integer.parseInt(num);
	    			if(input.hasMoreTokens() == false) {
	    				if(number > 1) {
		    				for(int i = 0; i < number; i++) {
		    					Critter.worldTimeStep();
		    				}
	    				}
	    				else {
	    					exception = true;
	    				}
	    			}
	    			else {
	    				exception = true;
	    			}
    			}
    			catch (NumberFormatException e) {
    				exception = true;
    			}
    		}
    		
    		else if(command.equals("seed") && input.hasMoreTokens() == true) {
    			try {
	    			String num = input.nextToken();
					int number = Integer.parseInt(num);
	    			if(input.hasMoreTokens() == false) {
	    				Critter.setSeed(number);
	    			}
	    			else {
	    				exception = true;
	    			}
    			}
    			catch (NumberFormatException e) {
    				exception = true;
    			}
    		}
    		
    		else if(command.equals("make") && input.hasMoreTokens() == true) {
    			String creatureType = input.nextToken();
    			if(input.hasMoreTokens()) {
    				try {
	    				String num = input.nextToken();
	    				int number = Integer.parseInt(num);
	    				
	    				if(input.hasMoreTokens()){
	    					exception = true;
	    				}
	    				
	    				
	    				if(number > 1) {
		    				for(int i = 0; i < number; i++) {
		        				try{
		        					Critter.makeCritter(creatureType);
		        				}
		        				catch (InvalidCritterException e) {
		        					exception = true;
		        				}
		    				}
	    				}
	    				else {
	    					exception = true;
	    				}
    				}
        			catch (NumberFormatException e) {
        				exception = true;
        			}
    			}
    			else {
    				try{
    					Critter.makeCritter(creatureType);
    				}
    				catch (InvalidCritterException e) {
    					exception = true;
    				}
    			}
    		}
    		
    		else if(command.equals("stats") && input.hasMoreTokens() == true) {
    			String creatureType = input.nextToken();
    			if(!input.hasMoreTokens()) {
    				try {
    					List<Critter> result = Critter.getInstances(creatureType);
						Class myCritter = Class.forName(myPackage + "." + creatureType);
						Method method = myCritter.getMethod("runStats", List.class);
						method.invoke(null , result);
						
						} 
    					catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								exception = true;
						} 
    					catch (SecurityException e) {
								// TODO Auto-generated catch block
								exception = true;
						}

	    				
						catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							exception = true;
						}
    					
    									
    					catch (InvocationTargetException e) {
    					// TODO Auto-generated catch block
					    exception = true;
    				
    					}
    				catch (IllegalAccessException e) {
    					// TODO Auto-generated catch block
					    exception = true;
    				
    					}
    				
    				catch (InvalidCritterException e) {
    					exception = true;
    				}
    			}
    			
    			else {
    				exception = true;
    			}
    		}
    		
    		else {
    			exception = true;
    		}
    		
    		if(exception == true) {
    			System.out.println("error processing: " + inputLine);
    		}
    		else {
    			System.out.print("critters>");
    		}
        }
        
        System.out.flush();

    }
}

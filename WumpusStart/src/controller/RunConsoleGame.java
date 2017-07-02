package controller;

import java.util.Random;
import java.util.Scanner;

public class RunConsoleGame {

	// Game objects
	//
	// Each object is represented by an integer. The integer is used
	// as an index into the 'locationOf' array to determine where the
	// game object is located in the cave rooms.
	//
	// In some cases, the code is written to expect that HUNTER is always
	// stored in the first index ('0') of the array. The index values for
	// the other objects could be changed withSystem.out impacting program
	// execution.

	public static int NUM_OF_OBJECTS = 5;

	public static final int HUNTER = 0; // Don't change! HUNTER is expected to
										// be index '0'
	public static final int WUMPUS = 1;
	public static final int PIT1 = 2;
	public static final int PIT2 = 3;
	public static final int PIT3 = 4;
	public static final int PIT4 = 5;
	public static final int PIT5 = 6;

	// Game states
	//
	// Game state is passed throughSystem.out the supporting procedures and
	// is used for execution flow control.

	public static final int WUMPUS_DEAD = 1;
	public static final int HUNTER_DEAD = 2;
	public static final int CONTINUE = 3;
	public static final int QUIT = 4;

	public static int MAX_LENGTH = 12;
	public static int MAX_WIDTH = 12;
	public static Room[][] caves = new Room[MAX_LENGTH][MAX_WIDTH];
	public static Room[] locationOf;
	public static int status;

	public static void Wumpus() {

		setup();
		System.out.println("Hunt the Wumpus\n");
		playGame();
		System.out.println("\nThank you for playing 'Hunt the Wumpus'!\n");

	} // constructor Wumpus

	/*
	 * Game event loop
	 * 
	 * Keep repeating so long as the game state is 'CONTINUE'. Look around the
	 * current cave, take an action, check for hazards, rinse, and repeat.
	 */

	public static void playGame() {

		status = CONTINUE;

		do {
			printCave();
			status = getAction();
			if (status == CONTINUE) {
				status = checkHazards(status);
			}
		} while (status == CONTINUE);

	} // method playGame

	public static void printCave() {
		System.out.println();
		for (int i = 0; i < MAX_LENGTH; i++) {
			for (int j = 0; j < MAX_WIDTH; j++)
				System.out.print(caves[i][j].toString() + " ");
			System.out.println();
		}
		getHazards();
		System.out.println();
	}

	/*
	 * Set up the game
	 * 
	 * Set up the game by placing the game objects into the cave System assuring
	 * that each cave room contains, at most, one object.
	 */

	public static void setup() {
		Random generator = new Random();
		NUM_OF_OBJECTS = NUM_OF_OBJECTS + generator.nextInt(2);
		locationOf = new Room[NUM_OF_OBJECTS];
		initializeCaves();
		for (int j = 0; j < NUM_OF_OBJECTS; j++) {
			Room loc = caves[generator.nextInt(MAX_LENGTH)][generator.nextInt(MAX_WIDTH)]; // pick
																							// a
																							// random
																							// location
			for (int k = (j - 1); k >= 0; k--) { // check prior locations to
													// assure no duplicates
				if (locationOf[k] == loc) { // found a duplicate, so try another
											// location
					loc = caves[generator.nextInt(MAX_LENGTH)][generator.nextInt(MAX_WIDTH)]; // pick
																								// a
																								// new
																								// random
																								// location
					k = j; // k will be reduced by one in the for loop
				} // if location match
			} // for k
			locationOf[j] = loc;
		} // for j
		locationOf[HUNTER].isHunter = true;
		locationOf[WUMPUS].isWumpus = true;
		setBlood(WUMPUS);
		setSlime(PIT1);
		setSlime(PIT2);
		setSlime(PIT3);
		if (NUM_OF_OBJECTS == 6)
			setSlime(PIT4);
		if (NUM_OF_OBJECTS == 7)
			setSlime(PIT5);
		setGoop();
	} // method setup

	public static void setGoop() {
		for (int i = 0; i < MAX_LENGTH; i++) {
			for (int j = 0; j < MAX_WIDTH; j++)
				if (caves[i][j].isBlood && caves[i][j].isSlime) {
					caves[i][j].isBlood = false;
					caves[i][j].isSlime = false;
					caves[i][j].isGoop = true;
				}
		}
	}

	public static void setSlime(int OBJ) {
		caves[locationOf[OBJ].x][locationOf[OBJ].y].isEmpty = false;

		if (locationOf[OBJ].x == 0)
			caves[MAX_LENGTH - 1][locationOf[OBJ].y].isSlime = true;
		else
			caves[locationOf[OBJ].x - 1][locationOf[OBJ].y].isSlime = true;
		if (locationOf[OBJ].x == MAX_LENGTH - 1)
			caves[0][locationOf[OBJ].y].isSlime = true;
		else
			caves[locationOf[OBJ].x + 1][locationOf[OBJ].y].isSlime = true;
		if (locationOf[OBJ].y == 0)
			caves[locationOf[OBJ].x][MAX_WIDTH - 1].isSlime = true;
		else
			caves[locationOf[OBJ].x][locationOf[OBJ].y - 1].isSlime = true;
		if (locationOf[OBJ].y == MAX_WIDTH - 1)
			caves[locationOf[OBJ].x][0].isSlime = true;
		else
			caves[locationOf[OBJ].x][locationOf[OBJ].y + 1].isSlime = true;

	}

	public static void setBlood(int OBJ) {
		if (locationOf[OBJ].x == 0) {
			caves[MAX_LENGTH - 1][locationOf[OBJ].y].isBlood = true;
		} else {
			caves[locationOf[OBJ].x - 1][locationOf[OBJ].y].isBlood = true;
		}
		if (locationOf[OBJ].x == MAX_LENGTH - 1) {
			caves[0][locationOf[OBJ].y].isBlood = true;
		} else {
			caves[locationOf[OBJ].x + 1][locationOf[OBJ].y].isBlood = true;
		}
		if (locationOf[OBJ].y == 0) {
			caves[locationOf[OBJ].x][MAX_WIDTH - 1].isBlood = true;
		} else {
			caves[locationOf[OBJ].x][locationOf[OBJ].y - 1].isBlood = true;
		}
		if (locationOf[OBJ].y == MAX_WIDTH - 1) {
			caves[locationOf[OBJ].x][0].isBlood = true;
		} else {
			caves[locationOf[OBJ].x][locationOf[OBJ].y + 1].isBlood = true;
		}
	}

	public static void initializeCaves() {
		for (int i = 0; i < MAX_LENGTH; i++)
			for (int j = 0; j < MAX_WIDTH; j++) {
				caves[i][j] = new Room(i, j);
				caves[i][j].isEmpty = true;
			}

	}

	/*
	 * Sense any game hazards
	 * 
	 * For each game object, except the hunter (HUNTER = '0'), check each of the
	 * connecting cave rooms to see if that object exists in that room adjacent
	 * room. If it does, then display a warning message on the console.
	 */

	public static void getHazards() {
		Room loc = locationOf[HUNTER];
		if (loc.isSlime)
			System.out.println("\nYou feel a draft.");
		else if (loc.isBlood)
			System.out.println("\nYou smell something foul, maybe 1 or 2 rooms away?");
		else if (loc.isGoop)
			System.out.println("\nYou feel a draft and smell something foul.");
	} // method senseHazards()

	/*
	 * Get the action the player wants to take
	 * 
	 * Repeats asking until a valid action is selected.
	 * 
	 * return the action identifier corresponding to the selected action
	 */

	public static int getAction() {
		while (true) {
			switch (userPrompt()) {
			case "arrow":
				return shoot();
			case "n":
				return moveNorth();
			case "s":
				return moveSouth();
			case "e":
				return moveEast();
			case "w":
				return moveWest();
			} // switch action identifier
		}
	} // method getAction

	@SuppressWarnings("resource")
	public static String userPrompt() {
		Scanner in = new Scanner(System.in);
		System.out.println("Move (n, s, e, w, arrow)? ");
		return in.next();
	}

	/*
	 * Move the hunter to a new room
	 * 
	 * return game state after moving to the new room (always CONTINUE)
	 */

	public static int moveNorth() {
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].visit();
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = false;

		if (locationOf[HUNTER].x == 0)
			locationOf[HUNTER] = caves[MAX_LENGTH - 1][locationOf[HUNTER].y];
		else
			locationOf[HUNTER] = caves[locationOf[HUNTER].x - 1][locationOf[HUNTER].y];

		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = true;
		return CONTINUE;
	} // method moveNorth

	public static int moveSouth() {
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].visit();
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = false;

		if (locationOf[HUNTER].x == MAX_LENGTH - 1)
			locationOf[HUNTER] = caves[0][locationOf[HUNTER].y];
		else
			locationOf[HUNTER] = caves[locationOf[HUNTER].x + 1][locationOf[HUNTER].y];

		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = true;
		return CONTINUE;
	} // method moveSouth

	public static int moveEast() {
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].visit();
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = false;

		if (locationOf[HUNTER].y == MAX_WIDTH - 1)
			locationOf[HUNTER] = caves[locationOf[HUNTER].x][0];
		else
			locationOf[HUNTER] = caves[locationOf[HUNTER].x][locationOf[HUNTER].y + 1];

		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = true;
		return CONTINUE;
	} // method moveEast

	public static int moveWest() {
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].visit();
		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = false;

		if (locationOf[HUNTER].y == 0)
			locationOf[HUNTER] = caves[locationOf[HUNTER].x][MAX_WIDTH - 1];
		else
			locationOf[HUNTER] = caves[locationOf[HUNTER].x][locationOf[HUNTER].y - 1];

		caves[locationOf[HUNTER].x][locationOf[HUNTER].y].isHunter = true;
		return CONTINUE;
	} // method moveWest

	/*
	 * Shoot an arrow from the hunter's bow
	 * 
	 * @return the game status after shooting an arrow
	 */

	public static int shoot() {
		while (true) {
			switch (userPromptShoot()) {
			case "n":
				return shootNorthAndSouth();
			case "s":
				return shootNorthAndSouth();
			case "e":
				return shootEastAndWest();
			case "w":
				return shootEastAndWest();
			} // switch action identifier
		}
	} // method getAction

	public static int shootEastAndWest() {
		if (locationOf[HUNTER].y == locationOf[WUMPUS].y) {
			System.out.println("Your arrow hit the Wumpus. Good shooting. Game over.");
			return WUMPUS_DEAD;
		}
		System.out.println("Your arrow hit you. Bad shooting. Game over.");
		return HUNTER_DEAD;
	}

	public static int shootNorthAndSouth() {
		if (locationOf[HUNTER].x == locationOf[WUMPUS].x) {
			System.out.println("Your arrow hit the Wumpus. Good shooting. Game over.");
			return WUMPUS_DEAD;
		}
		System.out.println("Your arrow hit you. Bad shooting. Game over.");
		return HUNTER_DEAD;
	}

	@SuppressWarnings("resource")
	public static String userPromptShoot() {
		Scanner in = new Scanner(System.in);
		System.out.println("\nShoot (n, s, e, w)? ");
		return in.next();
	}

	/*
	 * Check for the effect of any hazards
	 * 
	 * Check the room the hunter is currently in for the existence of any
	 * hazards. If any exist, take the appropriate action and return game state.
	 * 
	 */

	public static int checkHazards(int status) {
		if (locationOf[HUNTER] == locationOf[WUMPUS]) {
			return wumpusAction();
		} else if ((locationOf[HUNTER] == locationOf[PIT1]) || (locationOf[HUNTER] == locationOf[PIT2])
				|| locationOf[HUNTER] == locationOf[PIT3]) {
			return pitAction();
		} else if (NUM_OF_OBJECTS == 6 && locationOf[HUNTER] == locationOf[PIT4]) {
			return pitAction();
		} else if (NUM_OF_OBJECTS == 7 && locationOf[HUNTER] == locationOf[PIT4]) {
			return pitAction();
		} else {
			return status;
		} // if-else chain condition checks
	} // method checkHazards

	/*
	 * Wumpus Room
	 * 
	 * If enteredm hunter is killed.
	 */

	public static int wumpusAction() {
		System.out.println("\nYou walked into the Wumpus Room!");
		return HUNTER_DEAD;
	} // method wumpusAction

	/*
	 * Pit takes action
	 * 
	 * Not much to say about this--hutner dead, end of game
	 * 
	 */

	public static int pitAction() {
		System.out.println("\nYou fell into a pit! Bad luck.");
		return HUNTER_DEAD;
	} // method pitAction

	/*
	 * Static main method. Program entry point
	 * 
	 */

	public void main(String[] args) {
		Wumpus();
	} // static method main

}

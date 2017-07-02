package tests;


import static org.junit.Assert.*;

import org.junit.Test;

import controller.MainWumpus;
import controller.RunConsoleGame;

public class GameTest {

	/*
	 * Notes:
	 * 
	 * The Game can cover 96% code coverage and work but it only occurs when the game is fully played.
	 * i.e. when every event is triggered which in a single game sitting is not possible. Just letting 
	 * you know this now.
	 * 
	 * Also, the Blood only covers the forst spaces from the Wumpus. Had a problem with that one and haven'y
	 * had the chance to really debug it.
	 * 
	 * But other than those two the game should run completly.
	 * 
	 * 
	 */
	
  @Test
  public void testGame(){
	  RunConsoleGame game = new RunConsoleGame();
	  game.main(null);
  }

}
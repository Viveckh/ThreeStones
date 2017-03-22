package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/4/2017
*/

/* This class keeps track of a Player's current score and available number of white, black and clear stones.
*
*/

public class Player {
	//Variables
	private int score;
	private int whiteStones;
	private int blackStones;
	private int clearStones;

	//Default Constructor
	public Player() {
		score = 0;
		whiteStones = 15;
		blackStones = 15;
		clearStones = 6;
	}

	// Number of available stone Getters
	public int getScore() { return score; }
	public int getWhite() { return whiteStones; }
	public int getBlack() { return blackStones; }
	public int getClear() { return clearStones; }

	// Score adder
	public void addScore(int value) { score += value; }

	//Stone number updater after each use
	public void useWhite() { whiteStones--; }
	public void useBlack() { blackStones--; }
	public void useClear() { clearStones--; }
}

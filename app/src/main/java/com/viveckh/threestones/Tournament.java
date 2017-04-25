package com.viveckh.threestones;

/**
 * Tournament Class
 * Static class that keeps a track of the tournament wins, and also current game play variables.
 * Author: Vivek Pandey
 * Last Modified on: 04/17/2017
 */
public final class Tournament {
	// VARIABLE DECLARATIONS
	//Variables to store primary stone color in current game
	private static char humanStone;
	private static char computerStone;

	//Variables to store the available stones for both players in current game
	private static int humanWhiteStonesCount;
	private static int humanBlackStonesCount;
	private static int humanClearStonesCount;
	private static int computerWhiteStonesCount;
	private static int computerBlackStonesCount;
	private static int computerClearStonesCount;

	//Variables to store the scores of current game
	private static int humanScore;
	private static int computerScore;
	//Variables to store the overall wins by each player in the tournament
	private static int humanWins;
	private static int computerWins;

	//Variables necessary for implementing Game rules
	private static int rowOfLastPlacement;
	private static int columnOfLastPlacement;
	private static String nextPlayer;   //"human" or "computer" all lower case

	/**
	 * PRIVATE CONSTRUCTOR
	 */
	private Tournament() {
		humanWhiteStonesCount = 15;
		humanBlackStonesCount = 15;
		humanClearStonesCount = 6;
		computerWhiteStonesCount = 15;
		computerBlackStonesCount = 15;
		computerClearStonesCount = 6;

		humanScore = 0;
		computerScore = 0;
		humanWins = 0;
		computerWins = 0;
		humanStone = 'w';
		computerStone = 'b';

		rowOfLastPlacement = -1;
		columnOfLastPlacement = -1;
	}

	public static int GetHumanWhiteStonesCount() {
		return humanWhiteStonesCount;
	}

	public static int GetHumanBlackStonesCount() {
		return humanBlackStonesCount;
	}

	public static int GetHumanClearStonesCount() {
		return humanClearStonesCount;
	}

	public static int GetComputerWhiteStonesCount() {
		return computerWhiteStonesCount;
	}

	public static int GetComputerBlackStonesCount() {
		return computerBlackStonesCount;
	}

	public static int GetComputerClearStonesCount() {
		return computerClearStonesCount;
	}

	public static char GetHumanStone() {
		return humanStone;
	}

	public static char GetComputerStone() {
		return computerStone;
	}

	/**
	 * Gets Human Score in the tournament
	 * @return human score
	 */
	public static int GetHumanScore() {
		return humanScore;
	}

	/**
	 * Gets computer score in the tournament
	 * @return computer score
	 */
	public static int GetComputerScore() {
		return computerScore;
	}

	/**
	 * Gets Human wins in the tournament
	 * @return human wins
	 */
	public static int GetHumanWins() {
		return humanWins;
	}

	/**
	 * Gets computer wins in the tournament
	 * @return computer wins
	 */
	public static int GetComputerWins() {
		return computerWins;
	}

	public static int GetRowOfLastPlacement() {
		return rowOfLastPlacement;
	}

	public static int GetColumnOfLastPlacement() {
		return columnOfLastPlacement;
	}

	/**
	 * Gets next player in case if the tournament is resumed from a saved state
	 * @return Next Player
	 */
	public static String GetNextPlayer() {
		return nextPlayer;
	}

	public static void SaveCurrentGameStatus(char a_humanStone, char a_computerStone,
							     int a_humanWhiteStonesCount,
							     int a_humanBlackStonesCount,
							     int a_humanClearStonesCount,
							     int a_computerWhiteStonesCount,
							     int a_computerBlackStonesCount,
							     int a_computerClearStonesCount,
							     int a_humanScore, int a_computerScore) {
		humanWhiteStonesCount = a_humanWhiteStonesCount;
		humanBlackStonesCount = a_humanBlackStonesCount;
		humanClearStonesCount = a_humanClearStonesCount;
		computerWhiteStonesCount = a_computerWhiteStonesCount;
		computerBlackStonesCount = a_computerBlackStonesCount;
		computerClearStonesCount = a_computerClearStonesCount;

		humanStone = a_humanStone;
		computerStone = a_computerStone;
		humanScore = a_humanScore;
		computerScore = a_computerScore;
	}

	/**
	 * Increments Human Wins by certain points
	 * @param a_bumpWinsBy Value to bump Human wins by
	 */
	public static void IncrementHumanWinsBy(int a_bumpWinsBy) {
		humanWins += a_bumpWinsBy;
	}

	/**
	 * Increments Computer Wins by certain points
	 * @param a_bumpWinsBy Value to bump Computer wins by
	 */
	public static void IncrementComputerWinsBy(int a_bumpWinsBy) {
		computerWins += a_bumpWinsBy;
	}

	/**
	 * Resets scores for a fresh start
	 */
	public static void ResetScores() {
		humanScore = 0;
		computerScore = 0;
		humanWins = 0;
		computerWins = 0;
	}

	public static void SetControls(int a_rowOfLastPlacement, int a_columnOfLastPlacement,
						 String player) {
		rowOfLastPlacement = a_rowOfLastPlacement;
		columnOfLastPlacement = a_columnOfLastPlacement;

		if (player.equals("computer") || player.equals("Computer")) {
			nextPlayer = "computer";
		}
		else {
			nextPlayer = "human";
		}
	}
}

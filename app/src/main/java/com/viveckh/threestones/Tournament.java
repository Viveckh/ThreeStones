package com.viveckh.threestones;

/**
 * Tournament Class
 * Static class that keeps a track of the tournament scores and next player throughout the tournament.
 * Author: Vivek Pandey
 * Last Modified on: 04/17/2017
 */
public final class Tournament {
	// VARIABLES
	//Primary stone color in current game
	private static char humanStone;
	private static char computerStone;

	private static int humanWhiteStonesCount;
	private static int humanBlackStonesCount;
	private static int humanClearStonesCount;
	private static int computerWhiteStonesCount;
	private static int computerBlackStonesCount;
	private static int computerClearStonesCount;

	//Scores in current game in progress
	private static int humanScore;
	private static int computerScore;
	//Wins overall in the tournament
	private static int humanWins;
	private static int computerWins;
	private static String nextPlayer;   //"human" or "computer" all lower case

	/**
	 * PRIVATE CONSTRUCTOR
	 */
	private Tournament() {
		humanScore = 0;
		computerScore = 0;
		humanWins = 0;
		computerWins = 0;
		humanStone = 'w';
		computerStone = 'b';
	}

	//CONSTRUCTOR
	private Tournament(char a_humanStone, char a_computerStone, int a_humanWhiteStonesCount, int a_humanBlackStonesCount, int a_humanClearStonesCount, int a_computerWhiteStonesCount, int a_computerBlackStonesCount, int a_computerClearStonesCount, int a_humanScore, int a_computerScore, int a_humanWins, int a_computerWins) {
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
		humanWins = a_humanWins;
		computerWins = a_computerWins;
	}

	public static int GetHumanWhiteStonesCount() { return humanWhiteStonesCount; }

	public static int GetHumanBlackStonesCount() { return humanBlackStonesCount; }

	public static int GetHumanClearStonesCount() { return humanClearStonesCount; }

	public static int GetComputerWhiteStonesCount() { return computerWhiteStonesCount; }

	public static int GetComputerBlackStonesCount() { return computerBlackStonesCount; }

	public static int GetComputerClearStonesCount() { return computerClearStonesCount; }

	public static char GetHumanStone() { return humanStone; }

	public static char GetComputerStone() { return computerStone; }

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

	/**
	 * Gets next player in case if the tournament is resumed from a saved state
	 * @return Next Player
	 */
	public static String GetNextPlayer() {
		return nextPlayer;
	}

	public static void SaveCurrentGameStatus(char a_humanStone, char a_computerStone, int a_humanWhiteStonesCount, int a_humanBlackStonesCount, int a_humanClearStonesCount, int a_computerWhiteStonesCount, int a_computerBlackStonesCount, int a_computerClearStonesCount, int a_humanScore, int a_computerScore) {
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
	public static void IncrementHumanWins(int a_bumpWinsBy) {
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

	/**
	 * Sets next player
	 * @param player the next player ("computer" or "human")
	 */
	public static void SetNextPlayer(String player) {
		if (player == "computer" || player == "Computer") {
			nextPlayer = "computer";
		}
		else {
			nextPlayer = "human";
		}
	}
}

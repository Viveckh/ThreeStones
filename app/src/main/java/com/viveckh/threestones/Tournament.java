package com.viveckh.threestones;

/**
 * Tournament Class
 * Static class that keeps a track of the tournament scores and next player throughout the tournament.
 * Author: Vivek Pandey
 * Last Modified on: 04/17/2017
 */
public final class Tournament {
	// VARIABLES
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
	}

	//CONSTRUCTOR
	private Tournament(int a_humanScore, int a_computerScore, int a_humanWins, int a_computerWins) {
		humanScore = a_humanScore;
		computerScore = a_computerScore;
		humanWins = a_humanWins;
		computerWins = a_computerWins;
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

	/**
	 * Gets next player in case if the tournament is resumed from a saved state
	 * @return Next Player
	 */
	public static String GetNextPlayer() {
		return nextPlayer;
	}

	public static void SetGameScores(int a_humanScore, int a_computerScore) {
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
	 * Set game scores
	 */
	public static void SetScores(int a_humanScore, int a_computerScore) {
		humanScore = a_humanScore;
		computerScore = a_computerScore;
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

package com.viveckh.threestones;

/**
 * <h1>Tournament Model Class</h1>
 * This Singleton class keeps a track of the tournament wins, along with attributes associated with
 * current game like the primary stone of each player, available stones, scores and info about last
 * placement. It comes in handy while saving and restoring a tournament.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
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
	 * Tournament Default Constructor
	 * Privately initializes the class and sets the class variables to the default values
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

	/**
	 * Gets available white stones of human player
	 *
	 * @return Integer, Human player's available white stones
	 */
	public static int GetHumanWhiteStonesCount() {
		return humanWhiteStonesCount;
	}

	/**
	 * Gets available black stones of human player
	 *
	 * @return Integer, Human player's available black stones
	 */
	public static int GetHumanBlackStonesCount() {
		return humanBlackStonesCount;
	}

	/**
	 * Gets available clear stones of human player
	 *
	 * @return Integer, Human player's available clear stones
	 */
	public static int GetHumanClearStonesCount() {
		return humanClearStonesCount;
	}

	/**
	 * Gets available white stones of Computer player
	 *
	 * @return Integer, Computer player's available white stones
	 */
	public static int GetComputerWhiteStonesCount() {
		return computerWhiteStonesCount;
	}

	/**
	 * Gets available black stones of Computer player
	 *
	 * @return Integer, Computer player's available black stones
	 */
	public static int GetComputerBlackStonesCount() {
		return computerBlackStonesCount;
	}

	/**
	 * Gets available clear stones of Computer player
	 *
	 * @return Integer, Computer player's available clear stones
	 */
	public static int GetComputerClearStonesCount() {
		return computerClearStonesCount;
	}

	/**
	 * Gets human player's primary stone
	 *
	 * @return Character, Human player's primary stone
	 */
	public static char GetHumanStone() {
		return humanStone;
	}

	/**
	 * Gets Computer player's primary stone
	 *
	 * @return Character, Computer player's primary stone
	 */
	public static char GetComputerStone() {
		return computerStone;
	}

	/**
	 * Gets Human Score in the current game
	 *
	 * @return Integer, Human player's score in current game
	 */
	public static int GetHumanScore() {
		return humanScore;
	}

	/**
	 * Gets computer score in the tournament
	 *
	 * @return Integer, Computer player's score in current game
	 */
	public static int GetComputerScore() {
		return computerScore;
	}

	/**
	 * Gets total number of Human wins in the tournament so far
	 *
	 * @return Integer, human player's wins so far in the tournament
	 */
	public static int GetHumanWins() {
		return humanWins;
	}

	/**
	 * Gets total number of computer wins in the tournament so far
	 *
	 * @return Integer, computer player's wins in the tournament
	 */
	public static int GetComputerWins() {
		return computerWins;
	}

	/**
	 * Gets the row where the last stone was placed in the game
	 *
	 * @return Integer, row of last stone placement in game
	 */
	public static int GetRowOfLastPlacement() {
		return rowOfLastPlacement;
	}

	/**
	 * Gets the column where the last stone was placed in the game
	 *
	 * @return Integer, column of last stone placement in game
	 */
	public static int GetColumnOfLastPlacement() {
		return columnOfLastPlacement;
	}

	/**
	 * Gets next player in the game (useful for serialization)
	 *
	 * @return String, next player in the game
	 */
	public static String GetNextPlayer() {
		return nextPlayer;
	}

	/**
	 * SaveCurrentGame() - Stores the values associated with the current game
	 *
	 * @param a_humanStone               Character, primary stone of human player
	 * @param a_computerStone            Character, primary stone of computer player
	 * @param a_humanWhiteStonesCount    Integer, available white stones of human player
	 * @param a_humanBlackStonesCount    Integer, available black stones of human player
	 * @param a_humanClearStonesCount    Integer, available clear stones of human player
	 * @param a_computerWhiteStonesCount Integer, available white stones of computer player
	 * @param a_computerBlackStonesCount Integer, available black stones of computer player
	 * @param a_computerClearStonesCount Integer, available clear stones of computer player
	 * @param a_humanScore               Integer, current human player score in game
	 * @param a_computerScore            Integer, current computer player score in game
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
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
	 * Increments Human Wins in tournament by given value.
	 *
	 * @param a_bumpWinsBy Value to bump Human wins by. Should be 1 under normal conditions.
	 */
	public static void IncrementHumanWinsBy(int a_bumpWinsBy) {
		humanWins += a_bumpWinsBy;
	}

	/**
	 * Increments Computer Wins in tournament by given value
	 *
	 * @param a_bumpWinsBy Value to bump Computer wins by. Should be 1 under normal conditions
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
	 * Stores the variables associated with the current game controls in tournament
	 *
	 * @param a_rowOfLastPlacement    Integer, row of last stone placement
	 * @param a_columnOfLastPlacement Integer, column of last stone placement
	 * @param player                  String, next player to make the move
	 */
	public static void SetControls(int a_rowOfLastPlacement, int a_columnOfLastPlacement,
						 String player) {
		rowOfLastPlacement = a_rowOfLastPlacement;
		columnOfLastPlacement = a_columnOfLastPlacement;

		if (player.equals("computer") || player.equals("Computer")) {
			nextPlayer = "computer";
		} else {
			nextPlayer = "human";
		}
	}
}

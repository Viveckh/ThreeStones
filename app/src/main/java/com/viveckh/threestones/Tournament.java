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
	private static char m_humanStone;
	private static char m_computerStone;

	//Variables to store the available stones for both players in current game
	private static int m_humanWhiteStonesCount;
	private static int m_humanBlackStonesCount;
	private static int m_humanClearStonesCount;
	private static int m_computerWhiteStonesCount;
	private static int m_computerBlackStonesCount;
	private static int m_computerClearStonesCount;

	//Variables to store the scores of current game
	private static int m_humanScore;
	private static int m_computerScore;
	//Variables to store the overall wins by each player in the tournament
	private static int m_humanWins;
	private static int m_computerWins;

	//Variables necessary for implementing Game rules
	private static int m_rowOfLastPlacement;
	private static int m_columnOfLastPlacement;
	private static String m_nextPlayer;   //"human" or "computer" all lower case

	/**
	 * Tournament Default Constructor
	 * Privately initializes the class and sets the class variables to the default values
	 */
	private Tournament() {
		m_humanWhiteStonesCount = 15;
		m_humanBlackStonesCount = 15;
		m_humanClearStonesCount = 6;
		m_computerWhiteStonesCount = 15;
		m_computerBlackStonesCount = 15;
		m_computerClearStonesCount = 6;

		m_humanScore = 0;
		m_computerScore = 0;
		m_humanWins = 0;
		m_computerWins = 0;
		m_humanStone = 'w';
		m_computerStone = 'b';

		m_rowOfLastPlacement = -1;
		m_columnOfLastPlacement = -1;
	}

	/**
	 * Gets available white stones of human player
	 *
	 * @return Integer, Human player's available white stones
	 */
	public static int GetHumanWhiteStonesCount() {
		return m_humanWhiteStonesCount;
	}

	/**
	 * Gets available black stones of human player
	 *
	 * @return Integer, Human player's available black stones
	 */
	public static int GetHumanBlackStonesCount() {
		return m_humanBlackStonesCount;
	}

	/**
	 * Gets available clear stones of human player
	 *
	 * @return Integer, Human player's available clear stones
	 */
	public static int GetHumanClearStonesCount() {
		return m_humanClearStonesCount;
	}

	/**
	 * Gets available white stones of Computer player
	 *
	 * @return Integer, Computer player's available white stones
	 */
	public static int GetComputerWhiteStonesCount() {
		return m_computerWhiteStonesCount;
	}

	/**
	 * Gets available black stones of Computer player
	 *
	 * @return Integer, Computer player's available black stones
	 */
	public static int GetComputerBlackStonesCount() {
		return m_computerBlackStonesCount;
	}

	/**
	 * Gets available clear stones of Computer player
	 *
	 * @return Integer, Computer player's available clear stones
	 */
	public static int GetComputerClearStonesCount() {
		return m_computerClearStonesCount;
	}

	/**
	 * Gets human player's primary stone
	 *
	 * @return Character, Human player's primary stone
	 */
	public static char GetHumanStone() {
		return m_humanStone;
	}

	/**
	 * Gets Computer player's primary stone
	 *
	 * @return Character, Computer player's primary stone
	 */
	public static char GetComputerStone() {
		return m_computerStone;
	}

	/**
	 * Gets Human Score in the current game
	 *
	 * @return Integer, Human player's score in current game
	 */
	public static int GetHumanScore() {
		return m_humanScore;
	}

	/**
	 * Gets computer score in the tournament
	 *
	 * @return Integer, Computer player's score in current game
	 */
	public static int GetComputerScore() {
		return m_computerScore;
	}

	/**
	 * Gets total number of Human wins in the tournament so far
	 *
	 * @return Integer, human player's wins so far in the tournament
	 */
	public static int GetHumanWins() {
		return m_humanWins;
	}

	/**
	 * Gets total number of computer wins in the tournament so far
	 *
	 * @return Integer, computer player's wins in the tournament
	 */
	public static int GetComputerWins() {
		return m_computerWins;
	}

	/**
	 * Gets the row where the last stone was placed in the game
	 *
	 * @return Integer, row of last stone placement in game
	 */
	public static int GetRowOfLastPlacement() {
		return m_rowOfLastPlacement;
	}

	/**
	 * Gets the column where the last stone was placed in the game
	 *
	 * @return Integer, column of last stone placement in game
	 */
	public static int GetColumnOfLastPlacement() {
		return m_columnOfLastPlacement;
	}

	/**
	 * Gets next player in the game (useful for serialization)
	 *
	 * @return String, next player in the game
	 */
	public static String GetNextPlayer() {
		return m_nextPlayer;
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
		m_humanWhiteStonesCount = a_humanWhiteStonesCount;
		m_humanBlackStonesCount = a_humanBlackStonesCount;
		m_humanClearStonesCount = a_humanClearStonesCount;
		m_computerWhiteStonesCount = a_computerWhiteStonesCount;
		m_computerBlackStonesCount = a_computerBlackStonesCount;
		m_computerClearStonesCount = a_computerClearStonesCount;

		m_humanStone = a_humanStone;
		m_computerStone = a_computerStone;
		m_humanScore = a_humanScore;
		m_computerScore = a_computerScore;
	}

	/**
	 * Increments Human Wins in tournament by given value.
	 *
	 * @param a_bumpWinsBy Value to bump Human wins by. Should be 1 under normal conditions.
	 */
	public static void IncrementHumanWinsBy(int a_bumpWinsBy) {
		m_humanWins += a_bumpWinsBy;
	}

	/**
	 * Increments Computer Wins in tournament by given value
	 *
	 * @param a_bumpWinsBy Value to bump Computer wins by. Should be 1 under normal conditions
	 */
	public static void IncrementComputerWinsBy(int a_bumpWinsBy) {
		m_computerWins += a_bumpWinsBy;
	}

	/**
	 * Resets scores for a fresh start
	 */
	public static void ResetScores() {
		m_humanScore = 0;
		m_computerScore = 0;
		m_humanWins = 0;
		m_computerWins = 0;
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
		m_rowOfLastPlacement = a_rowOfLastPlacement;
		m_columnOfLastPlacement = a_columnOfLastPlacement;

		if (player.equals("computer") || player.equals("Computer")) {
			m_nextPlayer = "computer";
		} else {
			m_nextPlayer = "human";
		}
	}
}

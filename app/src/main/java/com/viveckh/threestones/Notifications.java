package com.viveckh.threestones;

import java.util.Vector;

/**
 * Notifications Class
 * A Static class that consists of a list of inline functions representing various error messages and notifications that arise during the game play.
 * Author: Vivek Pandey
 * Project: ThreeStones in Java Android
 * Last Modified on: 04/14/2017
 */

public final class Notifications {
	//variables declaration
	private static Vector<String> m_notificationsList = new Vector<>(100);

	/**
	 * PRIVATE CONSTRUCTOR
	 */
	private Notifications() {
	}

	/**
	 * Gets notification list
	 *
	 * @return Vector with all the error and notifications messages that have been triggered during a move attempt
	 */
	public static Vector GetNotificationsList() {
		return m_notificationsList;
	}

	/**
	 * Clears notification list
	 *
	 * @return true after the clearing of notification list is complete
	 */
	public static boolean ClearNotificationsList() {
		if (m_notificationsList != null && !m_notificationsList.isEmpty()) {
			m_notificationsList.clear();
		}
		return true;
	}

	/**
	 * When user taps on occupied pouches on board
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_AlreadyOccupied() {
		m_notificationsList.add("\u2022ERROR: You can't be tapping on those occupied pouches, man!\n");
		return true;
	}

	/**
	 * Improper input error msg
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_ImproperInput() {
		m_notificationsList.add("\u2022ERROR: Why you annoying me with improper inputs? Go, Try again!\n");
		return true;
	}

	/**
	 * Input out of bounds error msg
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_InputOutOfBounds() {
		m_notificationsList.add("\u2022ERROR: Input co-ordinates out of bound. *Rolls eyes* Try again!\n");
		return true;
	}

	/**
	 * No more stones of selected color
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_NoStonesToMove() {
		m_notificationsList.add("\u2022ERROR: No more stones of selected color\n");
		return true;
	}

	/**
	 * Invalid move error msg
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_InvalidMove() {
		m_notificationsList.add("\u2022ERROR: Disabled pouches aren't available at the moment\n");
		return true;
	}

	/**
	 * When user taps in uninitialized area on board
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_TapOnGameBoard() {
		m_notificationsList.add("\u2022FOCUS bud! Tap on the board, not on white spaces! \n");
		return true;
	}


	/**
	 * Program crash msg
	 *
	 * @return true after the crash msg is added to the vector
	 */
	public static boolean Msg_CrashedWhileMakingTheMove() {
		m_notificationsList.add("\u2022ERROR: Whoopsie Daisy! The program crashed while making the move.\n");
		return true;
	}

	/**
	 * Empty msg just to insert in ternary operations
	 *
	 * @return true always
	 */
	public static boolean Msg_NoMsg() {
		return true;
	}

	public static boolean Msg_PointsGained(char a_stone, int a_points) {
		if (a_stone == 'w') {
			m_notificationsList.add("\u2022POINTS GAINED BY TEAM GDRIVE:\t" + a_points + "\n\n");
		}
		if (a_stone == 'b') {
			m_notificationsList.add("\u2022POINTS GAINED BY TEAM DROPBOX:\t" + a_points + "\n\n");
		}
		return true;
	}

	public static boolean Msg_CompletedArrangement(String a_position1, String a_position2) {
		m_notificationsList.add("\u20221 POINT: " + a_position1 + "-" + a_position2 + "\n");
		return true;
	}

	public static boolean Msg_MoveDescription(char a_stone) {
		if (a_stone == 'w') {
			m_notificationsList.add("\u2022MOVE: A GDrive stone was placed at the blinking location.\n\n");
		} else if (a_stone == 'b') {
			m_notificationsList.add("\u2022MOVE: A Dropbox stone was placed at the blinking location.\n\n");
		} else {
			m_notificationsList.add("\u2022MOVE: A Github stone was placed at the blinking location.\n\n");
		}
		return true;
	}

	/**
	 * Displays results of the tournament
	 *
	 * @param botScore   tournament score of computer
	 * @param humanScore tournament score of human
	 * @return true after the msgs are added to the vector
	 */
	public static boolean Msg_DisplayResults(int botScore, int humanScore) {
		m_notificationsList.add("***************************************************************\n");
		m_notificationsList.add("\t\tTournament Results\n");
		m_notificationsList.add("***************************************************************\n");
		m_notificationsList.add("Computer Wins:	" + botScore + "\n");
		m_notificationsList.add("Human Wins: " + humanScore + "\n");
		if (botScore > humanScore) {
			m_notificationsList.add("The Computer Won the Tournament. *reinforcing the notion once again that we bots are better than you humans*\n");
		} else if (humanScore > botScore) {
			m_notificationsList.add("Congratulations! You won the Tournament. Our programmer must've done a terrible job on algorithms for someone like you to win.\n");
		} else {
			m_notificationsList.add("It was a draw. Guess we'll see who's better in the next tournament.\n");
		}
		return true;
	}

	/**
	 * Draws a divider line
	 *
	 * @return true always
	 */
	public static boolean DrawDivider() {
		m_notificationsList.add("\n\n-*-*-*-*-********************************************-*-*-*-*-\n \n");
		return true;
	}

	/*
		THE FOLLOWING FUNCTIONS ARE ESPECIALLY MEANT TO GUIDE THE USER THROUGH COMPUTER'S THOUGHT PROCESS
	*/

	/**
	 * Bot trying to find a placement that'll win the most points
	 *
	 * @return true once the msg is added to vector
	 */
	public static boolean BotsThink_FoundAPlacementToWinPoints(int a_points, String a_stone) {
		m_notificationsList.add("\u2022Best move so far is gaining " + a_points + " net points by using " + a_stone + "stone...\n");
		return true;
	}

	/*
	public static boolean BotsThink_FoundAPlacementToWinPoints(int a_points) {
		m_notificationsList.add("Bots Mumbling:\t Best move so far is Found a move to bump up our score by " + a_points + " points...\n");
		return true;
	}
	*/

	public static boolean BotsThink_TryingToWasteOpponentStones() {
		m_notificationsList.add("Bots Mumbling:\t Trying to waste opponent's stone...\n");
		return true;
	}

	public static boolean BotsThink_TryingToUseMagicStones() {
		m_notificationsList.add("Bots Mumbling:\t Trying to use the magic stones...\n");
		return true;
	}





	/**
	 * Msg notifying that the safety of key pieces/squares are being taken care of
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_CheckingKingNKeySquareSafety() {
		m_notificationsList.add("Bots Mumbling:\t Monitoring territory to ensure the King & KeySquare are safe...\n");
		return true;
	}

	/**
	 * Key Threat detected msg
	 * @param whosUnderThreat The piece under threat - King or KeySquare
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_KeyThreatDetected(String whosUnderThreat) {
		m_notificationsList.add("Bots Mumbling:\t Imminent threat has been detected for the " + whosUnderThreat + "\n");
		return true;
	}

	/**
	 * hostile opponent captured msg
	 * @param whosUnderThreat The piece under threat - King or KeySquare
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_HostileOpponentCaptured(String whosUnderThreat) {
		m_notificationsList.add("Bots Mumbling:\t That hostile opponent aiming to attack our " + whosUnderThreat + " has been captured.\n");
		return true;
	}

	/**
	 * hostile opponent not capturable msg
	 * @param whosUnderThreat The piece under threat - King or KeySquare
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_HostileOpponentUncapturable(String whosUnderThreat) {
		m_notificationsList.add("Bots Mumbling:\t That hostile opponent aiming to attack " + whosUnderThreat + " couldn't be captured. Trying alternatives...\n");
		return true;
	}

	/**
	 * Blocking move successful msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_BlockingMoveMade() {
		m_notificationsList.add("Bots Mumbling:\t A Blocking move was successfully made to obstruct the hostile opponent.\n");
		return true;
	}

	/**
	 * Blocking move not successful msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_BlockingMoveNotPossible() {
		m_notificationsList.add("Bots Mumbling:\t A Blocking move wasn't possible at this time. Trying other options...\n");
		return true;
	}

	/**
	 * King relocation successful msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_KingMoved() {
		m_notificationsList.add("Bots Mumbling:\t The king has been moved and the threat has been averted for now.\n");
		return true;
	}

	/**
	 * King move unsafe msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_UnsafeToMoveKing() {
		m_notificationsList.add("Bots Mumbling:\t No safe surroundings to move the king. The humans have trapped our King.\n");
		return true;
	}

	/**
	 * Trying to capture opponent msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_TryingToCaptureOpponentDice() {
		m_notificationsList.add("Bots Mumbling:\t Looking for any vulnerable opponent dice to capture at this point...\n");
		return true;
	}

	/**
	 * Captured opponent msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_CapturedOpponentDice() {
		m_notificationsList.add("Bots Mumbling:\t We captured an opponent die.\n");
		return true;
	}

	/**
	 * Checking to see if any of own dice are threatened by opponent dices
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_ProtectDicesFromPotentialCaptures() {
		m_notificationsList.add("Bots Mumbling:\t Checking if any of the own dices are under threat of being captured by opponent...\n");
		return true;
	}

	/**
	 * searching an ordinary move msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean BotsThink_SearchingOrdinaryMove() {
		m_notificationsList.add("Bots Mumbling:\t Examining possible moves to get closer to the opponent king/keysquare...\n");
		return true;
	}

	/**
	 * Help mode on msg
	 * @return true after the msg is added to the vector
	 */
	public static boolean Msg_HelpModeOn() {
		m_notificationsList.add("HELP MODE ACTIVATED!\n");
		return true;
	}

	/**
	 * Prints the move recommended by the computer
	 * @param startRow The row of the dice to move
	 * @param startCol The column of the dice to move
	 * @param endRow The destination row in the board where dice should be moved
	 * @param endCol The destination column in the board where dice should be moved
	 * @param pathChoice 1 if Vertical then Lateral, 2 if Lateral then Vertical, 3 if vertical only and 4 if lateral only
	 * @return true after the msg is added to the vector
	 */
	public static boolean Msg_HelpModeRecommendedMove(int startRow, int startCol, int endRow, int endCol, int pathChoice) {
		m_notificationsList.add("\nRECOMMENDED:\t Move the dice in square (" + startRow + ", " + startCol + ") to (" + endRow + ", " + endCol + ") using a ");
		switch (pathChoice) {
			case 1:
				m_notificationsList.add("Vertical then Lateral Path\n");
				break;
			case 2:
				m_notificationsList.add("Lateral then Vertical Path\n");
				break;
			case 3:
				m_notificationsList.add("Vertical Path\n");
				break;
			case 4:
				m_notificationsList.add("Lateral Path\n");
				break;
			default:
				break;
		}
		m_notificationsList.add("\nHELP MODE DEACTIVATED!\n");
		return true;
	}
}
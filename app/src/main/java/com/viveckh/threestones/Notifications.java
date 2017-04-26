package com.viveckh.threestones;

import java.util.Vector;

/**
 * <h1>Notifications Model Class</h1>
 * This Singleton class logs the various error messages and notifications that get generated at
 * various points during the game play. Everything is stored in a centralized vector, and
 * can be cleared periodically to start fresh.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
 */
public final class Notifications {

	//Variable Declaration - Vector where all the errors/messages are stored
	private static Vector<String> m_notificationsList = new Vector<>(100);

	/**
	 * Notifications Constructor - Private, self initialized
	 */
	private Notifications() {
	}

	/**
	 * Gets notification list
	 *
	 * @return Vector with all the error and notifications messages that have been triggered
	 */
	public static Vector GetNotificationsList() {
		return m_notificationsList;
	}

	/**
	 * Clears notification list
	 *
	 * @return true after the clearing of notification storage vector is complete
	 */
	public static boolean ClearNotificationsList() {
		if (m_notificationsList != null && !m_notificationsList.isEmpty()) {
			m_notificationsList.clear();
		}
		return true;
	}

	/**
	 * Logs error when user taps on occupied pouches on board
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_AlreadyOccupied() {
		m_notificationsList.add("•ERROR: You can't be tapping on those occupied pouches!\n");
		return true;
	}

	/**
	 * Logs error when the user selected input is out of bounds
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_InputOutOfBounds() {
		m_notificationsList.add("•ERROR: Input co-ordinates out of bound. Try again!\n");
		return true;
	}

	/**
	 * Logs error when there are no more stones of selected color
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_NoStonesToMove() {
		m_notificationsList.add("•ERROR: No more stones of selected color\n");
		return true;
	}

	/**
	 * Logs error in case an invalid move is attempted
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_InvalidMove() {
		m_notificationsList.add("•ERROR: Disabled pouches aren't available at the moment\n");
		return true;
	}

	/**
	 * Logs error when user taps in uninitialized area on board
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_TapOnGameBoard() {
		m_notificationsList.add("•FOCUS bud! Tap on the board, not on white spaces!\n");
		return true;
	}

	/**
	 * Empty filler function just to insert in ternary operations
	 *
	 * @return true always
	 */
	public static boolean Msg_NoMsg() {
		return true;
	}

	/**
	 * Logs when point(s) gained by one of the teams
	 *
	 * @param a_stone  Character, Stone representing a team
	 * @param a_points Integer, Points gained
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_PointsGained(char a_stone, int a_points) {
		if (a_stone == 'w') {
			m_notificationsList.add("•POINTS GAINED BY TEAM DUCK:\t" + a_points + "\n\n");
		}
		if (a_stone == 'b') {
			m_notificationsList.add("•POINTS GAINED BY TEAM PENGUIN:\t" + a_points + "\n\n");
		}
		return true;
	}

	/**
	 * Logs when a three-stone arrangement is completed
	 *
	 * @param a_position1 String, first position where match was found
	 * @param a_position2 String, second position where match was found
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_CompletedArrangement(String a_position1, String a_position2) {
		m_notificationsList.add("•1 POINT: " + a_position1 + "-" + a_position2 + "\n");
		return true;
	}

	/**
	 * Logs everytime a move is made and needs to be recorded
	 *
	 * @param a_stone Character, a stone that was placed
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_MoveDescription(char a_stone) {
		if (a_stone == 'w') {
			m_notificationsList.add("•MOVE: A Duck " +
				  "was placed at the blinking location.\n\n");
		} else if (a_stone == 'b') {
			m_notificationsList.add("•MOVE: A Penguin " +
				  "was placed at the blinking location.\n\n");
		} else {
			m_notificationsList.add("•MOVE: Miller " +
				  "was placed at the blinking location.\n\n");
		}
		return true;
	}

	/**
	 * Logs a Help move that was recommended by tehe computer
	 *
	 * @param a_stone        Character, stone recommended by the computer algorithm
	 * @param a_pointsToEarn Integer, possible points from this recommended move
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_HelpModeRecommendedMove(char a_stone, int a_pointsToEarn) {
		m_notificationsList.add("•HELP MODE ACTIVATED\n");
		if (a_stone == 'w') {
			m_notificationsList.add("•Place a Duck at rotating location.\n");
		} else if (a_stone == 'b') {
			m_notificationsList.add("•Place a Penguin at rotating location.\n");
		} else {
			m_notificationsList.add("•Place Miller at rotating location.\n");
		}

		if (a_pointsToEarn > 0) {
			m_notificationsList.add("•You will earn " + a_pointsToEarn + " net points.");
		}
		return true;
	}
}
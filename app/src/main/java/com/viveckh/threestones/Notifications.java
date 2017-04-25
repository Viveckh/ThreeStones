package com.viveckh.threestones;

import java.util.Vector;

/**
 * Notifications Class
 * Static class that consists of a list of inline functions representing various error messages and
 * notifications that arise during the game play.
 * Author: Vivek Pandey
 * Project: ThreeStones in Java Android
 * Last Modified on: 04/14/2017
 */

public final class Notifications {
	//Variable Declarations
	private static Vector<String> m_notificationsList = new Vector<>(100);

	/**
	 * PRIVATE CONSTRUCTOR
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
		m_notificationsList.add("•ERROR: You can't be tapping on those occupied pouches!\n");
		return true;
	}

	/**
	 * Input out of bounds error msg
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_InputOutOfBounds() {
		m_notificationsList.add("•ERROR: Input co-ordinates out of bound. Try again!\n");
		return true;
	}

	/**
	 * No more stones of selected color
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_NoStonesToMove() {
		m_notificationsList.add("•ERROR: No more stones of selected color\n");
		return true;
	}

	/**
	 * Invalid move error msg
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_InvalidMove() {
		m_notificationsList.add("•ERROR: Disabled pouches aren't available at the moment\n");
		return true;
	}

	/**
	 * When user taps in uninitialized area on board
	 *
	 * @return true when the msg is added to the vector
	 */
	public static boolean Msg_TapOnGameBoard() {
		m_notificationsList.add("•FOCUS bud! Tap on the board, not on white spaces!\n");
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
			m_notificationsList.add("•POINTS GAINED BY TEAM GDRIVE:\t" + a_points + "\n\n");
		}
		if (a_stone == 'b') {
			m_notificationsList.add("•POINTS GAINED BY TEAM DROPBOX:\t" + a_points + "\n\n");
		}
		return true;
	}

	public static boolean Msg_CompletedArrangement(String a_position1, String a_position2) {
		m_notificationsList.add("•1 POINT: " + a_position1 + "-" + a_position2 + "\n");
		return true;
	}

	public static boolean Msg_MoveDescription(char a_stone) {
		if (a_stone == 'w') {
			m_notificationsList.add("•MOVE: A GDrive stone " +
				  "was placed at the blinking location.\n\n");
		} else if (a_stone == 'b') {
			m_notificationsList.add("•MOVE: A Dropbox stone " +
				  "was placed at the blinking location.\n\n");
		} else {
			m_notificationsList.add("•MOVE: A Github stone " +
				  "was placed at the blinking location.\n\n");
		}
		return true;
	}

	public static boolean Msg_HelpModeRecommendedMove(char a_stone, int a_pointsToEarn) {
		m_notificationsList.add("•HELP MODE ACTIVATED\n");
		if (a_stone == 'w') {
			m_notificationsList.add("•Place a GDrive stone at rotating location.\n");
		} else if (a_stone == 'b') {
			m_notificationsList.add("•Place a Dropbox stone at rotating location.\n");
		} else {
			m_notificationsList.add("•Place a Github stone at rotating location.\n");
		}

		if (a_pointsToEarn > 0) {
			m_notificationsList.add("•You will earn " + a_pointsToEarn + " net points.");
		}
		return true;
	}
}
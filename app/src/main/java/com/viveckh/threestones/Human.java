package com.viveckh.threestones;

/**
 * This class inherits from Player class and expands on the features needed to handle human moves
 */
public class Human extends Player {
	//Constructor
	public Human(char a_primaryColor) {
		super(a_primaryColor);
	}

	public boolean Play(char a_stone, int a_row, int a_column, Board a_board) {
		printNotifications = true;

		//If index out of bounds, move cannot be made
		if (IndexOutOfBounds(a_row, a_column, a_board.GetBoardDimension())) {
			printStatus = printNotifications ?
				  Notifications.Msg_InputOutOfBounds() : Notifications.Msg_NoMsg();
			return false;
		}

		//Attempt the move and update the score if successful
		if (PlaceAStone(a_stone, a_row, a_column, a_board)) {
			UpdateScoreAfterMove(m_primaryColor,
				  m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board);
			return true;
		}
		return false;
	}

	public boolean IndexOutOfBounds(int a_row, int a_column, int a_dimension) {
		//If row is out of dimensional bounds, return true
		if (a_row < 0 || a_row >= a_dimension) {
			return true;
		}
		//If column is out of dimensional bounds, return true
		if (a_column < 0 || a_column >= a_dimension) {
			return true;
		}
		return false;
	}
}

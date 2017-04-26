package com.viveckh.threestones;

/**
 * <h1>Human Model Class</h1>
 * It inherits from the 'Player' class and serves the primary purpose of holding properties/attr
 * associated with the human player, validating & initiating the move on behalf of the human player.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
 */
public class Human extends Player {
	/**
	 * Human Constructor
	 * Constructs a Human object by calling the constructor of its Parent 'Player' class
	 *
	 * @param a_primaryColor Character representing player's primary stone, w or b.
	 */
	public Human(char a_primaryColor) {
		super(a_primaryColor);
	}

	/**
	 * Play(),
	 * Validates the move chosen by the human player and processes it if valid
	 *
	 * @param a_stone  Character, stone that is chosen to be placed
	 * @param a_row    Integer, row where the stone is chosen to be placed
	 * @param a_column Integer, column where the stone is chosen to be placed
	 * @param a_board  Board, Game board in context where the game is being played
	 * @return true if the move is successful, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
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

	/**
	 * IndexOutOfBounds(),
	 * Checks if the passed coordinates fall within the dimensions of the Board
	 *
	 * @param a_row       Integer, row where the stone is being attempted to be placed
	 * @param a_column    Integer, column where the stone is being attempted to be placed
	 * @param a_dimension Integer, dimension of the game board
	 * @return true if attempted coordinate falls out of the territory, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
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

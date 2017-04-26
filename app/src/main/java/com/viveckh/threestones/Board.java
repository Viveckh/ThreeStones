package com.viveckh.threestones;

import java.io.Serializable;

/**
 * <h1>Board Model Class</h1>
 * It initializes a game board and provides helper functions to access/modify the board.
 * The board itself an 11x11 multidimensional array of Block objects but only those Blocks that
 * fall within the Octagonal game board are initialized.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
 */
public class Board implements Serializable {

	//Variable Declarations
	private static final int m_DIMENSION = 11;
	Block[][] m_gameBoard;

	/**
	 * Board Default Constructor,
	 * Initializes the Board as a multidimensional array of Block objects. But only those Blocks
	 * within the Board are initialized and are usable that fall within an octagonal structure.
	 *
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public Board() {
		/*Since certain indexes in the multidimensional board need to be nullified
		to obtain an octagonal board*/
		int startingColumn = 4;
		int rowLength = 3;

		//Initializing the indexes of game board that fall in the octagonal region
		m_gameBoard = new Block[m_DIMENSION][m_DIMENSION];
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = startingColumn; column < (startingColumn + rowLength); column++) {
				if (row == m_DIMENSION / 2 && column == m_DIMENSION / 2) {
					// Leave the center blank
				} else {
					m_gameBoard[row][column] = new Block(row, column);
				}
			}

			// Updating variables to build the top half of the board
			if (row < (m_DIMENSION / 2 - 1)) {
				startingColumn--;
				rowLength += 2;
			}

			// Updating variables to build the bottom half of the board
			if (row > m_DIMENSION / 2) {
				startingColumn++;
				rowLength -= 2;
			}
		}
	}

	/**
	 * Board Copy Constructor,
	 * Constructs a Board object as a copy of another Board that has been passed as parameter
	 *
	 * @param a_board Board that needs to be replicated
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public Board(Board a_board) {
		//Initializing the game board
		m_gameBoard = new Block[m_DIMENSION][m_DIMENSION];

		//Copy all the indexes from the board passed as parameter
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = 0; column < m_DIMENSION; column++) {
				/*If an index in the passed game board isn't null,
				copy construct a block object on the corresponding index in this board*/
				if (a_board.m_gameBoard[row][column] != null) {
					m_gameBoard[row][column] = new Block(a_board.m_gameBoard[row][column]);
				}
			}
		}
	}

	/**
	 * DrawBoard() - Prints the Board object to the Console (For developer purposes)
	 *
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public void DrawBoard() {
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = 0; column < m_DIMENSION; column++) {
				//If an index in the passed board isn't null, and is occupied - Print it
				if (m_gameBoard[row][column] != null) {
					if (m_gameBoard[row][column].IsOccupied()) {
						System.out.println("(" + m_gameBoard[row][column].GetX() + ", "
							  + m_gameBoard[row][column].GetY() + ")");
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * Gets the Board Dimension
	 *
	 * @return Integer representing the Board Dimension
	 */
	public final int GetBoardDimension() {
		return m_DIMENSION;
	}

	/**
	 * SetStoneAtLocation() - Sets the occupying stone of a specific Block in the Board object
	 *
	 * @param a_row    Integer row within the Board where stone needs to be placed
	 * @param a_column Integer column within the Board where stone needs to be placed
	 * @param a_stone  A character representing the stone to set. n, w, b, c are valid inputs
	 * @return true if the stone is successfully set, false if a failure
	 */
	public boolean SetStoneAtLocation(int a_row, int a_column, char a_stone) {
		//If trying to set uninitialized no-go indexes, return false. Else, set the stone
		if (m_gameBoard[a_row][a_column] == null) {
			return false;
		}
		return m_gameBoard[a_row][a_column].SetStone(a_stone);
	}

	/**
	 * IsLocationOccupied() - Returns the occupancy state of the Block within the Board
	 *
	 * @param a_row    Integer row within the Board where occupancy needs to be checked
	 * @param a_column Integer column within Board where occupancy needs to be checked
	 * @return true if location is occupied, false if empty
	 */
	public boolean IsLocationOccupied(int a_row, int a_column) {
		//If trying to access uninitialized no-go indexes, return that true as well.
		if (m_gameBoard[a_row][a_column] == null) {
			return true;
		}
		return m_gameBoard[a_row][a_column].IsOccupied();
	}

	/**
	 * Gets block at a given location in board, returns null if accessing invalid coordinates
	 *
	 * @param a_row    Integer row within the Board whose Block needs to be accessed
	 * @param a_column Integer column within the Board whose Block needs to be accessed
	 * @return Block if one found, null otherwise
	 */
	public Block GetBlockAtLocation(int a_row, int a_column) {
		if (m_gameBoard[a_row][a_column] == null) {
			return null;
		} else {
			return m_gameBoard[a_row][a_column];
		}
	}

	/**
	 * Gets the stone of a particular location on the board
	 *
	 * @param a_row    Integer row within the Board whose stone needs to be returned
	 * @param a_column Integer column within the Board whose stone needs to be returned
	 * @return 'n' if none, 'w' if white, 'b' if black, 'c' if clear, 'e' if inaccessible location
	 */
	public char GetStoneAtLocation(int a_row, int a_column) {
		//If trying to attempt uninitialized no-go indexes, return 'e', else return the stone
		if (m_gameBoard[a_row][a_column] == null) {
			return 'e';
		}
		return m_gameBoard[a_row][a_column].GetStone();
	}
}

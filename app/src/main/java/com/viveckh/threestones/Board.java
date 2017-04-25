package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/3/2017
*/

/* This class serves as the Game Board. It creates a 11*11 multidimensional array of pouches
* and initializes only those pouches that fall in the Octagonal game board.
* Once initialized, it sets all the necessary relationship between the pouches
* so that the link can help in the tree formation later.
*/

import java.io.Serializable;

public class Board implements Serializable {
	//Variable Declarations
	private static final int m_DIMENSION = 11;
	Block[][] m_gameBoard;

	//Default Constructor
	public Board() {
		/*Since certain indexes in the multidimensional board need to be nullified
		to obtain an octagonal board*/
		int startingColumn = 4;
		int rowLength = 3;

		//Initializing the indexes of game board that fall in the octagonal region
		m_gameBoard = new Block[m_DIMENSION][m_DIMENSION];
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = startingColumn; column < (startingColumn+rowLength); column++) {
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

	//Copy Constructor
	public Board(Board a_board) {
		//Initializing the game board
		m_gameBoard = new Block[m_DIMENSION][m_DIMENSION];

		//Copy all the indexes from the board passed as parameter
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = 0; column < m_DIMENSION; column++) {
				/*If an index in the passed game board isn't null,
				copy construct a block object on the corresponding index in this board*/
				if (a_board.m_gameBoard[row][column] != null) {
					m_gameBoard[row][column] =new Block(a_board.m_gameBoard[row][column]);
				}
			}
		}
	}

	//Print board to console for purposes of debugging
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

	public final int GetBoardDimension(){
		return m_DIMENSION;
	}

	//Setts a stone somewhere on the board
	public boolean SetStoneAtLocation(int a_row, int a_column, char a_stone) {
		//If trying to set uninitialized no-go indexes, return false. Else, set the stone
		if (m_gameBoard[a_row][a_column] == null) {
			return false;
		}
		return m_gameBoard[a_row][a_column].SetStone(a_stone);
	}

	//Returns the occupancy state of a location on the board
	public boolean IsLocationOccupied(int a_row, int a_column) {
		//If trying to access uninitialized no-go indexes, return that true as well.
		if (m_gameBoard[a_row][a_column] == null) {
			return true;
		}
		return m_gameBoard[a_row][a_column].IsOccupied();
	}

	//Gets block at a given location in board, returns null if accessing invalid coordinates
	public Block GetBlockAtLocation(int a_row, int a_column) {
		if (m_gameBoard[a_row][a_column] == null) {
			return null;
		}
		else {
			return m_gameBoard[a_row][a_column];
		}
	}

	//Gets the stone of a particular location on the board
	public char GetStoneAtLocation(int a_row, int a_column) {
		//If trying to attempt uninitialized no-go indexes, return 'e', else return the stone
		if (m_gameBoard[a_row][a_column] == null) {
			return 'e';
		}
		return m_gameBoard[a_row][a_column].GetStone();
	}
}

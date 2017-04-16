package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/3/2017
*/

/* This class serves as the Game Board. It creates a 11*11 multim_DIMENSIONal array of pouches and initializes
* only those pouches that fall in the Octagonal game board. Once initialized, it sets all the necessary relationship between
* the pouches so that the link can help in the tree formation later.
* The score function calculates and returns the change in score after each stone insertion in the board.
*/

public class Board {
	//Variables
	private static final int m_DIMENSION = 11;
	Block[][] m_gameBoard; //Package Public

	//Default Constructor
	public Board() {
		//Since certain indexes in the multidimensional board need to be nullified to obtain an octagonal board
		int startingColumn = 4;
		int rowLength = 3;

		//Initialize the game board
		m_gameBoard = new Block[m_DIMENSION][m_DIMENSION];

		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = startingColumn; column < (startingColumn + rowLength); column++) {
				if (row == m_DIMENSION / 2 && column == m_DIMENSION / 2) {
					// Leave the center blank
				} else {
					m_gameBoard[row][column] = new Block(row, column);
					//System.out.print(m_gameBoard[row][column].GetY() + " ");
				}
			}
			//System.out.println();
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
		//Initialize the game board
		m_gameBoard = new Block[m_DIMENSION][m_DIMENSION];

		//Copy all the indexes from the board passed as parameter
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = 0; column < m_DIMENSION; column++) {
				//If an index in the passed gameboard isn't null
				if (a_board.m_gameBoard[row][column] != null) {
					//Copy construct a block object on the corresponding index in this board
					m_gameBoard[row][column] = new Block(a_board.m_gameBoard[row][column]);
					//System.out.print(m_gameBoard[row][column].GetY() + " ");
				}
			}
			//System.out.println();
		}
	}

	//Draw board
	public void DrawBoard() {
		//Copy all the indexes from the board passed as parameter
		//System.out.println("List of Occupied indexes");
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = 0; column < m_DIMENSION; column++) {
				//If an index in the passed gameboard isn't null
				if (m_gameBoard[row][column] != null) {
					//Mention it is occupied
					if (m_gameBoard[row][column].IsOccupied()) {
						//System.out.println("(" + m_gameBoard[row][column].GetX() + ", " + m_gameBoard[row][column].GetY() + ")");
					}
				}
			}
			//System.out.println();
		}
	}

	public final int GetBoardDimension(){
		return m_DIMENSION;
	}

	//Setting a stone somewhere on the gameboard
	public boolean SetStoneAtLocation(int a_row, int a_column, char a_stone) {
		//If trying to attempt uninitialized no-go indexes
		if (m_gameBoard[a_row][a_column] == null) {
			return false;
		}
		return m_gameBoard[a_row][a_column].SetStone(a_stone);
	}

	//Returns the occupancy state of a location on the board
	public boolean IsLocationOccupied(int a_row, int a_column) {
		//If trying to attempt uninitialized no-go indexes
		if (m_gameBoard[a_row][a_column] == null) {
			return true;
		}
		return m_gameBoard[a_row][a_column].IsOccupied();
	}

	//Gets block at a given location in board, returns null if trying to attempt invalid coordinates
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
		//If trying to attempt uninitialized no-go indexes
		if (m_gameBoard[a_row][a_column] == null) {
			return 'e';
		}
		return m_gameBoard[a_row][a_column].GetStone();
	}
}

package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/4/2017
*/

/* This class implements basic set of strategies for any player, validating and processing  moves, 
*  maintains m_scores and available number of white, black and clear stones.
*  General set of rules for any player are implemented here so their individual class can inherit from this class.
*/

public class Player {
	public static void main(String [] args) {
		Board board = new Board();
		board.m_gameBoard[4][1] =  null;
		Board board2 = new Board(board);

		Player myman = new Player('b');

		myman.PlaceAStone('b', 2, 4, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");

		myman.PlaceAStone('b', 3, 4, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");
		myman.PlaceAStone('b', 3, 5, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");
		myman.PlaceAStone('b', 2, 5, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");
		myman.PlaceAStone('b', 1, 5, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");
		myman.PlaceAStone('b', 1, 6, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");
		myman.PlaceAStone('b', 1, 4, board);
		System.out.println("Score: " + myman.GetScore() + ", Black: " + myman.GetBlackStonesAvailable() + "\n");

		board.DrawBoard();
	}


	//Variables
	private int m_score;
	private int m_whiteStonesAvailable;
	private int m_blackStonesAvailable;
	private int m_clearStonesAvailable;
	protected char m_primaryColor;
	protected static int m_rowOfPreviousPlacement = -1;
	protected static int m_columnOfPreviousPlacement = -1;

	//Default Constructor
	private Player() {
		m_score = 0;
		m_whiteStonesAvailable = 15;
		m_blackStonesAvailable = 15;
		m_clearStonesAvailable = 6;
	}

	public Player(char a_primaryColor) {
		this();
		m_primaryColor = a_primaryColor;
	}

	// Number of available stone Getters
	public int GetScore() { return m_score; }
	public int GetWhiteStonesAvailable() { return m_whiteStonesAvailable; }
	public int GetBlackStonesAvailable() { return m_blackStonesAvailable; }
	public int GetClearStonesAvailable() { return m_clearStonesAvailable; }


	/*
	 *	MOVE VALIDATION AND PLACEMENT RELATED FUNCTIONS
	 */

	//Validates and places a stone on the given location in game board
	protected boolean PlaceAStone(char a_stone, int a_row, int a_column, Board a_board) {
		//NOTIFICATION: A LOT OF NOTIFICATIONS NEED TO BE SET HERE NEXT TO EVERY IF STATEMENT
		//First, make sure the coordinate doesn't fall within the blocked territories of the multidimensional board
		if (a_board.m_gameBoard[a_row][a_column] != null){
			//Next, make sure the coordinate has not been occupied by another stone
			if (!a_board.IsLocationOccupied(a_row, a_column)) {
				//Not all vacant spots are available to place a stone. There are rules.
				//Make sure the attempted placement complies with them rules.
				if (HasPermissionToOccupyVacantSpot(a_row, a_column, a_board)) {
					//Finally, make sure the player has the chosen stone available, and make the move.
					if (IsStoneAvailable(a_stone)) {
						//Make the placement and update the stone count
						if (a_board.SetStoneAtLocation(a_row, a_column, a_stone)) {
							UseAStone(a_stone);
							m_rowOfPreviousPlacement = a_row;
							m_columnOfPreviousPlacement = a_column;
							IncrementScore(CalculatePointsGained(m_primaryColor, a_board));
							System.out.println("Inserting (" + a_row + ", " + a_column + ")");
						}
					}
				}
			}
		}
		return false;
	}

	//Verifies if the player has the right to place a stone in that row/column despite its vacancy at the moment
	private boolean HasPermissionToOccupyVacantSpot(int a_row, int a_column, Board a_board) {
		//Permission granted if not a single placement has been made so far
		if (m_rowOfPreviousPlacement < 0 && m_columnOfPreviousPlacement < 0) {
			return true;
		}

		//Permission granted if the attempted placement location matches a row or column of previous placement in the game
		if ((a_row == m_rowOfPreviousPlacement) || (a_column == m_columnOfPreviousPlacement)) {
			return true;
		}

		//Permission denied if any vacancy found within the row or column of last placement
		for (int index = 0; index < a_board.m_gameBoard[0].length; index++) {
			if (!a_board.IsLocationOccupied(index, m_columnOfPreviousPlacement)) {
				return false;
			}
			if (!a_board.IsLocationOccupied(m_rowOfPreviousPlacement, index)) {
				return false;
			}
		}

		//Permission granted if it gets to this point without finding a vacant spot from above loop
		return true;

	}

	// Check if a stone is available for use
	protected boolean IsStoneAvailable(char a_stone) {
		switch (a_stone)
		{
			case 'w':
				if (m_whiteStonesAvailable > 0) {
					return true;
				}
				else {
					return false;
				}
			case 'b':
				if (m_blackStonesAvailable > 0) {
					return true;
				}
				else {
					return false;
				}
			case 'c':
				if (m_clearStonesAvailable > 0) {
					return true;
				}
				else {
					return false;
				}
			default:
				//NOTIFICATIONS: Not a valid stone
				return false;
		}
	}

	// Update the stone count upon use of one
	protected void UseAStone(char a_stone) {
		switch (a_stone)
		{
			case 'w':
				m_whiteStonesAvailable--;
			case 'b':
				m_blackStonesAvailable--;
			case 'c':
				m_clearStonesAvailable--;
			default:
				//NOTIFICATIONS: Not a valid stone
		}
	}

	/*
	* SCORING RELATED FUNCTIONS
	* */

	// Increments score by the value that is passed as parameter
	protected boolean IncrementScore(int a_value) {
		if (a_value > 0) {
			m_score += a_value;
			return true;
		}
		return false;
	}

	//Calculates the change in points for a player after a move. Override by derived classes based on stone color picked at the start of the game
	//Row and Column of previous placement must be updated for this function to perform correctly
	public int CalculatePointsGained(char a_stone, Board a_board) {
		int points = 0;
		
		if (IsLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement - 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement + 1) == a_stone)
				points++;
		}
		if (IsTopFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsBottomFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement) == a_stone)
				points++;
		}
		if (IsTopLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsBottomRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement  - 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement  + 1) == a_stone)
				points++;
		}
		if (IsTopRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsBottomLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement  + 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement  - 1) == a_stone)
				points++;
		}


		if (IsLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement - 1, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement - 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement  - 2) == a_stone)
				points++;
		}
		if (IsRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement + 1, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement + 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement  + 2) == a_stone)
				points++;
		}
		if (IsTopFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsTopFavorable(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 2, m_columnOfPreviousPlacement) == a_stone)
				points++;
		}
		if (IsBottomFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsBottomFavorable(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 2, m_columnOfPreviousPlacement) == a_stone)
				points++;
		}
		if (IsTopLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsTopLeftFavorable(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement - 1, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement  - 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 2, m_columnOfPreviousPlacement  - 2) == a_stone)
				points++;
		}
		if (IsTopRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsTopRightFavorable(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement + 1, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 1, m_columnOfPreviousPlacement  + 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement - 2, m_columnOfPreviousPlacement  + 2) == a_stone)
				points++;
		}
		if (IsBottomLeftFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsBottomLeftFavorable(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement - 1, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement  - 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 2, m_columnOfPreviousPlacement  - 2) == a_stone)
				points++;
		}
		if (IsBottomRightFavorable(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board) && IsBottomRightFavorable(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement + 1, a_board)) {
			if (a_board.GetStoneAtLocation(m_rowOfPreviousPlacement, m_columnOfPreviousPlacement) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 1, m_columnOfPreviousPlacement  + 1) == a_stone || a_board.GetStoneAtLocation(m_rowOfPreviousPlacement + 2, m_columnOfPreviousPlacement  + 2) == a_stone)
				points++;
		}
		return points;
	}




	//Checks whether the left is favorable. Does the calculation based on two situations.
	// 1) What if my Stone is clear?
	// 2) What if my stone is colored (black/white)?
	boolean IsLeftFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block left = (a_currentCol > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol - 1)) : null;
		Block farLeft = (a_currentCol > 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol - 2)) : null;
		Block right = (a_currentCol < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol + 1)) : null;
		
		if (left != null) {
			if (left.GetStone()!= 'n') {
				if (current.GetStone() == 'c') {
					if (right != null && right.GetStone() != 'n') {
						if ((left.GetStone() == right.GetStone()) && (left.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != right.GetStone()) && (left.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
					if (farLeft != null && farLeft.GetStone() != 'n') {
						if ((left.GetStone() == farLeft.GetStone()) && (left.GetStone() != 'c' && farLeft.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != farLeft.GetStone()) && (left.GetStone() == 'c' || farLeft.GetStone() == 'c')) {
							return true;
						}
					}
				}
				else {
					if ((left.GetStone() == current.GetStone()) || left.GetStone() == 'c') return true;
				}
			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsRightFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block left = (a_currentCol > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol - 1)) : null;
		Block right = (a_currentCol < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol + 1)) : null;
		Block farRight = (a_currentCol < a_board.GetBoardDimension() - 2) ? new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol + 2)) : null;
		
		if (right != null) {
			if (right.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (left != null && left.GetStone() != 'n') {
						if ((left.GetStone() == right.GetStone()) && (left.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != right.GetStone()) && (left.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
					if (farRight != null && farRight.GetStone() != 'n') {
						if ((farRight.GetStone() == right.GetStone()) && (farRight.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((farRight.GetStone() != right.GetStone()) && (farRight.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((right.GetStone() == current.GetStone()) || right.GetStone() == 'c') return true;
				}

			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsTopFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block top = (a_currentRow > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol)) : null;
		Block farTop = (a_currentRow > 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 2, a_currentCol)) : null;
		Block bottom = (a_currentRow < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol)) : null;

		if (top != null) {
			if (top.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (bottom != null && bottom.GetStone() != 'n') {
						if ((top.GetStone() == bottom.GetStone()) && (top.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != bottom.GetStone()) && (top.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
					if (farTop != null && farTop.GetStone() != 'n') {
						if ((top.GetStone() == farTop.GetStone()) && (top.GetStone() != 'c' && farTop.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != farTop.GetStone()) && (top.GetStone() == 'c' || farTop.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((top.GetStone() == current.GetStone()) || top.GetStone() == 'c') return true;
				}
			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsBottomFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block top = (a_currentRow > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol)) : null;
		Block bottom = (a_currentRow < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol)) : null;
		Block farBottom = (a_currentRow < a_board.GetBoardDimension() - 2) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 2, a_currentCol)) : null;
		
		if (bottom != null) {
			if (bottom.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (top != null && top.GetStone() != 'n') {
						if ((top.GetStone() == bottom.GetStone()) && (top.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != bottom.GetStone()) && (top.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
					if (farBottom != null && farBottom.GetStone() != 'n') {
						if ((farBottom.GetStone() == bottom.GetStone()) && (farBottom.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((farBottom.GetStone() != bottom.GetStone()) && (farBottom.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((bottom.GetStone() == current.GetStone()) || bottom.GetStone() == 'c') return true;
				}
			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsTopLeftFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topLeft = (a_currentRow > 0 && a_currentCol > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol - 1)) : null;
		Block farTopLeft = (a_currentRow > 1 && a_currentCol > 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 2, a_currentCol - 2)) : null;
		Block bottomRight = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol + 1)) : null;
		
		if (topLeft != null) {
			if (topLeft.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (bottomRight != null && bottomRight.GetStone() != 'n') {
						if ((topLeft.GetStone() == bottomRight.GetStone()) && (topLeft.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != bottomRight.GetStone()) && (topLeft.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
					if (farTopLeft != null && farTopLeft.GetStone() != 'n') {
						if ((topLeft.GetStone() == farTopLeft.GetStone()) && (topLeft.GetStone() != 'c' && farTopLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != farTopLeft.GetStone()) && (topLeft.GetStone() == 'c' || farTopLeft.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((topLeft.GetStone() == current.GetStone()) || topLeft.GetStone() == 'c') return true;
				}
			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsTopRightFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topRight = (a_currentRow > 0 && a_currentCol < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol + 1)) : null;
		Block farTopRight = (a_currentRow > 1 && a_currentCol < a_board.GetBoardDimension() - 2) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 2, a_currentCol + 2)) : null;
		Block bottomLeft = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol - 1)) : null;

		if (topRight != null) {
			if (topRight.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (bottomLeft != null && bottomLeft.GetStone() != 'n') {
						if ((topRight.GetStone() == bottomLeft.GetStone()) && (topRight.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != bottomLeft.GetStone()) && (topRight.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
					if (farTopRight != null && farTopRight.GetStone() != 'n') {
						if ((topRight.GetStone() == farTopRight.GetStone()) && (topRight.GetStone() != 'c' && farTopRight.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != farTopRight.GetStone()) && (topRight.GetStone() == 'c' || farTopRight.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((topRight.GetStone() == current.GetStone()) || topRight.GetStone() == 'c') return true;
				}
			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsBottomLeftFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topRight = (a_currentRow > 0 && a_currentCol < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol + 1)) : null;
		Block bottomLeft = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol - 1)) : null;
		Block farBottomLeft = (a_currentRow < a_board.GetBoardDimension() - 2 && a_currentCol > 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 2, a_currentCol - 2)) : null;
		
		if (bottomLeft != null) {
			if (bottomLeft.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (topRight != null && topRight.GetStone() != 'n') {
						if ((topRight.GetStone() == bottomLeft.GetStone()) && (topRight.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != bottomLeft.GetStone()) && (topRight.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
					if (farBottomLeft != null && farBottomLeft.GetStone() != 'n') {
						if ((farBottomLeft.GetStone() == bottomLeft.GetStone()) && (farBottomLeft.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((farBottomLeft.GetStone() != bottomLeft.GetStone()) && (farBottomLeft.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((bottomLeft.GetStone() == current.GetStone()) || bottomLeft.GetStone() == 'c')
						return true;
				}
			}
		}
		return false;
	}

	//Refer to the description of isLeftFavorable
	public boolean IsBottomRightFavorable(int a_currentRow, int a_currentCol, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topLeft = (a_currentRow > 0 && a_currentCol > 0) ? new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol - 1)) : null;
		Block bottomRight = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol < a_board.GetBoardDimension() - 1) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol + 1)) : null;
		Block farBottomRight = (a_currentRow < a_board.GetBoardDimension() - 2 && a_currentCol < a_board.GetBoardDimension() - 2) ? new Block(a_board.GetBlockAtLocation(a_currentRow + 2, a_currentCol + 2)) : null;
		
		if (bottomRight != null) {
			if (bottomRight.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if (topLeft != null && topLeft.GetStone() != 'n') {
						if ((topLeft.GetStone() == bottomRight.GetStone()) && (topLeft.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != bottomRight.GetStone()) && (topLeft.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
					if (farBottomRight != null && farBottomRight.GetStone() != 'n') {
						if ((farBottomRight.GetStone() == bottomRight.GetStone()) && (farBottomRight.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((farBottomRight.GetStone() != bottomRight.GetStone()) && (farBottomRight.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
				} else {
					if ((bottomRight.GetStone() == current.GetStone()) || bottomRight.GetStone() == 'c')
						return true;
				}
			}
		}
		return false;
	}
}

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
	protected static char m_stoneOfPreviousPlacement = 'x';

	//Default Constructor. ATTENTION: Try to make this private as it doesn't intialize the primaryColor variable which will cause issues later
	public Player() {
		m_score = 0;
		m_whiteStonesAvailable = 15;
		m_blackStonesAvailable = 15;
		m_clearStonesAvailable = 6;
	}

	//Constructor
	public Player(char a_primaryColor) {
		this();
		m_primaryColor = a_primaryColor;
	}

	// Number of available stone Getters
	public char GetPlayerStoneColor() { return m_primaryColor; }
	public int GetScore() { return m_score; }
	public int GetWhiteStonesAvailable() { return m_whiteStonesAvailable; }
	public int GetBlackStonesAvailable() { return m_blackStonesAvailable; }
	public int GetClearStonesAvailable() { return m_clearStonesAvailable; }

	//Get previous placement info
	public char GetStoneOfPreviousPlacement() {return m_stoneOfPreviousPlacement; }
	public int GetRowOfPreviousPlacement() { return m_rowOfPreviousPlacement; }
	public int GetColumnOfPreviousPlacement() { return m_columnOfPreviousPlacement; }
	public void ResetPreviousPlacements() {
		m_rowOfPreviousPlacement = -1;
		m_columnOfPreviousPlacement = -1;
		m_stoneOfPreviousPlacement = 'x';
	}

	/*
	 *	MOVE VALIDATION AND PLACEMENT RELATED FUNCTIONS
	 */

	//Is game over
	protected boolean IsGameOver(){
		if (m_whiteStonesAvailable == 0 && m_blackStonesAvailable == 0 && m_clearStonesAvailable == 0) {
			return true;
		}
		return false;
	}

	//Places a stone on the given location in game board and updates the stone counts and score
	protected boolean PlaceAStone(char a_stone, int a_row, int a_column, Board a_board) {
		if (IsValidMove(a_stone, a_row, a_column, a_board)) {
			//Make the placement and update the stone count
			if (a_board.SetStoneAtLocation(a_row, a_column, a_stone)) {
				UseAStone(a_stone);
				m_rowOfPreviousPlacement = a_row;
				m_columnOfPreviousPlacement = a_column;
				m_stoneOfPreviousPlacement = a_stone;
				System.out.println("Inserting (" + a_row + ", " + a_column + ") " + a_stone);
				return true;
			}
		}
		return false;
	}

	//Validates if an attempted move complies with all of the game rules
	public boolean IsValidMove(char a_stone, int a_row, int a_column, Board a_board) {
		//NOTIFICATION: A LOT OF NOTIFICATIONS NEED TO BE SET HERE NEXT TO EVERY IF STATEMENT
		//First, make sure the coordinate doesn't fall within the blocked territories of the multidimensional board
		if (a_board.m_gameBoard[a_row][a_column] != null) {
			//Next, make sure the coordinate has not been occupied by another stone
			if (!a_board.IsLocationOccupied(a_row, a_column)) {
				//Not all vacant spots are available to place a stone. There are rules.
				//Make sure the attempted placement complies with them rules.
				if (HasPermissionToOccupyVacantSpot(a_row, a_column, a_board)) {
					//Finally, make sure the player has the chosen stone available, and make the move.
					if (IsStoneAvailable(a_stone)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//Verifies if the player has the right to place a stone in that row/column despite its vacancy at the moment
	public boolean HasPermissionToOccupyVacantSpot(int a_row, int a_column, Board a_board) {
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
				System.out.println("Decreased a white stone");
				break;
			case 'b':
				m_blackStonesAvailable--;
				System.out.println("Decreased a black stone");
				break;
			case 'c':
				m_clearStonesAvailable--;
				System.out.println("Decreased a clear stone");
				break;
			default:
				System.out.println("Invalid stone");
				break;
				//NOTIFICATIONS: Not a valid stone
		}
	}

	/*
	* SCORING RELATED FUNCTIONS
	* */
	//THIS NEEDS TO BE CALLED BY EACH PLAYER AFTER EITHER OF THE PLAYERS MOVE. THE STONE PARAMETER REFERS TO PLAYER'S PRIMARY COLO
	// Calculates the change in points for a player after a move. Override by derived classes based on stone color picked at the start of the game
	//Row and Column of previous placement must be updated for these function to perform correctly

	public int UpdateScoreAfterMove(char a_stone, int a_placedInRow, int a_placedInColumn, Board a_board) {
		return m_score += CalculateScoreAfterMove(a_stone, a_placedInRow, a_placedInColumn, a_board);
	}

	public int CalculateScoreAfterMove(char a_stone, int a_placedInRow, int a_placedInColumn, Board a_board) {
		int points = 0;
		
		if (IsLeftFavorable(a_placedInRow, a_placedInColumn, a_board) && IsRightFavorable(a_placedInRow, a_placedInColumn, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn - 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn + 1) == a_stone) {
				points++;
				System.out.println("Left and Right favorable");
			}
		}
		if (IsTopFavorable(a_placedInRow, a_placedInColumn, a_board) && IsBottomFavorable(a_placedInRow, a_placedInColumn, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn) == a_stone) {
				points++;
				System.out.println("Top and Bottom favorable");
			}
		}
		if (IsTopLeftFavorable(a_placedInRow, a_placedInColumn, a_board) && IsBottomRightFavorable(a_placedInRow, a_placedInColumn, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn  - 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn  + 1) == a_stone) {
				points++;
				System.out.println("TopLeft and BottomRight favorable");
			}
		}
		if (IsTopRightFavorable(a_placedInRow, a_placedInColumn, a_board) && IsBottomLeftFavorable(a_placedInRow, a_placedInColumn, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn  + 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn  - 1) == a_stone) {
				points++;
				System.out.println("TopRight and BottomLeft favorable");
			}
		}


		if (IsLeftFavorable(a_placedInRow, a_placedInColumn, a_board) && IsLeftFavorable(a_placedInRow, a_placedInColumn - 1, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn - 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn  - 2) == a_stone) {
				points++;
				System.out.println("Left and farLeft favorable");
			}

		}
		if (IsRightFavorable(a_placedInRow, a_placedInColumn, a_board) && IsRightFavorable(a_placedInRow, a_placedInColumn + 1, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn + 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn  + 2) == a_stone) {
				points++;
				System.out.println("Right and farRight favorable");
			}
		}
		if (IsTopFavorable(a_placedInRow, a_placedInColumn, a_board) && IsTopFavorable(a_placedInRow - 1, a_placedInColumn, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 2, a_placedInColumn) == a_stone) {
				points++;
				System.out.println("Top and farTop favorable");
			}

		}
		if (IsBottomFavorable(a_placedInRow, a_placedInColumn, a_board) && IsBottomFavorable(a_placedInRow + 1, a_placedInColumn, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 2, a_placedInColumn) == a_stone) {
				points++;
				System.out.println("Bottom and farBottom favorable");
			}
		}
		if (IsTopLeftFavorable(a_placedInRow, a_placedInColumn, a_board) && IsTopLeftFavorable(a_placedInRow - 1, a_placedInColumn - 1, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn  - 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 2, a_placedInColumn  - 2) == a_stone) {
				points++;
				System.out.println("TopLeft and farTopLeft favorable");
			}
		}
		if (IsTopRightFavorable(a_placedInRow, a_placedInColumn, a_board) && IsTopRightFavorable(a_placedInRow - 1, a_placedInColumn + 1, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn  + 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow - 2, a_placedInColumn  + 2) == a_stone) {
				points++;
				System.out.println("TopRight and farTopRight favorable");
			}
		}
		if (IsBottomLeftFavorable(a_placedInRow, a_placedInColumn, a_board) && IsBottomLeftFavorable(a_placedInRow + 1, a_placedInColumn - 1, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn  - 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 2, a_placedInColumn  - 2) == a_stone) {
				points++;
				System.out.println("BottomLeft and farBottomLeft favorable");
			}
		}
		if (IsBottomRightFavorable(a_placedInRow, a_placedInColumn, a_board) && IsBottomRightFavorable(a_placedInRow + 1, a_placedInColumn + 1, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn  + 1) == a_stone || a_board.GetStoneAtLocation(a_placedInRow + 2, a_placedInColumn  + 2) == a_stone) {
				points++;
				System.out.println("BottomRight and farBottomRight favorable");
			}
		}
		//Return only the change in score
		//System.out.println("Points gained :" + points);
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
		
		if (left != null && left.IsInitialized()) {
			if (left.GetStone()!= 'n') {
				if (current.GetStone() == 'c') {
					if ((right != null && right.IsInitialized()) && right.GetStone() != 'n') {
						if ((left.GetStone() == right.GetStone()) && (left.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != right.GetStone()) && (left.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farLeft != null && farLeft.IsInitialized()) && farLeft.GetStone() != 'n') {
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
		
		if (right != null && right.IsInitialized()) {
			if (right.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((left != null && left.IsInitialized()) && left.GetStone() != 'n') {
						if ((left.GetStone() == right.GetStone()) && (left.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != right.GetStone()) && (left.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farRight != null && farRight.IsInitialized()) && farRight.GetStone() != 'n') {
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

		if (top != null && top.IsInitialized()) {
			if (top.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((bottom != null && bottom.IsInitialized()) && bottom.GetStone() != 'n') {
						if ((top.GetStone() == bottom.GetStone()) && (top.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != bottom.GetStone()) && (top.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farTop != null && farTop.IsInitialized()) && farTop.GetStone() != 'n') {
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
		
		if (bottom != null && bottom.IsInitialized()) {
			if (bottom.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((top != null && top.IsInitialized()) && top.GetStone() != 'n') {
						if ((top.GetStone() == bottom.GetStone()) && (top.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != bottom.GetStone()) && (top.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farBottom != null && farBottom.IsInitialized()) && farBottom.GetStone() != 'n') {
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
		
		if (topLeft != null && topLeft.IsInitialized()) {
			if (topLeft.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((bottomRight != null && bottomRight.IsInitialized()) && bottomRight.GetStone() != 'n') {
						if ((topLeft.GetStone() == bottomRight.GetStone()) && (topLeft.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != bottomRight.GetStone()) && (topLeft.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farTopLeft != null && farTopLeft.IsInitialized()) && farTopLeft.GetStone() != 'n') {
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

		if (topRight != null && topRight.IsInitialized()) {
			if (topRight.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((bottomLeft != null && bottomLeft.IsInitialized()) && bottomLeft.GetStone() != 'n') {
						if ((topRight.GetStone() == bottomLeft.GetStone()) && (topRight.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != bottomLeft.GetStone()) && (topRight.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farTopRight != null && farTopRight.IsInitialized()) && farTopRight.GetStone() != 'n') {
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
		
		if (bottomLeft != null && bottomLeft.IsInitialized()) {
			if (bottomLeft.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((topRight != null && topRight.IsInitialized()) && topRight.GetStone() != 'n') {
						if ((topRight.GetStone() == bottomLeft.GetStone()) && (topRight.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != bottomLeft.GetStone()) && (topRight.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farBottomLeft != null && farBottomLeft.IsInitialized()) && farBottomLeft.GetStone() != 'n') {
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
		
		if (bottomRight != null && bottomRight.IsInitialized()) {
			if (bottomRight.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((topLeft != null && topLeft.IsInitialized()) && topLeft.GetStone() != 'n') {
						if ((topLeft.GetStone() == bottomRight.GetStone()) && (topLeft.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != bottomRight.GetStone()) && (topLeft.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
					if ((farBottomRight != null && farBottomRight.IsInitialized()) && farBottomRight.GetStone() != 'n') {
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

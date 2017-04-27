package com.viveckh.threestones;

/**
 * Player Model Class
 * It implements the rules and strategies of the game for a generic player, handles move
 * validations and processing, along with calculation/update of scores for the game play.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
 */
public class Player {
	//VARIABLE DECLARATIONS
	private int m_score;
	private int m_whiteStonesAvailable;
	private int m_blackStonesAvailable;
	private int m_clearStonesAvailable;
	protected char m_primaryColor;
	protected static int m_rowOfPreviousPlacement = -1;
	protected static int m_columnOfPreviousPlacement = -1;
	protected static char m_stoneOfPreviousPlacement = 'x';

	//Temporary variables to receive result for logging errors in tertiary operator
	protected boolean printStatus;
	protected static boolean printNotifications = false;

	/**
	 * Player Default Constructor
	 * Initializes the class variables to default values
	 */
	public Player() {
		m_score = 0;
		m_whiteStonesAvailable = 15;
		m_blackStonesAvailable = 15;
		m_clearStonesAvailable = 6;
	}

	/**
	 * Player Constructor
	 * Initializes the class variables to default values along with assignment of primary stone
	 *
	 * @param a_primaryColor Character, primary choice of stone for the player
	 */
	public Player(char a_primaryColor) {
		this();
		m_primaryColor = a_primaryColor;
	}

	/**
	 * Gets primary stone for the player
	 *
	 * @return Character, primary stone for player
	 */
	public char GetPlayerStoneColor() {
		return m_primaryColor;
	}

	/**
	 * Gets the score of the player
	 *
	 * @return Character, score of the player
	 */
	public int GetScore() {
		return m_score;
	}

	/**
	 * Gets available white stones
	 *
	 * @return Integer, available white stones
	 */
	public int GetWhiteStonesAvailable() {
		return m_whiteStonesAvailable;
	}

	/**
	 * Gets available black stones
	 *
	 * @return Integer, available black stones
	 */
	public int GetBlackStonesAvailable() {
		return m_blackStonesAvailable;
	}

	/**
	 * Gets available clear stones
	 *
	 * @return Integer, available clear stones
	 */
	public int GetClearStonesAvailable() {
		return m_clearStonesAvailable;
	}

	/**
	 * Gets stone of previous placement in game
	 *
	 * @return Character, stone of previous placement
	 */
	public char GetStoneOfPreviousPlacement() {
		return m_stoneOfPreviousPlacement;
	}

	/**
	 * Gets row of previous placement in game
	 *
	 * @return Integer, row of previous placement
	 */
	public int GetRowOfPreviousPlacement() {
		return m_rowOfPreviousPlacement;
	}

	/**
	 * Gets Column of previous placement
	 *
	 * @return Integer, column of previous placement
	 */
	public int GetColumnOfPreviousPlacement() {
		return m_columnOfPreviousPlacement;
	}

	/**
	 * Resets the class variables associated with previuos placements in game
	 */
	public void ResetPreviousPlacements() {
		m_rowOfPreviousPlacement = -1;
		m_columnOfPreviousPlacement = -1;
		m_stoneOfPreviousPlacement = 'x';
	}

	/**
	 * Sets class variables associated with previous placements in game
	 *
	 * @param a_row    Integer, row where the stone was placed
	 * @param a_column Integer, column where the stone was placed
	 */
	public void SetPreviousPlacements(int a_row, int a_column) {
		m_rowOfPreviousPlacement = a_row;
		m_columnOfPreviousPlacement = a_column;
	}

	/**
	 * Sets the number of available stones for the player
	 *
	 * @param a_whiteStonesAvailable Integer, number of white stones available
	 * @param a_blackStonesAvailable Integer, number of black stones available
	 * @param a_clearStonesAvailable Integer, number of clear stones available
	 */
	public void SetStonesAvailability(int a_whiteStonesAvailable,
						    int a_blackStonesAvailable,
						    int a_clearStonesAvailable) {
		m_whiteStonesAvailable = (a_whiteStonesAvailable <= 15) ? a_whiteStonesAvailable : 15;
		m_blackStonesAvailable = (a_blackStonesAvailable <= 15) ? a_blackStonesAvailable : 15;
		m_clearStonesAvailable = (a_clearStonesAvailable <= 6) ? a_clearStonesAvailable : 6;
	}

	/**
	 * Set the score of the player
	 *
	 * @param a_score Integer, value to set player's score to
	 */
	public void SetScore(int a_score) {
		m_score = a_score;
	}

	/*
	 *	MOVE VALIDATION AND PLACEMENT RELATED FUNCTIONS
	 */

	/**
	 * PlaceAStone() - Places a stone on the given location and updates the stone counts and score
	 *
	 * @param a_stone  Char, stone to place
	 * @param a_row    Integer, row to place the stone in
	 * @param a_column Integer, column to place the stone in
	 * @param a_board  Board, the game board in context
	 * @return true if placement successful, false if failure
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	protected boolean PlaceAStone(char a_stone, int a_row, int a_column, Board a_board) {
		if (IsValidMove(a_stone, a_row, a_column, a_board)) {
			//Make the placement and update the stone count
			if (a_board.SetStoneAtLocation(a_row, a_column, a_stone)) {
				UseAStone(a_stone);
				m_rowOfPreviousPlacement = a_row;
				m_columnOfPreviousPlacement = a_column;
				m_stoneOfPreviousPlacement = a_stone;
				printStatus = printNotifications ?
					  Notifications.Msg_MoveDescription(a_stone) :
					  Notifications.Msg_NoMsg();
				return true;
			}
		}
		return false;
	}

	/**
	 * IsValidMove() - Validates if an attempted move complies with all of the game rules
	 *
	 * @param a_stone  Character, the stone for which validation is to be done
	 * @param a_row    Integer, the row where the stone is being attempted to be placed
	 * @param a_column Integer, the column where the stone is being attemped to be placed
	 * @param a_board  Board, game board in context
	 * @return true if placement is valid, false if invalid
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	protected boolean IsValidMove(char a_stone, int a_row, int a_column, Board a_board) {
		//NOTIFICATION: A LOT OF NOTIFICATIONS NEED TO BE SET HERE NEXT TO EVERY IF STATEMENT
		//First, make sure the coordinate doesn't fall within the blocked territories
		if (a_board.m_gameBoard[a_row][a_column] != null) {
			//Next, make sure the coordinate has not been occupied by another stone
			if (!a_board.IsLocationOccupied(a_row, a_column)) {
				/*Not all vacant spots are available to place a stone. There are rules.
				*Make sure the attempted placement complies with them rules.*/
				if (HasPermissionToOccupyVacantSpot(a_row, a_column, a_board)) {
					//Finally, make sure the player has the chosen stone available.
					if (IsStoneAvailable(a_stone)) {
						return true;
					} else {
						printStatus = printNotifications ?
							  Notifications.Msg_NoStonesToMove() :
							  Notifications.Msg_NoMsg();
					}
				} else {
					printStatus = printNotifications ?
						  Notifications.Msg_InvalidMove() : Notifications.Msg_NoMsg();
				}
			} else {
				printStatus = printNotifications ?
					  Notifications.Msg_AlreadyOccupied() : Notifications.Msg_NoMsg();
			}
		} else {
			printStatus = printNotifications ?
				  Notifications.Msg_TapOnGameBoard() : Notifications.Msg_NoMsg();
		}
		return false;
	}

	/**
	 * HasPermissionToOccupyVacantSpot() - Verifies if the player has the right to place a stone
	 * in the attempted row/column
	 *
	 * @param a_row    Integer, row where the stone is to be placed
	 * @param a_column Integer, column where the stone is to be placed
	 * @param a_board  Board, the game board in context
	 * @return true if the the player has permission to occupy the spot, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	protected boolean HasPermissionToOccupyVacantSpot(int a_row, int a_column, Board a_board) {
		//Permission granted if not a single placement has been made so far
		if (m_rowOfPreviousPlacement < 0 && m_columnOfPreviousPlacement < 0) {
			return true;
		}

		//Permission granted if the placement location matches row/column of previous placement
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

		//Permission granted if it gets to this point without finding a vacant spot
		return true;
	}

	/**
	 * IsStoneAvailable() - Checks if a stone is available for use
	 *
	 * @param a_stone Character, stone that needs to be checked
	 * @return true if stone is available, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	protected boolean IsStoneAvailable(char a_stone) {
		switch (a_stone) {
			case 'w':
				if (m_whiteStonesAvailable > 0) {
					return true;
				} else {
					return false;
				}
			case 'b':
				if (m_blackStonesAvailable > 0) {
					return true;
				} else {
					return false;
				}
			case 'c':
				if (m_clearStonesAvailable > 0) {
					return true;
				} else {
					return false;
				}
			default:
				//NOTIFICATIONS: Not a valid stone
				return false;
		}
	}

	/**
	 * UseAStone() - Updates the available stones count based on the stone that is used
	 *
	 * @param a_stone Character, stone that is being used
	 */
	protected void UseAStone(char a_stone) {
		switch (a_stone) {
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
				break;
		}
	}

	/*
	* SCORING RELATED FUNCTIONS
	* */
	/*THIS NEEDS TO BE CALLED BY EACH PLAYER AFTER EITHER OF THE PLAYERS MOVE.
	* THE STONE PARAMETER REFERS TO PLAYER'S PRIMARY COLOR
	* Calculates the change in points for a player after a move.
	* Override by derived classes based on stone color picked at the start of the game
	* Row and Column of previous placement must be updated for these function to perform correctly
	*/

	/**
	 * UpdateScoreAfterMove() - Updates score for the player of given stone color after a move
	 *
	 * @param a_stone          Character, primary stone color of the player
	 * @param a_placedInRow    Integer, row where the move was last made
	 * @param a_placedInColumn Integer, column where the move was last made
	 * @param a_board          Board, the game board in context
	 * @return Integer, the total score of the player
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public int UpdateScoreAfterMove(char a_stone, int a_placedInRow, int a_placedInColumn,
						  Board a_board) {
		int points = CalculateScoreAfterMove(a_stone, a_placedInRow, a_placedInColumn, a_board);
		if (points > 0) {
			Notifications.Msg_PointsGained(a_stone, points);
		}
		return m_score += points;
	}

	/**
	 * CalculateScoreAfterMove() - Calculates the change in score for a player after a move
	 *
	 * @param a_stone          Character, primary stone color of the player
	 * @param a_placedInRow    Integer, row where the move was last made
	 * @param a_placedInColumn Integer, column where the move was last made
	 * @param a_board          Board, the game board in context
	 * @return Integer, the scores earned from the last move
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public int CalculateScoreAfterMove(char a_stone, int a_placedInRow, int a_placedInColumn,
						     Board a_board) {
		int points = 0;

		//If Left and Right have favorable stones, a point is gained
		if (IsLeftFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsRightFavorable(a_placedInRow, a_placedInColumn, true, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn - 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn + 1) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("Left", "Right") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If Top and Bottom have favorable stones, a point is gained
		if (IsTopFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsBottomFavorable(a_placedInRow, a_placedInColumn, true, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("Top", "Bottom") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If TopLeft and BottomRight have favorable stones, a point is gained
		if (IsTopLeftFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsBottomRightFavorable(a_placedInRow, a_placedInColumn, true, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn - 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn + 1) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("TopLeft", "BottomRight") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If TopRight and BottomLeft have favorable stones, a point is gained
		if (IsTopRightFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsBottomLeftFavorable(a_placedInRow, a_placedInColumn, true, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn + 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn - 1) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("TopRight", "BottomLeft") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If Left and FarLeft have favorable stones, a point is gained
		if (IsLeftFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsLeftFavorable(a_placedInRow, a_placedInColumn - 1, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn - 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn - 2) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("Left", "FarLeft") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If Right and FarRight have favorable stones, a point is gained
		if (IsRightFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsRightFavorable(a_placedInRow, a_placedInColumn + 1, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn + 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn + 2) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("Right", "FarRight") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If Top and FarTop have favorable stones, a point is gained
		if (IsTopFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsTopFavorable(a_placedInRow - 1, a_placedInColumn, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 2, a_placedInColumn) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("Top", "FarTop") :
					  Notifications.Msg_NoMsg();
			}

		}

		//If Bottom and FarBottom have favorable stones, a point is gained
		if (IsBottomFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsBottomFavorable(a_placedInRow + 1, a_placedInColumn, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 2, a_placedInColumn) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("Bottom", "FarBottom") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If TopLeft and FarTopLeft are favorable stones, a point is gained
		if (IsTopLeftFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsTopLeftFavorable(a_placedInRow - 1, a_placedInColumn - 1, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn - 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 2, a_placedInColumn - 2) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("TopLeft", "FarTopLeft") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If TopRight and FarTopRight are favorable stones, a point is gained
		if (IsTopRightFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsTopRightFavorable(a_placedInRow - 1, a_placedInColumn + 1, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 1, a_placedInColumn + 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow - 2, a_placedInColumn + 2) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("TopRight", "FarTopRight") :
					  Notifications.Msg_NoMsg();
			}
		}

		//If BottomLeft and FarBottomLeft are favorable stones, a point is gained
		if (IsBottomLeftFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsBottomLeftFavorable(a_placedInRow + 1, a_placedInColumn - 1, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn - 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 2, a_placedInColumn - 2) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("BottomLeft", "FarBottomLeft")
					  : Notifications.Msg_NoMsg();
			}
		}

		//If BottomRight and FarBottomRight have favorable stones, a point is gained
		if (IsBottomRightFavorable(a_placedInRow, a_placedInColumn, true, a_board) &&
			  IsBottomRightFavorable(a_placedInRow + 1, a_placedInColumn + 1, false, a_board)) {
			if (a_board.GetStoneAtLocation(a_placedInRow, a_placedInColumn) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 1, a_placedInColumn + 1) == a_stone ||
				  a_board.GetStoneAtLocation(a_placedInRow + 2, a_placedInColumn + 2) == a_stone) {
				points++;
				printStatus = printNotifications ?
					  Notifications.Msg_CompletedArrangement("BottomRight", "FarBottomRight") :
					  Notifications.Msg_NoMsg();
			}
		}
		//Return only the change in score
		return points;
	}

	//Checks whether the left is favorable. Does the calculation based on two situations.
	// 1) What if my Stone is clear?
	// 2) What if my stone is colored (black/white)?

	/**
	 * IsLeftFavorable() - Checks whether the left block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farLeft block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if left block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsLeftFavorable(int a_currentRow, int a_currentCol,
						  boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block left = (a_currentCol > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol - 1)) : null;
		Block farLeft = (a_currentCol > 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol - 2)) : null;
		Block right = (a_currentCol < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol + 1)) : null;

		if (left != null && left.IsInitialized()) {
			if (left.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((right != null && right.IsInitialized()) &&
						  right.GetStone() != 'n') {
						if ((left.GetStone() == right.GetStone()) &&
							  (left.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != right.GetStone()) &&
							  (left.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farLeft != null && farLeft.IsInitialized()) &&
							  farLeft.GetStone() != 'n') {
							if ((left.GetStone() == farLeft.GetStone()) &&
								  (left.GetStone() != 'c' && farLeft.GetStone() != 'c')) {
								return true;
							}
							if ((left.GetStone() != farLeft.GetStone()) &&
								  (left.GetStone() == 'c' || farLeft.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((left.GetStone() == current.GetStone()) || left.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * IsRightFavorable() - Checks whether the right block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farRight block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if right block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsRightFavorable(int a_currentRow, int a_currentCol,
						   boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block left = (a_currentCol > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol - 1)) : null;
		Block right = (a_currentCol < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol + 1)) : null;
		Block farRight = (a_currentCol < a_board.GetBoardDimension() - 2) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol + 2)) : null;

		if (right != null && right.IsInitialized()) {
			if (right.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((left != null && left.IsInitialized()) && left.GetStone() != 'n') {
						if ((left.GetStone() == right.GetStone()) &&
							  (left.GetStone() != 'c' && right.GetStone() != 'c')) {
							return true;
						}
						if ((left.GetStone() != right.GetStone()) &&
							  (left.GetStone() == 'c' || right.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farRight != null && farRight.IsInitialized()) &&
							  farRight.GetStone() != 'n') {
							if ((farRight.GetStone() == right.GetStone()) &&
								  (farRight.GetStone() != 'c' && right.GetStone() != 'c')) {
								return true;
							}
							if ((farRight.GetStone() != right.GetStone()) &&
								  (farRight.GetStone() == 'c' || right.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((right.GetStone() == current.GetStone()) || right.GetStone() == 'c') {
						return true;
					}
				}

			}
		}
		return false;
	}

	/**
	 * IsTopFavorable() - Checks whether the top block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farTop block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if top block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsTopFavorable(int a_currentRow, int a_currentCol,
						 boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block top = (a_currentRow > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol)) : null;
		Block farTop = (a_currentRow > 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 2, a_currentCol)) : null;
		Block bottom = (a_currentRow < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol)) : null;

		if (top != null && top.IsInitialized()) {
			if (top.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((bottom != null && bottom.IsInitialized()) &&
						  bottom.GetStone() != 'n') {
						if ((top.GetStone() == bottom.GetStone()) &&
							  (top.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != bottom.GetStone()) &&
							  (top.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farTop != null && farTop.IsInitialized()) &&
							  farTop.GetStone() != 'n') {
							if ((top.GetStone() == farTop.GetStone()) &&
								  (top.GetStone() != 'c' && farTop.GetStone() != 'c')) {
								return true;
							}
							if ((top.GetStone() != farTop.GetStone()) &&
								  (top.GetStone() == 'c' || farTop.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((top.GetStone() == current.GetStone()) || top.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * IsBottomFavorable() - Checks whether the Bottom block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farBottom block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if bottom block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsBottomFavorable(int a_currentRow, int a_currentCol,
						    boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block top = (a_currentRow > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol)) : null;
		Block bottom = (a_currentRow < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol)) : null;
		Block farBottom = (a_currentRow < a_board.GetBoardDimension() - 2) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 2, a_currentCol)) : null;

		if (bottom != null && bottom.IsInitialized()) {
			if (bottom.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((top != null && top.IsInitialized()) &&
						  top.GetStone() != 'n') {
						if ((top.GetStone() == bottom.GetStone()) &&
							  (top.GetStone() != 'c' && bottom.GetStone() != 'c')) {
							return true;
						}
						if ((top.GetStone() != bottom.GetStone()) &&
							  (top.GetStone() == 'c' || bottom.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farBottom != null && farBottom.IsInitialized()) &&
							  farBottom.GetStone() != 'n') {
							if ((farBottom.GetStone() == bottom.GetStone()) &&
								  (farBottom.GetStone() != 'c' && bottom.GetStone() != 'c')) {
								return true;
							}
							if ((farBottom.GetStone() != bottom.GetStone()) &&
								  (farBottom.GetStone() == 'c' || bottom.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((bottom.GetStone() == current.GetStone()) || bottom.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * IsTopLeftFavorable() - Checks whether the TopLeft block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farTopLeft block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if topLeft block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsTopLeftFavorable(int a_currentRow, int a_currentCol,
						     boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topLeft = (a_currentRow > 0 && a_currentCol > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol - 1)) : null;
		Block farTopLeft = (a_currentRow > 1 && a_currentCol > 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 2, a_currentCol - 2)) : null;
		Block bottomRight = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol + 1)) : null;

		if (topLeft != null && topLeft.IsInitialized()) {
			if (topLeft.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((bottomRight != null && bottomRight.IsInitialized()) &&
						  bottomRight.GetStone() != 'n') {
						if ((topLeft.GetStone() == bottomRight.GetStone()) &&
							  (topLeft.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != bottomRight.GetStone()) &&
							  (topLeft.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farTopLeft != null && farTopLeft.IsInitialized()) &&
							  farTopLeft.GetStone() != 'n') {
							if ((topLeft.GetStone() == farTopLeft.GetStone()) &&
								  (topLeft.GetStone() != 'c' && farTopLeft.GetStone() != 'c')) {
								return true;
							}
							if ((topLeft.GetStone() != farTopLeft.GetStone()) &&
								  (topLeft.GetStone() == 'c' || farTopLeft.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((topLeft.GetStone() == current.GetStone()) || topLeft.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * IsTopRightFavorable() - Checks whether the topRight block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farTopRight block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if topRight block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsTopRightFavorable(int a_currentRow, int a_currentCol,
							boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topRight = (a_currentRow > 0 && a_currentCol < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol + 1)) : null;
		Block farTopRight = (a_currentRow > 1 && a_currentCol < a_board.GetBoardDimension() - 2) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 2, a_currentCol + 2)) : null;
		Block bottomLeft = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol - 1)) : null;

		if (topRight != null && topRight.IsInitialized()) {
			if (topRight.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((bottomLeft != null && bottomLeft.IsInitialized()) &&
						  bottomLeft.GetStone() != 'n') {
						if ((topRight.GetStone() == bottomLeft.GetStone()) &&
							  (topRight.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != bottomLeft.GetStone()) &&
							  (topRight.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farTopRight != null && farTopRight.IsInitialized())
							  && farTopRight.GetStone() != 'n') {
							if ((topRight.GetStone() == farTopRight.GetStone()) &&
								  (topRight.GetStone() != 'c' && farTopRight.GetStone() != 'c')) {
								return true;
							}
							if ((topRight.GetStone() != farTopRight.GetStone()) &&
								  (topRight.GetStone() == 'c' || farTopRight.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((topRight.GetStone() == current.GetStone()) || topRight.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * IsBottomLeftFavorable() - Checks whether the bottomLeft block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farBottomLeft block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if bottomLeft block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsBottomLeftFavorable(int a_currentRow, int a_currentCol,
							  boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topRight = (a_currentRow > 0 && a_currentCol < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol + 1)) : null;
		Block bottomLeft = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol - 1)) : null;
		Block farBottomLeft = (a_currentRow < a_board.GetBoardDimension() - 2 && a_currentCol > 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 2, a_currentCol - 2)) : null;

		if (bottomLeft != null && bottomLeft.IsInitialized()) {
			if (bottomLeft.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((topRight != null && topRight.IsInitialized()) &&
						  topRight.GetStone() != 'n') {
						if ((topRight.GetStone() == bottomLeft.GetStone()) &&
							  (topRight.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
							return true;
						}
						if ((topRight.GetStone() != bottomLeft.GetStone()) &&
							  (topRight.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farBottomLeft != null && farBottomLeft.IsInitialized()) &&
							  farBottomLeft.GetStone() != 'n') {
							if ((farBottomLeft.GetStone() == bottomLeft.GetStone()) &&
								  (farBottomLeft.GetStone() != 'c' && bottomLeft.GetStone() != 'c')) {
								return true;
							}
							if ((farBottomLeft.GetStone() != bottomLeft.GetStone()) &&
								  (farBottomLeft.GetStone() == 'c' || bottomLeft.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((bottomLeft.GetStone() == current.GetStone()) || bottomLeft.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * IsBottomRightFavorable() - Checks whether the bottomRight block is favorable to current block
	 *
	 * @param a_currentRow   Integer, row of current block
	 * @param a_currentCol   Integer, column of current block
	 * @param a_checkFarther Boolean, whether to use farBottomRight block for calculations
	 * @param a_board        Board, the game board in context
	 * @return true if bottomRight block favorable, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	private boolean IsBottomRightFavorable(int a_currentRow, int a_currentCol, boolean a_checkFarther, Board a_board) {
		Block current = new Block(a_board.GetBlockAtLocation(a_currentRow, a_currentCol));
		Block topLeft = (a_currentRow > 0 && a_currentCol > 0) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow - 1, a_currentCol - 1)) : null;
		Block bottomRight = (a_currentRow < a_board.GetBoardDimension() - 1 && a_currentCol < a_board.GetBoardDimension() - 1) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 1, a_currentCol + 1)) : null;
		Block farBottomRight = (a_currentRow < a_board.GetBoardDimension() - 2 && a_currentCol < a_board.GetBoardDimension() - 2) ?
			  new Block(a_board.GetBlockAtLocation(a_currentRow + 2, a_currentCol + 2)) : null;

		if (bottomRight != null && bottomRight.IsInitialized()) {
			if (bottomRight.GetStone() != 'n') {
				if (current.GetStone() == 'c') {
					if ((topLeft != null && topLeft.IsInitialized()) &&
						  topLeft.GetStone() != 'n') {
						if ((topLeft.GetStone() == bottomRight.GetStone()) &&
							  (topLeft.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
							return true;
						}
						if ((topLeft.GetStone() != bottomRight.GetStone()) &&
							  (topLeft.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
							return true;
						}
					}
					if (a_checkFarther) {
						if ((farBottomRight != null && farBottomRight.IsInitialized()) &&
							  farBottomRight.GetStone() != 'n') {
							if ((farBottomRight.GetStone() == bottomRight.GetStone()) &&
								  (farBottomRight.GetStone() != 'c' && bottomRight.GetStone() != 'c')) {
								return true;
							}
							if ((farBottomRight.GetStone() != bottomRight.GetStone()) &&
								  (farBottomRight.GetStone() == 'c' || bottomRight.GetStone() == 'c')) {
								return true;
							}
						}
					}
				} else {
					if ((bottomRight.GetStone() == current.GetStone()) || bottomRight.GetStone() == 'c') {
						return true;
					}
				}
			}
		}
		return false;
	}
}

package com.viveckh.threestones;

/**
 * This class implements the functionality to perform moves on behalf of the computer
 */
public class Computer extends Player{
	private char m_ownStoneColor, m_opponentStoneColor, m_commonStoneColor;
	private char m_recommendedStoneColor;
	private int m_recommendedRow, m_recommendedColumn;
	private int m_highestScorePossible;
	//int bestRowForPlacement = 0, bestColumnForPlacement = 0;
	//char bestStoneForPlacement = 'x';	//Default value
	
	//Constructor
	public Computer(char a_primaryColor) {
		//m_primaryColor = a_primaryColor;
		super(a_primaryColor);
	}


	/*
		To get recommended move info. But gotta call the play function first before asking for recommended moves
	 */
	public char GetRecommendedStone() {
		return m_recommendedStoneColor;
	}

	public int GetRecommendedRow() {
		return m_recommendedRow;
	}

	public int GetRecommendedColumn() {
		return m_recommendedColumn;
	}

	public int GetHighestScorePossible() {
		return m_highestScorePossible;
	}



	//Calculate and display the best movement for the computer based on the calculations
	//The stone, row, and column parameters are actually meant for pass by reference purposes so that the caller knows what move the computer made
	public boolean Play(Boolean a_helpModeOn, Board a_board, Human a_human) {
		printNotifications = false;
		
		//STEP 1: SET THE CLASS VARIABLES TO PROPER STONES BASED ON WHO IS CALLING THE PLAY FUNCTION
		//If help mode is off, pick computer's stone as ownStone. If help mode is on, pick computer's stone as opponentStone
		if (!a_helpModeOn) {
			m_ownStoneColor = m_primaryColor;
			if (m_ownStoneColor == 'w') {
				m_opponentStoneColor = 'b';
			}
			else {
				m_opponentStoneColor = 'w';
			}
		}
		else {
			m_opponentStoneColor = m_primaryColor;
			if (m_opponentStoneColor == 'w') {
				m_ownStoneColor = 'b';
			}
			else {
				m_ownStoneColor = 'w';
			}
		}
		m_commonStoneColor = 'c';


		int highestScoreDifference = -10;	//Setting this to ensure a move is made even in situations when the best move might result in loss of points
		int bestRowForPlacement = 0, bestColumnForPlacement = 0;
		char bestStoneForPlacement = 'x';	//Default value

		for (int row = 0; row < a_board.GetBoardDimension(); row++) {
			for (int column = 0; column < a_board.GetBoardDimension(); column++) {
				//If an index in the passed gameboard isn't null and isn't occupied
				if (a_board.GetBlockAtLocation(row, column) != null && !a_board.IsLocationOccupied(row, column)) {
					//Temporary variables for calculations purposes
					int points = 0;      //Storing temporary score calculation
					boolean isValidMove = false;	//storing move validation temporary results
					Board tempBoard1 = new Board(a_board);
					Board tempBoard2 = new Board(a_board);
					Board tempBoard3 = new Board(a_board);

					//Check best possible coordinate to place own stone
					//If helpMode is on, check move validation for human, otherwise, check move validation for computer
					isValidMove = a_helpModeOn ? a_human.IsValidMove(m_ownStoneColor, row, column, tempBoard1) : IsValidMove(m_ownStoneColor, row, column, tempBoard1);
					if (isValidMove) {
						//Place the stone in this temp board and calculate what would the score look like
						if (tempBoard1.SetStoneAtLocation(row, column, m_ownStoneColor)) {
							points = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard1) - CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard1);
							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = m_ownStoneColor;
								//Notifications.BotsThink_FoundAPlacementToWinPoints(points, "own");
							}
						}
					}

					//Check best possible coordinate to place opponent stone
					//If helpMode is on, check move validation for human, otherwise, check move validation for computer
					isValidMove = a_helpModeOn ? a_human.IsValidMove(m_opponentStoneColor, row, column, tempBoard1) : IsValidMove(m_opponentStoneColor, row, column, tempBoard1);
					if (isValidMove) {
						//Place the stone in this temp board and calculate what would the score look like
						if (tempBoard2.SetStoneAtLocation(row, column, m_opponentStoneColor)) {
							points = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard2) - CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard2);
							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = m_opponentStoneColor;
								//Notifications.BotsThink_FoundAPlacementToWinPoints(points, "opponent");
							}
						}
					}

					//Check best possible coordinate to place clear stone
					//If helpMode is on, check move validation for human, otherwise, check move validation for computer
					isValidMove = a_helpModeOn ? a_human.IsValidMove(m_commonStoneColor, row, column, tempBoard1) : IsValidMove(m_commonStoneColor, row, column, tempBoard1);
					if (isValidMove) {
						//Place the stone in this temp board and calculate what would the score look like
						if (tempBoard3.SetStoneAtLocation(row, column, m_commonStoneColor)) {
							points = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard3) - CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard3);

							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = m_commonStoneColor;
								//Notifications.BotsThink_FoundAPlacementToWinPoints(points, "magic");
							}
						}
					}
				}
			}
		}

		//Now that the computer has calculated its move, it is safe to print notifications
		printNotifications = true;


		//Minimax(a_board, m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, 3, 0, 0, true, 0);
		//If the value of bestStone has been changed from default invalid one to something else, we have a move
		if (!a_helpModeOn && bestStoneForPlacement != 'x') {
			//Attempt the move and update the score if successful
			if (PlaceAStone(bestStoneForPlacement, bestRowForPlacement, bestColumnForPlacement, a_board)) {
				UpdateScoreAfterMove(m_primaryColor, m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board);
				return true;
			}
		}

		if (a_helpModeOn && bestStoneForPlacement != 'x') {
			m_recommendedStoneColor = bestStoneForPlacement;
			m_recommendedRow = bestRowForPlacement;
			m_recommendedColumn = bestColumnForPlacement;
			m_highestScorePossible = highestScoreDifference;
			return true;
		}
		return false;
	}


	/*
	//Populates the best placement details in the class variable
	private int Minimax(Board a_board, int a_lastRowOfPlacement, int a_lastColumnOfPlacement, int a_depth, int a_ownBestPoints, int a_opponentWorstPoints, boolean a_maximizingPlayer, int a_heuristicVal) {

		//STEP 1: HANDLE RETURN CONDITIONS
		//If at least one vacant spot is found in the same row/column of last placement, then it is not a leaf node
		boolean isLeafNode = true;
		for (int row = 0; row < a_board.GetBoardDimension(); row++) {
			for (int column = 0; column < a_board.GetBoardDimension(); column++) {
				//If the current row/column matches the row/column of last placement
				if (row == a_lastRowOfPlacement || column == a_lastColumnOfPlacement) {
					//If the block in this location is not null and not occupied, then this node is not a leaf node
					if (a_board.GetBlockAtLocation(row, column) != null && !a_board.IsLocationOccupied(row, column)) {
						isLeafNode = false;
					}
				}
			}
		}

		//If max depth is reached or the current node is a leaf node - meaning no vacant spots in the row and column of last placement, then return;
		if (a_depth == 0 || isLeafNode) {
			System.out.println("Returning heuristic Value of : " + a_heuristicVal);
			return a_heuristicVal;
		}

		//STEP 2: CALL THE MINIMAX FUNCTION RECURSIVELY BY PASSING APPROPRIATE PARAMETERS
		if (a_maximizingPlayer) {
			int points = Integer.MIN_VALUE;

			//GO THROUGH ALL THE CHILD NODES
			for (int row = 0; row < a_board.GetBoardDimension(); row++) {
				for (int column = 0; column < a_board.GetBoardDimension(); column++) {
					//If the current row/column matches the row/column of last placement
					if (row == a_lastRowOfPlacement || column == a_lastColumnOfPlacement) {
						//If the block in this location is not null and not occupied, then this node is not a leaf node
						if (a_board.GetBlockAtLocation(row, column) != null && !a_board.IsLocationOccupied(row, column)) {
							Board tempBoard = new Board(a_board);

							//Check best possible coordinate to place own stone
							if (IsValidMove(m_ownStoneColor, row, column, tempBoard)) {
								//Place the stone in this temp board and calculate what would the score look like
								if (tempBoard.SetStoneAtLocation(row, column, m_ownStoneColor)) {
									int ownPointsGained = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard);
									int opponentPointsGained = CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard);
									points = ownPointsGained - opponentPointsGained;
									System.out.println("Points gained: " + points);
									//If this is the best move so far, make a note of it
									points = Math.max(points, Minimax(tempBoard, row, column, a_depth - 1,a_ownBestPoints, a_opponentWorstPoints, false, a_heuristicVal + points));
									a_ownBestPoints = Math.max(a_ownBestPoints, points);
									if (a_opponentWorstPoints <= a_ownBestPoints) {
										break;
									}
									else {
										bestRowForPlacement = row;
										bestColumnForPlacement = column;
										bestStoneForPlacement = m_ownStoneColor;
									}
								}
							}
						}
					}
				}
			}
			return points;
		}
		else {
			int points = Integer.MAX_VALUE;

			//GO THROUGH ALL THE CHILD NODES
			for (int row = 0; row < a_board.GetBoardDimension(); row++) {
				for (int column = 0; column < a_board.GetBoardDimension(); column++) {
					//If the current row/column matches the row/column of last placement
					if (row == a_lastRowOfPlacement || column == a_lastColumnOfPlacement) {
						//If the block in this location is not null and not occupied, then this node is not a leaf node
						if (a_board.GetBlockAtLocation(row, column) != null && !a_board.IsLocationOccupied(row, column)) {
							Board tempBoard = new Board(a_board);

							//Check best possible coordinate to place own stone
							if (IsValidMove(m_opponentStoneColor, row, column, tempBoard)) {
								//Place the stone in this temp board and calculate what would the score look like
								if (tempBoard.SetStoneAtLocation(row, column, m_opponentStoneColor)) {
									int ownPointsGained = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard);
									int opponentPointsGained = CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard);
									points = ownPointsGained - opponentPointsGained;
									//If this is the best move so far, make a note of it
									points = Math.min(points, Minimax(tempBoard, row, column, a_depth - 1,a_ownBestPoints, a_opponentWorstPoints, true, a_heuristicVal + points));
									a_opponentWorstPoints = Math.min(a_opponentWorstPoints, points);
									if (a_opponentWorstPoints <= a_ownBestPoints) {
										break;
									}
									else {
										bestRowForPlacement = row;
										bestColumnForPlacement = column;
										bestStoneForPlacement = m_ownStoneColor;
									}
								}
							}
						}
					}
				}
			}
			return points;
		}
	}

	*/
}

package com.viveckh.threestones;

/**
 * <h1>Computer Model Class</h1>
 * It inherits from the 'Player' class and implements a greedy-best-first-search algorithm
 * to issue moves on behalf of the computer player and to recommend moves to the Human player
 * under Help Mode.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
 */
public class Computer extends Player {

	//Variable Declarations
	private char m_ownStoneColor, m_opponentStoneColor, m_commonStoneColor;
	private char m_recommendedStoneColor;
	private int m_recommendedRow, m_recommendedColumn;
	private int m_highestScorePossible;

	/**
	 * Computer Constructor
	 *
	 * @param a_primaryColor Primary stone color of the Computer. 'w' or 'b'
	 */
	public Computer(char a_primaryColor) {
		super(a_primaryColor);
	}

	/**
	 * Gets recommended stone during Help Mode. Ensure Play() is called first
	 *
	 * @return A Character representing recommended stone. w, b or c.
	 */
	public char GetRecommendedStone() {
		return m_recommendedStoneColor;
	}

	/**
	 * Gets recommended row of placement during Help Mode. Ensure Play() is called first
	 *
	 * @return Integer representing recommended row of placement in Board
	 */
	public int GetRecommendedRow() {
		return m_recommendedRow;
	}

	/**
	 * Gets recommended column of placement during Help Mode. Ensure Play() is called first
	 *
	 * @return Integer representing recommended column of placement in Board
	 */
	public int GetRecommendedColumn() {
		return m_recommendedColumn;
	}

	/**
	 * Gets highest possible net points possible to earn in next move during Help Mode.
	 *
	 * @return Integer with highest possible points that calling player can earn in next move
	 */
	public int GetHighestScorePossible() {
		return m_highestScorePossible;
	}

	/**
	 * Play(),
	 * Calculates next best move using a greedy-best-first search algorithm. And depending on
	 * whether it is called by Computer player or under Help Mode, it either initiates move on
	 * behalf of the computer, or simply loads the 'recommended___' class variables with best
	 * recommendation for the calling player.
	 *
	 * @param a_helpModeOn true if called by human player under help mode, false otherwise
	 * @param a_board      Board object in the context where best moves needs to be calculated
	 * @param a_human      Human player who is playing against the computer player
	 * @return true if a move was successfully initiated/recommended, false otherwise
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public boolean Play(Boolean a_helpModeOn, Board a_board, Human a_human) {
		printNotifications = false;
		
		/*STEP 1: SET THE CLASS VARIABLES BASED ON WHO IS CALLING THE PLAY FUNCTION
		If help mode is off, pick computer's stone as ownStone.
		Else, pick computer's stone as opponentStone*/
		if (!a_helpModeOn) {
			m_ownStoneColor = m_primaryColor;
			if (m_ownStoneColor == 'w') {
				m_opponentStoneColor = 'b';
			} else {
				m_opponentStoneColor = 'w';
			}
		} else {
			m_opponentStoneColor = m_primaryColor;
			if (m_opponentStoneColor == 'w') {
				m_ownStoneColor = 'b';
			} else {
				m_ownStoneColor = 'w';
			}
		}
		m_commonStoneColor = 'c';

		//STEP 2: GO THROUGH ALL THE POSSIBLE PLACEMENTS AND CALCULATE THE BEST ONE
		int highestScoreDifference = -10;
		int bestRowForPlacement = 0, bestColumnForPlacement = 0;
		char bestStoneForPlacement = 'x';      //Default value

		for (int row = 0; row < a_board.GetBoardDimension(); row++) {
			for (int column = 0; column < a_board.GetBoardDimension(); column++) {
				//If an index in the passed game board isn't null and isn't occupied
				if (a_board.GetBlockAtLocation(row, column) != null &&
					  !a_board.IsLocationOccupied(row, column)) {

					//Temporary variables for calculation purposes
					int points = 0;      //Storing temporary score calculation
					boolean isValidMove = false;      //storing move validation result
					Board tempBoard1 = new Board(a_board);
					Board tempBoard2 = new Board(a_board);
					Board tempBoard3 = new Board(a_board);

					/*Check best possible coordinate to place own stone
					If helpMode is on, check move validation for human,
					otherwise, check move validation for computer*/
					isValidMove = a_helpModeOn ?
						  a_human.IsValidMove(m_ownStoneColor, row, column, tempBoard1) :
						  IsValidMove(m_ownStoneColor, row, column, tempBoard1);
					if (isValidMove) {
						//Place the stone in this temp board and calculate score
						if (tempBoard1.SetStoneAtLocation(row, column, m_ownStoneColor)) {
							points = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard1) -
								  CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard1);
							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = m_ownStoneColor;
							}
						}
					}

					/*Check best possible coordinate to place opponent stone
					If helpMode is on, check move validation for human,
					otherwise, check move validation for computer*/
					isValidMove = a_helpModeOn ?
						  a_human.IsValidMove(m_opponentStoneColor, row, column, tempBoard1) :
						  IsValidMove(m_opponentStoneColor, row, column, tempBoard1);
					if (isValidMove) {
						//Place the stone in this temp board and calculate score
						if (tempBoard2.SetStoneAtLocation(row, column, m_opponentStoneColor)) {
							points = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard2) -
								  CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard2);
							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = m_opponentStoneColor;
							}
						}
					}

					/*Check best possible coordinate to place clear stone
					If helpMode is on, check move validation for human,
					otherwise, check move validation for computer*/
					isValidMove = a_helpModeOn ?
						  a_human.IsValidMove(m_commonStoneColor, row, column, tempBoard1) :
						  IsValidMove(m_commonStoneColor, row, column, tempBoard1);
					if (isValidMove) {
						//Place the stone in this temp board and calculate score
						if (tempBoard3.SetStoneAtLocation(row, column, m_commonStoneColor)) {
							points = CalculateScoreAfterMove(m_ownStoneColor, row, column, tempBoard3) -
								  CalculateScoreAfterMove(m_opponentStoneColor, row, column, tempBoard3);

							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = m_commonStoneColor;
							}
						}
					}
				}
			}
		}

		//Now that the computer has calculated its move, it is safe to print notifications
		printNotifications = true;

		/*STEP 3: IF THE VALUE OF BEST STONE HAS BEEN CHANGED FROM 'x', WE HAVE A MOVE
		IF HELP MODE OFF, INITIATE THE MOVE. IF ON, ONLY SAVE MOVE TO RECOMMEND VARIABLES*/
		if (!a_helpModeOn && bestStoneForPlacement != 'x') {
			//Attempt the move and update the score if successful
			if (PlaceAStone(bestStoneForPlacement,
				  bestRowForPlacement, bestColumnForPlacement, a_board)) {

				UpdateScoreAfterMove(m_primaryColor,
					  m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board);
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
}

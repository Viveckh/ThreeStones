package com.viveckh.threestones;

/**
 * This class implements the functionality to perform moves on behalf of the computer
 */
public class Computer extends Player{
	//Constructor
	public Computer(char a_primaryColor) {
		//m_primaryColor = a_primaryColor;
		super(a_primaryColor);
	}

	//Calculate and display the best movement for the computer based on the calculations
	//The stone, row, and column parameters are actually meant for pass by reference purposes so that the caller knows what move the computer made
	public boolean Play(Boolean a_helpModeOn, char a_stone, int a_row, int a_column, Board a_board) {
		char ownStoneColor, opponentStoneColor, commonStoneColor;

		//If help mode is off, pick computer's stone as ownStone. If help mode is on, pick computer's stone as opponentStone
		if (!a_helpModeOn) {
			ownStoneColor = m_primaryColor;
			if (ownStoneColor == 'w') {
				opponentStoneColor = 'b';
			}
			else {
				opponentStoneColor = 'w';
			}
		}
		else {
			opponentStoneColor = m_primaryColor;
			if (opponentStoneColor == 'w') {
				ownStoneColor = 'b';
			}
			else {
				ownStoneColor = 'w';
			}
		}
		commonStoneColor = 'c';

		int highestScoreDifference = 0;
		int bestRowForPlacement = 0, bestColumnForPlacement = 0;
		char bestStoneForPlacement = 'x';	//Default value

		for (int row = 0; row < a_board.GetBoardDimension(); row++) {
			for (int column = 0; column < a_board.GetBoardDimension(); column++) {
				//If an index in the passed gameboard isn't null and isn't occupied
				if (a_board.GetBlockAtLocation(row, column) != null && !a_board.IsLocationOccupied(row, column)) {
					//Temporary variables for calculations purposes
					int points = 0;	//Storing temporary score calculation
					Board tempBoard1 = new Board(a_board);
					Board tempBoard2 = new Board(a_board);
					Board tempBoard3 = new Board(a_board);

					//Check best possible coordinate to place own stone
					if (IsValidMove(ownStoneColor, row, column, tempBoard1)) {
						//Place the stone in this temp board and calculate what would the score look like
						if (tempBoard1.SetStoneAtLocation(row, column, ownStoneColor)) {
							points = CalculateScoreAfterMove(ownStoneColor, row, column, tempBoard1);
							System.out.println("Going through (" + row + ", " + column + ") " + ownStoneColor + " " + points);
							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = ownStoneColor;
							}

						}
					}

					//Check best possible coordinate to place opponent stone
					if (IsValidMove(opponentStoneColor, row, column, tempBoard2)) {
						//Place the stone in this temp board and calculate what would the score look like
						if (tempBoard2.SetStoneAtLocation(row, column, opponentStoneColor)) {
							points = CalculateScoreAfterMove(ownStoneColor, row, column, tempBoard2);
							System.out.println("Going through (" + row + ", " + column + ") " + opponentStoneColor + " " + points);
							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = opponentStoneColor;
							}
						}
					}


					//Check best possible coordinate to place clear stone
					if (IsValidMove(commonStoneColor, row, column, tempBoard3)) {
						//Place the stone in this temp board and calculate what would the score look like
						if (tempBoard3.SetStoneAtLocation(row, column, commonStoneColor)) {
							points = CalculateScoreAfterMove(ownStoneColor, row, column, tempBoard3);
							System.out.println("Going through (" + row + ", " + column + ") " + commonStoneColor + " " + points);

							//If this is the best move so far, make a note of it
							if (points > highestScoreDifference) {
								highestScoreDifference = points;
								bestRowForPlacement = row;
								bestColumnForPlacement = column;
								bestStoneForPlacement = commonStoneColor;
							}
						}
					}
				}
			}
		}

		//If the value of bestStone has been changed from default invalid one to something else, we have a move
		if (bestStoneForPlacement != 'x') {
			System.out.println("You should place a " + bestStoneForPlacement + " stone at (" + bestRowForPlacement + ", " + bestColumnForPlacement + ")");
			//Attempt the move and update the score if successful
			if (PlaceAStone(bestStoneForPlacement, bestRowForPlacement, bestColumnForPlacement, a_board)) {
				UpdateScoreAfterMove(m_primaryColor, m_rowOfPreviousPlacement, m_columnOfPreviousPlacement, a_board);

				//Updating these values so that the caller knows what move the computer made
				a_stone = bestStoneForPlacement;
				a_row = bestRowForPlacement;
				a_column = bestColumnForPlacement;
				return true;
			}
		}
		return false;
	}
}

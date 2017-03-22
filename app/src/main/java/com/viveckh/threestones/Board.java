package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/3/2017
*/

/* This class serves as the Game Board. It creates a 11*11 multidimensional array of pouches and initializes
* only those pouches that fall in the Octagonal game board. Once initialized, it sets all the necessary relationship between
* the pouches so that the link can help in the tree formation later.
* The score function calculates and returns the change in score after each stone insertion in the board.
*/

public class Board {
	//Variables
	private int dimension;
	private int startingColumn;
	private int rowLength;

	public Block[][] cell;

	//Default Constructor
	public Board() {
		dimension = 11;
		startingColumn = 4;
		rowLength = 3;

		cell = new Block[dimension][dimension];

		for (int i = 0; i < dimension; i++) {
			for (int j = startingColumn; j < (startingColumn + rowLength); j++) {
				if (i == dimension / 2 && j == dimension / 2) {
					// Leave the center blank
				} else {
					cell[i][j] = new Block(i, j);

					System.out.println("(" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") created!");
				}
			}
			// Building the top half of the board
			if (i < (dimension / 2 - 1)) {
				startingColumn--;
				rowLength += 2;
			}

			// Building the bottom half of the board
			if (i > dimension / 2) {
				startingColumn++;
				rowLength -= 2;
			}
		}

		//Setting up the relationship properties in between the cells
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (cell[i][j] != null) {
					if ((j > 0) && (cell[i][j - 1] != null)) {
						cell[i][j].setLeft(cell[i][j - 1]);
						System.out.println("left of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i][j - 1].getX() + ", " + cell[i][j - 1].getY() + ")");
					}
					if ((j < dimension - 1) && (cell[i][j + 1] != null)) {
						cell[i][j].setRight(cell[i][j + 1]);
						System.out.println("right of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i][j + 1].getX() + ", " + cell[i][j + 1].getY() + ")");
					}
					if ((i > 0) && (cell[i - 1][j] != null)) {
						cell[i][j].setTop(cell[i - 1][j]);
						System.out.println("top of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i - 1][j].getX() + ", " + cell[i - 1][j].getY() + ")");
					}
					if ((i < dimension - 1) && (cell[i + 1][j] != null)) {
						cell[i][j].setBottom(cell[i + 1][j]);
						System.out.println("bottom of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i + 1][j].getX() + ", " + cell[i + 1][j].getY() + ")");
					}
					if ((i > 0) && (j > 0) && (cell[i - 1][j - 1] != null)) {
						cell[i][j].setTopLeft(cell[i - 1][j - 1]);
						System.out.println("topLeft of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i - 1][j - 1].getX() + ", " + cell[i - 1][j - 1].getY() + ")");
					}
					if ((i > 0) && (j < dimension - 1) && (cell[i - 1][j + 1] != null)) {
						cell[i][j].setTopRight(cell[i - 1][j + 1]);
						System.out.println("topRight of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i - 1][j + 1].getX() + ", " + cell[i - 1][j + 1].getY() + ")");
					}
					if ((i < dimension - 1) && (j > 0) && (cell[i + 1][j - 1] != null)) {
						cell[i][j].setBottomLeft(cell[i + 1][j - 1]);
						System.out.println("bottomLeft of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i + 1][j - 1].getX() + ", " + cell[i + 1][j - 1].getY() + ")");
					}
					if ((i < dimension - 1) && (j < dimension - 1) && (cell[i + 1][j + 1] != null)) {
						cell[i][j].setBottomRight(cell[i + 1][j + 1]);
						System.out.println("bottomRight of (" + cell[i][j].getX() + ", " + cell[i][j].getY() + ") = " +
							  "(" + cell[i + 1][j + 1].getX() + ", " + cell[i + 1][j + 1].getY() + ")");
					}
					System.out.println();
				}
			}
		}
	}


	// The score function calculates and returns the change in score after each stone insertion in the board.
	// Considers all twelve possibilities of score increments in every insertion.
	public int score(int x, int y, char stone) {
		int score = 0;

		if (cell[x][y].isLeftFavorable() && cell[x][y].isRightFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getLeft().getStone() == stone || cell[x][y].getRight().getStone() == stone)
				score++;
		}
		if (cell[x][y].isTopFavorable() && cell[x][y].isBottomFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getTop().getStone() == stone || cell[x][y].getBottom().getStone() == stone)
				score++;
		}
		if (cell[x][y].isTopLeftFavorable() && cell[x][y].isBottomRightFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getTopLeft().getStone() == stone || cell[x][y].getBottomRight().getStone() == stone)
				score++;
		}
		if (cell[x][y].isTopRightFavorable() && cell[x][y].isBottomLeftFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getTopRight().getStone() == stone || cell[x][y].getBottomLeft().getStone() == stone)
				score++;
		}


		if (cell[x][y].isLeftFavorable() && cell[x][y].getLeft().isLeftFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getLeft().getStone() == stone || cell[x][y].getLeft().getLeft().getStone() == stone)
				score++;
		}
		if (cell[x][y].isRightFavorable() && cell[x][y].getRight().isRightFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getRight().getStone() == stone || cell[x][y].getRight().getRight().getStone() == stone)
				score++;
		}
		if (cell[x][y].isTopFavorable() && cell[x][y].getTop().isTopFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getTop().getStone() == stone || cell[x][y].getTop().getTop().getStone() == stone)
				score++;
		}
		if (cell[x][y].isBottomFavorable() && cell[x][y].getBottom().isBottomFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getBottom().getStone() == stone || cell[x][y].getBottom().getBottom().getStone() == stone)
				score++;
		}
		if (cell[x][y].isTopLeftFavorable() && cell[x][y].getTopLeft().isTopLeftFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getTopLeft().getStone() == stone || cell[x][y].getTopLeft().getTopLeft().getStone() == stone)
				score++;
		}
		if (cell[x][y].isTopRightFavorable() && cell[x][y].getTopRight().isTopRightFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getTopRight().getStone() == stone || cell[x][y].getTopRight().getTopRight().getStone() == stone)
				score++;
		}
		if (cell[x][y].isBottomLeftFavorable() && cell[x][y].getBottomLeft().isBottomLeftFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getBottomLeft().getStone() == stone || cell[x][y].getBottomLeft().getBottomLeft().getStone() == stone)
				score++;
		}
		if (cell[x][y].isBottomRightFavorable() && cell[x][y].getBottomRight().isBottomRightFavorable()) {
			if (cell[x][y].getStone() == stone || cell[x][y].getBottomRight().getStone() == stone || cell[x][y].getBottomRight().getBottomRight().getStone() == stone)
				score++;
		}
		return score;
	}
}

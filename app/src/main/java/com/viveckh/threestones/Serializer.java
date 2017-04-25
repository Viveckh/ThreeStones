package com.viveckh.threestones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Serializer Class
 * Contains necessary member functions to serialize or restore a tournament to and from a text file.
 * Author: Vivek Pandey
 * Last Modified on: 04/24/2017
 */

public class Serializer {
	//CONSTANTS
	private final int m_DIMENSION = 11;

	//Variable declarations
	private char[][] m_serializedGameBoard;
	private String m_fileName;
	private File m_storageLocation;

	/**
	 * DEFAULT CONSTRUCTOR
	 */
	public Serializer(File a_storageLocation) {
		m_storageLocation = a_storageLocation;
		m_fileName = "LastGame.txt";
		m_serializedGameBoard = new char[m_DIMENSION][m_DIMENSION];

		//initializing all the indexes of the serializedGameBoard multi-dimensional char array
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int column = 0; column < m_DIMENSION; column++) {
				m_serializedGameBoard[row][column] = 'x';
			}
		}
	}

	/**
	 * Writing serialized game state along with tournament history results to file
	 * @param a_fileName The name of file to which the current game state should be written to
	 * @param a_board The game board to be written
	 * @return true if the writing is successful; false if any exceptions arise
	 */
	public boolean WriteToFile(String a_fileName, Board a_board) {
		// Setup the proper location to write the game state to
		m_fileName = a_fileName;
		File dir =  new File (m_storageLocation.getAbsoluteFile().toString());
		dir.mkdirs();
		File file = new File(dir, m_fileName);

		// Update the multidimensional string array for serialization first
		UpdateSerializedBoard(a_board);

		try {
			FileOutputStream outputStream = new FileOutputStream(file);

			// Writing the contents of the Board first
			outputStream.write("Board\n".getBytes());
			for (int row = 0; row < m_DIMENSION; row++) {
				for (int col = 0; col < m_DIMENSION; col++) {
					outputStream.write((m_serializedGameBoard[row][col]+"\t").getBytes());
				}
				outputStream.write("\n".getBytes());
			}

			// Writing the number of wins, scores, available stones, next player, etc
			outputStream.write("\n".getBytes());
			outputStream.write(("computer stone: " +
				  Tournament.GetComputerStone() + "\n").getBytes());
			outputStream.write(("computer score: " +
				  Tournament.GetComputerScore() + "\n").getBytes());
			outputStream.write(("computer wins: " +
				  Tournament.GetComputerWins() + "\n").getBytes());
			outputStream.write(("computer white stones: " +
				  Tournament.GetComputerWhiteStonesCount() + "\n").getBytes());
			outputStream.write(("computer black stones: " +
				  Tournament.GetComputerBlackStonesCount() + "\n").getBytes());
			outputStream.write(("computer clear stones: " +
				  Tournament.GetComputerClearStonesCount() + "\n\n").getBytes());

			outputStream.write(("human stone: " +
				  Tournament.GetHumanStone() + "\n").getBytes());
			outputStream.write(("human score: " +
				  Tournament.GetHumanScore() + "\n").getBytes());
			outputStream.write(("human wins: " +
				  Tournament.GetHumanWins() + "\n").getBytes());
			outputStream.write(("human white stones: " +
				  Tournament.GetHumanWhiteStonesCount() + "\n").getBytes());
			outputStream.write(("human black stones: " +
				  Tournament.GetHumanBlackStonesCount() + "\n").getBytes());
			outputStream.write(("human clear stones: " +
				  Tournament.GetHumanClearStonesCount() + "\n\n").getBytes());

			outputStream.write(("last placement row: " +
				  Tournament.GetRowOfLastPlacement() + "\n").getBytes());
			outputStream.write(("last placement column: " +
				  Tournament.GetColumnOfLastPlacement() + "\n").getBytes());
			outputStream.write(("next player: " +
				  Tournament.GetNextPlayer() + "\n").getBytes());
			outputStream.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * Reads a serialization file, stores tournament and game state into a multidimensional
	 * string array and sets the provided board accordingly
	 * @param a_fileName Name of the file from which the tournament state should be read from
	 * @param a_board The game board that needs to be set based on the contents from file
	 * @return true if the restoration is successful; false otherwise
	 */
	public boolean ReadFromFile(String a_fileName, Board a_board) {
		File dir =  new File (m_storageLocation.getAbsoluteFile().toString());
		File file = new File(dir, a_fileName);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			//Step 1: Reading the line with the "Board" text
			reader.readLine();

			//Step 2: Reading the Board and populating the multidimensional string array
			int row = 0;
			while (((line = reader.readLine()) != null) && (row < m_DIMENSION)) {
				// To get rid of all the leading and trailing spaces in the read string
				line = line.trim();
				//Storing values in a string array, even though they're actually chars
				String temp[] = line.split("\\s+");
				for (int col = 0; col < m_DIMENSION; col++) {
					if (col < temp.length) {
						//Getting the zero index of string since its actually a char
						m_serializedGameBoard[row][col] = temp[col].charAt(0);
					}
				}
				row++;
			}

			//Step 3: Setting the board and reset tournament scores
			SetBoard(a_board);
			Tournament.ResetScores();

			//Step 4: Reading the human and computer stones, scores, wins and the next player
			char computerPrimaryStone = 'b';
			char humanPrimaryStone = 'w';
			int computerScore = 0;
			int humanScore = 0;
			int computerWhiteStones = 15;
			int computerBlackStones = 15;
			int computerClearStones = 6;
			int humanWhiteStones = 15;
			int humanBlackStones = 15;
			int humanClearStones = 6;
			int lastRow = -1;
			int lastColumn = -1;
			String nextPlayer = "human";

			while (((line = reader.readLine()) != null)) {
				// Continue if the line is not empty
				if (!line.trim().isEmpty()) {
					//Parse computer's and human's choice of stone
					if (line.matches("(\\s*)[Cc]omputer(\\s+)[Ss]tone(.*)"))
					{
						if (line.matches("(.*):(.*)[Ww](.*)")) {
							computerPrimaryStone = 'w';
							humanPrimaryStone = 'b';
						}
						else {
							computerPrimaryStone = 'b';
							humanPrimaryStone = 'w';
						}
					}

					//Parse number of available white stones for computer
					if(line.matches("(\\s*)[Cc]omputer(\\s+)[Ww]hite(\\s+)[Ss]tones(.*)"))
					{
						computerWhiteStones = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					//Parse number of available black stones for computer
					if(line.matches("(\\s*)[Cc]omputer(\\s+)[Bb]lack(\\s+)[Ss]tones(.*)"))
					{
						computerBlackStones = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					//Parse number of available clear stones for computer
					if(line.matches("(\\s*)[Cc]omputer(\\s+)[Cc]lear(\\s+)[Ss]tones(.*)"))
					{
						computerClearStones = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					//Parse number of available white stones for human
					if (line.matches("(\\s*)[Hh]uman(\\s+)[Ww]hite(\\s+)[Ss]tones(.*)"))
					{
						humanWhiteStones = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					//Parse number of available black stones for human
					if (line.matches("(\\s*)[Hh]uman(\\s+)[Bb]lack(\\s+)[Ss]tones(.*)"))
					{
						humanBlackStones = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					//Parse number of available clear stones for human
					if (line.matches("(\\s*)[Hh]uman(\\s+)[Cc]lear(\\s+)[Ss]tones(.*)"))
					{
						humanClearStones = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					// Parse computer score in running game
					if (line.matches("(\\s*)[Cc]omputer(\\s+)[Ss]core(.*)"))
					{
						computerScore = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					// Parse human score in running game
					if (line.matches("(\\s*)[Hh]uman(\\s+)[Ss]core(.*)"))
					{
						humanScore = Integer.
							  parseInt(line.replaceAll("[\\D]", ""));
					}

					// Parse number of computer wins
					if (line.matches("(\\s*)[Cc]omputer(\\s+)[Ww]ins(.*)"))
					{
						int botWins = Integer.parseInt(line.replaceAll("[\\D]", ""));
						//assuming the wins are initially set to zero
						Tournament.IncrementComputerWinsBy(botWins);
					}

					// Parse number of human wins
					if (line.matches("(\\s*)[Hh]uman(\\s+)[Ww]ins(.*)"))
					{
						int humanWins = Integer.parseInt(line.replaceAll("[\\D]", ""));
						//assuming the wins are initially set to zero
						Tournament.IncrementHumanWinsBy(humanWins);
					}


					//Parse row of last placement
					if (line.matches("(\\s*)[Ll]ast(\\s+)[Pp]lacement(\\s+)[Rr]ow(.*)"))
					{
						//Pattern gets rid of "Any character except a digit or a '-' "
						lastRow = Integer.parseInt(line.replaceAll("[^\\d-]", ""));
					}

					//Parse column of last placement
					if(line.matches("(\\s*)[Ll]ast(\\s+)[Pp]lacement(\\s+)[Cc]olumn(.*)"))
					{
						//Pattern gets rid of "Any character except a digit or a '-' "
						lastColumn = Integer.parseInt(line.replaceAll("[^\\d-]", ""));
					}

					// Parse the next player
					if (line.matches("(\\s*)[Nn]ext(\\s+)[Pp]layer(.*)"))
					{
						if (line.matches("(.*):(.*)[Cc]omputer(.*)")) {
							nextPlayer = "computer";
						}
						else {
							nextPlayer = "human";
						}
					}
				}
			}

			Tournament.SaveCurrentGameStatus(humanPrimaryStone, computerPrimaryStone,
				  humanWhiteStones, humanBlackStones, humanClearStones,
				  computerWhiteStones, computerBlackStones, computerClearStones,
				  humanScore, computerScore);
			Tournament.SetControls(lastRow, lastColumn, nextPlayer);
			reader.close();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Sets the given board based on the contents of the string array restored by reading file
	 * @param a_board Board that needs to be set using the contents of multidimensional str array
	 */
	private void SetBoard(Board a_board) {
		// Go through every index of the serialized board and update the actual game board
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int col = 0; col < m_DIMENSION; col++) {
				switch (m_serializedGameBoard[row][col])
				{
					case 'x':
						//Since this refers to an uninitialized index in board
						break;
					case 'w':
						//Occupied by White stone
						a_board.SetStoneAtLocation(row, col, 'w');
						break;
					case 'b':
						//Occupied by Black stone
						a_board.SetStoneAtLocation(row, col, 'b');
						break;
					case 'c':
						//Occupied by Clear stone
						a_board.SetStoneAtLocation(row, col, 'c');
						break;
					default:
						//Empty
						a_board.SetStoneAtLocation(row, col, 'n');
						break;
				}
			}
		}
	}

	/**
	 * Stores the game state in a multidimensional string array.
	 * @param a_board Board whose contents are to be stored in the class's multidimensional array
	 */
	private void UpdateSerializedBoard(Board a_board) {
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int col = 0; col < m_DIMENSION; col++) {
				if (a_board.GetBlockAtLocation(row, col) != null) {
					m_serializedGameBoard[row][col] =a_board.GetStoneAtLocation(row, col);
				}
			}
		}
	}
}
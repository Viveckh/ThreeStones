package com.viveckh.threestones;

import android.content.Context;
import android.os.Environment;

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

	private int m_DIMENSION = 11;
	//Declaring variables
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

		//initialize all the indexes of the serializedGameBoard multi-dimensional char array
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
		File writeToDir =  new File (m_storageLocation.getAbsoluteFile().toString());
		writeToDir.mkdirs();
		File file = new File(writeToDir, m_fileName);

		// Update the multidimensional string array for serialization first
		UpdateSerializedBoard(a_board);

		try {
			FileOutputStream outputStream = new FileOutputStream(file);

			// Writing the board first
			outputStream.write("Board\n".getBytes());
			for (int row = 0; row < m_DIMENSION; row++) {
				for (int col = 0; col < m_DIMENSION; col++) {
					outputStream.write((m_serializedGameBoard[row][col] + "\t").getBytes());
				}
				outputStream.write("\n".getBytes());
			}

			// Writing the number of wins and next Player
			outputStream.write("\n".getBytes());
			outputStream.write(("Computer Score: " + Tournament.GetComputerScore() + "\n").getBytes());
			outputStream.write(("Human Score: " + Tournament.GetHumanScore() + "\n").getBytes());
			//outputStream.write(("Next Player: " + Tournament.GetNextPlayer() + "\n").getBytes());
			outputStream.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * Stores the game state in a multidimensional string array.
	 * @param a_board The game board whose contents are to be stored into the class's multidimensional array for writing-to-file purposes
	 */
	private void UpdateSerializedBoard(Board a_board) {
		for (int row = 0; row < m_DIMENSION; row++) {
			for (int col = 0; col < m_DIMENSION; col++) {
				if (a_board.GetBlockAtLocation(row, col) != null) {
					m_serializedGameBoard[row][col] = a_board.GetStoneAtLocation(row, col);
				}
			}
		}
	}


}
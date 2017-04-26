package com.viveckh.threestones;

import java.io.Serializable;

/**
 * <h1>Block Model Class</h1>
 * It holds the properties associated with an individual pouch within the bigger game board.
 * Properties like initialization state, X-Y Coordinate, occupied/occupied state, contained stone
 * Can be occupied by one of the stones: 'n' for none, 'w' for white, 'b' for black, 'c' for clear.
 *
 * @author Vivek Pandey
 * @since 2017-04-25
 */
public class Block implements Serializable {

	// Variable Declarations
	private int m_initialized;
	private int m_xAxis, m_yAxis;
	private boolean m_occupied;
	private char m_stone;    // w for white, b for black, c for clear, n for none

	/**
	 * Block Constructor,
	 * Initializes the object, assigns the passed coordinates, sets occupancy false
	 *
	 * @param a_xAxis X coordinate of the block within a board
	 * @param a_yAxis Y coordinate of the block within a board
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public Block(int a_xAxis, int a_yAxis) {
		m_initialized = 1;
		m_xAxis = a_xAxis;
		m_yAxis = a_yAxis;
		m_occupied = false;
		m_stone = 'n';  // default stone to 'n' i.e. none
	}

	/**
	 * Block Copy Constructor,
	 * Initializes a new object as a copy of another block
	 *
	 * @param a_block Block to be copied from
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public Block(Block a_block) {
	  /*Handling null cases so that issues don't come up while trying to copy construct
	   Block using null object references*/
		if (a_block != null) {
			//Copying the property values from passed block object
			m_initialized = a_block.m_initialized;
			m_xAxis = a_block.m_xAxis;
			m_yAxis = a_block.m_yAxis;
			m_occupied = a_block.m_occupied;
			m_stone = a_block.m_stone;
		}
	}

	/**
	 * Returns the X coordinate of the Block
	 *
	 * @return X coordinate of the Block
	 */
	public int GetX() {
		return m_xAxis;
	}

	/**
	 * Returns the Y coordinate of the Block
	 *
	 * @return Y coordinate of the Block
	 */
	public int GetY() {
		return m_yAxis;
	}

	/**
	 * Returns the Initialized state of the Block
	 *
	 * @return true if Initialized, false if uninitialized
	 */
	public boolean IsInitialized() {
		if (m_initialized == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the occupancy status of the Block
	 *
	 * @return true if occupied, false if unoccupied
	 */
	public boolean IsOccupied() {
		return m_occupied;
	}

	/**
	 * Returns the stone in the Block
	 *
	 * @return a character representing the stone present in the Block
	 */
	public char GetStone() {
		return m_stone;
	}

	/**
	 * SetStone() -Sets the occupying stone and alters occupancy status of Block after validations
	 *
	 * @param a_stone A character representing the stone to set. n, w, b, c are valid ones
	 * @return true if the stone is successfully set, false if a failure
	 * @author Vivek Pandey
	 * @since 2017-04-25
	 */
	public boolean SetStone(char a_stone) {
		//Validate that a valid stone is passed as parameter
		if (a_stone == 'w' || a_stone == 'b' || a_stone == 'c' || a_stone == 'n') {
			m_stone = a_stone;
			//If the block is being set to no stone, the occupied state would be false
			if (m_stone == 'n') {
				m_occupied = false;
			} else {
				m_occupied = true;
			}
			return true;
		}
		return false;
	}
}
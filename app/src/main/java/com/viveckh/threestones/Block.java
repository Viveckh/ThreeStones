package com.viveckh.threestones;

/* Name: Vivek Pandey
 * Date: 3/3/2017
*/
/* This class serves as a pouch in the game Board. It keeps a track of
* A) X-Y Coordinate of the pouch in the board
* B) Occupied/Unoccupied state of the pouch, stone contained (n(none), w(white), b(black), c(clear))
* C) Its surrounding pouches - left, right, top, bottom, topLeft, topRight, bottomLeft, bottomRight.
*/

import java.io.Serializable;

public class Block implements Serializable {

    // Variables
    private int m_initialized;
    private int m_xAxis, m_yAxis;
    private boolean m_occupied;
    private char m_stone;    // w for white, b for black, c for clear, n for none

    // Constructor
    // assigns the passed coordinates, sets occupied state to false.
    // Sets the default stone to 'n' i.e. none
    public Block(int a_xAxis, int a_yAxis) {
        m_initialized = 1;
        m_xAxis = a_xAxis;
        m_yAxis = a_yAxis;
        m_occupied = false;
        m_stone = 'n';
    }

    //Copy Constructor
    public Block(Block a_block) {
        //Copying the variables from passed block object
        //Handling null cases so that issues don't come up while trying to copy construct Block using null object references
        if (a_block != null) {
            m_initialized = a_block.m_initialized;
            m_xAxis = a_block.m_xAxis;
            m_yAxis = a_block.m_yAxis;
            m_occupied = a_block.m_occupied;
            m_stone = a_block.m_stone;
        }
    }

    //Co-ordinate getters
    public int GetX() {
        return m_xAxis;
    }

    public int GetY() {
        return m_yAxis;
    }

    public boolean IsInitialized() {
        if (m_initialized == 1) {
            return true;
        }
        return false;
    }

    public boolean IsOccupied() {
        return m_occupied;
    }

    public char GetStone() {
        return m_stone;
    }

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

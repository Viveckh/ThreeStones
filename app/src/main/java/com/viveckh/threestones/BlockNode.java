package com.viveckh.threestones;

public class BlockNode {
	private Block m_block;
	private int m_row;
	private int m_column;
	private int m_heuristicVal;

	public BlockNode() {
		m_block = null;
		m_row = 0;
		m_column = 0;
		m_heuristicVal = 0;
	}

	public BlockNode(Block a_block, int a_row, int a_column, int a_heuristicVal) {
		m_block = a_block;
		m_row = a_row;
		m_column = a_column;
		m_heuristicVal = a_heuristicVal;
	}

	public Block GetBlock() {
		return m_block;
	}

	public int GetRow() {
		return m_row;
	}

	public int GetColumn() {
		return m_column;
	}

	public boolean IsEmpty() {
		return m_block == null;
	}

	public void SetHeuristicVal(int a_val) {
		m_heuristicVal = a_val;
	}

	public int GetHeuristicVal () {
		return m_heuristicVal;
	}
}

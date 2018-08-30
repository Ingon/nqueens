package org.ingon.nqueens;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Board {
	protected final Cell[][] cells;
	protected final Deque<Cell> placed = new LinkedList<>();
	
	public Board(int n) {
		this.cells = new Cell[n][n];
		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}
	}
	
	public void solve() {
		placeQueenOnRow(0);
	}
	
	private void boardConfigurationFound() {
		System.out.println(this);
	}
	
	private void placeQueenOnRow(int row) {
		if (row >= size()) {
			boardConfigurationFound();
			return;
		}
		placeQueenOnRow(row, this::placeQueenOnRow);
	}
	
	protected void placeQueenOnRow(int row, Consumer<Integer> nextStep) {
		for (int col = 0, sz = size(); col < sz; col++) {
			if (cells[row][col].isThreatened()) {
				continue;
			}

			placeQueen(row, col);
			nextStep.accept(row + 1);
			unplaceQueen(row, col);
		}
	}
	
	private void placeQueen(int row, int col) {
		cells[row][col].place();
		
		markThreats(row, col);
		markLines(row, col);
	}
	
	private void unplaceQueen(int row, int col) {
		unmarkLines();
		unmarkThreats(row, col);

		cells[row][col].unplace();
	}
	
	private void markThreats(int row, int col) {
		applyThreats(row, col, cell -> cell.threaten());
	}

	private void unmarkThreats(int row, int col) {
		applyThreats(row, col, cell -> cell.unthreaten());
	}
	
	protected void applyThreats(int row, int col, Consumer<Cell> changeFn) {
		for (int d = 1, nrow = row + 1, sz = size(); nrow < sz; d++, nrow++) {
			if (col - d >= 0) {
				changeFn.accept(cells[nrow][col - d]);
			}
			changeFn.accept(cells[nrow][col]);
			if (col + d < sz) {
				changeFn.accept(cells[nrow][col + d]);
			}
		}
	}

	private void markLines(int row, int col) {
		Cell comingCell = cells[row][col];
		applyLinesFormedWith(comingCell, cell -> cell.lineThreaten());
		placed.push(comingCell);
	}
	
	private void unmarkLines() {
		Cell leavingCell = placed.pop();
		applyLinesFormedWith(leavingCell, cell -> cell.lineUnthreaten());
	}
	
	protected void applyLinesFormedWith(Cell changingCell, Consumer<Cell> changeFn) {
		for (Cell cell : placed) {
			int drow = changingCell.row - cell.row;
			int dcol = changingCell.col - cell.col;
			
			int nextRow = changingCell.row + drow;
			int nextCol = changingCell.col + dcol;
			while (nextRow >= 0 && nextRow < size() && nextCol >= 0 && nextCol < size()) {
				changeFn.accept(cells[nextRow][nextCol]);
				
				nextRow += drow;
				nextCol += dcol;
			}
		}
	}

	public int size() {
		return cells.length;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells.length; col++) {
				sb.append(cells[row][col]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}

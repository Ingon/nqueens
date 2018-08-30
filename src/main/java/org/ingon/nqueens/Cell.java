package org.ingon.nqueens;

public class Cell {
	public final int row;
	public final int col;

	private boolean hasQueen = false;
	private int numberOfThreats = 0;
	private int lineThreats = 0;
	
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public void place() {
		hasQueen = true;
	}

	public boolean hasQueen() {
		return hasQueen;
	}

	public void unplace() {
		hasQueen = false;
	}

	public void threaten() {
		numberOfThreats++;
	}
	
	public boolean isThreatened() {
		return numberOfThreats > 0;
	}

	public void unthreaten() {
		numberOfThreats--;
	}

	public void lineThreaten() {
		threaten();
		lineThreats++;
	}

	public void lineUnthreaten() {
		unthreaten();
		lineThreats--;
	}

	@Override
	public String toString() {
		if (hasQueen) {
			return "Q";
		}
		if (numberOfThreats > 0) {
			if (lineThreats > 0) {
				return "L";
			}
			return "x";
		}
		return ".";
	}
}

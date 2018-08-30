package org.ingon.nqueens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class BoardTests {
	@Test
	public void testApplyThreats() {
		Board board = new Board(5);
		
		Set<Cell> touched = new HashSet<>();
		board.applyThreats(0, 2, cell -> assertTrue(touched.add(cell)));
		
		assertEquals(8, touched.size());

		assertContainsCell(touched, 1, 1);
		assertContainsCell(touched, 1, 2);
		assertContainsCell(touched, 1, 3);

		assertContainsCell(touched, 2, 0);
		assertContainsCell(touched, 2, 2);
		assertContainsCell(touched, 2, 4);

		assertContainsCell(touched, 3, 2);
		assertContainsCell(touched, 4, 2);
	}
	
	@Test
	public void testLinesDiagonaly() {
		Board board = new Board(5);
		
		board.placed.push(board.cells[0][4]);
		Set<Cell> touched = new HashSet<>();
		board.applyLinesFormedWith(board.cells[1][3], cell -> assertTrue(touched.add(cell)));

		assertEquals(3, touched.size());

		assertContainsCell(touched, 2, 2);
		assertContainsCell(touched, 3, 1);
		assertContainsCell(touched, 4, 0);
	}

	@Test
	public void testLinesSloped() {
		Board board = new Board(5);
		
		board.placed.push(board.cells[0][4]);
		Set<Cell> touched = new HashSet<>();
		board.applyLinesFormedWith(board.cells[1][2], cell -> assertTrue(touched.add(cell)));

		assertEquals(1, touched.size());

		assertContainsCell(touched, 2, 0);
	}
	
	@Test
	public void testPlaceOnRow() {
		Board board = new Board(5);
		
		AtomicBoolean alreadyChecked = new AtomicBoolean(false);
		board.placeQueenOnRow(0, row -> { 
			if (alreadyChecked.get()) {
				return;
			}
			alreadyChecked.set(true);

			assertEquals(1, (int) row); 
			
			assertTrue(board.cells[0][0].hasQueen());
			for (int irow = 1; irow < 5; irow++) {
				assertTrue(board.cells[irow][0].isThreatened());
				assertTrue(board.cells[irow][irow].isThreatened());
			}
		});
		
		for(int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				assertFalse(board.cells[row][col].hasQueen());
				assertFalse(board.cells[row][col].isThreatened());
			}
		}
	}
	
	private void assertContainsCell(Set<Cell> cells, int row, int col) {
		assertTrue(cells.stream().anyMatch(cell -> cell.row == row && cell.col == col));
	}
}

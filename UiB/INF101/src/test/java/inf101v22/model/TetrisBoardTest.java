package inf101v22.model;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf101v22.grid.Coordinate;
import inf101v22.model.piece.PieceShape;
import inf101v22.model.piece.PositionedPieceFactory;

class TetrisBoardTest {
	
	TetrisBoard board;
	TetrisModel model;
	Tile tile;
	PositionedPieceFactory factory;
	List<PieceShape> shapes;

	@BeforeEach
	void setUp() throws Exception {
		shapes = new ArrayList<PieceShape>();
		factory = new PositionedPieceFactory(shapes);
		tile = new Tile(null, '0');
		model = new TetrisModel(factory);
	}

	
	@Test
	void testBoardSize() {
		assertEquals(15, model.getNumRows());
		assertEquals(10, model.getNumCols());
		
	}
	
	
	/**
	 * Check whether the tiles are placed correctly
	 */
	@Test
	void testTilePlacement() {
		model.board.set(new Coordinate(0, 0), new Tile(Color.BLUE, 'b'));
		model.board.set(new Coordinate(0, 9), new Tile(Color.CYAN, 'c'));
		model.board.set(new Coordinate(14, 0), new Tile(Color.GREEN, 'g'));
		model.board.set(new Coordinate(14, 9), new Tile(Color.YELLOW, 'y'));
		
		char[][] testArray = model.board.toCharArray2d();
		String visualRepresentation = model.board.charArray2dToString(testArray);
		System.out.println("tilePlacement: \n" + visualRepresentation + "\n\n");
		assertEquals('b', testArray[0][0]);
		assertEquals('c', testArray[0][9]);
		assertEquals('g', testArray[14][0]);
		assertEquals('y', testArray[14][9]);
		
	
		
	
	}
	
	/**
	 * Check whether the falling piece is placed correctly on the board
	 */
	@Test
	void testPiecePlacement() {
		char[][] testArray = model.pieceToCharArray2d();
		String visualRepresentation = model.charArray2dToString(testArray);
		
		
	}
	
	@Test
	void testCopyElementsToNewRow() {
		model.board.set(new Coordinate(0, 0), new Tile(Color.BLUE, 'b'));
		model.board.set(new Coordinate(0, 9), new Tile(Color.CYAN, 'c'));
		model.board.copyElementsToNewRow(0, 1);
		char[][] testArray = model.board.toCharArray2d();
		String visualRepresentation = model.board.charArray2dToString(testArray);
		System.out.println("copyTest: \n" + visualRepresentation + "\n\n");
		assertEquals('b', testArray[0][0]);
		assertEquals('c', testArray[0][9]);
		assertEquals('b', testArray[1][0]);
		assertEquals('c', testArray[1][9]);
	}
	
	@Test
	void testIfRowFull() {
		for (int i = 0; i < model.getNumCols(); i++) {
			model.board.set(new Coordinate(0, i), new Tile(Color.BLUE, 'b'));
		}
		for (int i = 4; i < model.getNumCols(); i++) {
			model.board.set(new Coordinate(4, i), new Tile(Color.BLUE, 'x'));
		}
		for (int i = 0; i < model.getNumCols(); i++) {
			model.board.set(new Coordinate(14, i), new Tile(Color.BLUE, 'k'));
		}
		assertTrue(model.board.ifRowFull(0));
		assertFalse(model.board.ifRowFull(4));
		assertTrue(model.board.ifRowFull(14));
		
		char[][] testArray = model.board.toCharArray2d();
		String visualRepresentation = model.board.charArray2dToString(testArray);
		System.out.println("checkFullRow: \n" + visualRepresentation);
	}
	
	@Test
	void testFillRow() {
		model.board.fillRow(new Tile(Color.BLACK, 'k'), 0);
		char[][] testArray = model.board.toCharArray2d();
		
		for (int i = 0; i < model.getNumCols(); i++) {
			assertEquals('k', testArray[0][i]);
		}
		
		String visualRepresentation = model.board.charArray2dToString(testArray);
		System.out.println("fillRowTest: \n" + visualRepresentation + "\n\n");
	}

	@Test
	void testRemoveRows() {
		model.board.set(new Coordinate(0, 9), new Tile(Color.BLACK, 'b'));
		model.board.set(new Coordinate(0, 0), new Tile(Color.BLACK, 'a'));
		model.board.fillRow(new Tile(Color.BLACK, 'k'), 7);
		
		char[][] testArray = model.board.toCharArray2d();
		String visualRepresentation = model.board.charArray2dToString(testArray);
		System.out.println("Before removing row: \n" + visualRepresentation + "\n\n");
		
		assertEquals(1, model.board.removeFullRows());
		
		char[][] testArray2 = model.board.toCharArray2d();
		String visualRepresentation2 = model.board.charArray2dToString(testArray2);
		System.out.println("After removing row: \n" + visualRepresentation2 + "\n\n");
	}

}

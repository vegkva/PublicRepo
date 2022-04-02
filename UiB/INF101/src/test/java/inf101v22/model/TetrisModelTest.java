package inf101v22.model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf101v22.grid.Coordinate;
import inf101v22.model.piece.PieceShape;
import inf101v22.model.piece.PositionedPiece;
import inf101v22.model.piece.PositionedPieceFactory;

class TetrisModelTest {

	TetrisBoard board;
	TetrisModel model;
	Tile tile;
	PositionedPieceFactory factory;
	ArrayList<PieceShape> shapes;
	PositionedPiece testPiece;
	
	
	@BeforeEach
	void setUp() throws Exception {
		shapes = new ArrayList<PieceShape>();
		factory = new PositionedPieceFactory(shapes);
		tile = new Tile(null, '0');
		model = new TetrisModel(factory);
	}
	
	@Test
	void testFallingPiece() {
		
		char[][] testArray = model.pieceToCharArray2d();
		String visualRepresentation = model.charArray2dToString(testArray);
		System.out.println(visualRepresentation + "\n\n");
		model.moveFallingPiece(1, 0);
		char[][] testArray2 = model.pieceToCharArray2d();
		String visualRepresentation2 = model.charArray2dToString(testArray2);
		System.out.println(visualRepresentation2);
		
		assertEquals("----------\n"
				   + "-----ttt--\n"
				   + "------t---\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------", model.charArray2dToString(testArray2));
		
		assertFalse(model.moveFallingPiece(15, 0));
		
	}
	
	@Test
	void testRotatePieceRight() {
		
		char[][] testArray = model.pieceToCharArray2d();
		String visualRepresentation = model.charArray2dToString(testArray);
		System.out.println(visualRepresentation + "\n\n");
		
		model.rotateFallingPiece("right");
		char[][] testArray2 = model.pieceToCharArray2d();
		String visualRepresentation2 = model.charArray2dToString(testArray2);
		System.out.println(visualRepresentation2);
		
		assertEquals("------t---\n"
				   + "-----tt---\n"
				   + "------t---\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------", model.charArray2dToString(testArray2));
	}

	@Test
	void testRotatePieceLeft() {
		
		char[][] testArray = model.pieceToCharArray2d();
		String visualRepresentation = model.charArray2dToString(testArray);
		System.out.println(visualRepresentation + "\n\n");
		
		model.rotateFallingPiece("left");
		char[][] testArray2 = model.pieceToCharArray2d();
		String visualRepresentation2 = model.charArray2dToString(testArray2);
		System.out.println(visualRepresentation2);
		
		assertEquals("-----t----\n"
				   + "-----tt---\n"
				   + "-----t----\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------\n"
				   + "----------", model.charArray2dToString(testArray2));
	}
	

	
	
	
	
}











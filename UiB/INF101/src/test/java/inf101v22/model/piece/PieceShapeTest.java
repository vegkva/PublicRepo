package inf101v22.model.piece;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class PieceShapeTest {

	
	PieceShape t = PieceShape.T;
	PieceShape s = PieceShape.S;
	PieceShape z = PieceShape.Z;
	PieceShape i = PieceShape.I;
	PieceShape j = PieceShape.J;
	PieceShape l = PieceShape.L;
	PieceShape o = PieceShape.O;
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	void testTileCharacter() {
		assertEquals('t', t.getTile().getCharacter());
		assertEquals('s', s.getTile().getCharacter());
		assertEquals('z', z.getTile().getCharacter());
		assertEquals('i', i.getTile().getCharacter());
		assertEquals('j', j.getTile().getCharacter());
		assertEquals('l', l.getTile().getCharacter());
		assertEquals('o', o.getTile().getCharacter());
	}
	
	@Test
	void Test() {
		for (PieceShape shape : PieceShape.STANDARD_TETRIS_PIECES) {
			System.out.println(shape.getTile().getCharacter());
		}
	}
	
	@Test
	void testPieceHeight() {
		assertEquals(2, t.getHeight());
		assertEquals(1, i.getHeight());
	}
	
	@Test
	void testPieceWidth() {
		assertEquals(2, o.getWidth());
		assertEquals(3, j.getWidth());
		assertEquals(4, i.getWidth());
	}
	
	@Test
	void testRotate() {
		
		Boolean[][] rotateLRight = new Boolean[][] {
			{true, false},
			{true, false},
			{true, true},
		};
		PieceShape rotatedLRight = l.getRotatedPiece(l, "right");
		assertTrue(Arrays.deepEquals(rotateLRight, rotatedLRight.shape));
		
		
		
		Boolean[][] rotateLLeft = new Boolean[][] {
			{true, true},
			{false, true},
			{false, true},
		};
		PieceShape rotatedLLeft = l.getRotatedPiece(l, "left");
		assertTrue(Arrays.deepEquals(rotateLLeft, rotatedLLeft.shape));
		
		
		
		Boolean[][] rotateI = new Boolean[][] {
			{true},
			{true},
			{true},
			{true},
		};
		
		PieceShape rotatedI = i.getRotatedPiece(i, "right");
		assertTrue(Arrays.deepEquals(rotateI, rotatedI.shape));
		
	
	}

	
}





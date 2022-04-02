package inf101v22.model.piece;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionedPieceFactoryTest {

	PositionedPieceFactory factory;
	PositionedPiece testPiece;
	List<PieceShape> shapes;
	
	@BeforeEach
	void setUp() throws Exception {
		shapes = new ArrayList<>();
		factory = new PositionedPieceFactory(shapes);
		for (PieceShape shape : shapes) {
			System.out.println(shape.tile.character);
		}
	}

	@Test
	void testGetNextPositionedPiece() {
		testPiece = factory.getNextActivePiece();
		assertEquals('t', testPiece.getTile().character);
		testPiece = factory.getNextActivePiece();
		assertEquals('s', testPiece.getTile().character);
	}

}

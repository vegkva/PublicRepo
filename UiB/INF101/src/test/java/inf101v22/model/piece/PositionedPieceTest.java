package inf101v22.model.piece;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf101v22.grid.Coordinate;
import inf101v22.grid.CoordinateItem;
import inf101v22.model.Tile;

class PositionedPieceTest {
	
	PositionedPiece pPiece;
	PieceShape shape;
	Coordinate coordinate;

	@BeforeEach
	void setUp() throws Exception {
		coordinate = new Coordinate(2, 1);
		shape = PieceShape.J;
		pPiece = new PositionedPiece(shape, coordinate);
	}

	
	 @Test
    void testIterator() {

        List<String> items = new ArrayList<>();
        for (CoordinateItem<Tile> coordinateItem : pPiece) {
            items.add(coordinateItem.coordinate.toString());
        }
        
        assertEquals(coordinate.toString(), items.get(0));
    }

}

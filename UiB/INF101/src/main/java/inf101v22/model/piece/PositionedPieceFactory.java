package inf101v22.model.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import inf101v22.grid.Coordinate;

public class PositionedPieceFactory {

	private int centerCol;
	private PieceShape shape;
	private Random rand = new Random();
	
	List<PieceShape> testList;
	
	public PositionedPieceFactory() {
	}
	
	public PositionedPieceFactory(List<PieceShape> shapes) {
		this.testList = new ArrayList<PieceShape>();
		this.testList = shapes;
		for (int i = 0; i < 7; i++) {
			this.testList.add(shape.STANDARD_TETRIS_PIECES[i]);
		}
	}
	
	public void setCenterColumn(int value) {
		this.centerCol = value;
	}
	
	/**
	 * For test purposes, this method will return the next piece in the list.
	 * @return a new random active piece on top of the board
	 */
	public PositionedPiece getNextActivePiece() {
		shape = PieceShape.STANDARD_TETRIS_PIECES[rand.nextInt(7)];
		if (testList != null && testList.size() > 0) {
			shape = testList.iterator().next();
			testList.remove(0);
		}
		return new PositionedPiece(shape, new Coordinate(0, this.centerCol));
		
	}
}

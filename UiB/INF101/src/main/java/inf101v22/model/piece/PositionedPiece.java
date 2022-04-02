package inf101v22.model.piece;

import java.util.ArrayList;
import java.util.Iterator;

import inf101v22.grid.Coordinate;
import inf101v22.grid.CoordinateItem;
import inf101v22.model.Tile;

public class PositionedPiece implements Iterable<CoordinateItem<Tile>> {

	private PieceShape shape;
	private Coordinate coordinate;
	
	public PositionedPiece(PieceShape shape, Coordinate coordinate) {
		this.shape = shape;
		this.coordinate = coordinate;
	}
	
	public PositionedPiece getPiece() {
		return this;
	}
	
	public int getWidth() {
		return shape.getWidth();
	}
	
	
	public int getHeight() {
		return shape.getHeight();
	}
	
	public Tile getTile() {
		return shape.getTile();
	}
	
	public Coordinate getCoordinate() {
		return this.coordinate;
	}
	
	public PieceShape getShape() {
		return this.shape;
	}
	
	/**
	 * Method to return a copy of the active piece. Is used when moving an active piece,
	 * and when creating the shadow of the current active piece
	 * @param deltaRow number of rows added to the current row position
	 * @param deltaCol number of columns added to the current column position
	 * @return a new active piece with new coordinates
	 */
	public PositionedPiece getCopy(int deltaRow, int deltaCol) {
		
		return new PositionedPiece(shape, new Coordinate(coordinate.row + deltaRow, coordinate.col + deltaCol));
		
	}
	
	/**
	 * Method to create a rotated active piece. deltaRow and deltaCol can be used to modify the position 
	 * of the new rotated piece compared to the original
	 * 
	 * @param piece piece to rotate
	 * @param deltaRow number of rows added to the current row position
	 * @param deltaCol number of columns added to the current column position
	 * @param dir the direction of rotation
	 * @return a new active rotated piece
	 */
	public PositionedPiece getRotatedCopy(PositionedPiece piece, int deltaRow, int deltaCol, String dir) {
		PieceShape copy = piece.shape.getRotatedPiece(shape, dir);

		
		return new PositionedPiece(copy, new Coordinate(coordinate.row + deltaRow, coordinate.col + deltaCol));
	}
	
	
	@Override
	public Iterator<CoordinateItem<Tile>> iterator() {
		ArrayList<CoordinateItem<Tile>> coordinateItem = new ArrayList<CoordinateItem<Tile>>();
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
				if (this.shape.getShape()[row][col]) {
					Coordinate coordinate = new Coordinate(this.coordinate.row+row, this.coordinate.col+col);
					CoordinateItem<Tile> item = new CoordinateItem<Tile>(coordinate, getTile());
					coordinateItem.add(item);
				}
			}
		}
		return coordinateItem.iterator();
	}

}

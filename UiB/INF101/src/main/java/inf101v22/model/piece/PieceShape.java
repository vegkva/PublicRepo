package inf101v22.model.piece;

import java.awt.Color;

import inf101v22.model.Tile;

public class PieceShape {

	public final Tile tile;
	public final Boolean[][] shape;
	
	// T
	static final PieceShape T = new PieceShape(
			 new Tile(Color.GREEN, 't'), new Boolean[][] {
					{true, true, true},
					{false, true, false},
			});
	
	// S
	static final PieceShape S = new PieceShape(
			new Tile(Color.YELLOW, 's'), new Boolean[][] {
					{false, true, true},
					{true, true, false},
			});
	
	// Z
	static final PieceShape Z = new PieceShape(
			new Tile(Color.CYAN, 'z'), new Boolean[][] {
					{true, true, false},
					{false, true, true},
			});
	
		
	// I
	static final PieceShape I = new PieceShape(
			new Tile(Color.RED, 'i'), new Boolean[][] {
					{true, true, true, true},
			});
	
	// J
	static final PieceShape J = new PieceShape(
			new Tile(Color.MAGENTA, 'j'), new Boolean[][] {
					{true, false, false},
					{true, true, true},
			});
	
	// L
	static final PieceShape L = new PieceShape(
			new Tile(Color.ORANGE, 'l'), new Boolean[][] {
					{false, false, true},
					{true, true, true},
			});
	
	// O
	static final PieceShape O = new PieceShape(
			new Tile(new Color(123,104,255), 'o'), new Boolean[][] {
					{true, true},
					{true, true},
			});
	
	
	
	static final PieceShape[] STANDARD_TETRIS_PIECES = { T, S, Z, I, J, L, O };
	
	
	private PieceShape(Tile tile, Boolean[][] shape) {
		this.tile = tile;
		this.shape = shape;
	}
	
	public Boolean[][] getShape() {
		return this.shape;
	}
	
	public int getWidth() {
		return this.shape[0].length;
	}
	
	public int getHeight() {
		return this.shape.length;
	}
	
	public Tile getTile() {
		return this.tile;
	}
	
	
	/**
	 * Method to rotate an active piece. 
	 * 
	 * @param shape the shape to be rotated
	 * @param dir which direction the piece is being rotated
	 * @return a rotated piece
	 */
	public PieceShape getRotatedPiece(PieceShape shape, String dir) {
		int width = shape.shape[0].length;
		int height = shape.shape.length;
		Boolean[][] oldShape = shape.shape;
		
		Boolean[][] rotated = new Boolean[width][height];
		if (dir.equals("right")) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					rotated[i][j] = oldShape[height - j - 1][i]; 
				}
			}
		} else {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					rotated[i][j] = oldShape[j][width - i - 1]; 
					
				}
			}
		}
		
		return new PieceShape(tile, rotated);
		
	}
	
	
}

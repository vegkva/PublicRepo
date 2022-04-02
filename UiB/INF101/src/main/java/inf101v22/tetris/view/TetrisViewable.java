package inf101v22.tetris.view;

import inf101v22.grid.CoordinateItem;
import inf101v22.model.Tile;
import inf101v22.model.piece.PositionedPiece;
import inf101v22.tetris.model.GameScreen;
import inf101v22.tetris.model.GameState;

public interface TetrisViewable {

	public int getNumRows();
	
	public int getNumCols();
	
	/**
	 * 
	 * @return an iterable that iterates over all tiles on the board
	 */
	public Iterable<CoordinateItem<Tile>> tilesOnTheBoard();
	
	
	/**
	 * 
	 * @return an iterable that iterates over tiles of the "active" piece
	 * which is not yet placed on the board
	 */
	public Iterable<CoordinateItem<Tile>> activePiece();
	
	/**
	 * Copies the original positioned piece so that the view can 
	 * draw the shadow
	 */
	public Iterable<CoordinateItem<Tile>> shadowPiece();
	
	public GameScreen getGameScreen();
	
	public GameState getGameState();
	
	public int getScore();
	
	public int getHighScore();
	
	/**
	 * TetrisView uses this method to fetch and draw the next piece
	 *
	 * @return the piece coming next
	 */
	public PositionedPiece getUpcomingPiece();

	
	
	
	
}

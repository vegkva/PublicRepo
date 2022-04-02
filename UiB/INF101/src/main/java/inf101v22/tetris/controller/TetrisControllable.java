package inf101v22.tetris.controller;

import java.util.ArrayList;
import java.util.HashMap;
import inf101v22.model.piece.PositionedPiece;
import inf101v22.tetris.model.GameScreen;
import inf101v22.tetris.model.GameState;

public interface TetrisControllable {

	/**
	 * Moves a piece. If the move was valid, the "active" piece
	 * is replaced with the new moved piece. 
	 * @param deltaRow how many rows the piece is moved
	 * @param deltaCol how many columns the piece is moved
	 * @return true if the move was valid
	 */
	public boolean moveFallingPiece(int deltaRow, int deltaCol);
	
	
	
	/**
	 * This method will rotate the falling piece.
	 * If the rotated piece's centre is not equal to the original one, the piece is moved accordingly
	 * @param dir The direction of rotation
	 */
	public boolean rotateFallingPiece(String dir);
	
	
	/**
	 * Drops the active piece as far as possible down the board. When no more moves
	 * are possible, the active piece is placed on the board.
	 * Before getting the next piece, we are checking for full rows
	 */
	public void dropFallingPiece();
	
	public GameScreen getGameScreen();
	
	public GameState getGameState();
	
	
	/**
	 * Based on the total amount of pieces placed on the board, a delay is
	 * computed. Rows removed will not affect this number.
	 * @return an updated timer delay
	 */
	public int calculateStep();

	
	/**
	 * This does the same as {@link #dropFallingPiece()}, but only 
	 * every clock tick (timer).
	 */
	public void clockTick();

	
	
	/**
	 * Until filledRows is empty, {@link #removeTileAnimation()} is called.
	 * Then the score is set based on how many rows that were removed.  
	 * 
	 * If multiplayer is played, then the boolean sendRowsToEnemy is set to true.
	 */
	public void doVisualEffect();
	
	
	
	
	
	
	
	public void setGameState(GameState state);
	
	/**
	 * Sets the tiles to be black and resets all relevant variables
	 * Sets the GameScreen to ACTIVE_GAME
	 */
	public void resetGame();

	public void setGameScreen(GameScreen activeGame);
	
	
	/**
	 * When a client receives rows from the server, this method is called with the received rows as parameter.
	 * @param enemyRows the rows to be inserted
	 */
	public void insertRows(HashMap<Integer, ArrayList<Character>> enemyRows);
	
	public HashMap<Integer, ArrayList<Character>> getEnemyRows();
	
	/**
	 * Resets the list of rows that should be sent to the other client
	 */
	public void resetRows();
	
	public boolean canSendToEnemy();
	
	public void setSendToEnemy(boolean value);
}

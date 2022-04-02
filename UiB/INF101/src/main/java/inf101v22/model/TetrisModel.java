package inf101v22.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import inf101v22.grid.Coordinate;
import inf101v22.grid.CoordinateItem;
import inf101v22.model.piece.PositionedPiece;
import inf101v22.model.piece.PositionedPieceFactory;
import inf101v22.tetris.controller.TetrisControllable;
import inf101v22.tetris.model.GameScreen;
import inf101v22.tetris.model.GameState;
import inf101v22.tetris.view.TetrisViewable;

public class TetrisModel implements TetrisViewable, TetrisControllable{

	public PositionedPiece activePiece;
	private PositionedPieceFactory activePieceFactory;
	
	TetrisBoard board;
	private int rows;
	private int cols;
	private int score;
	private int highScore;
	
	private ArrayList<PositionedPiece> nextPiece;
	
	private GameScreen gameScreen;
	private GameState state;
	private int countPieces;
	
	private boolean right;
	
	private boolean sendRowsToEnemy;
	private ArrayList<Integer> filledRows;
	private int colToBeRemovedRight = 0;
	private int colToBeRemovedLeft = 9;
	
	
	public TetrisModel() {
		this.rows = 15;
		this.cols = 10;
		this.gameScreen = GameScreen.WELCOME_SCREEN;
		this.state = GameState.NORMAL;
		this.board = new TetrisBoard(rows, cols, new Tile(Color.BLACK, '0'));
		this.right = false;
		this.sendRowsToEnemy = false;
		activePieceFactory = new PositionedPieceFactory();
		activePieceFactory.setCenterColumn((this.cols/2)-1);
		this.nextPiece = new ArrayList<>();
		this.filledRows = new ArrayList<>();
		addNextPieceToList();
		activePiece = nextPiece.get(0);
		
		
		
	}
	
	public TetrisModel(PositionedPieceFactory factory) {
		this.rows = 15;
		this.cols = 10;
		this.activePieceFactory = factory;
		this.activePieceFactory.setCenterColumn(this.cols/2);
		activePiece = this.activePieceFactory.getNextActivePiece();
		
		this.board = new TetrisBoard(rows, cols, new Tile(Color.BLACK, '0'));
		
	}
	
	
	@Override
	public int getNumRows() {
		return this.rows;
	}

	@Override
	public int getNumCols() {
		return this.cols;
	}

	
	public Iterable<CoordinateItem<Tile>> tilesOnTheBoard() {
		return board;
	}

	@Override
	public Iterable<CoordinateItem<Tile>> activePiece() {
		// TODO Auto-generated method stub
		return activePiece;
	}

	
	@Override
	public Iterable<CoordinateItem<Tile>> shadowPiece() {
		PositionedPiece shadowPiece = activePiece.getCopy(0, 0);
		
		for (int i = 0; i < rows; i++) {
			PositionedPiece newPositionedPiece = shadowPiece.getCopy(1, 0);
			if (checkIfValidPosition(newPositionedPiece)) {
				shadowPiece = newPositionedPiece;
			} else {
				continue;
			}
		}
		return shadowPiece;
	}
	
	public PositionedPiece getUpcomingPiece() {
		return this.nextPiece.get(0);
	}

	public int getScore() {
		return this.score;
	}
	
	private void setScore(int value) {
		this.score += value*value;
	}
	
	public int getHighScore() {
		return this.highScore;
	}
	
	private void setHighScore(int newScore) {
		if (this.highScore < newScore) {
			this.highScore = newScore;
		}
		
	}
	
	public void resetGame() {
		for (CoordinateItem<Tile> tile : tilesOnTheBoard()) {
			board.set(new Coordinate(tile.coordinate.row, tile.coordinate.col), new Tile(Color.black, '0'));
		}
		this.gameScreen = GameScreen.ACTIVE_GAME;
		setHighScore(this.score);
		this.score = 0;
		this.countPieces = 0;
		resetRows();
	}
	
	

	/**
	 * This, together with {@link #charArray2dToString(char[][])}, will convert 
	 * the grid structure to a visual representation that can be printed
	 * out the console
	 * 
	 * @return an array with the falling piece on top of the board
	 */
	public char[][] pieceToCharArray2d() {
		char[][] charArray2d = board.toCharArray2d();
		for (CoordinateItem<Tile> coordinate : activePiece()) {
			charArray2d[coordinate.coordinate.row][coordinate.coordinate.col] = coordinate.item.character;
		}
		
		return charArray2d;
		
	}
	
	/**
	 * 
	 * @param charArray2d
	 * @return a string representing the falling piece on top of the board
	 */
	public String charArray2dToString(char[][] charArray2d) {
		String result = "";
		int count = 0;
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				if (count > this.cols-1) {
					result += "\n";
					count = 0;
				}
				result += charArray2d[row][col];
				count ++;
				
			}
			
		}
		return result;
	}

	@Override
	public boolean moveFallingPiece(int deltaRow, int deltaCol) {
		PositionedPiece newActivePiece = activePiece.getCopy(deltaRow, deltaCol);
		if (checkIfValidPosition(newActivePiece)) {
			this.activePiece = newActivePiece;
			return true;
		}
		return false;
		
	}
	
	private boolean checkIfValidPosition(PositionedPiece pieces) {
		
		boolean result = true;
		boolean result2 = true;
		
		// TODO: Make this cleaner
		
		for (CoordinateItem<Tile> piece : pieces) {
			for (CoordinateItem<Tile> tile : tilesOnTheBoard()) {
				if (tile.item.character != '0' && !tile.coordinate.equals(piece.coordinate)) {
					result = true;
				} 
				else if (tile.item.character != '0' && tile.coordinate.equals(piece.coordinate)) {
					result2 = false;
				}
				else if (piece.coordinate.col < 0 || piece.coordinate.col > this.cols-1 || piece.coordinate.row < 0 || piece.coordinate.row > this.rows-1) {
					result2 = false;
				}
			}
		}
		if (!result2) {
			return result2;
		}
		
		return result;
	}

	
	
	@Override
	public boolean rotateFallingPiece(String dir) {
		PositionedPiece newActivePiece = activePiece.getRotatedCopy(activePiece, 0, 0, dir);
		
		int centerX = newActivePiece.getWidth()/2;
		int originalCenterX = activePiece.getWidth()/2;
		
		if (centerX < originalCenterX) {
			newActivePiece = activePiece.getRotatedCopy(newActivePiece, 0, 1, dir);
		}
		if (centerX > originalCenterX) {
			newActivePiece = activePiece.getRotatedCopy(newActivePiece, 0, -1, dir);
		}
		
		if (checkIfValidPosition(newActivePiece)) {
			this.activePiece = newActivePiece;
			return true;
		} else {
			
			newActivePiece = movePieceToValidPosition(newActivePiece);
			if (checkIfValidPosition(newActivePiece)) {
				this.activePiece = newActivePiece;
				return true;
			}
		}
		return false;
	}

	/**
	 * Helper method to {@link #rotateFallingPiece()}. If the new rotated positioned piece is 
	 * not within the boundaries of the grid, the piece is moved until it is in a valid position.
	 * This method will also move the piece if the rotated version is in conflict with other
	 * pieces on the board
	 * @param pieceToBeMoved the rotated piece which is not in a valid position
	 * @return a copy of the rotated piece, but moved to a valid position
	 */
	private PositionedPiece movePieceToValidPosition(PositionedPiece pieceToBeMoved) {
		
		PositionedPiece movedActivePiece = pieceToBeMoved.getCopy(0, 0);
		if (checkIfValidPosition(movedActivePiece = pieceToBeMoved.getCopy(0, -1))) {
			return movedActivePiece;
		}
		else if (checkIfValidPosition(movedActivePiece = pieceToBeMoved.getCopy(0, 1))) {
			return movedActivePiece;
		}
		else if (checkIfValidPosition(movedActivePiece = pieceToBeMoved.getCopy(1, 0))) {
			return movedActivePiece;
		}
		else if (checkIfValidPosition(movedActivePiece = pieceToBeMoved.getCopy(-1, 0))) {
			return movedActivePiece;
		}
		else if (checkIfValidPosition(movedActivePiece = pieceToBeMoved.getCopy(0, 2))) {
			return movedActivePiece;
		}
		else if (checkIfValidPosition(movedActivePiece = pieceToBeMoved.getCopy(0, -2))) {
			return movedActivePiece;
		}
		
		return movedActivePiece;
		
	}
		
	

	@Override
	public void dropFallingPiece() {
		for (int i = 0; i < rows; i++ ) {
			moveFallingPiece(1, 0);
		}
		placePieceOnBoard();
		//shadow.remove(0);
		checkForFullRows();
		
		getNextPiece();
		
		
		
	}
	
	/**
	 * Method to get next piece. When the piece is placed on top of the screen,
	 * {@link #addNextPieceToList()} is called to show which piece is coming next
	 *
	 * Increments the total amount of pieces placed on the board
	 **/
	private void getNextPiece() {
		PositionedPiece newActivePiece = nextPiece.remove(0);
		addNextPieceToList();
		if (checkIfValidPosition(newActivePiece)) {
			this.activePiece = newActivePiece;
			
		} else {
			if (this.state == GameState.NORMAL) {
				this.gameScreen = GameScreen.GAME_OVER;
			}
			
		}
		countPieces ++;
	}
	
	
	
	
	
	
	private void addNextPieceToList() {
		PositionedPiece nextActivePiece = activePieceFactory.getNextActivePiece();
		if (nextPiece.isEmpty()) {
			nextPiece.add(nextActivePiece);
		}
		
	}
	
	void placePieceOnBoard() {
		for (CoordinateItem<Tile> tile : activePiece) {
			board.set(new Coordinate(tile.coordinate.row, tile.coordinate.col), tile.item);
		}
	}

	@Override
	public GameScreen getGameScreen() {
		return this.gameScreen;
	}
	
	@Override
	public GameState getGameState() {
		return this.state;
	}
	

	@Override
	public int calculateStep() {
		double startMS = 6000;
		
		return (int) (startMS * Math.pow(0.98, countPieces));
	}

	
	
	
	@Override
	public void clockTick() {
		
		
		if (moveFallingPiece(1, 0)) {
			
		}
		else {
			placePieceOnBoard();
			checkForFullRows();
			getNextPiece();
			}
	}
	
	
	/**
	 * "Paints" each tile in a row black. Alternates between right and left.
	 * 
	 */
	private boolean removeTileAnimation() {
		// if animation has *not* been done in the right hand direction
		if (!right) {
			if (colToBeRemovedRight  < this.cols) {
				board.set(new Coordinate(this.filledRows.get(0), colToBeRemovedRight), new Tile(Color.BLACK, 'i'));
			}
			colToBeRemovedRight++;
		}
		// otherwise, the animation will continue in the left hand direction
		if (right) {
			if (colToBeRemovedLeft  >= 0) {
				board.set(new Coordinate(this.filledRows.get(0), colToBeRemovedLeft), new Tile(Color.BLACK, 'i'));
			}
			colToBeRemovedLeft--;
			
			if (colToBeRemovedLeft < 0) {
				filledRows.remove(0);
				colToBeRemovedLeft = this.cols-1;
				right = false;
			}
		}
		
		if (colToBeRemovedRight == this.cols) {
			filledRows.remove(0);
			colToBeRemovedRight = 0;
			right = true;
		}
		return true;
		
	}
	
	/**
	 * Loops through all the rows. If a row is full, this row
	 * is added to a list
	 */
	private void checkForFullRows() {
		for (int i = this.rows-1; i >= 0; i--) {
			if(board.ifRowFull(i)) {
				filledRows.add(i);
				this.state = GameState.VISUALEFFECT;
			} 
		}
	}

	@Override
	public void doVisualEffect() {
		if (filledRows.size() > 0) {
			removeTileAnimation();
			
		} else {
			setScore(board.removeFullRows());
			this.sendRowsToEnemy = true;
			this.state = GameState.NORMAL;
			right = false;
			
		}
		
	}

	@Override
	public void setGameState(GameState state) {
		this.state = state;
		
	}

	@Override
	public void setGameScreen(GameScreen value) {
		this.gameScreen = value;
		
	}

	
	@Override
	public void insertRows(HashMap<Integer, ArrayList<Character>> enemyRows) {
		for (Integer rows : enemyRows.keySet()) {
			board.insertRowsOnBottom(enemyRows.get(rows));
		}
		
	}


	@Override
	public HashMap<Integer, ArrayList<Character>> getEnemyRows() {
		return board.toBeAdded;
	}

	public void resetRows() {
		board.toBeAdded.clear();
	}

	@Override
	public boolean canSendToEnemy() {
		return this.sendRowsToEnemy;
	}

	@Override
	public void setSendToEnemy(boolean value) {
		this.sendRowsToEnemy = value;
	}

	

	
	
	

	


}















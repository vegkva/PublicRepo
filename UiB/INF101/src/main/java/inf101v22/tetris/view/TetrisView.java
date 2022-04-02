package inf101v22.tetris.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

import inf101v22.grid.CoordinateItem;
import inf101v22.model.Tile;
import inf101v22.tetris.model.GameScreen;

public class TetrisView extends JComponent {
	
	{
		this.setFocusable(true);
	}
	
	TetrisViewable model;
	
	public TetrisView(TetrisViewable tetrisViewable) {
		this.model = tetrisViewable;
		
		
		
		
	}
	
	@Override
	public void paint(Graphics canvas) {
		super.paint(canvas);
        int componentWidth = this.getWidth();
        int componentHeight = this.getHeight();
        int tileWidth = componentWidth-this.getWidth()/4;
        int tileHeight = componentHeight;
        int padding = 4;
        this.drawTetrisBoard(canvas, padding+2, padding+2, tileWidth - 2*padding, tileHeight - 2*padding, padding);
       
        ifGameOver(canvas, padding, padding, componentWidth, componentHeight);
       
        drawScore(canvas, padding, padding, componentWidth, componentHeight, Color.black);
        
	}
	
	
	/*
	 * This method iterates over the parameter iterable, and draws each tile in this iterable
	 * The layer parameter is used only for colouring the shadow
	 */
	public void drawBoardWithRightBottomPadding(Graphics canvas, int boardX, int boardY, int width, int height, int padding, Iterable<CoordinateItem<Tile>> iterable, int layer) {
		int rows = model.getNumRows();
		int cols = model.getNumCols();
		Color color = new Color(0,0,0,128);
		for (CoordinateItem<Tile> tile : iterable) {
			int row = tile.coordinate.row;
			int col = tile.coordinate.col;
			
			color = tile.item.color;
			if (layer == 3) {
				int red = tile.item.color.getRed();
				int green = tile.item.color.getGreen();
				int blue = tile.item.color.getBlue();
				color = new Color(red, green, blue, 80);
			}
			

            // Calculate the coordinates of the tile and paint it
			int x = boardX + col * width / cols;
			int y = boardY + row * height / rows;
            int nextY = boardY + (row + 1) * height / rows;
            int nextX = boardX + (col + 1) * width / cols;
            int tileWidth = nextX - x;
            int tileHeight = nextY - y;
            drawTileWithRightBottomPadding(canvas, x, y, tileWidth, tileHeight, padding, color, tile);
		}
	}
	
	/*
	 * Fetches the current score and highscore and draws it on the board
	 */
	public void drawScore(Graphics canvas, int x, int y, int width, int height, Color color) {
        canvas.setColor(Color.BLACK);
        float fontSize = (this.getHeight()+this.getWidth())/50;
		canvas.setFont(canvas.getFont().deriveFont(fontSize));
		canvas.drawString("Score: " + model.getScore(), (this.getWidth()/10)*8, (this.getHeight()/10));
		 
		canvas.drawString("Next piece: ", (this.getWidth()/17)*13, (this.getHeight()/10)*3);
		
		canvas.drawString("HighScore: ", (this.getWidth()/17)*13, (height/10)*8);
		canvas.drawString("" +model.getHighScore(), (this.getWidth()/18)*16, (height/11)*10);
		
         
	}
	
	public void drawTileWithRightBottomPadding(Graphics canvas, int x, int y, int width, int height, int padding, Color color, CoordinateItem<Tile> coordinate) {
		canvas.setColor(color);
        canvas.fillRect(x, y, width-padding, height-padding);
	}
	
	private void drawBinds(Graphics canvas, int x, int y, int width, int height) {
		GraphicHelperMethods.drawCenteredString(
                canvas, "Keybinds:",
                0, y+180, width, height);
		GraphicHelperMethods.drawCenteredString(
                canvas, "__________",
                0, y+181, width, height);
		GraphicHelperMethods.drawCenteredString(
                canvas, "'space': drop piece",
                0, y+220, width, height);
		GraphicHelperMethods.drawCenteredString(
                canvas, "'arrow up': rotate right",
                0, y+250, width, height);
		GraphicHelperMethods.drawCenteredString(
                canvas, "'Ctrl/Command': rotate left",
                0, y+280, width, height);
		GraphicHelperMethods.drawCenteredString(
                canvas, "'Esc': pause game",
                0, y+320, width, height);
	}
	
	private void drawWelcomeScreen(Graphics canvas, int x, int y, int width, int height) {
		canvas.setColor(new Color(0,0,100,128));
		canvas.fillRect(x, y, width, height);
		canvas.setColor(Color.WHITE);
		canvas.setFont(canvas.getFont().deriveFont(40f));
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "Welcome to INF101 Tetris!",
	                0, -250, width, height);
		 canvas.setFont(canvas.getFont().deriveFont(20f));
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "Singleplayer",
	                0, -140, width, height);
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "__________",
	                0, -141, width, height);
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "Press 'Enter' to start",
	                0, -110, width, height);
		 
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "Multiplayer",
	                0, -50, width, height);
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "__________",
	                0, -51, width, height);
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "Press 'n' to host: This will start the server and a client",
	                0, -20, width, height);
		 GraphicHelperMethods.drawCenteredString(
	                canvas, "or 'c' to join: This will only start a client",
	                0, 10, width, height);
		 drawBinds(canvas, x, -20, width, height);
	}
	
	/**
	 * This method will draw the tetris board including the active piece, its shadow, and the next piece
	 * At first it will draw a welcome screen
	 */
	public void drawTetrisBoard(Graphics canvas, int x, int y, int width, int height, int padding) {
		
		
		
		if (model.getGameScreen() == GameScreen.WELCOME_SCREEN) {
			drawWelcomeScreen(canvas, x, y, width, height);
			
		}
		else {
			//first empty board
			Iterable<CoordinateItem<Tile>> iter = model.tilesOnTheBoard();
			drawBoardWithRightBottomPadding(canvas, x, y, width, height, padding, iter, 1);
			
			//then draw the active piece
			Iterable<CoordinateItem<Tile>> iter2 = model.activePiece();
			drawBoardWithRightBottomPadding(canvas, x, y, width, height, padding, iter2, 2);
			
			//shadow piece
			Iterable<CoordinateItem<Tile>> iter5 = model.shadowPiece();
			drawBoardWithRightBottomPadding(canvas, x, y, width, height, padding, iter5, 3);
			
			//upcoming piece
			Iterable<CoordinateItem<Tile>> iter3 = model.getUpcomingPiece();
			drawBoardWithRightBottomPadding(canvas, (this.getWidth()/11)*7, (this.getHeight()/15)*5, (this.getWidth()/10)*4, (this.getHeight()/10)*5, 2, iter3, 4);
			
		}
		ifPause(canvas, padding, padding, width, height);
		//draw score
		//drawScore(canvas, 450, 20, 130, 50, 0, Color.black);
		
	}
	
	public void ifPause(Graphics canvas, int x, int y, int width, int height) {
		
		if (model.getGameScreen() == GameScreen.PAUSE) {
			canvas.setColor(new Color(0,0,0,200));
			canvas.fillRect(x, y, width, height);
			canvas.setColor(Color.WHITE);
			canvas.setFont(canvas.getFont().deriveFont(40f));
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "GAME PAUSED!",
		                0, -130, width, height);
			 canvas.setFont(canvas.getFont().deriveFont(20f));
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "Resume : 'Enter'",
		                0, -80, width, height);
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "Exit game : 'e'",
		                0, -40, width, height);
			 drawBinds(canvas, x, -80, width, height);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700,700);
	}
	
	public void ifGameOver(Graphics canvas, int x, int y, int width, int height) {
		
		if (model.getGameScreen() == GameScreen.GAME_OVER) {
			canvas.setColor(new Color(0,0,0,200));
			canvas.fillRect(x, y, width, height);
			canvas.setColor(Color.WHITE);
			canvas.setFont(canvas.getFont().deriveFont(60f));
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "GAME OVER",
		                0, -50, width, height);
			 canvas.setFont(canvas.getFont().deriveFont(30f));
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "Final score: " + model.getScore(),
		                0, 0, width, height);
			 canvas.setFont(canvas.getFont().deriveFont(20f));
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "Press 'r' to restart game",
		                0, 80, width, height);
			 GraphicHelperMethods.drawCenteredString(
		                canvas, "Press 'e' to exit",
		                0, 120, width, height);
			 
		}
	}
}

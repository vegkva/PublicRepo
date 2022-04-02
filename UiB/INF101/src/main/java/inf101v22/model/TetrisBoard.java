package inf101v22.model;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import inf101v22.grid.Coordinate;
import inf101v22.grid.Grid;

public class TetrisBoard extends Grid<Tile>{

	
	private ArrayList<Character> oldItems;
	private ArrayList<Character> tilesToBeInserted;
	
	private HashMap<Integer, ArrayList<Character>> intermediate;
	public HashMap<Integer, ArrayList<Character>> toBeAdded;
	
	private HashMap<Character, Tile> tiles;
	
	private boolean directTransfer;
	
	public TetrisBoard(int rows, int cols, Tile item) {
		super(rows, cols, item);
		this.oldItems = new ArrayList<>();
		this.tilesToBeInserted = new ArrayList<>();
		this.intermediate = new HashMap<>();
		this.toBeAdded = new HashMap<>();
		this.directTransfer = true;
		createMappings();
	}

	/*
	 * Creates a map of tiles and their corresponding characters.
	 * Used in multiplayer
	 */
	private void createMappings() {
		this.tiles = new HashMap<>();
		tiles.put('0', new Tile(Color.BLACK, '0'));
		tiles.put('t', new Tile(Color.GREEN, 't'));
		tiles.put('s', new Tile(Color.YELLOW, 's'));
		tiles.put('z', new Tile(Color.CYAN, 'z'));
		tiles.put('i', new Tile(Color.RED, 'i'));
		tiles.put('j', new Tile(Color.MAGENTA, 'j'));
		tiles.put('l', new Tile(Color.ORANGE, 'l'));
		tiles.put('o', new Tile(Color.BLUE, 'o'));
	}
	
	public char[][] toCharArray2d() {
		char[][] charArray2d = new char[rows][cols];
		for (int row = 0; row < this.getRows(); row++) {
			for (int col = 0; col < this.getCols(); col++) {
				if (this.get(new Coordinate(row, col)).character == '0') {
					charArray2d[row][col] = '-';
				} else {
					charArray2d[row][col] = this.get(new Coordinate(row, col)).character;
				}
				
			}
		}
		return charArray2d;
		
	}
	
	
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
	
	private ArrayList<Character> copy(ArrayList<Character> arrayToBeCopied) {
		ArrayList<Character> copy = new ArrayList<>();
		for (int i = 0; i < arrayToBeCopied.size(); i++) {
			copy.add(arrayToBeCopied.get(i));
		}
		return copy;
	}
	
	/**
	 * This method checks if a row is full. This method is also continuously saving
	 * intermediate rows for multiplayer purpose. If a client removes a row, this row
	 * minus the last inserted piece is sent to the other client
	 * 
	 * @param row The row to be checked
	 * @return true if the row is full
	 */
	public boolean ifRowFull(int row) {
		int count = 0;
			for (int j = 0; j < this.cols; j++) {
					if (this.get(new Coordinate(row, j)).character != '0') {
					count ++;
				} 
					oldItems.add(this.get(new Coordinate(row, j)).character);
				
			}
			if (count == this.cols) {
				if (directTransfer && !tilesToBeInserted.isEmpty()) {
					System.err.println("direct transfer");
					intermediate.put(row, copy(tilesToBeInserted));
					directTransfer = false;
					tilesToBeInserted.clear();
				}
				for (Integer rowBeforeFull : intermediate.keySet()) {
					if (rowBeforeFull == row) {
						toBeAdded.put(rowBeforeFull, intermediate.get(rowBeforeFull));
					}
				}
				tilesToBeInserted.clear();
				return true;
			}
		intermediate.put(row, copy(oldItems));
		oldItems.clear();
		directTransfer = false;
		return false;
		
	}
	
	/**
	 * Fills a row with a specific tile
	 * @param tile The tile to be inserted
	 * @param row The row that are being filled
	 */
	public void fillRow(Tile tile, int row) {
			for (int i = 0; i < this.cols; i++) {
				this.set(new Coordinate(row, i), tile);
			}
	}
	
	/**
	 * Copies elements in a row to a new row
	 * @param from the row to be copied from
	 * @param to the row to be copied to
	 */
	public void copyElementsToNewRow(int from, int to) {
		
		for (int i = 0; i < this.cols; i++) {
			Tile oldItem = this.get(new Coordinate(from, i));
			this.set(new Coordinate(to, i), oldItem);
		}
	}
	

	/**
	 * Removes full rows
	 * @return amount of full rows
	 */
	public int removeFullRows() {
		int a = rows-1;
		int b = rows-1;
		int count = 0;
		while (a >= 0) {
			while (b >= 0 && ifRowFull(b)) {
				count++;
				b--;
			}
			if (b >= 0) {
				copyElementsToNewRow(b, a);
			} else {
				fillRow(new Tile(Color.BLACK, '0'), a);
			}
			a--;
			b--;
		}
		
		return count;
	}
	
	/**
	 * First, the rows has to be moved one row up.
	 * Then the "enemy row" can be inserted.
	 * This method will be called for every row to be inserted.
	 * @param chars
	 */
	public void insertRowsOnBottom(ArrayList<Character> chars) {
		tilesToBeInserted = chars;
		for (int i = 1; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				Tile oldItem = this.get(new Coordinate(i, j));
				this.set(new Coordinate(i-1, j), oldItem);
			}
		}
		
			for (int i = 0; i < this.cols; i++) {
				
				this.set(new Coordinate(this.rows-1, i), tiles.get(chars.get(i)));
	
		}
		
		
		
	}
	
	
}

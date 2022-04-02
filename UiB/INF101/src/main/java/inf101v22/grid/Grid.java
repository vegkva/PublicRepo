package inf101v22.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Grid<E> implements IGrid<E>{
	
	public final int rows;
	public final int cols;
	private List<E> grid;
	private E item;
	
	/**
	 * Constructor of grid with rows and columns
	 * @param rows
	 * @param cols
	 */
	public Grid(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.grid = new ArrayList<E>();
		for (int i = 0; i < rows * cols; ++i) {
			grid.add(null);
		}
	}
	
	/**
	 * Constructor that includes item into the grid
	 */
	public Grid(int rows, int cols, E item) {
		this(rows, cols);
		this.item = item;
		grid = new ArrayList<E>();
		for (int i = 0; i < rows * cols; ++i) {
			grid.add(item);
		}
		
	}
	
	
	
	
	/**
	 * Copies values from the grid into an ArrayList that can be iterated over
	 */
	@Override
	public Iterator<CoordinateItem<E>> iterator() {
		ArrayList<CoordinateItem<E>> coordinateItem = new ArrayList<CoordinateItem<E>>();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Coordinate coordinate = new Coordinate(row, col);
				CoordinateItem<E> item = new CoordinateItem<E>(coordinate, grid.get(locationToIndex(coordinate)));
				coordinateItem.add(item);
			}
		}
		return coordinateItem.iterator();
	}
	
	

	@Override
	public int getRows() {
		return this.rows;
	}

	@Override
	public int getCols() {
		return this.cols;
	}

	@Override
	public void set(Coordinate coordinate, E value) {
		grid.set(locationToIndex(coordinate), value);
		
	}
	
	/**
	 * This method computes which index in the list belongs to a given Location
	 * @author Martin Vatshelle
	 */
	public int locationToIndex(Coordinate coordinate) {
		return coordinate.row + coordinate.col * rows;
	}


	@Override
	public E get(Coordinate coordinate) {
		if (!coordinateIsOnGrid(coordinate)) {
			throw new IndexOutOfBoundsException();
		}
		return grid.get(locationToIndex(coordinate));
	}

	
	
	@Override
	public boolean coordinateIsOnGrid(Coordinate coordinate) {
		if (coordinate.row < 0 || coordinate.row >= rows) {
			return false;
		}

		return coordinate.col >= 0 && coordinate.col < cols;

	}

}

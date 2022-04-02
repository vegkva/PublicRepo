package inf101v22.grid;

import java.util.Objects;

/*
 * Class that creates a coordinate
 */

public class Coordinate {
	
	public final int row;
	public final int col;
	
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(col, row);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return col == other.col && row == other.row;
	}
    
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("{ row='%d', col='%d' }", this.row, this.col);
	}
	
}

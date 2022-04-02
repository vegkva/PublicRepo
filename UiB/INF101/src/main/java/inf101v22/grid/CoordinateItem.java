package inf101v22.grid;

import java.util.Objects;

public class CoordinateItem <E> {
    public final Coordinate coordinate;
    public final E item;
    
    
    public CoordinateItem(Coordinate coordinate, E item) {
		this.coordinate = coordinate;
		this.item = item;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordinate, item);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoordinateItem<?> other = (CoordinateItem<?>) obj;
		return Objects.equals(coordinate, other.coordinate) && Objects.equals(item, other.item);
	}
    
	@Override
	public String toString() {
		return String.format("{ coordinate='{ row='%d', col='%d' }', item='%s' }", coordinate.row, coordinate.col, this.item.toString());
	}
    
    
}

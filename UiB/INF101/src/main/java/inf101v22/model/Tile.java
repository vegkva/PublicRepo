package inf101v22.model;

import java.awt.Color;

public class Tile {

	public final Color color;
	public final char character;
	
	public Tile(Color color, char character) {
		this.color = color;
		this.character = character;
	}

	public Color getColor() {
		return color;
	}

	public char getCharacter() {
		return character;
	}
	
	
	
}

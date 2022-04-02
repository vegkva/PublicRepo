package inf101v22.tetris.net.client;

import java.util.ArrayList;
import java.util.HashMap;

import inf101v22.tetris.controller.TetrisControllable;

public class ClientController {

	
	public KryonetClient client;
	ClientListener listener;
	TetrisControllable model;
	public boolean sending;
	
	public HashMap<Integer, ArrayList<Character>> enemyTilesToBeAdded;
	
	private int tcpPort = 5555, udpPort = 27960;
	
	public ClientController(TetrisControllable model) {
		this.sending = false;
		this.model = model;
		this.client = new KryonetClient(tcpPort, udpPort);
		
		
	}
	
	public boolean tilesToBeAdded() {
		return client.attack;
	}
	
	/**
	 * Updates the hashmap of which tiles to give to the enemy
	 */
	public void update() {
		enemyTilesToBeAdded = model.getEnemyRows();
		
	}
	/**
	 * Sends data to the server
	 */
	public void sendData() {
		client.sendTiles(client.client, enemyTilesToBeAdded);
		model.resetRows();
		
	}
	
	/**
	 *  Inserting rows into the grid
	 */
	public void applyTiles() {
		if (client.getHashMap() != null || client.getHashMap().size() != 0) {
			model.insertRows(client.getHashMap());
			client.setAttack(false);
			client.getHashMap().clear();
			
			
		}
		
	}
}

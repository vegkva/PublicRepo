package inf101v22.tetris.net.server;

import inf101v22.tetris.controller.TetrisControllable;

public class ServerController {

	
	TetrisControllable model;
	public KryonetServer server;
	ServerListener listener;
	
	public ServerController(TetrisControllable model) {
		this.model = model;
		this.server = new KryonetServer();
		
	}
	
	
}

package inf101v22.tetris.net;


import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf101v22.tetris.net.client.KryonetClient;
import inf101v22.tetris.net.server.KryonetServer;

class ServerClientTest {
	
	KryonetClient client;
	KryonetClient client2;
	KryonetServer server;
	HashMap<Integer, ArrayList<Character>> map1;
	HashMap<Integer, ArrayList<Character>> map2;
	HashMap<Integer, ArrayList<Character>> map3;
	HashMap<Integer, ArrayList<Character>> map4;
	ArrayList<Character> row1;
	ArrayList<Character> row2;
	ArrayList<Character> row3;
	ArrayList<Character> row4;
	ArrayList<Character> row5;
	
	static int tcpPort = 5555, udpPort = 27960;

	@BeforeEach
	void setUp() throws Exception {
		server = new KryonetServer();
		client = new KryonetClient(tcpPort, udpPort);
		client2 = new KryonetClient(tcpPort, udpPort);
		row1 = new ArrayList<>();
		row2 = new ArrayList<>();
		row3 = new ArrayList<>();
		row4 = new ArrayList<>();
		row5 = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			row1.add('a');
			row2.add('b');
			row3.add('c');
			row4.add('d');
			row5.add('e');
		}
		
		
	}

	
	@Test
	void testSendTiles() {
		map1 = new HashMap<>();
		map2 = new HashMap<>();
		map3 = new HashMap<>();
		map4 = new HashMap<>();
		
		map1.put(1, row3); // c
		map1.put(2, row1); // a
		
		map2.put(1, row1); // a
		map2.put(2, row2); // b
		
		map3.put(1, row3); // c
		map3.put(2, row4); // d
		
		map4.put(1, row5); // e
		map4.put(2, row2); // b
		
		
		client.sendTiles(client.client, map1);
		
		client2.sendTiles(client2.client, map2);
		
		client.sendTiles(client.client, map3);
		
		client.sendTiles(client.client, map2);
		
		client2.sendTiles(client2.client, map4);
		
		
		
	}
	
	

}

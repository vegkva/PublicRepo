package inf101v22.tetris.net.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import inf101v22.tetris.net.packets.Packet;



public class KryonetClient {
	
	public Client client;
	int tcpPort, udpPort;
	ClientListener listener;
	public boolean attack;
	
	HashMap<Integer, ArrayList<Character>> toBeAdded;
	
	

	public KryonetClient(int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		toBeAdded = new HashMap<>();
		this.attack = false;
		client = new Client();
		Kryo kryo = client.getKryo();
		kryo.register(String.class);
		kryo.register(Packet.class);
		kryo.register(java.util.HashMap.class);
		kryo.register(java.util.ArrayList.class);
		
		client.start();
		try {
			client.connect(5000, "localhost",this.tcpPort, this.udpPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("Client: " + client.getID() + " is running");
		client.addListener(new ClientListener(this, this.client));
		
	}
	
	
	void setAttack(boolean value) {
		this.attack = value;
	}
	
	
	/**
	 * If a client has received a packet from the server, this 
	 * method return true
	 */
	public boolean getAttack() {
		return this.attack;
	}
	
	/**
	 * Whenever the client removes rows, these rows are added to a list.
	 * This list is then sent to the server, and the server decides which rows
	 * to send to the other client, based upon earlier received rows.
	 * 
	 * @param client The client that removed the rows from their grid
	 * @param rowsToServer The hashMap being sent to the server
	 */
	public void sendTiles(Client client, HashMap<Integer, ArrayList<Character>> rowsToServer) {
		Packet toServer = new Packet();
		toServer.playerID = client.getID();
		toServer.toBeAdded = rowsToServer;
		//System.out.println(client.getID() + " sending  " + toServer.toBeAdded + " to server");
		this.client.sendUDP(toServer);
	}
	
	
	public boolean updateHashMap(HashMap<Integer, ArrayList<Character>> value) {
		if (value != null) {
			this.toBeAdded.clear();
			this.toBeAdded = value;
			return true;
		}
		
		return false;
	}
	
	public HashMap<Integer, ArrayList<Character>> getHashMap() {
		return this.toBeAdded;
	}
}

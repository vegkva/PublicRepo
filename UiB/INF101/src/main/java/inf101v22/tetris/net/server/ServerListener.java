package inf101v22.tetris.net.server;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import inf101v22.tetris.net.packets.Packet;


public class ServerListener extends Listener{
	
	public Map<Integer, HashMap<Integer, ArrayList<Character>>> players;
	
	public ArrayList<Integer> toBeRemoved;
	public ArrayList<ArrayList<Character>> rows;
	
	
	public HashMap<Integer, ArrayList<Character>> temp;
	
	
	KryonetServer server;
	
	public ServerListener(KryonetServer kryonetServer) {
		this.server = kryonetServer;
		this.players = new HashMap<Integer, HashMap<Integer, ArrayList<Character>>>();
		this.temp = new HashMap<>();
		this.toBeRemoved = new ArrayList<>();
		rows = new ArrayList<>();
	}
	
	
	@Override
	public void connected(Connection c) {
		players.put(c.getID(), null);
		System.out.println("[SERVER] Client: " + c.getID() + " has connected!");
	}
	
	@Override
	public void disconnected(Connection c) {
		players.remove(c.getID());
		System.out.println("[SERVER] Client: " + c.getID() + " has disconnected");
	}
	
	@Override
	public void received(Connection c, Object o) {
		
		if (o instanceof Packet) {
			Packet packet = (Packet) o;
			temp = packet.toBeAdded;
			
			
			for (Integer player : players.keySet()) {
				if (player == c.getID()) {
					players.put(player, packet.toBeAdded);
				}
			}
		
			checkForIdenticalRows(c, packet);
			cleanUpIdenticalRows(c);
			sendToClient(c);
			
		}
		
	}


	private void sendToClient(Connection c) {
		for (Integer player : players.keySet()) {
			if (c.getID() != player) {
				Packet newMsg = new Packet();
				newMsg.toBeAdded = temp;
				server.server.sendToUDP(player, newMsg);
			}
		}
		
	}
	private void cleanUpIdenticalRows(Connection c) {
		// remove from players
		Iterator<HashMap<Integer, ArrayList<Character>>> it = players.values().iterator();
		try {
			Iterator<ArrayList<Character>> it2 = it.next().values().iterator();
			if (it.hasNext()) {
				while (it2.hasNext()) {
					ArrayList<Character> nextRow = it2.next();
						if (rows.contains(nextRow)) {
							//System.err.println(nextRow + " removed");
							it2.remove();
						}
					}
				}
			} catch (Exception e) {
		}
		
		// remove the identical rows
		for (Integer row : toBeRemoved) {
			temp.remove(row);
		}
		toBeRemoved.clear();
		rows.clear();
		
	}


	private void checkForIdenticalRows(Connection c, Packet packet) {
		for (Integer player : players.keySet()) {
			if (!players.containsValue(null)) {
				for (ArrayList<Character>  list : players.get(player).values()) {
					if (player != c.getID()) {
						for (Integer row : packet.toBeAdded.keySet()) {
							if (packet.toBeAdded.get(row).hashCode() == list.hashCode()) {
								System.err.println("identical rows: " + row + "\nRejected!");
								toBeRemoved.add(row);
								rows.add(list);
							}
						}
					} 
				}
			} 
		}
		
	}

}

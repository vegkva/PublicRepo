package inf101v22.tetris.net.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import inf101v22.tetris.net.packets.Packet;

public class ClientListener extends Listener {
	
	Client client;
	KryonetClient kryoClient;
	
	
	

	public ClientListener(KryonetClient kryonetClient, Client client) {
		this.client = client;
		this.kryoClient = kryonetClient;
	}

	@Override
	public void connected(Connection c) {
		System.out.println("[CLIENT: "+c.getID() + "] Connected!");
	}
	
	@Override
	public void disconnected(Connection c) {
		System.out.println("[CLIENT: " +c.getID() + "] Disconnected!");
	}
	
	@Override
	public void received(Connection c, Object o) {
		if (o instanceof Packet) {
			Packet msg = (Packet) o;
			if (msg.playerID != c.getID()) {
				if(kryoClient.updateHashMap(msg.toBeAdded)) {
					kryoClient.setAttack(true);
				}
				
			}
		}
	}
	
	
	
	
}

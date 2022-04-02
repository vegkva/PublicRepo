package inf101v22.tetris.net.server;

import java.io.IOException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import inf101v22.tetris.net.packets.Packet;


public class KryonetServer {
	
	public Server server;
	static int udpPort = 27960, tcpPort = 5555;
	
	public KryonetServer() {
		 server = new Server();
		 Kryo kryo = server.getKryo();
		 kryo.register(String.class);
		 kryo.register(Packet.class);
		 kryo.register(java.util.HashMap.class);
		 kryo.register(java.util.ArrayList.class);
		 server.start();
		 
		 try {
			server.bind(tcpPort, udpPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 server.addListener(new ServerListener(this));
		 
		 System.out.println("Server listening for connections...");
		 
		 
	}

}

package farkle.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Server 
{
	class ServerEventHandler extends Thread
	{
	}
	
	class ClientHandler extends Thread
	{
		final DataInputStream dis; 
		final DataOutputStream dos; 
		final Socket socket;
		
		public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos)
		{
			this.socket = socket;
			this.dis = dis;
			this.dos = dos;
		}
	}
	
	

	private Main main;
	private int port;
	private ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
	
	public Server(Main main, int port)
	{
		this.main = main;
		this.port = port;
	}
	
	public void start()
	{
		
	}
	
	
	
	
}



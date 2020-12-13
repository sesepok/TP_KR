package farkle.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import farkle.game.GameState;
import farkle.main.userAction.DieUserAction;
import farkle.main.userAction.LeaveUserAction;
import farkle.main.userAction.UserAction;

public class Client 
{
	
	private Main main;
	private String name;
	private String IP;
	private int port;
	
	private Socket socket;
	DataInputStream dis; 
    DataOutputStream dos; 
    
    private ServerHandler serverHandler;
    
    private boolean receivedHello = false; //server accepted connection
    
    private String[] players; //last information about players in lobby 
    
    class ServerHandler extends Thread
    {
    	public void run()
    	{
    		try
    		{
    			while (true)
    			{
    				if (interrupted()) return;
    				String input = dis.readUTF();
    				receivedHello = true;
    				
    				processMessage(input);
    			}
    		}
    		catch (EOFException e)
    		{
    			if (receivedHello)
    				System.out.println("The server closed connection");
    			else
    				System.out.println("The server is probably full");
				main.dispatchUserAction(new LeaveUserAction());
    		}
    		catch (SocketException e)
    		{
    			if (e.getMessage().equals("Connection reset"))
    			{
    				System.out.println("Disconnected from host");
    				main.dispatchUserAction(new LeaveUserAction());
    			}
    			else if (e.getMessage().equals("Socket closed"))
    				System.out.println("Client closed connection");
    			else
    				e.printStackTrace();
    		}
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    	}
    	
    }
	
	public Client (Main main, String name, String IP, int port)
	{
		this.main = main;
		this.name = name;
		this.IP = IP;
		this.port = port;
	}
	
	public void connect() throws UnknownHostException, IOException
	{
		socket = new Socket(IP, port);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		
		dos.writeUTF("HELLO:" + name);
		serverHandler = new ServerHandler();
		serverHandler.start();
	}
	
	private void processMessage(String input)
	{
		//System.out.println("Client got: " + input);
		
		String[] inputWords = input.split(":");
	
		if (inputWords[0].equals("LOBBY"))
		{
			players = inputWords[1].split(" ");
			main.setLobbyPlayerNames(players);
		}
		else if (input.equals("START"))
		{
			main.serverStartedGame(players);
		}
		else if (inputWords[0].equals("GAME"))
		{
			main.updateGameState(GameState.fromString(inputWords[1]));
		}
	}
	
	public void sendUserActionToServer(UserAction action)
	{
		try
		{
			switch (action.type)
			{
			case DIE:
				dos.writeUTF("DIE:" + ((DieUserAction)action).n);
				break;
			case ROLL:
				dos.writeUTF("ROLL");
				break;
			case BANK:
				dos.writeUTF("BANK");
				break;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException
	{
		if(serverHandler != null) serverHandler.interrupt();
		if(socket != null) socket.close();
		
	}
	

}

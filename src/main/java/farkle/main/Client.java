package farkle.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import farkle.main.userAction.LeaveUserAction;

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
    				System.out.println("Client got: " + input);
    				
    				String[] inputWords = input.split(" ");
				
    				if (inputWords[0].equals("LOBBY"))
    				{
    					main.setLobbyPlayerNames(input.substring(6).split(" "));
    				}
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
    			System.out.println("The client closed connection");
				//main.dispatchUserAction(new LeaveUserAction());
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
		
		dos.writeUTF("HELLO " + name);
		serverHandler = new ServerHandler();
		serverHandler.start();
	}
	
	public void close() throws IOException
	{
		if(serverHandler != null) serverHandler.interrupt();
		if(socket != null) socket.close();
		
	}
	

}

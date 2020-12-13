package farkle.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import farkle.game.GameState;
import farkle.main.userAction.BankUserAction;
import farkle.main.userAction.DieUserAction;
import farkle.main.userAction.JoinGameUserAction;
import farkle.main.userAction.LeaveUserAction;
import farkle.main.userAction.NetworkUserAction;
import farkle.main.userAction.RollUserAction;

public class Server
{
	private Main main;
	private int port;
	private ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
	private ServerSocket serverSocket;
	private LobbyFiller lobbyFiller;
	private GameEventHandler gameEventHandler;
	private boolean running = true;
	private boolean gameStarted = false;
	
	private volatile BlockingQueue<NetworkUserAction> clientActionQueue = new LinkedBlockingDeque<NetworkUserAction>();

	
	class LobbyFiller extends Thread
	{
		public void run()
		{
			while (true)
			{
				if (interrupted()) return;
				/*if (clientHandlers.size() == 5)
				{
					try 
					{
						synchronized (clientHandlers)
						{
							clientHandlers.wait();
						}
						
					} catch (InterruptedException e) 
					{
						interrupt();
						return;
					}
				}*/
				
				Socket socket;
				try 
				{
					socket = serverSocket.accept();
					if (clientHandlers.size() == 5) socket.close();
					System.out.println("A new client is connected : " + socket);
					
					DataInputStream dis = new DataInputStream(socket.getInputStream()); 
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					
					System.out.println("Assigning new thread for this client");
					
					ClientHandler ch = new ClientHandler(socket, dis, dos, clientHandlers.size() + 1);
					clientHandlers.add(ch);
					ch.start();
				}
				catch (SocketException e)
				{
					if (interrupted()) //will always throw on interrupt, cuz socket is stuck in accept
						interrupt();
					else
						e.printStackTrace();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
				
			}
		}
	}
	
	class GameEventHandler extends Thread
	{
		
		public void run()
		{
			while (true)
			{
				try {
					main.dispatchNetworkUserAction(clientActionQueue.take());
				} catch (InterruptedException e) 
				{
					return;
				}
			}
		}
	}
	
	class ClientHandler extends Thread
	{
		final DataInputStream dis; 
		final DataOutputStream dos; 
		final Socket socket;
		
		public int playerIndex;
		
		
		
		public ClientHandler(Socket socket, DataInputStream dis, DataOutputStream dos, int playerIndex)
		{
			this.socket = socket;
			this.dis = dis;
			this.dos = dos;
			this.playerIndex = playerIndex;
		}
		
		public void run()
		{
			try 
			{
				String input = dis.readUTF();
				//System.out.println("Client " + playerIndex + " sent:\n" + input);
				String[] inputWords = input.split(":");
				if (inputWords[0].equals("HELLO"))
				{
					main.dispatchNetworkUserAction(
							new NetworkUserAction(new JoinGameUserAction(inputWords[1], null, playerIndex), playerIndex));
				}
				else
				{
					removeClient(playerIndex);
				}

				while(true)
				{
					try
					{
						input = dis.readUTF();
						processClientMessage(input);
					}
					catch (SocketException e)
					{
						if (!running) return;
						System.out.println("Clinet " + playerIndex + " disconnected (SocketException).");
						removeClient(playerIndex);
						main.dispatchNetworkUserAction(new NetworkUserAction(
								new LeaveUserAction(), playerIndex));
						return;
					}
					catch (EOFException e)
					{
						System.out.println("Clinet " + playerIndex + " disconnected (EOFException).");
						removeClient(playerIndex);
						main.dispatchNetworkUserAction(new NetworkUserAction(
								new LeaveUserAction(), playerIndex));
						return;
					}
				}
				
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		public void sendMessage(String message) throws IOException
		{
			dos.writeUTF(message);
		}
		
		public void sendPlayerNames(String[] names) throws IOException
		{
			dos.writeUTF("LOBBY:" + String.join(" ", names));
		}
		
		private void processClientMessage(String input)
		{
			//System.out.println("Client " + playerIndex + " sent:\n" + input);
			String[] words = input.split(":");
			if (words[0].equals("ROLL"))
			{
				main.dispatchNetworkUserAction(
						new NetworkUserAction(
								new RollUserAction(),
								playerIndex));
			}
			else if (words[0].equals("BANK"))
			{
				main.dispatchNetworkUserAction(
						new NetworkUserAction(
								new BankUserAction(),
								playerIndex));
			}
			else if (words[0].equals("DIE"))
			{
				main.dispatchNetworkUserAction(
						new NetworkUserAction(
								new DieUserAction(Integer.parseInt(words[1])),
								playerIndex));
			}
		}
		
		public void close() throws IOException
		{
			socket.close();
			interrupt();
		}
		
		
	}
	

	
	public Server(Main main, int port)
	{
		super();
		this.main = main;
		this.port = port;
		
	}
	
	public void start() throws IOException
	{
		running = true;
		System.out.println("Starting server");
		serverSocket = new ServerSocket(port);
		
		lobbyFiller = new LobbyFiller();
		lobbyFiller.start();
		
		gameEventHandler = new GameEventHandler();
		gameEventHandler.start();
			
	}
	
	public void removeClient(int index)
	{
		clientHandlers.remove(index - 1);
		normalizeIndexes();
	}
	
	private void normalizeIndexes()
	{
		for (int i = 0; i < clientHandlers.size(); i++)
			clientHandlers.get(i).playerIndex = i + 1;  
	}
	
	public void close() throws IOException
	{
		running = false;
		System.out.println("Closing server");
		lobbyFiller.interrupt();
		
		gameEventHandler.interrupt();
		while(!clientHandlers.isEmpty())
		{
			clientHandlers.get(0).close();
			clientHandlers.remove(0);

		}
		serverSocket.close();
		
	}
	
	public void broadcastPlayerNames(String[] names) throws IOException
	{
		for (ClientHandler ch : clientHandlers)
		{
			ch.sendPlayerNames(names);
		}
	}

	public void broadcastStartGame() throws IOException 
	{
		for (ClientHandler ch : clientHandlers)
		{
			ch.sendMessage("START");
		}
	}

	public void startGame() throws IOException 
	{
		gameStarted = true;
		lobbyFiller.interrupt();
		serverSocket.close();
	}
	
	public void broadcastGameState(GameState state) throws IOException
	{
		for (ClientHandler ch : clientHandlers)
		{
			state.myTurn = state.currentPlayer == ch.playerIndex;
			ch.sendMessage("GAME:" + state.toString());
		}
	}
	
	
}



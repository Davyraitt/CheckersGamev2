package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class Server {
	
	
	private final int port = 19999;
	private ServerSocket serverSocket;
	private ArrayList<ServerClient> clients = new ArrayList<>();
	private HashMap<String, Thread> clientThreads = new HashMap<>();
	private boolean isRunning;
	private boolean alreadyContainsName;
	private boolean isAChatMessage;
	
	
	public static void main(String[] args) {
		
		System.out.println("Loading server");
		Server server = new Server();
		server.connect();

		
	}
	
	
	public void connect() {
		
		try {
			this.serverSocket = new ServerSocket(port);
			
			isRunning= true;
			while (isRunning) {
				
				System.out.println("Waiting for clients...");
				Socket socket = this.serverSocket.accept();
				
				
				System.out.println("Client connected via address: " + socket.getInetAddress().getHostAddress());
				System.out.println("Connected clients: " + this.clients.size());


				DataInputStream in = new DataInputStream(socket.getInputStream());


				String message = in.readUTF();
				System.out.println ("We received: " + message );
				
				alreadyContainsName = false;
				
				
				ServerClient serverClient = new ServerClient(socket, message, this);
				Thread t = new Thread(serverClient);
				t.start();
				
				System.out.println ("message is: "  + message );
				
				
				if (message.contains ( "chatmsgMessage" )) {
					System.out.println ("Message is a chat message!!!" );
					sendToAllClients ( message );
					isAChatMessage = true;
				}
				
				for ( int i = 0 ; i <  clients.size (); i++ )
				{
					
					if (this.clients.get ( i ).getName ().equals ( message )) {
						alreadyContainsName = true;
					}
				}
				
				
				if ( !alreadyContainsName && !isAChatMessage) {
					this.clientThreads.put(message, t);
					this.clients.add(serverClient);

				}
				
				isAChatMessage = false;
				
				
				alreadyContainsName = false;
				
				Thread readSocketThread = new Thread( () -> {
					receiveDataFromSocket(in);
				});
				
				readSocketThread.start();
				
//				sendToAllClients ( "Number of current clients: " + clients.size () ); // if we want to check how many clients are connected
				
				for ( int i = 0 ; i < clients.size ( ); i++ )
				{
					System.out.println ("Clients contains this name: " + clients.get ( i ).getName () );
				}
				
				
			}
			
			this.serverSocket.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receiveDataFromSocket(DataInputStream in) {
		String received = "";
		while (isRunning) {
			
			try {
				received = in.readUTF();
				System.out.println("Received this on method receiveDataFromSocket" + received);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void sendToAllClients(String text) {
		for (ServerClient client : clients) {
			client.writeUTF(text);
		}
	}
	
	
	public void writeStringToSocket(Socket socket, String text) {
		
		try {
			socket.getOutputStream().write(text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeClient(ServerClient serverClient) {
		String nickname = serverClient.getName();
		this.clients.remove(serverClient);
		
		Thread t = this.clientThreads.get(nickname);
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.clientThreads.remove(nickname);
		
	}
}

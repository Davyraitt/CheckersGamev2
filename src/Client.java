import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
	
	private String hostname;
	private int port;
	private boolean isConnected = true;
	private Socket socket ;
	private DataInputStream in ;
	private DataOutputStream out ;
	
	
	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		try
		{
			socket = new Socket(this.hostname, this.port);
		} catch ( IOException e )
		{
			e.printStackTrace ( );
		}
		try
		{
			assert socket != null;
			in = new DataInputStream(socket.getInputStream());
		} catch ( IOException e )
		{
			e.printStackTrace ( );
		}
		try
		{
			out = new DataOutputStream(socket.getOutputStream());
		} catch ( IOException e )
		{
			e.printStackTrace ( );
		}
		
	}
	
	public void connect(String nickName) {
		System.out.println("Connecting to server: " + this.hostname + " on port " + this.port);
		
		
		try {
			
			out.writeUTF(nickName);
			
			System.out.println("Connected as " + nickName);
			
			
			Thread readSocketThread = new Thread( () -> {
				receiveDataFromSocket(in);
			});
			
			readSocketThread.start();
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToServer (String nickname, String message) {
		try
		{
			out.writeUTF ( "Message by" + " " + nickname + ":  " + message);
		} catch ( IOException e )
		{
			e.printStackTrace ( );
		}
		
	}
	
	
	private void receiveDataFromSocket(DataInputStream in) {
		String received = "";
		while (isConnected) {
			
			try {
				received = in.readUTF();
				if (received.contains ( "Message" )) {
					System.out.println("before error");
					String finalReceived = received;
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							CheckersApp.setListOfMessages (finalReceived);
						}
// ...
					});

					System.out.println("after error");

				}
				
				System.out.println(received);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
}

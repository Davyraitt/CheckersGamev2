package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerOS {
    String host;
    Integer portnr;
    ServerSocket server;
    Boolean running;

    public ServerOS(String host, int portnr) {
        this.host = host;
        this.portnr = portnr;
        this.server = null;
        this.running = false;
        //Defining all the variables in the contrstuctor
    }

    public static void main(String[] args) {

        System.out.println("Loading server");
        ServerOS server = new ServerOS("127.0.0.1" , 10301);
        try {
            server.startServer();
        } catch (IOException e) {
            System.out.println("Can't start the server.... :( " + e.getMessage());
            e.printStackTrace();
        }


    }

    public void startServer() throws IOException {
        if (this.server != null) {
            System.out.println("Server is already running");
            return;
        }


        //Creating the server socket
        try {
            this.server = new ServerSocket(this.portnr);
            this.running = true;
        } catch (IOException e) {
            throw e;
        }

        //Creating a seperate thread
        while (this.running) {
            System.out.println("Waiting for clients to connect...");
            Socket client = this.server.accept(); // Blocking call, blijven dus wachten....

            // When this is defined, we get our first client.. After that we create a new thread to handle the connection
            new Thread(() -> {
                handleClientConnection(client);

            }).start();
        }
    }

    private void handleClientConnection(Socket client) {
        System.out.println("Client connected! Proceeding to handle the connection now");
        try (DataInputStream in = new DataInputStream(client.getInputStream());
             DataOutputStream out = new DataOutputStream(client.getOutputStream());) // In the try, it is now our resource. When this fails the connection will close

        {
            out.writeUTF("Hi! Welcome client");
            client.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean getRunning() {
        return running;
    }
}

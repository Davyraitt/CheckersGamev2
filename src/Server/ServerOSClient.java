package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerOSClient {

    Socket socket;
    String host;
    Integer port;
    boolean connected;
    DataInputStream in;
    DataOutputStream out;

    public ServerOSClient(String host, int port) {
        this.socket = null;
        this.host = host;
        this.port = port;
        this.connected = false;
        this.in = null;
        this.out = null;
    }





    public boolean connect() {
        if (this.connected) {
            System.out.println("Client already connected with the server");
            return true;
        }


        try {
            this.socket = new Socket(this.host, this.port);
            this.connected = true;

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());


            String message = this.in.readUTF(); // Blocking call..

            System.out.println("Got a welcome message from the server: +  " + message);


        } catch (IOException e) {
            System.out.println("Can't connect to server" +"    " + e.getMessage());
        }

        return false;
    }
}

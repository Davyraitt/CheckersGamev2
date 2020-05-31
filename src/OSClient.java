import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class OSClient implements Serializable{

    Socket socket;
    String host;
    Integer port;
    boolean connected;
    DataInputStream in;
    DataOutputStream out;

    ObjectInputStream inOb;
    ObjectOutputStream outOb;

    public OSClient(String host, int port) {
        this.socket = null;
        this.host = host;
        this.port = port;
        this.connected = false;
        this.in = null;
        this.out = null;
        this.inOb = null;
        this.outOb = null;
    }


    public boolean connectObject(Tile[][] boardTiles) {
        if (this.connected) {
            System.out.println("Client already connected with the server");
            return true;
        }


        try {
            System.out.println("Created the objectstreams");
            this.socket = new Socket(this.host, this.port);
            this.connected = true;


            this.outOb = new ObjectOutputStream(this.socket.getOutputStream());
            this.inOb = new ObjectInputStream(this.socket.getInputStream());

            while (this.connected) {
                this.outOb.writeObject(boardTiles);


            }


        } catch (IOException e) {
            System.out.println("Can't connect to server" + "    " + e.getMessage());
        }

        return false;
    }


    public boolean connectData() {
        if (this.connected) {
            System.out.println("Client already connected with the server");
            return true;
        }


        try {
            this.socket = new Socket(this.host, this.port);
            this.connected = true;

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());


            String serverMessage = this.in.readUTF();
            System.out.println("Connected with server: " + serverMessage);


            Scanner scanner = new Scanner(System.in);

            while (this.connected) {
                System.out.println("Enter your message:   ");
                String message = scanner.nextLine();
                this.out.writeUTF(message);

                String response = this.in.readUTF();
                System.out.println("Got this response: " + response);
            }


        } catch (IOException e) {
            System.out.println("Can't connect to server" + "    " + e.getMessage());
        }

        return false;
    }
}

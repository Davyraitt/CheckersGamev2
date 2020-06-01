import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class OSClient implements Serializable {

    Socket socket;
    String host;
    Integer port;
    boolean connected;
    DataInputStream in;
    DataOutputStream out;
    private ObjectOutputStream outOb;
    private ObjectInputStream inOb;


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
            //ObjectInputStream inObj = new ObjectInputStream(this.socket.getInputStream());
            this.outOb = new ObjectOutputStream(this.socket.getOutputStream());


//             while (this.connected) {

            outOb.writeObject(boardTiles);

            outOb.close();

//              }


        } catch (IOException e) {
            System.out.println("Can't connect to server" + "    " + e.getMessage());
        }

        return false;
    }

}

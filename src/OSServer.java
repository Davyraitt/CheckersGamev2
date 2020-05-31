import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class OSServer implements Serializable {
    String host;
    Integer portnr;
    ServerSocket server;
    Boolean running;
    Integer playerNumber;
    public static final int TILE_SIZE = 100; // i tile is 100*100
    public static final int WIDTH = 8; // the board is 8 width
    public static final int HEIGHT = 8; // the board is 8 height
    private Tile[][] boardTiles = new Tile[WIDTH][HEIGHT];
    private ArrayList<OSServerClient> clients = new ArrayList<>();


    public OSServer(String host, int portnr) {

        this.host = host;
        this.portnr = portnr;
        this.server = null;
        this.running = false;
        this.playerNumber = 0;
        //Defining all the variables in the contrstuctor
    }

    public static void main(String[] args) {

        System.out.println("Loading server");
        OSServer server = new OSServer("127.0.0.1", 10301);
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
            OSServerClient serverClient = new OSServerClient(client, this);
            this.clients.add(serverClient);
            System.out.println("There are currently this many people in the arraylist: " + this.clients.size());


            // When this is defined, we get our first client.. After that we create a new thread to handle the connection
            new Thread(() -> {
                handleClientConnectionObject(client);

            }).start();
        }
    }

    private void handleClientConnectionObject(Socket client) {
        System.out.println("Client connected! Proceeding to handle the connection now (ObjectStream)");
        System.out.println("Connected clients: " + this.clients.size());
        try (ObjectOutputStream outObj = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream inObj = new ObjectInputStream(client.getInputStream()))
        // In the try, it is now our resource. When this fails the connection will close

        {
            boolean connected = true;

            while (connected) {
                boardTiles = (Tile[][]) inObj.readObject();
                System.out.println("This is boardtiles : " + boardTiles);

            }


            client.close();


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        }

    }

    public void sendToAllClients(Tile[][] boardTiles) {
        for (OSServerClient client : clients) {
            client.writeArray(boardTiles);
        }
    }

    private void handleClientConnectionData(Socket client) {
        System.out.println("Client connected! Proceeding to handle the connection now");
        try (DataInputStream in = new DataInputStream(client.getInputStream());
             DataOutputStream out = new DataOutputStream(client.getOutputStream());) // In the try, it is now our resource. When this fails the connection will close

        {
            boolean connected = true;
            out.writeUTF("Hi! I am the Amstedam server");
            while (connected) {
                String messageReceived = in.readUTF();
                out.writeUTF(messageReceived);
            }


            client.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean getRunning() {
        return running;
    }
}

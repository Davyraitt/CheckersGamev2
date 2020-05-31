import java.io.*;
import java.net.Socket;

public class OSServerClient {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String name;
    private OSServer server;
    private boolean isConnected = true;

    public OSServerClient(Socket socket, OSServer server) {
        this.socket = socket;
        this.name = name;
        this.server = server;

        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeArray(Tile[][] boardTiles) {
        try {
            this.out.writeObject(boardTiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

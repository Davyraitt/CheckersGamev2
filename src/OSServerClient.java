import java.io.*;
import java.net.Socket;

public class OSServerClient implements Serializable, Runnable{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private OSServer server;
    private boolean isConnected = true;

    public OSServerClient(Socket socket, OSServer server) {
        this.socket = socket;
        this.server = server;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());

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

    @Override
    public void run() {
        System.out.println("running?");
    }
}

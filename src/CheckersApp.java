import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class CheckersApp extends Application implements Serializable {

    public static final int TILE_SIZE = 100; // i tile is 100*100
    public static final int WIDTH = 8; // the board is 8 width
    public static final int HEIGHT = 8; // the board is 8 height
    private Scene sceneMenu;
    private Scene sceneCheckers;

    private Group tileGroup = new Group(); //seperate groups for the tiles, Group is a collection from Javafx
    private Group pieceGroup = new Group(); //seperate groups for the pieces, Group is a collection from Javafx


    private Tile[][] boardTiles = new Tile[WIDTH][HEIGHT];

    private String hostname;
    private int port;
    private boolean isConnected = true;
    private Client client;

    private Stage globalStage;

    @FXML
    TextField loginFormUsername;

    @FXML
    TextField chatText;

    @FXML
    TextField chatNameText;

    @FXML
    ListView listView;

    @FXML
    Label labelAbovePlay;

    @FXML
    Button playButton;

    private static ObservableList<String> listOfMessages = FXCollections.observableList(new ArrayList<>());
    private OSClient checkersClient;

    @Override
    public void start(Stage window) throws Exception {
        this.hostname = "127.0.0.1";
        this.port = 10301;
        checkersClient = new OSClient("127.0.0.1", 10301);

        sceneMenu = new Scene(createContentMenu());

        window.setScene(sceneMenu);

        window.show();

        globalStage = window;

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void playButtonPressed(ActionEvent event) {
        Stage window2 = new Stage(); // new stage
        window2.initModality(Modality.APPLICATION_MODAL);
        window2.initOwner(globalStage);
        sceneCheckers = new Scene(createContentCheckers());
        window2.setScene(sceneCheckers);
        window2.show();

        //((Stage)(((Button)event.getSource()).getScene().getWindow())).close();


        // Play button is pressed, we are now going to create the checkers clients and server

        System.out.println("Creating a new checkerClient..");
        checkersClient.connectObject(boardTiles);


    }

    private Parent createContentCheckers() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup);

        //Creating our tiles
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                boardTiles[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.RED, x, y);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceType.BLUE, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().addAll(piece);
                }

            }

        }

        return root;
    }

    private Parent createContentMenu() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    private Piece makePiece(PieceType type, int x, int y) {
        Piece piece = new Piece(type, x, y);

        piece.setOnMouseReleased(e -> {
            int newX = convertToBoardCoordinates(piece.getLayoutX());
            int newY = convertToBoardCoordinates(piece.getLayoutY());

            MoveResult result = tryMove(piece, newX, newY);

            int x0 = convertToBoardCoordinates(piece.getOldMouseX());
            int y0 = convertToBoardCoordinates(piece.getOldMouseY());

            switch (result.getType()) {
                case NONE:
                    System.out.println("UNABLE TO MOVE");
                    piece.cancelMove();
                    break;
                case NORMAL:
                    System.out.println("NORMAL MOVE");
                    piece.move(newX, newY);
                    boardTiles[x0][y0].setPiece(null);
                    boardTiles[newX][newY].setPiece(piece);
                    break;
                case KILL:
                    System.out.println("KILL MOVE");
                    piece.move(newX, newY);
                    boardTiles[x0][y0].setPiece(null);
                    boardTiles[newX][newY].setPiece(piece);

                    Piece otherPiece = result.getPiece();
                    boardTiles[convertToBoardCoordinates(otherPiece.getOldMouseX())][convertToBoardCoordinates(otherPiece.getOldMouseY())].setPiece(null);
                    pieceGroup.getChildren().remove(otherPiece);
                    break;
            }
        });

        return piece;
    }

    private MoveResult tryMove(Piece piece, int newX, int newY) {
        if (boardTiles[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = convertToBoardCoordinates(piece.getOldMouseX());
        int y0 = convertToBoardCoordinates(piece.getOldMouseY());

        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (boardTiles[x1][y1].hasPiece() && boardTiles[x1][y1].getPiece().getType() != piece.getType()) {
                return new MoveResult(MoveType.KILL, boardTiles[x1][y1].getPiece());
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    private int convertToBoardCoordinates(double pixel) {
        return (int) (pixel + TILE_SIZE / 2) / TILE_SIZE; // divided by 2 because we want it to be centered
    }


    public void connectButtonPressed(ActionEvent event) {
        this.client = new Client(this.hostname, 10302);
        client.connect(loginFormUsername.getText());
        labelAbovePlay.setText("Welcome " + loginFormUsername.getText() + " press the play button to start " + "a" + " game!");
        chatNameText.setText(loginFormUsername.getText());
    }

    public void sendChatMessage(ActionEvent event) {
        this.client = new Client(this.hostname, 10302);
        client.sendMessageToServer(chatNameText.getText(), chatText.getText());
        listView.setItems(listOfMessages);

    }

    public static ObservableList<String> getListOfMessages() {
        return listOfMessages;
    }

    public static void setListOfMessages(String add) {
        listOfMessages.add(add);
    }
}

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;



public class Piece extends StackPane {
	
	private PieceType type;
	
	private double mouseX, mouseY;
	private double oldMouseX, oldMouseY;
	
	public Piece (PieceType type, int x, int y) {
		this.type = type;
		
		move ( x , y  );
		
		Ellipse backGroundEllipse = new Ellipse(CheckersApp.TILE_SIZE * 0.3125 ,CheckersApp.TILE_SIZE * 0.26  );
		backGroundEllipse.setFill ( Color.BLACK );
		backGroundEllipse.setStroke ( Color.BLACK  );
		backGroundEllipse.setStrokeWidth ( CheckersApp.TILE_SIZE * 0.03 );
		backGroundEllipse.setTranslateX ( (CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2 );
		backGroundEllipse.setTranslateY ( (CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2 + CheckersApp.TILE_SIZE * 0.07);
		// We want to center so we distract from the TILE_SIZE, which is why we - our radiusX from the ellipse
		// then we also have to think of the diameter, so we multiply by 2
		// but we only want to move half, so we divide by 2 again
	
		
		Ellipse shadowEllipse = new Ellipse(CheckersApp.TILE_SIZE * 0.3125 ,CheckersApp.TILE_SIZE * 0.26  );
		shadowEllipse.setFill ( getColor() );
		shadowEllipse.setStroke ( Color.BLACK );
		shadowEllipse.setStrokeWidth ( CheckersApp.TILE_SIZE * 0.03 );
		shadowEllipse.setTranslateX ( (CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.3125 * 2) / 2 );
		shadowEllipse.setTranslateY ( (CheckersApp.TILE_SIZE - CheckersApp.TILE_SIZE * 0.26 * 2) / 2 );
		// Here we change the Y axis a bit more, so we create a cool shadow
		
		getChildren ().addAll ( backGroundEllipse, shadowEllipse);
		
		setOnMousePressed ( e -> {
			mouseX = e.getSceneX ();
			mouseY = e.getSceneY ();
		
		} );
		
		setOnMouseDragged ( e -> {
			relocate ( e.getSceneX () - mouseX + oldMouseX , e.getSceneY () - mouseY + oldMouseY);
		} );
		
		
	}
	
	public void move (int x, int y) {
		oldMouseX = x * CheckersApp.TILE_SIZE;
		oldMouseY = y  * CheckersApp.TILE_SIZE;
		relocate ( oldMouseX, oldMouseY );
	
	}
	
	private Paint getColor ( ) {
		if (type == PieceType.RED) {
			return Color.valueOf ( Color.RED.toString ()); // setting the colour of the tiles
		}
		
		
		else {
			return Color.valueOf ( Color.BLUE.toString ()); // setting the colour of the tiles
		}
		
	}
	
	public void cancelMove() {
		relocate ( oldMouseX, oldMouseY );
	}
	
	public PieceType getType ( ) {
		return type;
	}
	
	public double getMouseX ( ) {
		return mouseX;
	}
	
	public double getMouseY ( ) {
		return mouseY;
	}
	
	public double getOldMouseX ( ) {
		return oldMouseX;
	}
	
	public double getOldMouseY ( ) {
		return oldMouseY;
	}
}

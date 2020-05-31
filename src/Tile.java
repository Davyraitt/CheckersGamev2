import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
	
	private Piece piece;
	private boolean light;
	
	
	public Tile(boolean light, int x, int y) {
		this.light = light;
		setWidth ( CheckersApp.TILE_SIZE );
		setHeight ( CheckersApp.TILE_SIZE  );
		relocate ( x * (CheckersApp.TILE_SIZE), y * (CheckersApp.TILE_SIZE) );
		setFill ( checkColour() );
		
	}
	
	private Paint checkColour ( ) {
		if (light) {
			return Color.valueOf ( Color.LIGHTGOLDENRODYELLOW.toString ()); // setting the colour of the tiles
		}
		
		else {
			return Color.valueOf ( Color.LIGHTSLATEGREY.toString () ); // setting the colour of the tiles
		}
	}
	
	public boolean hasPiece() {
		return piece != null;
	}
	
	public Piece getPiece ( ) {
		return piece;
	}
	
	public void setPiece ( Piece piece ) {
		this.piece = piece;
	}
}

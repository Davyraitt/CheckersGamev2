public enum PieceType {
	RED(1), BLUE (-1); // one goes down, the other one goes up in the y axis..
	
	final int moveDir; // in which direction do the pieces move?
	
	PieceType (int moveDir) {
		this.moveDir = moveDir;
	}
	
}

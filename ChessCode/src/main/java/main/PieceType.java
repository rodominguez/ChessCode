package main;

public enum PieceType {

	KING("K", "k", 1),
	QUEEN("Q", "q", 2),
	PAWN("P", "p", 3),
	KNIGHT("N", "n", 4),
	BISHOP("B", "b", 5),
	ROOK("R", "r", 6),
	CHARIOT("C", "c", 7),
	GENERAL("G", "g", 0),
	BLANK("1", "1", 0);
	
	public String whiteName, blackName;
	
	public int number;
	
	private PieceType (String whiteName, String blackName, int number) {
		this.whiteName = whiteName;
		this.blackName = blackName;
		this.number = number;
	}
	
	public static PieceType getFromNumber (int num) {
		switch (num) {
		case 0:
			return GENERAL;
		case 1:
			return KING;
		case 2:
			return QUEEN;
		case 3:
			return PAWN;
		case 4:
			return KNIGHT;
		case 5:
			return BISHOP;
		case 6:
			return ROOK;
		case 7:
			return CHARIOT;
		}
		return null;
	}
}

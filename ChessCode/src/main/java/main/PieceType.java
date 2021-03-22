package main;

public enum PieceType {
	
	KING("K", "k", 1),
	QUEEN("Q", "q", 2),
	PAWN("P", "p", 3),
	KNIGHT("N", "n", 4),
	BISHOP("B", "b", 5),
	ROOK("R", "r", 6),
	CHARIOT("C", "c", 7),
	BLANK("1", "1", 8);
	
	public String whiteName, blackName;
	
	public int number;
	
	private PieceType (String whiteName, String blackName, int number) {
		this.whiteName = whiteName;
		this.blackName = blackName;
		this.number = number;
	}
	
	public static PieceType getFromNumber (int num) {
		switch (num) {
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
		case 8:
			return BLANK;
		}
		return BLANK;
	}
}

package main;

public enum PieceType {
	
	KING("K", "k", 7),
	QUEEN("Q", "q", 1),
	PAWN("P", "p", 2),
	KNIGHT("N", "n", 3),
	BISHOP("B", "b", 4),
	ROOK("R", "r", 5),
	CHARIOT("C", "c", 6),
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
		case 7:
			return KING;
		case 1:
			return QUEEN;
		case 2:
			return PAWN;
		case 3:
			return KNIGHT;
		case 4:
			return BISHOP;
		case 5:
			return ROOK;
		case 6:
			return CHARIOT;
		case 0:
			return BLANK;
		}
		return BLANK;
	}
}

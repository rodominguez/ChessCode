package main;

public class Piece {

	public PieceType type;
	public int color;

	public Piece(PieceType type, int color) {
		this.type = type;
		this.color = color;
	}

	public static Piece[] pieceArrayFromBytes(byte[] bytes) {
		Piece[] res = new Piece[bytes.length * 2];
		int color1 = 1;
		int color2 = 16;

		int type1 = 14;
		int type2 = 224;

		int color;
		int pieceNum;

		for (int i = 0, j = 0; i < bytes.length; i++ , j += 2) {
			// First Half
			color = bytes[i] & color2;
			color >>>= 4;
			pieceNum = bytes[i] & type2;
			pieceNum >>>= 5;
			res[j] = new Piece(PieceType.getFromNumber(pieceNum), color);

			// Second Half
			
			color = bytes[i] & color1;
			pieceNum = bytes[i] & type1;
			pieceNum >>>= 1;
			res[j + 1] = new Piece(PieceType.getFromNumber(pieceNum), color);
		}
		
		return res;
	}
	
	public static byte[] pieceArrayToBytes (Piece[] pieces) {
		byte[] res = new byte[pieces.length / 2];
		int x, temp;
		for (int i = 0; i < pieces.length; i += 2) {
			x = pieces[i].type.number;
			x <<= 1;
			x += pieces[i].color;
			x <<=4;
			
			temp = pieces[i + 1].type.number;
			temp <<= 1;
			x += temp + pieces[i + 1].color;
			
			res[i] = (byte)x;
		}
		
		
		return res;
	}
	
	public static void main (String[] args) {
		String test = "hola como estas";
		pieceArrayFromBytes(test.getBytes());
	}

	public static Piece createPieceFromChar(char c) {
		switch (c) {
		case 'K':
			return new Piece(PieceType.KING, 1);
		case 'k':
			return new Piece(PieceType.KING, 0);
		case 'Q':
			return new Piece(PieceType.QUEEN, 1);
		case 'q':
			return new Piece(PieceType.QUEEN, 0);
		case 'P':
			return new Piece(PieceType.PAWN, 1);
		case 'p':
			return new Piece(PieceType.PAWN, 0);
		case 'N':
			return new Piece(PieceType.KNIGHT, 1);
		case 'n':
			return new Piece(PieceType.KNIGHT, 0);
		case 'B':
			return new Piece(PieceType.BISHOP, 1);
		case 'b':
			return new Piece(PieceType.BISHOP, 0);
		case 'R':
			return new Piece(PieceType.ROOK, 1);
		case 'r':
			return new Piece(PieceType.ROOK, 0);
		case 'C':
			return new Piece(PieceType.CHARIOT, 1);
		case 'c':
			return new Piece(PieceType.CHARIOT, 0);
		default:
			return new Piece(PieceType.BLANK, 0);
		}
	}
}

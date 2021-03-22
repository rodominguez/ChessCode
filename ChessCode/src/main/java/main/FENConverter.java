package main;

public class FENConverter {

	public static String pieceArrayToFEN(Piece[] pieces) {
		String res = "";

		int blankSpace = 0;
		for (int i = 0; i < pieces.length; i++) {
			if (i % 8 == 0 && i != 0) {
				
				if (blankSpace != 0) {
					res += blankSpace;
					blankSpace = 0;
				}
					
				res += "/";
			}
			if (!pieces[i].type.equals(PieceType.BLANK)) {

				if (blankSpace != 0) {
					res += blankSpace;
					blankSpace = 0;
				}

				if (pieces[i].color == 1)
					res += pieces[i].type.whiteName;
				else
					res += pieces[i].type.blackName;
				blankSpace = 0;
			} else
				blankSpace++;
		}

		return res;
	}

	public static Piece[] FENToPieceArray(String fen) {
		Piece[] res = new Piece[64];

		int i = 0;
		for (char c : fen.toCharArray()) {
			if (c != '/')
				try {
					int blanks = Integer.parseInt(c + "");
					for (int j = 0; j < blanks; j++) {
						res[i] = new Piece(PieceType.BLANK, 0);
						i++;
					}
				} catch (Exception e) {
					res[i] = Piece.createPieceFromChar(c);
					i++;
				}
		}

		return res;
	}
}

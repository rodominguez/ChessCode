package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessBoard {

	private Piece board[][];
	public final int COLS, ROWS;
	
	public ChessBoard(int c, int r) {
		COLS = c;
		ROWS = r;
		board = new Piece[ROWS][COLS];
		initEmptyBoard();
	}
	
	public ChessBoard (int c, int r, Piece[] pieces) {
		this(c, r);
		convertToMatrix(pieces);
	}
	
	private void convertToMatrix(Piece[] pieces){
		for (int i = 0; i < pieces.length; i++)
			board[i / COLS][i % COLS] = pieces[i];
	}
	
	private void initEmptyBoard() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				board[i][j] = new Piece(PieceType.BLANK, 0);
			}
		}
	}
	
	public String toFEM() {
		// Get FEM string from board
		return "";
	}
	
	public void fromFEM(String femStr) {
		
	}
	
	public Piece get(int r, int c) {
		return board[r][c];
	}
	
	public void set(int r, int c, PieceType type, int color) {
		board[r][c].type = type;
		board[r][c].color = color;
	}
	
	public static List<ChessBoard> boardsFromPieces (int c, int r, Piece[] pieces) {
		List<ChessBoard> res = new ArrayList<>();
		
		for (int i = 0; i < pieces.length; i += c * r)
			res.add(new ChessBoard(c, r, Arrays.copyOfRange(pieces, i, i + c * r)));
		
		return res;
	}
}

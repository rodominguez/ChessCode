package main;

public class ChessBoard {

	private Piece board[][];
	public final int COLS, ROWS;
	
	public ChessBoard(int c, int r) {
		COLS = c;
		ROWS = r;
		board = new Piece[ROWS][COLS];
		initEmptyBoard();
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
		// Set Board pieces to FEM
	}
	
	public Piece get(int r, int c) {
		return board[r][c];
	}
	
	public void set(int r, int c, PieceType type, int color) {
		board[r][c].type = type;
		board[r][c].color = color;
	}
}

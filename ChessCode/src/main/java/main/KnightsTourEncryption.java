package main;

public class KnightsTourEncryption {

	private KnightsTour knightsTour;
	
	private char[][] blocks;
	
	public KnightsTourEncryption(long seed) {
		knightsTour = new KnightsTour(seed);
		knightsTour.solve();
	}
	
	public char[] encrypt(char[] chars) {
		blocks = new char[(int)Math.ceil(chars.length / 64.0)][64];
		for (int i = 0; i < chars.length; i++) {
			blocks[i / 64][knightsTour.getSolution()[i % 64] - 1] = chars[i];
		}
		
		return linearization(blocks);
	}
	
	public Piece[] encrypt (Piece[] pieces) {
		Piece[][] blocks = new Piece[(int)Math.ceil(pieces.length / 64.0)][64];
		for (int i = 0; i < pieces.length; i++) {
			blocks[i / 64][knightsTour.getSolution()[i % 64] - 1] = pieces[i];
		}
		
		return linearization(blocks);
	}
	
	private char[] linearization(char[][] matrix) {
		char[] res = new char[matrix.length * 64];
		
		for (int i = 0; i < matrix.length * 64; i++)
			res[i] = matrix[i / 64][i % 64];
		
		return res;
	}
	
	private Piece[] linearization(Piece[][] matrix) {
		Piece[] res = new Piece[matrix.length * 64];
		
		for (int i = 0; i < matrix.length * 64; i++)
			res[i] = matrix[i / 64][i % 64];
		
		return res;
	}
	
	public char[] decrypt(char[] chars) {
		char[][] result = new char[(int)Math.ceil(chars.length / 64)][64];
		for (int i = 0; i < chars.length; i++)
			result[i / 64][i % 64] = chars[(i / 64) * 64 + knightsTour.getSolution()[i % 64] - 1];
			
		return linearization(result);
	}
	
	public Piece[] decrypt(Piece[] pieces) {
		Piece[][] result = new Piece[(int)Math.ceil(pieces.length / 64)][64];
		for (int i = 0; i < pieces.length; i++)
			result[i / 64][i % 64] = pieces[(i / 64) * 64 + knightsTour.getSolution()[i % 64] - 1];
			
		return linearization(result);
	}
	
}

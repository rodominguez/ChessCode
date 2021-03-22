package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import neuralNetwork.DecryptAlphabetNetwork;
import neuralNetwork.EncryptAlphabetNetwork;

public class Controller {

	private SettingsPanel settingsPanel;
	
	private ChessBoardDisplay chessBoardDisplay;
	
	private ChessBoardDisplay chessBoardDisplayKnightsKey;
	
	private EncryptAlphabetNetwork encryptNetwork;
	
	private DecryptAlphabetNetwork decryptNetwork;
	
	private KnightsTourEncryption knightsTourEncryption;
	
	private List<ChessBoard> boards;
	
	private int indexBoard;
	
	public Controller() {
		settingsPanel = new SettingsPanel();
		settingsPanel.setController(this);
		chessBoardDisplay = new ChessBoardDisplay(640, 640);
		chessBoardDisplayKnightsKey = new ChessBoardDisplay(640, 640);
		
		encryptNetwork = new EncryptAlphabetNetwork("Encrypt Network");
		decryptNetwork = new DecryptAlphabetNetwork("Decrypt Network",
				encryptNetwork.getAlphabet(), encryptNetwork.getInverseAlphabet());
		knightsTourEncryption = new KnightsTourEncryption();
	}
	
	public String encrypt (String code) {
		if (!encryptNetwork.getIsTrained())
			return "Network not trained";
		boards = new LinkedList<>();
		indexBoard = 0;
		
		String encrypt = encryptNetwork.encrypt(code);
		chessBoardDisplay.showNeuralSubstitution(code, encrypt);
		
		System.out.println("NN Encrypt: " + encrypt);
		
		char[] encryptedKnights = knightsTourEncryption.encrypt(encrypt.toCharArray());
		
		System.out.println("Knight Encrypt: " + String.valueOf(encryptedKnights));
		
		Piece[] pieces = Piece.pieceArrayFromBytes(String.valueOf(encryptedKnights).getBytes());
		
		System.out.println("Bytes: " + printByteArray(String.valueOf(encryptedKnights).getBytes()));
		
		System.out.println("Pieces: " + Piece.arrayToString(pieces));
		
		pieces = knightsTourEncryption.encrypt(pieces);
		
		System.out.println("Pieces Shuffle: " + Piece.arrayToString(pieces));
		
		boards = ChessBoard.boardsFromPieces(8, 8, pieces);
		
		chessBoardDisplay.showMessageChessboard(boards.get(indexBoard));
		
		System.out.println("FEN: " + FENConverter.pieceArrayToFEN(pieces));
		
		return FENConverter.pieceArrayToFEN(pieces);
	}
	
	public String decrypt (String encription) {
		if (!decryptNetwork.getIsTrained())
			return "Network not trained";
		boards = new LinkedList<>();
		indexBoard = 0;
		
		System.out.println("FEN: " + encription);
		
		Piece[] pieces = FENConverter.FENToPieceArray(encription);
		
		System.out.println("Pieces: " + Piece.arrayToString(pieces));
		
		boards = ChessBoard.boardsFromPieces(8, 8, pieces);
		
		chessBoardDisplay.showMessageChessboard(boards.get(indexBoard));
		
		pieces = knightsTourEncryption.decrypt(pieces);
		
		System.out.println("Pieces Unshuffle: " + Piece.arrayToString(pieces));
		
		byte[] bytes = Piece.pieceArrayToBytes(pieces);
		
		System.out.println("Bytes: " + printByteArray(bytes));
		
		char[] decryptedKnights = new String(bytes).toCharArray();
		
		System.out.println("Knight Encrypt: " + String.valueOf(decryptedKnights));
		
		decryptedKnights = knightsTourEncryption.decrypt(decryptedKnights);
		
		String decription = decryptNetwork.decrypt(String.valueOf(decryptedKnights));
		
		return decription;
	}
	
	public void createAlphabet () throws InterruptedException {
		
		setSeed(new Random().nextLong());
		
		encryptNetwork.start();
		decryptNetwork.start();
	}
	
	public void export () {
		encryptNetwork.weightsToFile();
		decryptNetwork.weightsToFile();
	}
	
	public void setSeed (Long seed) {
		knightsTourEncryption.setSeed(seed);
		chessBoardDisplayKnightsKey.showKnightsTour(new ChessBoard(8, 8), knightsTourEncryption.getSolution(), false);
	}
	
	public long getSeed() {
		return knightsTourEncryption.getSeed();
	}
	
	public void nextBoard() {
		indexBoard++;
		if (indexBoard < boards.size()) {
			chessBoardDisplay.showMessageChessboard(boards.get(indexBoard));
		}
	}
	
	public String printByteArray (byte[] bytes) {
		String res = "";
		
		for (int i = 0; i < bytes.length; i++) {
			res += bytes[i] + ", ";
		}
		
		return res;
	}
}

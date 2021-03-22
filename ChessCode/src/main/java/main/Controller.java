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
		knightsTourEncryption = new KnightsTourEncryption(23444);
	}
	
	public String encrypt (String code) {
		boards = new LinkedList<>();
		indexBoard = 0;
		
		String encrypt = encryptNetwork.encrypt(code);
		chessBoardDisplay.showNeuralSubstitution(code, encrypt);
		
		char[] encryptedKnights = knightsTourEncryption.encrypt(encrypt.toCharArray());
		
		Piece[] pieces = Piece.pieceArrayFromBytes(String.valueOf(encryptedKnights).getBytes());
		
		pieces = knightsTourEncryption.encrypt(pieces);
		
		boards = ChessBoard.boardsFromPieces(8, 8, pieces);
		
		chessBoardDisplay.showMessageChessboard(boards.get(indexBoard));
		
		return FENConverter.pieceArrayToFEN(pieces);
	}
	
	public void createAlphabet () throws InterruptedException {
		
		setSeed(new Random().nextLong());
		
		encryptNetwork.start();
		decryptNetwork.start();
		
		encryptNetwork.join();
		decryptNetwork.join();
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
}

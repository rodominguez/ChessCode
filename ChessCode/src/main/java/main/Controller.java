package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
		if (code.equals(""))
			return "";
		boards = new LinkedList<>();
		indexBoard = 0;
		
		String encrypt = encryptNetwork.encrypt(code);
		//chessBoardDisplay.showNeuralSubstitution(code, encrypt);
		
		System.out.println("NN Encrypt: " + encrypt);
		
		char[] encryptedKnights = knightsTourEncryption.encrypt(encrypt.toCharArray());
		
		System.out.println("Knight Encrypt: " + String.valueOf(encryptedKnights));
		
		Piece[] pieces = Piece.pieceArrayFromBytes(String.valueOf(encryptedKnights).getBytes());
		
		System.out.println("Bytes: " + printByteArray(String.valueOf(encryptedKnights).getBytes()));
		
		System.out.println("Pieces: " + Piece.arrayToString(pieces));
		
		pieces = knightsTourEncryption.encrypt(pieces);
		
		System.out.println("Pieces Shuffle: " + Piece.arrayToString(pieces));
		
		boards = ChessBoard.boardsFromPieces(8, 8, pieces);
		
		chessBoardDisplay.setBoard(boards.get(indexBoard));
		
		chessBoardDisplay.showMessageChessboard();
		
		System.out.println("FEN: " + FENConverter.pieceArrayToFEN(pieces));
		
		return FENConverter.pieceArrayToFEN(pieces);
	}
	
	public String decrypt (String encription) {
		if (!decryptNetwork.getIsTrained())
			return "Network not trained";
		if (encription.equals(""))
			return "";
		boards = new LinkedList<>();
		indexBoard = 0;
		
		System.out.println("FEN: " + encription);
		
		Piece[] pieces = FENConverter.FENToPieceArray(encription);
		
		System.out.println("Pieces: " + Piece.arrayToString(pieces));
		
		boards = ChessBoard.boardsFromPieces(8, 8, pieces);
		
		chessBoardDisplay.setBoard(boards.get(indexBoard));
		
		chessBoardDisplay.showMessageChessboard();
		
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
	
	public void loadWeights(byte[] bytes) {
		String string = new String(bytes);
		
		encryptNetwork.loadFile(string.split("\n")[0].getBytes());
		decryptNetwork.loadFile(string.split("\n")[1].getBytes());
	}
	
	public void export () {
		try {
		      File myObj = new File("Key.txt");
		      if (myObj.createNewFile()) {
		        System.out.println("File created: " + myObj.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		try {
		      FileWriter myWriter = new FileWriter("Key.txt");
		      myWriter.write(encryptNetwork.convertWeightsToString());
		      myWriter.write("\n");
		      myWriter.write(decryptNetwork.convertWeightsToString());
		      myWriter.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public void setSeed (Long seed) {
		knightsTourEncryption.setSeed(seed);
		chessBoardDisplayKnightsKey.setSolution(knightsTourEncryption.getSolution());
		chessBoardDisplayKnightsKey.showKnightsTour();
	}
	
	public long getSeed() {
		return knightsTourEncryption.getSeed();
	}
	
	public void nextBoard() {
		indexBoard++;
		if (indexBoard < boards.size()) {
			chessBoardDisplay.setBoard(boards.get(indexBoard));
			chessBoardDisplay.showMessageChessboard();
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

package main;
import java.util.Scanner;

import neuralNetwork.DecryptAlphabetNetwork;
import neuralNetwork.EncryptAlphabetNetwork;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Scanner scan = new Scanner(System.in);
		EncryptAlphabetNetwork encryptNetwork = new EncryptAlphabetNetwork("Encrypt Network");
		DecryptAlphabetNetwork decryptNetwork = new DecryptAlphabetNetwork("Decrypt Network",
				encryptNetwork.getAlphabet(), encryptNetwork.getInverseAlphabet());
		KnightsTourEncryption knightsTourEncryption = new KnightsTourEncryption(23444);
		
		encryptNetwork.start();
		decryptNetwork.start();
		
		encryptNetwork.join();
		decryptNetwork.join();
		
		int reps = 0;
		while(reps < 10) {
			
			String code = scan.nextLine();
			
			String encrypt = encryptNetwork.encrypt(code);
			
			char[] encryptedKnights = knightsTourEncryption.encrypt(encrypt.toCharArray());
			
			char[] decryptedKnights = knightsTourEncryption.decrypt(encryptedKnights);
			
			String decrypt = decryptNetwork.decrypt(String.valueOf(decryptedKnights));
			
			System.out.println("Encryption Neural Network: " + encrypt);
			System.out.println("Encryption Knights Tour: " + String.valueOf(encryptedKnights));
			System.out.println("Decryption Knights Tour: " + String.valueOf(decryptedKnights));
			System.out.println("Decryption Neural Network: " + decrypt);
			
			reps++;
		}
		
		scan.close();
	}
}

package neuralNetwork;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class EncryptAlphabetNetwork implements Runnable{

	private static final int offset = 96;

	private static final int ALPHABET_SIZE = 128 - offset;

	private static final int bindSize = 4;

	private HashMap<Character, String> alphabet;

	private HashMap<String, Character> inverseAlphabet;

	private NeuralNetwork neuralNetwork;
	
	private Thread thread;
	
	private String name;

	public EncryptAlphabetNetwork(String name) {
		this.name = name;
		inverseAlphabet = new HashMap<String, Character>();
		alphabet = createAlphabet();
		neuralNetwork = new NeuralNetwork(constructSamples(), constructYs());
	}
	
	public void start () {
		thread = new Thread(this);
		thread.start();
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}
	
	@Override
	public void run() {
		neuralNetwork.train();
		System.out.println(name + " has finished training with a " + neuralNetwork.calculateError() + " error.");
	}

	public String encrypt(String s) {
		//printAlphabet(alphabet);
		s = s.replaceAll("" + (char)32, "" + (char)96);
		String string = "";
		for (char c : s.toCharArray()) {
			float[] res = neuralNetwork.evaluate(getSampleFromCharBias(("" + c).toCharArray()));
			//printVector(res);
			string += printEvaluation(res);
		}
		
		return string;
	}

	private float[] getSampleFromChar(char[] chars) {
		float[] res = new float[ALPHABET_SIZE];

		for (char c : chars) {
			int ascii = (int) c - offset;

			res[ascii] = 1;
		}

		return res;
	}

	private float[] getSampleFromCharBias(char[] chars) {
		float[] res = new float[ALPHABET_SIZE + 1];
		res[0] = 1;

		// for (int i = 1; i < res.length; i++)
		// res[i] = 1 / ALPHABET_SIZE;

		for (char c : chars) {
			int ascii = (int) c - offset;

			res[ascii + 1] = 1;
		}

		return res;
	}

	private float[][] constructSamples() {
		float[][] res = new float[ALPHABET_SIZE][ALPHABET_SIZE + 1];

		int i = 0;
		for (char c : alphabet.keySet()) {
			res[i] = getSampleFromCharBias(("" + c).toCharArray());
			i++;
		}

		return res;
	}

	private float[][] constructYs() {
		float[][] res = new float[ALPHABET_SIZE][ALPHABET_SIZE];

		int i = 0;
		for (char c : alphabet.keySet()) {
			res[i] = getSampleFromChar(alphabet.get(c).toCharArray());
			i++;
		}

		return res;
	}

	private HashMap<Character, String> createAlphabet() {
		Random r = new Random();
		HashMap<Character, String> res = new HashMap<>();
		for (int i = 0; i < 128 - offset; i++) {
			String bind = "";
			while (bind.equals("") || inverseAlphabet.containsKey(bind)) {
				bind = "";
				int[] bindArray = new int[bindSize];
				for (int j = 0; j < bindSize; j++) {
					int next = (r.nextInt(ALPHABET_SIZE) + offset);
					while (isIntInArray(bindArray, next))
						next = (r.nextInt(ALPHABET_SIZE) + offset);
					bindArray[j] = next;
				}
				Arrays.sort(bindArray);
				for (int j = 0; j < bindSize; j++)
					bind += (char) bindArray[j];
			}
			res.put((char) (i + offset), bind);
			inverseAlphabet.put(bind, (char) (i + offset));
		}

		return res;
	}
	
	private boolean isIntInArray(int[] array, int x) {
		for (int i = 0; i < array.length; i++)
			if (array[i] == x)
				return true;
		return false;
	}

	private void printAlphabet(HashMap<Character, String> alphabet) {
		int i = 0;
		for (char c : alphabet.keySet()) {
			System.out.println(i + " - " + c + " - " + alphabet.get(c));
			i++;
		}
	}

	private String printEvaluation(float[] y) {
		String res = "";
		for (int i = 0; i < y.length; i++)
			if (y[i] >= 0.5)
				res += (char) (i + offset);

		return res;
	}

	private void printVector(float[] vector) {
		for (int i = 0; i < vector.length; i++) {
			System.out.print(vector[i] + ",");
		}
		System.out.println("");
	}

	public HashMap<Character, String> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(HashMap<Character, String> alphabet) {
		this.alphabet = alphabet;
	}

	public HashMap<String, Character> getInverseAlphabet() {
		return inverseAlphabet;
	}

	public void setInverseAlphabet(HashMap<String, Character> inverseAlphabet) {
		this.inverseAlphabet = inverseAlphabet;
	}
}

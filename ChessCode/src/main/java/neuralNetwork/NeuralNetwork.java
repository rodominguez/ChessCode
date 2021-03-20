package neuralNetwork;
import java.util.Random;

public class NeuralNetwork {

	private static final float MIN_ERROR = 0.000001f;

	private static final float ALFA = 0.01f;

	private static final int MAX_EPOCHS = 100000;

	private float weights[][];

	private float x[][];

	private float y[][];

	public NeuralNetwork(float[][] x, float[][] y) {
		this.x = x;
		this.y = y;
		weights = new float[y[0].length][x[0].length];
		System.out.println("Original X");
		printMatrix(x);
		// x = scaleX(x);
		// System.out.println("Scaled X");
		// printMatrix(x);
		randomWeights();
	}

	public float[] evaluate(float[] x) {
		//printMatrix(weights);
		float[][] xM = new float[1][x.length];
		xM[0] = x;
		// xM = scaleX(xM);
		float[] res = new float[weights.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = hypothesis(weights[i], xM[0]);
		}

		return res;
	}

	public void train() {
		int epochs = 0;
		while (epochs < MAX_EPOCHS) {
			weights = GD(weights, x, y, ALFA);
			// System.out.println("Error");
			if (epochs % 100 == 0)
				System.out.println(calculateError());
			// System.out.println("Weights");
			// printMatrix(weights);
			epochs++;
		}
	}

	private float[][] GD(float[][] weights, float[][] samples, float[][] y, float alfa) {
		float[][] temp = copyMatrix(weights);
		for (int k = 0; k < weights.length; k++) {
			for (int j = 0; j < weights[k].length; j++) {
				float acum = 0;
				float error = 0;
				float predictionAverage = 0;
				for (int i = 0; i < samples.length; i++) {
					float prediction = hypothesis(weights[k], samples[i]);
					error = prediction - y[i][k];
					acum += error * samples[i][j];
					predictionAverage += prediction;
				}
				predictionAverage /= samples.length;
				acum /= samples.length;
				//float gradient = sigmoidDer(predictionAverage);
				float step = acum * alfa;
				temp[k][j] = weights[k][j] - step;
			}
		}
		return temp;
	}

	public float calculateError() {
		float error = 0;

		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < weights.length; j++) {
				error += Math.pow((hypothesis(weights[j], x[i]) - y[i][j]), 2);
			}
		}

		error /= x.length;

		return error;
	}

	private float[][] scaleX(float[][] original) {
		for (int i = 1; i < original[0].length; i++) {
			float acum = 0;
			float max_val = 0;
			for (int j = 0; j < original.length; j++) {
				acum += original[j][i];
				if (original[j][i] > max_val)
					max_val = original[j][i];
			}
			float avg = acum / original.length;
			for (int j = 0; j < original.length; j++) {
				original[j][i] = (original[j][i] - avg) / max_val;
			}
		}
		return original;
	}

	private void randomWeights() {
		Random r = new Random();
		for (int i = 0; i < weights.length; i++)
			// weights[i][i + 1] = 1;
			for (int j = 0; j < weights[i].length; j++)
				weights[i][j] = r.nextFloat() * (r.nextBoolean() ? -1 : 1);
	}

	private float hypothesis(float[] weights, float[] input) {
		float res = 0f;
		for (int i = 0; i < input.length; i++) {
			res += weights[i] * input[i];
		}
		return res;
	}

	private float sigmoid(float x) {
		return (float) (1f / (1f + Math.exp(-x)));
	}

	private float sigmoidDer(float x) {
		return sigmoid(x) * (1f - sigmoid(x));
	}

	private void printVector(float[] vector) {
		for (int i = 0; i < vector.length; i++) {
			System.out.print(vector[i] + ",");
		}
		System.out.println("");
	}

	private float[][] copyMatrix(float[][] origin) {
		float[][] res = new float[origin.length][origin[0].length];

		for (int i = 0; i < origin.length; i++)
			for (int j = 0; j < origin[i].length; j++)
				res[i][j] = origin[i][j];

		return res;
	}

	private void printMatrix(float[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + ", ");
			}
			System.out.println("");
		}
	}
}

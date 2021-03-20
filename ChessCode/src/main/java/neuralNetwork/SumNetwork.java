package neuralNetwork;

public class SumNetwork {

	private NeuralNetwork neuralNetwork;
	
	public SumNetwork(){
		float x[][] = {{1,1,1},{1,2,2},{1,3,3},{1,4,4},{1,5,5}};
		float y[][] = {{2},{4},{6},{8},{10}};
		neuralNetwork = new NeuralNetwork(x,y);
		neuralNetwork.train();
		float eval[] = {1,4,7};
		float res[] = neuralNetwork.evaluate(eval);
		System.out.println("Result");
		printResult(res);
	}
	
	public void printResult(float[] res) {
		for (int i = 0; i < res.length; i++)
			System.out.println(res[i]);
	}
}

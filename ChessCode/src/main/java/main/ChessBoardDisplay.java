package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ChessBoardDisplay extends JPanel implements Runnable {

	private JFrame jFrame;
	private final int WIDTH, HEIGHT;
	private BufferedImage bImages[];
	private BufferedImage wImages[];
	private BufferedImage netImage;

	private final Color DARK = new Color(90, 40, 1);
	private final Color LIGHT = new Color(201, 134, 62);

	private Thread thread;
	private ChessBoard board;
	private int[] solution;
	private int state;
	
	private int lookup = 1;

	public ChessBoardDisplay(int width, int height) {
		super();
		board = new ChessBoard(8, 8);
		WIDTH = width;
		HEIGHT = height;

		createWindow();
		loadImages();
		showNeuralSubstitution("", "");

		start();
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void join() throws InterruptedException {
		thread.join();
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(100);
				repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		g.fillRect(0, 0, 10000, 10000);
		switch (state) {
		case 1:
			showKnightsTour(g, board, solution, true);
			break;
		case 2:
			showMessageChessboard(g, board);
			break;
		}
	}

	private void createWindow() {
		jFrame = new JFrame();
		jFrame.setTitle("Play Cipher");
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setResizable(false);
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		jFrame.add(this);
		jFrame.pack();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.show();
	}

	private void loadImages() {
		bImages = new BufferedImage[8];
		wImages = new BufferedImage[8];
		try {
			String bNames[] = { "kingb.png", "queenb.png", "pawnb.png", "knightb.png", "bishopb.png", "rookb.png",
					"chariotb.png", "generalb.png" };
			String wNames[] = { "kingw.png", "queenw.png", "pawnw.png", "knightw.png", "bishopw.png", "rookw.png",
					"chariotw.png", "generalw.png" };
			for (int i = 0; i < bNames.length; i++) {
				bImages[i] = (BufferedImage) ImageIO.read(getClass().getClassLoader().getResource("./" + bNames[i]));
				wImages[i] = (BufferedImage) ImageIO.read(getClass().getClassLoader().getResource("./" + wNames[i]));
			}
			netImage = (BufferedImage) ImageIO.read(getClass().getClassLoader().getResource("./network.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clear(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	public void drawTilePattern(Graphics g, ChessBoard board) {
		int rows = board.ROWS;
		int cols = board.COLS;
		int cellWidth = WIDTH / cols, cellHeight = HEIGHT / rows;

		for (int i = 0; i < board.ROWS; i++) {
			int index = i % 2;
			for (int j = 0; j < board.COLS; j++) {
				if (index % 2 == 0)
					g.setColor(DARK);
				else
					g.setColor(LIGHT);

				g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

				index++;
			}
		}
	}

	public void drawBoardPieces(Graphics g, ChessBoard board) {
		int rows = board.ROWS;
		int cols = board.COLS;
		int cellWidth = WIDTH / cols, cellHeight = HEIGHT / rows;

		for (int i = 0; i < board.ROWS; i++) {
			for (int j = 0; j < board.COLS; j++) {
				Piece p = board.get(i, j);
				if (p.type == PieceType.BLANK)
					continue;
				BufferedImage imgType = p.color == 0 ? bImages[p.type.ordinal()] : wImages[p.type.ordinal()];
				g.drawImage(imgType, j * cellWidth, i * cellHeight, cellWidth, cellHeight, null);
			}
		}
	}

	public void drawTourNumbers(Graphics g, ChessBoard board, int solution[], boolean animated) {
		int rows = board.ROWS;
		int cols = board.COLS;
		int cellWidth = WIDTH / cols, cellHeight = HEIGHT / rows;
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		g.setColor(Color.WHITE);

		if (animated) {
			// Buscar la siguiente casilla a donde hay que saltar
			for (int i = 0; i < solution.length; i++) {
				if (solution[i] < lookup) {
					int r = i / cols;
					int c = i % cols;
					g.drawString(Integer.toString(solution[i]), c * cellWidth + cellWidth / 2 - 8,
							r * cellHeight + cellHeight / 2 + 8);
				}
			}
			for (int i = 0; i < solution.length && lookup < solution.length+1; i++) {
				if (solution[i] == lookup) {
					int r = i / cols;
					int c = i % cols;
					g.drawString(Integer.toString(solution[i]), c * cellWidth + cellWidth / 2 - 8,
							r * cellHeight + cellHeight / 2 + 8);
					
					lookup++;
					break;
				}
			}
		} else {
			for (int i = 0; i < board.ROWS; i++) {
				for (int j = 0; j < board.COLS; j++) {
					int index = i * cols + j;
					g.drawString(Integer.toString(solution[index]), j * cellWidth + cellWidth / 2 - 8,
							i * cellHeight + cellHeight / 2 + 8);
				}
			}
		}
	}

	public void showNeuralSubstitution(String inMessage, String outMessage) {
		SwingUtilities.invokeLater(() -> {
			Graphics g = getGraphics();
			g.setFont(new Font("TimesRoman", Font.PLAIN, 36));
			clear(g);

			g.setColor(Color.BLACK);
			g.drawString("In: " + inMessage, 20, 60);
			g.drawString("Out: " + outMessage, 20, 580);

			g.drawImage(netImage, 20, 80, 400, 400, null);
		});
	}

	public void showKnightsTour() {
		lookup = 1;
		state = 1;
	}

	private void showKnightsTour(Graphics g, ChessBoard emptyBoard, int solution[], boolean animated) {
		drawTilePattern(g, emptyBoard);

		drawTourNumbers(g, emptyBoard, solution, animated);
	}

	public void showMessageChessboard() {
		state = 2;
	}

	private void showMessageChessboard(Graphics g, ChessBoard board) {
		drawTilePattern(g, board);
		drawBoardPieces(g, board);
	}

	public void showFEMMessage(String fem) {
		SwingUtilities.invokeLater(() -> {
			Graphics g = getGraphics();
			g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
			clear(g);

			g.setColor(Color.BLACK);
			g.drawString("FEM: " + fem, 20, 100);
		});
	}

	public static void main(String args[]) {

		ChessBoard board = new ChessBoard(8, 8);
		board.set(2, 3, PieceType.QUEEN, 0);
		board.set(0, 0, PieceType.CHARIOT, 1);
		ChessBoardDisplay display = new ChessBoardDisplay(640, 640);

		ArrayList<Integer> solList = new ArrayList<Integer>();
		for (int i = 0; i < 64; i++)
			solList.add(i + 1);
		Collections.shuffle(solList);
		int sol[] = new int[64];
		for (int i = 0; i < 64; i++)
			sol[i] = solList.get(i);

		/*new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
//					display.showNeuralSubstitution("Hola Mundo!", "xsada3r3arfwawdsaawseaeda");
//					display.showFEMMessage("p3pp4\\a");
					display.showMessageChessboard(board);
//					display.showKnightsTour(board, sol, true);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}).start();*/
	}

	public ChessBoard getBoard() {
		return board;
	}

	public void setBoard(ChessBoard board) {
		this.board = board;
	}

	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}

}

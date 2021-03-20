package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessBoardDisplay extends JPanel implements Runnable {
	
	private JFrame jFrame;
	
	private final int WIDTH, HEIGHT;
	private Thread thread;
	
	private ChessBoard board;
	
	public ChessBoardDisplay(int width, int height, ChessBoard board) {
		super();
		WIDTH = width;
		HEIGHT = height;
		
		thread = new Thread(this);
		
		this.board = board;
		createWindow();
	}

	
	private void createWindow() {
		jFrame = new JFrame();
		jFrame.setTitle("Play Cipher");
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		jFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLocationRelativeTo(null);
		
		setSize(WIDTH, HEIGHT);
		jFrame.setVisible(true);
		jFrame.add(this);
		jFrame.show();
	}
	
	public void start() {
		thread.start();
	}
	
	public void drawTilePattern(Graphics g, ChessBoard board) {
		int rows = board.ROWS;
		int cols = board.COLS;
		
		int cellWidth = WIDTH / cols,
			cellHeight = HEIGHT / rows;
		
		for (int i = 0; i < board.ROWS; i ++) {
			int index = i % 2;
			for (int j = 0; j < board.COLS; j ++) {
				if (index % 2 == 0)
					g.setColor(Color.BLACK);
				else
					g.setColor(Color.WHITE);
				
				g.fillRect(j*cellWidth, i*cellHeight, cellWidth, cellHeight);
				
				index++;
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		drawTilePattern(g, board);
	}
	
	@Override
	public void run() {
		while (true) {			
//			EventQueue.invokeLater(() -> {			
				repaint();
//			});
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	public static void main(String args[]) {
		
		ChessBoard board = new ChessBoard(8, 8);
		board.set(2, 3, PieceType.KING, 1);
		
		ChessBoardDisplay display = new ChessBoardDisplay(640, 640, board);
		display.start();
		
	}

	
}

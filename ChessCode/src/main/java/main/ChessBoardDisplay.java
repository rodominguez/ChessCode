package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessBoardDisplay extends JPanel {
	
	private JFrame jFrame;
	private final int WIDTH, HEIGHT;
	private ChessBoard board;
	private BufferedImage images[];
	private BufferedImage netImage;
	
	private final Color DARK = new Color(90, 40, 1);
	private final Color LIGHT = new Color(201, 134, 62);
	
	public ChessBoardDisplay(int width, int height, ChessBoard board) {
		super();
		WIDTH = width;
		HEIGHT = height;
		
		this.board = board;
		
		createWindow();
		loadImages();
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
		images = new BufferedImage[8];
		try {
			String names[] = {"king.png", "queen.png", "pawn.png", "knight.png", "bishop.jpg", "rook.png", "chariot.jpg"};
			for (int i = 0; i < names.length; i++)
				images[i] = (BufferedImage) ImageIO.read(getClass().getClassLoader().getResource("./"+names[i]));
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
		int cellWidth = WIDTH / cols,
			cellHeight = HEIGHT / rows;
		
		for (int i = 0; i < board.ROWS; i ++) {
			int index = i % 2;
			for (int j = 0; j < board.COLS; j ++) {
				if (index % 2 == 0)
					g.setColor(DARK);
				else
					g.setColor(LIGHT);
				
				g.fillRect(j*cellWidth, i*cellHeight, cellWidth, cellHeight);
				
				index++;
			}
		}
	}
	
	public void drawBoardPieces(Graphics g, ChessBoard board) {
		int rows = board.ROWS;
		int cols = board.COLS;
		int cellWidth = WIDTH / cols,
			cellHeight = HEIGHT / rows;
		
		for (int i = 0; i < board.ROWS; i ++) {
			for (int j = 0; j < board.COLS; j ++) {
				Piece p = board.get(i, j);
				if (p.type == PieceType.BLANK) continue;				
				BufferedImage imgType = images[p.type.ordinal()];
				g.drawImage(imgType, j*cellWidth, i*cellHeight, cellWidth, cellHeight, null);
			}
		}
	}
	
	public void drawTourNumbers(Graphics g, ChessBoard board, int solution[], boolean animated) {
		int rows = board.ROWS;
		int cols = board.COLS;
		int cellWidth = WIDTH / cols,
			cellHeight = HEIGHT / rows;
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		g.setColor(Color.WHITE);
		
		for (int i = 0; i < board.ROWS; i ++) {
			for (int j = 0; j < board.COLS; j ++) {
				int index = i*cols + j;
				g.drawString(Integer.toString(solution[index]+1), j*cellWidth+cellWidth/2-8, i*cellHeight+cellHeight/2+8);
				if (animated) {					
					try {					
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
		}
	}
	
	public void showNeuralSubstitution(String inMessage, String outMessage) {
		EventQueue.invokeLater(() -> {
			Graphics g = getGraphics();
			g.setFont(new Font("TimesRoman", Font.PLAIN, 36)); 
			clear(g);
			
			g.setColor(Color.BLACK);
			g.drawString("In: "+inMessage, 20, 60);
			g.drawString("Out: "+outMessage, 20, 580);
			
			g.drawImage(netImage, 20, 80, 400, 400, null);
		});
	}
	
	public void showKnightsTour(ChessBoard emptyBoard, int solution[], boolean animated) {
		EventQueue.invokeLater(() -> {
			Graphics g = getGraphics();
			drawTilePattern(g, board);
			
			drawTourNumbers(g, board, solution, animated);
		});
	}
	
	public void showMessageChessboard(ChessBoard board) {
		EventQueue.invokeLater(() -> {
			Graphics g = getGraphics();
			drawTilePattern(g, board);
			drawBoardPieces(g, board);
		});
	}
	
	public void showFEMMessage(String fem) {
		EventQueue.invokeLater(() -> {
			Graphics g = getGraphics();
			g.setFont(new Font("TimesRoman", Font.PLAIN, 48)); 
			clear(g);
			
			g.setColor(Color.BLACK);
			g.drawString("FEM: "+fem, 20, 100);
		});
	}
	
	
	
	
	public static void main(String args[]) {
		
		ChessBoard board = new ChessBoard(8, 8);
		board.set(2, 3, PieceType.KING, 1);
		board.set(0, 0, PieceType.CHARIOT, 1);
		ChessBoardDisplay display = new ChessBoardDisplay(640, 640, board);
		
		int sol[] = new int[64];
		for (int i = 0; i < 64; i++)
			sol[i] = i;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
//					display.showNeuralSubstitution("Hola Mundo!", "xsada3r3arfwawdsaawseaeda");
//					display.showFEMMessage("p3pp4\\a");
//					display.showMessageChessboard(board);
					display.showKnightsTour(board, sol, true);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		}).start();
	}

}

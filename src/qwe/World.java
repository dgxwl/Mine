package qwe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int COLS = 16;
	public static final int ROWS = 16;
	public static final int BLOCK_SIZE = 20;
	public static final int WIDTH = COLS * BLOCK_SIZE;
	public static final int HEIGHT = ROWS * BLOCK_SIZE;
	public static final int MINE_NUM = 40;
	
	private Mine[] mines = new Mine[MINE_NUM];
	private Num[] nums = new Num[COLS * ROWS - MINE_NUM];
	
	private boolean[][] isMine = new boolean[COLS][ROWS];
	private Map<Place, Num> numMap = new HashMap<>();
	
	public void start() {
		Random random = new Random();
		for (int i = 0; i < mines.length; i++) {
			while (true) {
				int c = random.nextInt(COLS);
				int r = random.nextInt(ROWS);
				if (isMine[c][r]) {
					continue;
				}
				mines[i] = new Mine(c, r);
				isMine[c][r] = true;
				break;
			}
		}
		
		int countNum = 0;
		for (int i = 0; i < COLS; i++) {
			for (int j = 0; j < ROWS; j++) {
				if (isMine[i][j]) {
					continue;
				}
				
				int n = 0;
				if (i - 1 >= 0 && j - 1 >= 0 && isMine[i - 1][j - 1]) {
					n++;
				}
				if (j - 1 >= 0 && isMine[i][j - 1]) {
					n++;
				}
				if (i + 1 <= COLS - 1 && j - 1 >= 0 && isMine[i + 1][j - 1]) {
					n++;
				}
				if (i - 1 >= 0 && isMine[i - 1][j]) {
					n++;
				}
				if (i + 1 <= COLS - 1 && isMine[i + 1][j]) {
					n++;
				}
				if (i - 1 >= 0 && j + 1 <= ROWS - 1 && isMine[i - 1][j + 1]) {
					n++;
				}
				if (j + 1 <= ROWS - 1 && isMine[i][j + 1]) {
					n++;
				}
				if (i + 1 <= COLS - 1 && j + 1 <= ROWS - 1 && isMine[i + 1][j + 1]) {
					n++;
				}
				
				Place place = new Place(i, j);
				nums[countNum] = new Num(place, n);
				numMap.put(place, nums[countNum]);
				
				countNum++;
			}
		}
		
		addListener();
	}
	
	public void addListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int col = e.getX() / BLOCK_SIZE;
				int row = e.getY() / BLOCK_SIZE;

				Num n = numMap.get(new Place(col, row));
				if (n != null) {
					n.setUncovered(true);
				}
				repaint();
			}
		};
		
		this.addMouseListener(l);
	}
	
	public void drawBlank(Num num, Graphics g) {
		g.setColor(Color.WHITE);
		if (num.getNum() != 0) {
			g.fillRect(num.getCol() * BLOCK_SIZE + 1, num.getRow() * BLOCK_SIZE + 1, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
			g.setColor(Color.BLUE);
			g.drawString(num.getNum()+"", num.getCol() * BLOCK_SIZE + 5, num.getRow() * BLOCK_SIZE + 18);
		} else {
			g.fillRect(num.getCol() * BLOCK_SIZE + 1, num.getRow() * BLOCK_SIZE + 1, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
			
			Num topLeft = numMap.get(new Place(num.getCol() - 1, num.getRow() - 1));
			if (topLeft != null && !topLeft.isUncovered()) {
				topLeft.setUncovered(true);
				drawBlank(topLeft, g);
			}
			
			Num top = numMap.get(new Place(num.getCol(), num.getRow() - 1));
			if (top != null && !top.isUncovered()) {
				top.setUncovered(true);
				drawBlank(top, g);
			}
			
			Num topRight = numMap.get(new Place(num.getCol() + 1, num.getRow() - 1));
			if (topRight != null && !topRight.isUncovered()) {
				topRight.setUncovered(true);
				drawBlank(topRight, g);
			}
			
			Num left = numMap.get(new Place(num.getCol() - 1, num.getRow()));
			if (left != null && !left.isUncovered()) {
				left.setUncovered(true);
				drawBlank(left, g);
			}
			
			Num right = numMap.get(new Place(num.getCol() + 1, num.getRow()));
			if (right != null && !right.isUncovered()) {
				right.setUncovered(true);
				drawBlank(right, g);
			}
			
			Num bottomLeft = numMap.get(new Place(num.getCol() - 1, num.getRow() + 1));
			if (bottomLeft != null && !bottomLeft.isUncovered()) {
				bottomLeft.setUncovered(true);
				drawBlank(bottomLeft, g);
			}
			
			Num bottom = numMap.get(new Place(num.getCol(), num.getRow() + 1));
			if (bottom != null && !bottom.isUncovered()) {
				bottom.setUncovered(true);
				drawBlank(bottom, g);
			}
			
			Num bottomRight = numMap.get(new Place(num.getCol() + 1, num.getRow() + 1));
			if (bottomRight != null && !bottomRight.isUncovered()) {
				bottomRight.setUncovered(true);
				drawBlank(bottomRight, g);
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		for (int i = 1; i < ROWS; i++) {
			g.drawLine(0, BLOCK_SIZE * i, WIDTH, BLOCK_SIZE * i);
		}
		for (int i = 1; i < COLS; i++) {
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, HEIGHT);
		}
		
		g.setColor(Color.GRAY);
		for (int i = 0; i < mines.length; i++) {
			g.fillOval(mines[i].getCol() * BLOCK_SIZE, mines[i].getRow() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
		}
		
		g.setFont(new Font(null, 0, 20));
		for (Num num : nums) {
			if (num.isUncovered()) {
				drawBlank(num, g);
			}
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("mine");
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		World world = new World();
		world.start();
		
		frame.add(world);
		frame.setVisible(true);
	}
}
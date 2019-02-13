package qwe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
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
				if (j + 1 <= ROWS - 1 - 1 && isMine[i][j + 1]) {
					n++;
				}
				if (i + 1 <= COLS - 1 && j + 1 <= ROWS - 1 && isMine[i + 1][j + 1]) {
					n++;
				}
				nums[countNum] = new Num(i, j, n);
				countNum++;
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		for (int i = 1; i < ROWS; i++) {
			g.drawLine(0, BLOCK_SIZE * i, WIDTH, BLOCK_SIZE * i);
		}
		for (int i = 1; i < COLS; i++) {
			g.drawLine(BLOCK_SIZE * i, 0, BLOCK_SIZE * i, HEIGHT);
		}
		
		g.setColor(Color.GRAY);
		for (int i = 0; i < mines.length; i++) {
			g.fillOval(mines[i].getCols() * BLOCK_SIZE, mines[i].getRows() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
		}
		g.setColor(Color.BLUE);
		g.setFont(new Font(null, 0, 20));
		for (Num num : nums) {
			g.drawString(num.getNum() == 0 ? "" : num.getNum()+"", num.getCols() * BLOCK_SIZE + 5, num.getRows() * BLOCK_SIZE + 18);
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
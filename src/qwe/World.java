package qwe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
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
	private boolean[][] isFlagged = new boolean[COLS][ROWS];
	private Place firstClickPlace;
	private Map<Place, Num> numMap = new HashMap<>();
	
	private boolean firstClick = true;
	private boolean isGameover = false;
	private boolean isWin = false;
	
	public void start() {
		addListener();
	}
	
	public void generate() {
		//随机生成地雷
		Random random = new Random();
		for (int i = 0; i < mines.length; i++) {
			while (true) {
				int c = random.nextInt(COLS);
				int r = random.nextInt(ROWS);
				
				if (isMine[c][r]) {
					continue;
				}
				
				if (c == firstClickPlace.col && r == firstClickPlace.row) {
					continue;
				}
				
				mines[i] = new Mine(c, r);
				isMine[c][r] = true;
				break;
			}
		}
		
		//根据地雷的分布生成数字
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
	}
	
	public void addListener() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int col = e.getX() / BLOCK_SIZE;
				int row = e.getY() / BLOCK_SIZE;
				
				if (firstClick) {
					firstClickPlace = new Place(col, row);
					generate();
					firstClick = false;
				}
				
				if (!isGameover && !isWin) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (isFlagged[col][row]) {
							return ;
						}
						
						if (isMine[col][row]) {
							isGameover = true;
							repaint();
							return;
						}
						
						Num n = numMap.get(new Place(col, row));
						if (n != null) {
							n.setUncovered(true);
						}
					}
					
					if (e.getButton() == MouseEvent.BUTTON3) {
						Num n = numMap.get(new Place(col, row));
						boolean isUncovered = false;
						if (n != null) {
							isUncovered = n.isUncovered();
						}
						if (!isUncovered && !isFlagged[col][row]) {
							isFlagged[col][row] = true;
						} else {
							isFlagged[col][row] = false;
						}
					}
					
					checkWin();
					repaint();
				}
			}
		};
		
		this.addMouseListener(l);
	}
	
	//检查是否赢了
	public void checkWin() {
		int flaggedNum = 0;  //当前旗子个数
		for (int i = 0; i < isFlagged.length; i++) {
			for (int j = 0; j < isFlagged[i].length; j++) {
				if (isFlagged[i][j]) {
					if (!isMine[i][j]) {
						return ;
					} else {
						flaggedNum++;
					}
				}
			}
		}
		
		if (flaggedNum == MINE_NUM) {
			for (Num num : nums) {  //所有雷已经标记了, 没翻开的数字自动翻开
				if (!num.isUncovered()) {
					num.setUncovered(true);
				}
			}
			isWin = true;
		}
	}
	
	//画已翻开的数字;如果是0,递归翻开周围格子
	public void drawBlock(Num num, Graphics g) {
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
				drawBlock(topLeft, g);
			}
			
			Num top = numMap.get(new Place(num.getCol(), num.getRow() - 1));
			if (top != null && !top.isUncovered()) {
				top.setUncovered(true);
				drawBlock(top, g);
			}
			
			Num topRight = numMap.get(new Place(num.getCol() + 1, num.getRow() - 1));
			if (topRight != null && !topRight.isUncovered()) {
				topRight.setUncovered(true);
				drawBlock(topRight, g);
			}
			
			Num left = numMap.get(new Place(num.getCol() - 1, num.getRow()));
			if (left != null && !left.isUncovered()) {
				left.setUncovered(true);
				drawBlock(left, g);
			}
			
			Num right = numMap.get(new Place(num.getCol() + 1, num.getRow()));
			if (right != null && !right.isUncovered()) {
				right.setUncovered(true);
				drawBlock(right, g);
			}
			
			Num bottomLeft = numMap.get(new Place(num.getCol() - 1, num.getRow() + 1));
			if (bottomLeft != null && !bottomLeft.isUncovered()) {
				bottomLeft.setUncovered(true);
				drawBlock(bottomLeft, g);
			}
			
			Num bottom = numMap.get(new Place(num.getCol(), num.getRow() + 1));
			if (bottom != null && !bottom.isUncovered()) {
				bottom.setUncovered(true);
				drawBlock(bottom, g);
			}
			
			Num bottomRight = numMap.get(new Place(num.getCol() + 1, num.getRow() + 1));
			if (bottomRight != null && !bottomRight.isUncovered()) {
				bottomRight.setUncovered(true);
				drawBlock(bottomRight, g);
			}
		}
	}
	
	//画旗子图案
	public void drawFlag(Graphics g, int col, int row) {
		g.setColor(Color.RED);
		Polygon p = new Polygon();
		p.addPoint(col * BLOCK_SIZE + BLOCK_SIZE/2, row * BLOCK_SIZE + 1);
		p.addPoint(col * BLOCK_SIZE + BLOCK_SIZE/2, row * BLOCK_SIZE + BLOCK_SIZE/2);
		p.addPoint(col * BLOCK_SIZE + BLOCK_SIZE - 1, row * BLOCK_SIZE + BLOCK_SIZE/2);
		g.fillPolygon(p);
		g.drawLine(col * BLOCK_SIZE + BLOCK_SIZE/2, row * BLOCK_SIZE + BLOCK_SIZE/2, 
				col * BLOCK_SIZE + BLOCK_SIZE/2, row * BLOCK_SIZE + BLOCK_SIZE - 2);
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
		
		for (int i = 0; i < isFlagged.length; i++) {
			for (int j = 0; j < isFlagged[i].length; j++) {
				if (isFlagged[i][j]) {
					drawFlag(g, i, j);
				}
			}
		}
		
		if (isGameover) {
			g.setColor(Color.GRAY);
			for (int i = 0; i < mines.length; i++) {
				g.fillOval(mines[i].getCol() * BLOCK_SIZE, mines[i].getRow() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
			}
		}
		
		if (!firstClick) {
			g.setFont(new Font(null, 0, 20));
			for (Num num : nums) {
				if (num.isUncovered()) {
					drawBlock(num, g);
				}
			}
		}
		
		if (isWin) {
			g.setColor(Color.RED);
			g.setFont(new Font(null, 0, 55));
			g.drawString("Clear!", WIDTH / 2, HEIGHT / 2);
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
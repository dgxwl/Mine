package qwe;

public class Num {
	private int cols;
	private int rows;
	private int num;

	public Num(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}
	
	public Num(int cols, int rows, int num) {
		this.cols = cols;
		this.rows = rows;
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "Num [cols=" + cols + ", rows=" + rows + ", num=" + num + "]";
	}

}
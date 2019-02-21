package qwe;

public class Num {
	
	private Place place;
	private int num;
	private boolean uncovered;

	public Num(Place place, int num) {
		this.place = place;
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public boolean isUncovered() {
		return uncovered;
	}

	public void setUncovered(boolean uncovered) {
		this.uncovered = uncovered;
	}
	
	public int getCol() {
		return place.col;
	}

	public void setCol(int col) {
		place.col = col;
	}

	public int getRow() {
		return place.row;
	}

	public void setRow(int row) {
		place.row = row;
	}

	@Override
	public String toString() {
		return "num=" + num;
	}
}
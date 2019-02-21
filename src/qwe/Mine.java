package qwe;

public class Mine {
	
	private Place place;

	public Mine(int col, int row) {
		place = new Place(col, row);
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
}
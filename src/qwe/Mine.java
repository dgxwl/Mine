package qwe;

public class Mine {
	
	private Place place;
	private boolean flagged;

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

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}
}
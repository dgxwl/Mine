package qwe;

public class Place {
	protected int col;
	protected int row;

	public Place(int col, int row) {
		this.col = col;
		this.row = row;
	}

	@Override
	public String toString() {
		return "(" + col + ", " + row + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}

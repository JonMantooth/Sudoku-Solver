
public class Cell {

	private boolean cellSolved;
	private int row;
	private int column;
	private int grid;
	private int rowIndex;
	private int columnIndex;
	private int gridIndex;
	private int [] containsArray;
	
	public Cell(int index) {
		cellSolved = false;
		row = 1+(index-1)/9;
		column=1+(index-1)%9;
		rowIndex=column;
		columnIndex=row;
		grid=(((index-1)%9)/3+1)+(((index-1)/27)*3);
		gridIndex=(((rowIndex-1)%3+1+(row-1)*3)-1)%9+1;
		containsArray = new int[10];
		
		for (int i=1; i<=9; i++) {
			containsArray[i]=0;
		}
	}
	
	public boolean doesntContain(Block row, Block column, Block grid) {
		boolean cellUpdated = false;
		
		for (int i=1; i<=9; i++) {
			if (row.getDoesntContain(rowIndex)[i]==1 || column.getDoesntContain(columnIndex)[i] == 1 || grid.getDoesntContain(gridIndex)[i] ==1){
				if (containsArray[i]!=1) {
					containsArray[i] =1;
					cellUpdated=true;
				}
			}
		}
		
		if (cellUpdated) {
			row.updateCell(rowIndex, containsArray);
			column.updateCell(columnIndex, containsArray);
			grid.updateCell(gridIndex, containsArray);
		}
		return cellUpdated;
	}
		
	public boolean solved() {
		return cellSolved;
	}
		
	public int getRow() {
		return row;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	
	public int getColumn() {
		return column;
	}	
	
	public int getColumnIndex() {
		return columnIndex;
	}	
	
	public int getGrid() {
		return grid;
	}	
	
	public int getGridIndex() {
		return gridIndex;
	}	
}

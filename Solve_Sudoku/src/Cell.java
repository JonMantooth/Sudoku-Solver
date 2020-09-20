import javax.swing.JOptionPane;

public class Cell {

	/*
	 * This is an integer array representing the total number of values that a cell
	 * cannot equal. When this number reaches 8 you can find the value that the cell 
	 * can equal and know that the cell does equal that value
	 */
	private int[] cellTotal;
	
	/*
	 * This is a single unique number representing all values a cell cannot equal as digits
	 * ex. If a cell cannot be a 4,5 or 6 this number would be 456
	 *The purpose of this value is to compare different cells for identical cannot equal values
	 */
	private int[] allCannotEqual;
	private boolean cellSolved;
	private int row;
	private int column;
	private int grid;
	private int rowIndex;
	private int columnIndex;
	private int gridIndex;
	private int [] containsArray;
	private String secondaryBlockType;
	private int secondaryBlockValue;
	
	public Cell(int index) {
		cellSolved = false;
		secondaryBlockType="none";
		secondaryBlockValue=0;
		row = 1+(index-1)/9;
		column=1+(index-1)%9;
		rowIndex=column;
		columnIndex=row;
		grid=(((index-1)%9)/3+1)+(((index-1)/27)*3);
		gridIndex=(((rowIndex-1)%3+1+(row-1)*3)-1)%9+1;
		containsArray = new int[10];
		
		for (int i=0; i<=9; i++) {
			containsArray[i]=0;
		}
	}
	
	public void addValue(int value) {
		for (int i=1; i<=9; i++) {
			containsArray[i]=1;
		}
		containsArray[value]=2;
		containsArray[0]=10;
	}
	
	/*
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
	*/
	
	public boolean wingNut(Block thisRow, Block thisCol, int _cellIndex) {
		int[] secondaryRowIndex = new int[2];
		int[] secondaryColIndex = new int[2];
	
		secondaryRowIndex[0] = 3*((rowIndex-1)/3)+rowIndex%3+1;
		secondaryRowIndex[1] = 3*((rowIndex-1)/3)+(rowIndex+1)%3+1;
		secondaryColIndex[0] = 3*((columnIndex-1)/3)+columnIndex%3+1;
		secondaryColIndex[1] = 3*((columnIndex-1)/3)+(columnIndex+1)%3+1;
		int rowPrimary=0;
		int colPrimary=0;
		int rowSecondary=0;
		int colSecondary=0;
		
		for (int i=0; i<=1; i++) {
			if (thisRow.getCell(secondaryRowIndex[i]).cellContains(0)==7) {
				
				for (int j=0;j<=1;j++) {
					
					if(thisCol.getCell(secondaryColIndex[j]).cellContains(0)==7) {
				
						for (int k=1;k<=9;k++){
							if(thisRow.getCell(secondaryRowIndex[i]).cellContains(k)==0 && thisCol.getCell(secondaryColIndex[j]).cellContains(k)==0) {
								rowPrimary=k;
								colPrimary=k;
							}else if (thisRow.getCell(secondaryRowIndex[i]).cellContains(k)==0) {
								rowSecondary=k;
							}else if (thisCol.getCell(secondaryColIndex[j]).cellContains(k)==0) {
								colSecondary=k;
							}
						}
				
				
						if(rowPrimary!=0 && rowSecondary!=0 && colPrimary!=0 && colSecondary!=0) {
							for (int k=1;k<=9;k++) {
								if (thisRow.getCell(k).cellContains(0)==7 && thisRow.getCell(k).cellContains(rowSecondary)==0 && thisRow.getCell(k).cellContains(colSecondary)==0 && containsArray[colSecondary]==0) {
									containsArray[colSecondary]=1;
									containsArray[0]++;
									JOptionPane.showMessageDialog(null, "row 2 Wingnut found on " + _cellIndex + "\n" + "Row Primary " + rowPrimary+ "\n" + "Row Secondary " + rowSecondary + "\n" + "Column Primary " + colPrimary + "\n" + "Column Secondary " + colSecondary + "\n Outer Index is " + k +"\n" + thisRow.getCell(k).toStringCellContains());
									return true;
								}
								if (thisCol.getCell(k).cellContains(0)==7 && thisCol.getCell(k).cellContains(rowSecondary)==0 && thisCol.getCell(k).cellContains(colSecondary)==0 && containsArray[rowSecondary]==0) {
									containsArray[rowSecondary]=1;
									containsArray[0]++;
									JOptionPane.showMessageDialog(null, "col 2 Wingnut found on " + _cellIndex + "\n" + "Row Primary " + rowPrimary+ "\n" + "Row Secondary " + rowSecondary + "\n" + "Column Primary " + colPrimary + "\n" + "Column Secondary " + colSecondary + "\n Outer Index is " + k + "\n" + thisCol.getCell(k).toStringCellContains());
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		if (thisRow.getCell(secondaryRowIndex[0]).cellContains(0)==6 && thisRow.getCell(secondaryRowIndex[1]).cellContains(0)==6) {
			for (int i=0;i<=1;i++) {
				if (thisCol.getCell(secondaryColIndex[i]).cellContains(0)==7) {
					for (int j=1; j<=9; j++) {
						if (thisRow.getCell(secondaryRowIndex[0]).cellContains(j)==0 && thisRow.getCell(secondaryRowIndex[1]).cellContains(j)==0 && thisCol.getCell(secondaryColIndex[i]).cellContains(j)==0) {
							colPrimary=j;
						}else if (thisRow.getCell(secondaryRowIndex[0]).cellContains(j)==0 && thisRow.getCell(secondaryRowIndex[1]).cellContains(j)==0) {
							rowPrimary=j;
						}else if ((thisRow.getCell(secondaryRowIndex[0]).cellContains(j)==0 && thisCol.getCell(secondaryColIndex[i]).cellContains(j)==0) || (thisRow.getCell(secondaryRowIndex[1]).cellContains(j)==0 && thisCol.getCell(secondaryColIndex[i]).cellContains(j)==0)) {
							colSecondary=j;
						}else if (thisRow.getCell(secondaryRowIndex[0]).cellContains(j)==0 || thisRow.getCell(secondaryRowIndex[1]).cellContains(j)==0) {
							rowSecondary=j;
						}
					}
					
					if(rowPrimary!=0 && rowSecondary!=0 && colPrimary!=0 && colSecondary!=0) {
						for (int j=1;j<=9;j++) {
							if (thisRow.getCell(j).cellContains(0)==6 && thisRow.getCell(j).cellContains(rowPrimary)==0 && thisRow.getCell(j).cellContains(rowSecondary)==0 && thisRow.getCell(j).cellContains(colSecondary)==0 && containsArray[colSecondary]==0) {
								containsArray[colSecondary]=1;
								containsArray[0]++;
								JOptionPane.showMessageDialog(null, "row 3 Wingnut found on " + _cellIndex + "\n" + "Row Primary " + rowPrimary+ "\n" + "Row Secondary " + rowSecondary + "\n" + "Column Primary " + colPrimary + "\n" + "Column Secondary " + colSecondary);
								return true;
							}
						}
					}
				}
			}
		}
		
		if (thisCol.getCell(secondaryColIndex[0]).cellContains(0)==6 && thisCol.getCell(secondaryColIndex[1]).cellContains(0)==6) {
			for (int i=0;i<=1;i++) {
				if (thisRow.getCell(secondaryRowIndex[i]).cellContains(0)==7) {
					for (int j=1; j<=9; j++) {
						if (thisCol.getCell(secondaryColIndex[0]).cellContains(j)==0 && thisCol.getCell(secondaryColIndex[1]).cellContains(j)==0 && thisRow.getCell(secondaryRowIndex[i]).cellContains(j)==0) {
							rowPrimary=j;
						}else if (thisCol.getCell(secondaryColIndex[0]).cellContains(j)==0 && thisCol.getCell(secondaryColIndex[1]).cellContains(j)==0) {
							colPrimary=j;
						}else if ((thisCol.getCell(secondaryColIndex[0]).cellContains(j)==0 && thisRow.getCell(secondaryRowIndex[i]).cellContains(j)==0) || (thisCol.getCell(secondaryColIndex[1]).cellContains(j)==0 && thisRow.getCell(secondaryRowIndex[i]).cellContains(j)==0)) {
							rowSecondary=j;
						}else if (thisCol.getCell(secondaryColIndex[0]).cellContains(j)==0 || thisCol.getCell(secondaryColIndex[1]).cellContains(j)==0) {
							colSecondary=j;
						}
					}
					
					if(rowPrimary!=0 && rowSecondary!=0 && colPrimary!=0 && colSecondary!=0) {
						for (int j=1;j<=9;j++) {
							if (thisCol.getCell(j).cellContains(0)==6 && thisCol.getCell(j).cellContains(colPrimary)==0 && thisCol.getCell(j).cellContains(colSecondary)==0 && thisCol.getCell(j).cellContains(rowSecondary)==0 && containsArray[rowSecondary]==0) {
								containsArray[rowSecondary]=1;
								containsArray[0]++;
								JOptionPane.showMessageDialog(null, "col 3 Wingnut found on " + _cellIndex + "\n" + "Row Primary " + rowPrimary+ "\n" + "Row Secondary " + rowSecondary + "\n" + "Column Primary " + colPrimary + "\n" + "Column Secondary " + colSecondary);
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
		
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
	
	public int cellContains(int value) {
		return containsArray[value];
	}
	
	public void incrementCellContains(int value) {
		containsArray[value]=1;
		containsArray[0]++;
	}
	
	public void setSecondaryBlockType(String type) {
		secondaryBlockType=type;
	}
	
	public String getSecondaryBlockType() {
		return secondaryBlockType;
	}
	
	public void setSecondaryBlockValue(int value) {
		secondaryBlockValue=value;
	}
	
	public int getSecondaryBlockValue() {
		return secondaryBlockValue;
	}
	
	public String toStringCellContains() {
		String cellContains="";
		
		for (int i=0; i<=9; i++){
			cellContains = cellContains + containsArray[i] + "...";
		}
		return cellContains;
	}
	

}

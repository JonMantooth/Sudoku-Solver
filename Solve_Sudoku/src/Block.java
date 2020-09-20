import java.util.ArrayList;

import javax.swing.JOptionPane;

/*
	 * This class represents a block in a Sudoku. A block is a collection of 9 cells and can be a row,
	 * column, or 3x3 grid. Each block must contain every value 1-9 and can only contain each value once.
	 * @Author Jon Mantooth
	 * @Version1
	 */
public class Block {

	/*
	 * array of all cell elements in the block
	 */
	private Cell[] block;
	
	/*
	 * This is an integer array representing the total number of cells that cannot
	 * equal a specific value in the block. When this number reaches 8 you can find 
	 * the cell that can equal that value and know that it does equal that value
	 */
	private int[] valueTotal;
	
	/*
	 * This is an integer array representing the total number of values that a cell
	 * cannot equal. When this number reaches 8 you can find the value that the cell 
	 * can equal and know that the cell does equal that value
	 */
	private int[] cellTotal=new int[10];
	
	/*
	 * This is an integer array representing identical cells to the cell at an index
	 * ex. If the 1st, 4th, and 7th cell of a row are identical this value would be 3 for indices 1, 4 and 7 
	 * in the array.
	 *If cells are identical it could indicate that other cells cannot contain those values
	 */
	private int[] identicalCells;
	
	/*
	 * 1-indexed boolean array of length 10. Each element 1-9 is true if the 
	 * value of that index is contained within the block and false otherwise. 
	 * Element 0 is disregarded
	 */
	private boolean[] contains;
	
	/*
	 * The constructor initalizes the contains vector and the block matrix
	 */
	public Block() {
		block = new Cell[10];  
		contains = new boolean[10];
		
		//initialize all values in array to false. Initially block contains no values
		for (int i=0; i<=9; i++) {
			contains[i]=false;
		}
		
	}
	
	public void addCell(Cell cell, int index) {
		block[index]= cell;
	}
	
	/*
	 * This method updates the matrix accordingly when a value has been found
	 * @param index - block index of element for which value has been found
	 * @param value - value of element
	 * @return boolean 
	 * 		true if value added successfully
	 * 		false if value already existed in block (this is illegal Sudoku)
	 */
	public boolean addValue(int index, int value) {
		
		/*
	 	*For each unknown value (matrix element is 0) set every element in value column to 1 
	 	*meaning that element cannot equal said value (correct value will later be updated to 2)
	 	*and increment sum in that elements row
	 	*/
		for (int i=1; i<=9; i++) {
			/*
			 *if any element in the value column is 2 that means the value input is already contained 
			 *in the block. This is an illegal Sudoku and method will return false. 
			 */
			if (i!=index && block[i].cellContains(value)==2)
				return false;
			else if (i!=index && block[i].cellContains(value)==0){
				block [i].incrementCellContains(value); 
			}
			
			//if (block[i].cellContains(i)==0) {
			//	cellTotal[i]++;
			//}
		}	
		
		//update sum in value column and index row to 10 meaning correct value has been found
		cellTotal[value]=10;
		
		return true;
	}
	
	/*
	 * This method checks to determine if the cell at a given index 
	 * can be solved
	 * @param index - block index of element to solve
	 * @return int 
	 * 		value of cell at index if found
	 * 		0 if no value can be found
	 */
	public int checkCell(int index) {
		
		/*
		 *check if 8 values can be eliminated from cell at index and if so
		 *find 9th and return 
		 */
		if (block[index].cellContains(0)==8) {
			for (int i=1; i<=9;i++) {
				if (block[index].cellContains(i)==0) {
					return i;
				}
			}
		}
		
		/*
		 *check all values under block if 8 cells have been eliminated and
		 *if so check that cell at this index is the 9th. If so return value
		 */
		for (int i=1; i<=9; i++) {
			if (cellTotal[i]==8){
				if (block[index].cellContains(i)==0) {
					return i;
				}
					
			}
		}
		
		//return 0 if value not found 
		return 0;
	}
	
	public Cell secondaryBlock(String blockType, int blockIndex) {
		ArrayList<Cell> secondaryRowCells;
		ArrayList<Cell> secondaryColumnCells;
		ArrayList<Cell> secondaryGridCells;
		int secondaryGridIndex;
		int secondaryRowIndex;
		int secondaryColumnIndex;
		boolean first;
		
		for (int i=1; i<=9; i++) {
			if (cellTotal[i]==6 || cellTotal[i]==7) {
				first=true;
				secondaryRowCells = new ArrayList<Cell>();
				secondaryColumnCells = new ArrayList<Cell>();
				secondaryGridCells = new ArrayList<Cell>();
				secondaryGridIndex=0;
				secondaryRowIndex=0;
				secondaryColumnIndex=0;
				if (blockType.equals("grid")) {
					for(int j=1;j<=9;j++) {
						if (block[j].cellContains(i)==0) {
							if (first) {
								secondaryColumnIndex=(j-1)%3+1;
								secondaryRowIndex=(j-1)/3+1;
								first=false;
							}
						
							if ((j-1)%3+1==secondaryColumnIndex) {
								JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Checking Cell " + j + " for value " + i);
								secondaryColumnCells.add(block[j]);
							}
							else if ((j-1)/3+1==secondaryRowIndex) {
								JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Checking Cell " + j + " for value " + i);
								secondaryRowCells.add(block[j]);
							}
						}
					}
					
					if (cellTotal[i]==(9-secondaryColumnCells.size())) {
						for (int k=0; k<secondaryColumnCells.size();k++) {
							secondaryColumnCells.get(k).setSecondaryBlockType("column");
							secondaryColumnCells.get(k).setSecondaryBlockValue(i);
						}
						JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Column " + secondaryColumnIndex + "Value " + i);
						return secondaryColumnCells.get(0);
					}else if (cellTotal[i]==(9-secondaryRowCells.size())) {
						for (int k=0; k<secondaryRowCells.size();k++) {
							secondaryRowCells.get(k).setSecondaryBlockType("row");
							secondaryRowCells.get(k).setSecondaryBlockValue(i);
						}
						JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Row " + secondaryColumnIndex + "Value " + i);
						return secondaryRowCells.get(0);
					}
				}else if(blockType.equals("row")) {
					for(int j=1;j<=9;j++) {
						if (block[j].cellContains(i)==0) {
							if (first) {
								secondaryGridIndex=(j-1)/3+1;
								first=false;
							}
						
							if ((j-1)/3+1==secondaryGridIndex) {
								JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Checking Cell " + j + " for value " + i);
								secondaryGridCells.add(block[j]);
							}
						}
					}
					
					if (cellTotal[i]==(9-secondaryGridCells.size())) {
						for (int k=0; k<secondaryColumnCells.size();k++) {
							secondaryGridCells.get(k).setSecondaryBlockType("grid");
							secondaryGridCells.get(k).setSecondaryBlockValue(i);
						}
						JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Grid " + secondaryColumnIndex + "Value " + i);
						return secondaryGridCells.get(0);
					}
				}else if(blockType.equals("column")) {
					for(int j=1;j<=9;j++) {
						if (block[j].cellContains(i)==0) {
							if (first) {
								secondaryGridIndex=(j-1)/3+1;
								first=false;
							}
						
							if ((j-1)/3+1==secondaryGridIndex) {
								JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Checking Cell " + j + " for value " + i);
								secondaryGridCells.add(block[j]);
							}
						}
					}
					
					if (cellTotal[i]==(9-secondaryGridCells.size())) {
						for (int k=0; k<secondaryColumnCells.size();k++) {
							secondaryGridCells.get(k).setSecondaryBlockType("grid");
							secondaryGridCells.get(k).setSecondaryBlockValue(i);
						}
						JOptionPane.showMessageDialog(null, blockType + " " + blockIndex + "\n" + "Grid " + secondaryColumnIndex + "Value " + i);
						return secondaryGridCells.get(0);
					}
				}						
			}
		}
		return null;
	}
	
	public boolean updateSecondary(int value) {
		
		boolean cellUpdated=false;
		
		for (int i=1;i<=9;i++) {
			if(block[i].getSecondaryBlockValue()!=value && block[i].cellContains(value)==0) {
				block[i].incrementCellContains(value);
				cellTotal[value]++;
				cellUpdated=true;
			}
		}
		
		return cellUpdated;
	}
	
	/*
	public int[] getDoesntContain(int index) {
		int[] doesntContain = new int[10];
		for (int i=1;i<=9;i++) {
			doesntContain[i]=block[index][i];
		}
		return doesntContain;
	}
	*/
	
	/*
	 * Update cellTotal with all new information from cell
	 */
	public boolean updateBlock() {
		
		boolean updated = false;
		int cellTotalCopy;
		
		for (int i=1;i<=9;i++) {
			if (cellTotal[i]<8){
				cellTotalCopy=cellTotal[i];
				cellTotal[i]=0;
				for (int j=1;j<=9;j++) {
					if (block[j].cellContains(i)==1)
						cellTotal[i]++;
				}
				
				if (cellTotalCopy==cellTotal[i])
					updated=true;
			}
		}
		
		return updated;
	}
	
	/*
	public void updateCell(int index, int[] doesntContain) {
		for (int i=1; i<=9; i++) {
			if (block[index][i] == 0) {
				block[index][i]=doesntContain[i];
				block[index][0]+=doesntContain[i];
				block[0][i]+=doesntContain[i];
			}
		}
	}
	*/
	
	/*
	 * This method users a higher order algorithm to determine what value a cell
	 * can contain. If a given number of cells in a block can only hold the same 
	 * number of values then it can be determined that no other cell in that block
	 * can hold any of those values. 
	 * ex. if 2 cells can only hold 2 values (1 or 2) then the other 7 cells cannot be
	 * 1 or 2.
	 * This method tests all combinations of values to determine if the case holds for this block
	 * and if so will update all other cells. 
	 * @return int - 1 if no cell was updated, 0 if any cell was updated
	 */
	public int squareSolve(String message) {
		
		ArrayList<Integer> squareValue; //all values held in the square
		ArrayList<Integer> squareCell; //all cell indices in the square
		int cellUpdated=1; 
		String debug = "";
		String debug2="";
		
		//nested for loops that find all combinations of 2 cells
		for (int i=1;i<=3;i++) {
			for (int j=i+1;j<=4;j++) {
				for (int k=j+1;k<=5;k++) {
					for (int l=k+1;l<=6;l++) {
						for (int m=l+1;m<=7;m++) {
							for (int n=m+1;n<=8;n++) {
								for (int o=n+1;o<=9;o++) {
									squareCell = new ArrayList<Integer>();
									squareValue = new ArrayList<Integer>();
									//build arraylist of all being tested
									for (int p=1; p<=9; p++) {
										if (p!=i && p!=j && p!=k && p!=l && p!=m && p!=n && p!=o) 
											squareValue.add(p);
									}
									//build arraylist of all cells that can contain only the values being tested
									for (int p=1; p<=9; p++) {
										if (block[p].cellContains(0)<8) {
											if (block[p].cellContains(i)==1 && block[p].cellContains(j)==1 && block[p].cellContains(k)==1 && block[p].cellContains(l)==1 && block[p].cellContains(m)==1 && block[p].cellContains(n)==1 && block[p].cellContains(o)==1)
												squareCell.add(p);
										}
									}
								
									/*
									 *if the number of cells in the cell list is equal to the number of values being tested you have
									 *a square and you can edit all other cells
									 */
									if (squareCell.size()==2) {
										for (int p=1; p<=9; p++) {
											if (!squareCell.contains(p)) {
												for (int q=1; q<=9; q++) {
													if (squareValue.contains(q) && block[p].cellContains(q)==0) {
														block[p].incrementCellContains(q);
														cellUpdated=0;
														
														//debug message
														for (int r=0;r<squareCell.size();r++) {
															debug = debug + squareCell.get(r)+"...";
															debug2=debug2 + squareValue.get(r)+"...";
														}
														
														JOptionPane.showMessageDialog(null, message + "\n" + debug + "\n" + debug2);
														debug="";
														debug2="";
														 //end debug message
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	
		//nested for loops that find all combinations of 3 cells
		for (int i=1;i<=4;i++) {
			for (int j=i+1;j<=5;j++) {
				for (int k=j+1;k<=6;k++) {
					for (int l=k+1;l<=7;l++) {
						for (int m=l+1;m<=8;m++) {
							for (int n=m+1;n<=9;n++) {
								squareCell = new ArrayList<Integer>();
								squareValue = new ArrayList<Integer>();
								
								//build arraylist of all being tested
								for (int p=1; p<=9; p++) {
									if (p!=i && p!=j && p!=k && p!=l && p!=m && p!=n) 
										squareValue.add(p);
								}
								//build arraylist of all cells that can contain only the values being tested
								for (int p=1; p<=9; p++) {
									if (block[p].cellContains(0)<8) {
										if (block[p].cellContains(i)==1 && block[p].cellContains(j)==1 && block[p].cellContains(k)==1 && block[p].cellContains(l)==1 && block[p].cellContains(m)==1 && block[p].cellContains(n)==1)
											squareCell.add(p);
									}
								}
							
								/*
								 *if the number of cells in the cell list is equal to the number of values being tested you have
								 *a square and you can edit all other cells
								 */
								if (squareCell.size()==3) {
									for (int p=1; p<=9; p++) {
										if (!squareCell.contains(p)) {
											for (int q=1; q<=9; q++) {
												if (squareValue.contains(q) && block[p].cellContains(q)==0) {
													block[p].incrementCellContains(q);
													cellUpdated=0;
													
													//debug message
													for (int r=0;r<squareCell.size();r++) {
														debug = debug + squareCell.get(r)+"...";
														debug2=debug2 + squareValue.get(r)+"...";
													}
													
													JOptionPane.showMessageDialog(null, message + "\n" + debug + "\n" + debug2);
													debug="";
													debug2="";
													 //end debug message
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	
		//nested for loops that find all combinations of 4 cells
		for (int i=1;i<=5;i++) {
			for (int j=i+1;j<=6;j++) {
				for (int k=j+1;k<=7;k++) {
					for (int l=k+1;l<=8;l++) {
						for (int m=l+1;m<=9;m++) {
							squareCell = new ArrayList<Integer>();
							squareValue = new ArrayList<Integer>();
									
							//build arraylist of all being tested
							for (int p=1; p<=9; p++) {
								if (p!=i && p!=j && p!=k && p!=l && p!=m) 
									squareValue.add(p);
							}
							
							//build arraylist of all cells that can contain only the values being tested
							for (int p=1; p<=9; p++) {
								if (block[p].cellContains(0)<8) {
									if (block[p].cellContains(i)==1 && block[p].cellContains(j)==1 && block[p].cellContains(k)==1 && block[p].cellContains(l)==1 && block[p].cellContains(m)==1)
										squareCell.add(p);
								}
							}
								
							/*
							*if the number of cells in the cell list is equal to the number of values being tested you have
							*a square and you can edit all other cells
							*/
							if (squareCell.size()==4) {
								for (int p=1; p<=9; p++) {
									if (!squareCell.contains(p)) {
										for (int q=1; q<=9; q++) {
											if (squareValue.contains(q) && block[p].cellContains(q)==0) {
												block[p].incrementCellContains(q);
												cellUpdated=0;
												
												//debug message
												for (int r=0;r<squareCell.size();r++) {
													debug = debug + squareCell.get(r)+"...";
													debug2=debug2 + squareValue.get(r)+"...";
												}
												
												JOptionPane.showMessageDialog(null, message + "\n" + debug + "\n" + debug2);
												debug="";
												debug2="";
												 //end debug message
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		//nested for loops that find all combinations of 5 cells
		for (int i=1;i<=6;i++) {
			for (int j=i+1;j<=7;j++) {
				for (int k=j+1;k<=8;k++) {
					for (int l=k+1;l<=9;l++) {
						squareCell = new ArrayList<Integer>();
						squareValue = new ArrayList<Integer>();
									
						//build arraylist of all being tested
						for (int p=1; p<=9; p++) {
								if (p!=i && p!=j && p!=k && p!=l) 
									squareValue.add(p);
						}
							
						//build arraylist of all cells that can contain only the values being tested
						for (int p=1; p<=9; p++) {
							if (block[p].cellContains(0)<8) {
								if (block[p].cellContains(i)==1 && block[p].cellContains(j)==1 && block[p].cellContains(k)==1 && block[p].cellContains(l)==1)
									squareCell.add(p);
							}
						}
								
						/*
						*if the number of cells in the cell list is equal to the number of values being tested you have
						*a square and you can edit all other cells
						*/
						if (squareCell.size()==5) {
							for (int p=1; p<=9; p++) {
								if (!squareCell.contains(p)) {
									for (int q=1; q<=9; q++) {
										if (squareValue.contains(q) && block[p].cellContains(q)==0) {
											block[p].incrementCellContains(q);
											cellUpdated=0;
											
											//debug message
											for (int r=0;r<squareCell.size();r++) {
												debug = debug + squareCell.get(r)+"...";
												debug2=debug2 + squareValue.get(r)+"...";
											}
											
											JOptionPane.showMessageDialog(null, message + "\n" + debug + "\n" + debug2);
											debug="";
											debug2="";
											 //end debug message
										}
									}
								}
							}
						}
					}
				}
			}
		}

		//nested for loops that find all combinations of 6 cells
		for (int i=1;i<=7;i++) {
			for (int j=i+1;j<=8;j++) {
				for (int k=j+1;k<=9;k++) {
					squareCell = new ArrayList<Integer>();
					squareValue = new ArrayList<Integer>();
									
					//build arraylist of all being tested
					for (int p=1; p<=9; p++) {
						if (p!=i && p!=j && p!=k) 
							squareValue.add(p);
					}
							
					//build arraylist of all cells that can contain only the values being tested
					for (int p=1; p<=9; p++) {
						if (block[p].cellContains(0)<8) {
							if (block[p].cellContains(i)==1 && block[p].cellContains(j)==1 && block[p].cellContains(k)==1)
								squareCell.add(p);
						}
					}
								
					/*
					*if the number of cells in the cell list is equal to the number of values being tested you have
					*a square and you can edit all other cells
					*/
					if (squareCell.size()==6) {
						for (int p=1; p<=9; p++) {
							if (!squareCell.contains(p)) {
								for (int q=1; q<=9; q++) {
									if (squareValue.contains(q) && block[p].cellContains(q)==0) {
										block[p].incrementCellContains(q);
										cellUpdated=0;
										
										//debug message
										for (int r=0;r<squareCell.size();r++) {
											debug = debug + squareCell.get(r)+"...";
											debug2=debug2 + squareValue.get(r)+"...";
										}
										
										JOptionPane.showMessageDialog(null, message + "\n" + debug + "\n" + debug2);
										debug="";
										debug2="";
										 //end debug message
									}
								}
							}
						}
					}
				}
			}
		}

		//nested for loops that find all combinations of 7 cells
		for (int i=1;i<=8;i++) {
			for (int j=i+1;j<=9;j++) {
					squareCell = new ArrayList<Integer>();
					squareValue = new ArrayList<Integer>();
											
					//build arraylist of all being tested
					for (int p=1; p<=9; p++) {
						if (p!=i && p!=j) 
							squareValue.add(p);
						}
									
					//build arraylist of all cells that can contain only the values being tested
					for (int p=1; p<=9; p++) {
						if (block[p].cellContains(0)<8) {
							if (block[p].cellContains(i)==1 && block[p].cellContains(j)==1)
								squareCell.add(p);
						}
					}
										
					/*
					*if the number of cells in the cell list is equal to the number of values being tested you have
					*a square and you can edit all other cells
					*/
					if (squareCell.size()==7) {
						for (int p=1; p<=9; p++) {
							if (!squareCell.contains(p)) {
								for (int q=1; q<=9; q++) {
									if (squareValue.contains(q) && block[p].cellContains(q)==0) {
										block[p].incrementCellContains(q);
										cellUpdated=0;
										
										//debug message
										for (int r=0;r<squareCell.size();r++) {
											debug = debug + squareCell.get(r)+"...";
											debug2=debug2 + squareValue.get(r)+"...";
										}
										
										JOptionPane.showMessageDialog(null, message + "\n" + debug + "\n" + debug2);
										debug="";
										debug2="";
										 //end debug message
									}
								}
							}
						}
					}
				}
			}
		
			return cellUpdated;		
	}
	
	public Cell getCell(int index) {
		return block[index];
	}
	
	public String toString() {
		String blockString;
		blockString = "*----0----1----2----3----4----5----6----7----8----9----";
		for (int i=0; i<=9; i++) {
			blockString = blockString+"\n"+i+"----";
			for (int j=0; j<=11; j++) {
				blockString=blockString+block[i].cellContains(j)+"----";
			}
		}
		
		return blockString;
	}
	
	public String toStringBlockCantContain() {
	
		String blockString="";
		
		for (int i=0; i<=9; i++) {
			blockString = blockString + cellTotal[i] + "...";
		}
		
		return blockString;
	}
	
}

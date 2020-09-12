import javax.swing.JOptionPane;

/*
	 * This class represents a block in a Sudoku. A block is a collection of 9 cells and can be a row,
	 * column, or 3x3 grid. Each block must contain every value 1-9 and can only contain each value once.
	 * @Author Jon Mantooth
	 * @Version1
	 */
public class Block {

	/*
	 * 2D matrix representing every value of every cell in the block. The 1st dimension is the index of 
	 * the cell in the block and the 2nd dimension is the value of the cell at that index. A 1 in the 
	 * matrix means the cell at the index cannot equal that value. A 2 means the cell at the index does 
	 * equal that value. A 0 means it is undetermined whether the cell at that index equals that value. 
	 * 
	 * The 0 index of every row and column is the sum of all other elements in that row or column. 
	 * 
	 * For any cell index a sum of 10 means the value at that index has been found (8 values have a 1 
	 * meaning they cannot be the value and the 9th value has a 2 meaning that it is the correct value). 
	 * A sum of 8 means that 8 values hold a 1 and cannot be correct meaning that the 9th value (value at 
	 * which the matrix holds a 0) must be the correct value. 
	 * 
	 * Likewise, for any value a sum of 10 means the index for that value has been found (8 indices have a 
	 * 1 meaning they cannot be that value and the 9th index has a 2 meaning that it is that value). 
	 * A sum of 8 means that 8 indices hold a 1 and cannot hold that value meaning that the 9th index 
	 * (index at which the matrix holds a 0) must be that value. 
	 */
	private int[][] block;
	
	/*
	 * 1-indexed boolean array of length 10. Each element 1-9 is true if the 
	 * value of that index is contained within the block and false otherwise. 
	 * Element 0 is disregarded
	 */
	
	private boolean[] contains;
	
	/*
	 * The constructor intializes the contains vector and the block matrix
	 */
	public Block() {
		block = new int[10][12];  
		contains = new boolean[10];
		
		//initialize all values in array to false. Initially block contains no values
		for (int i=0; i<=9; i++) {
			contains[i]=false;
		}
		
		//initialize all values in block matrix to 0
		for (int i=0;i<=9; i++) {
			for (int j=0;j<=11; j++) {
				block[i][j]=0;
			}
		}
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
		if (block[index][value]==2)
			return false;
		
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
			if (block[i][value]==2)
				return false;
			else if (block[i][value]==0){
				block [i][value]=1; 
				block [i][0]++;
				block [0][value]++;
			}
		}
		
		/*
	 	*For each value for the given block index, if value is unknown (matrix element is 0) 
	 	*update element to 1 meaning that element cannot equal said value (correct value will later be 
	 	*updated to 2) and increment sum in that elements column
	 	*/
		for (int i=1; i<=9; i++) {
			if (block[index][i]==0) {
				block [index][i]=1;
				block [0][i]++;
			}
		}		
		
		//update correct element to 2 in matrix
		block[index][value]=2;
		
		//update sum in value column and index row to 10 meaning correct value has been found
		block[0][value]=10;
		block[index][0]=10;
		
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
		if (block[index][0]==8) {
			for (int i=1; i<=9;i++) {
				if (block[index][i]==0)
					return i;
			}
		}
		
		/*
		 *check all values under block if 8 cells have been eliminated and
		 *if so check that cell at this index is the 9th. If so return value
		 */
		for (int i=1; i<=9; i++) {
			if (block[0][i]==8){
				if (block[index][i]==0)
					return i;
			}
		}
		
		//return 0 if value not found 
		return 0;
	}
	
	public int[] getDoesntContain(int index) {
		int[] doesntContain = new int[10];
		for (int i=1;i<=9;i++) {
			doesntContain[i]=block[index][i];
		}
		return doesntContain;
	}
	
	public void updateCell(int index, int[] doesntContain) {
		for (int i=1; i<=9; i++) {
			if (block[index][i] == 0) {
				block[index][i]=doesntContain[i];
				block[index][0]+=doesntContain[i];
				block[0][i]+=doesntContain[i];
			}
		}
	}
	
	public int squareSolve(String message) {
		int cellUpdated=1;
		double indexCode=0;
		double pow = 0;
		int identicalCells=0;
		
		//find unique code for every index. Unique code represents which values are still unknown
		for (int i=1; i<=9; i++)
		{
			for (int j=1; j<=9; j++) {
				if (block[i][j]==0) {
					indexCode=indexCode+(j*Math.pow(10, pow));
					pow++;
				}
			}
			block [i][10]=(int) indexCode;
			indexCode=0;
			pow=0;
		}
		
		//find identical rows
		for (int i=1; i<=9; i++) {
			for (int j=1; j<=9; j++) {
				if (block[i][10] == block[j][10]) {
					identicalCells++;
				}
			}
			block[i][11]=identicalCells;
			identicalCells=0;
		}
		
		JOptionPane.showMessageDialog(null, message + "\n\n" + this.toString());
		
		for (int i=1; i<=9; i++) {
			if (block[i][0]+block[i][11]==9 && block[i][0]<8) {
				for(int j=1; j<=9; j++) {
					if (block[i][j]==0) {
						for (int k=1; k<=9; k++) {
							if (block[k][j] == 0 && block[k][10]!= block[i][10]) {
								block[k][j]=1;
								block[k][0]++;
								block[0][j]++;
								cellUpdated=0;
								JOptionPane.showMessageDialog(null, message +"\n\nUpdated");
							
							}
						}
					}
				}
			}
		}
		
		return cellUpdated;
		
	}
	
	public String toString() {
		String blockString;
		blockString = "*----0----1----2----3----4----5----6----7----8----9----";
		for (int i=0; i<=9; i++) {
			blockString = blockString+"\n"+i+"----";
			for (int j=0; j<=11; j++) {
				blockString=blockString+block[i][j]+"----";
			}
		}
		
		return blockString;
	}
	
}

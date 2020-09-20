import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sudoku_GUI implements ActionListener{
	
	private JTextField[] cellField = new JTextField[82];
	private JFrame frame;
	private JButton solve;
	private Block[] rows = new Block [10];
	private Block[] columns = new Block [10];
	private Block[] grids = new Block [10];
	private JPanel quadrant1;
	private JPanel quadrant2;
	private JPanel quadrant3;
	private JPanel quadrant4;
	private JPanel quadrant5;
	private JPanel quadrant6;
	private JPanel quadrant7;
	private JPanel quadrant8;
	private JPanel quadrant9;
	
	public Sudoku_GUI(){
		
		frame=new JFrame();
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Sudoku Solver");
		frame.setSize(575, 800);
		int width = 50;
		
		quadrant1=new JPanel();
		quadrant1.setBounds(width, width, 3*width, 3*width);
		quadrant1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant2=new JPanel();
		quadrant2.setBounds(4*width, width, 3*width, 3*width);
		quadrant2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant3=new JPanel();
		quadrant3.setBounds(7*width, width, 3*width, 3*width);
		quadrant3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant4=new JPanel();
		quadrant4.setBounds(width, 4*width, 3*width, 3*width);
		quadrant4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant5=new JPanel();
		quadrant5.setBounds(4*width, 4*width, 3*width, 3*width);
		quadrant5.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant6=new JPanel();
		quadrant6.setBounds(7*width, 4*width, 3*width, 3*width);
		quadrant6.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant7=new JPanel();
		quadrant7.setBounds(width, 7*width, 3*width, 3*width);
		quadrant7.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant8=new JPanel();
		quadrant8.setBounds(4*width, 7*width, 3*width, 3*width);
		quadrant8.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		quadrant9=new JPanel();
		quadrant9.setBounds(7*width, 7*width, 3*width, 3*width);
		quadrant9.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		for (int i=1; i<=81; i++) {
			cellField[i]=new JTextField();
			cellField[i].setBounds((((i-1)%9)+1)*width, ((i-1)/9+1)*width, width, width);
			//cellField[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
			cellField[i].setHorizontalAlignment(JTextField.CENTER);
			frame.add(cellField[i]);
		}
		
		solve = new JButton("Solve");
		solve.setBounds(200, 550, 150, 60);
		solve.addActionListener(this);
		frame.add(quadrant1);
		frame.add(quadrant2);
		frame.add(quadrant3);
		frame.add(quadrant4);
		frame.add(quadrant5);
		frame.add(quadrant6);
		frame.add(quadrant7);
		frame.add(quadrant8);
		frame.add(quadrant9);
		frame.add(solve);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Sudoku_GUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Cell[] cells = new Cell[82];
		
		Cell thisCell;
		int cellIndex=1;
		int value=0;
		int cellsSolved=0; //total cells solved. When this equals 81 puzzle has been solved
		int cellsStuck=0;
		int squareSolveUpdated=1;
		boolean stuck=false;
		boolean cellUpdated;
		Block thisRow;
		Block thisColumn;
		Block thisGrid;
		int thisRowIndex;
		int thisColumnIndex;
		int thisGridIndex;
		Cell secondaryCell;
		
		/*
		 * Create 27 new blocks (9 rows, 9 columns, 9 grids)
		 */
		for (int i=1; i<=9; i++) {
			rows[i]=new Block();
			columns[i]=new Block();
			grids[i]=new Block();
		}
		
		/*
		 * Create 81 new cells and move them all to a
		 * row, column and grid
		 */
		for (int i=1; i<=81; i++) {
			cells[i]=new Cell(i);
			rows[cells[i].getRow()].addCell(cells[i],cells[i].getRowIndex());
			columns[cells[i].getColumn()].addCell(cells[i],cells[i].getColumnIndex());
			grids[cells[i].getGrid()].addCell(cells[i],cells[i].getGridIndex());
		}
		
		/*
		 * Initial Traversal through all 81 cells to read in all values available at the start.
		 * This loop will determine if any entries are illegal (not an integer from 1-9 or if any numbers
		 * are input twice from same block
		 */
		for (int i=1; i<=81; i++) {
			
			//assign blocks and block indices for the cell
			thisCell = cells[i];
			thisRow=rows[thisCell.getRow()];
			thisColumn=columns[thisCell.getColumn()];
			thisGrid=grids[thisCell.getGrid()];
			thisRowIndex = thisCell.getRowIndex();
			thisColumnIndex = thisCell.getColumnIndex();
			thisGridIndex = thisCell.getGridIndex();
			
			if (!cellField[i].getText().isEmpty()) {
					
				try {
					value=Integer.parseInt(cellField[i].getText());	
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, "The value in row " + thisColumnIndex + " column " + thisRowIndex + " is not valid. Please ensure all entries are integers between 1 and 9");
					return;
				}
					
				if (value<1 || value>9) {
					JOptionPane.showMessageDialog(null, "The value in row " + thisColumnIndex + " column " + thisRowIndex + " is not a valid number. Please ensure all entries are integers between 1 and 9");
					return;
				}
				
				thisCell.addValue(value);
				
				if (!thisRow.addValue(thisRowIndex, value)) {
					JOptionPane.showMessageDialog(null, "Row " + thisColumnIndex + " contains multiple " + value + "s. This is not a valid Sudoku.");
					return;
				}
				if (!thisColumn.addValue(thisColumnIndex, value)) {
					JOptionPane.showMessageDialog(null, "Column " + thisRowIndex + " contains multiple " + value + "s. This is not a valid Sudoku.");
					return;
				}
				if (!thisGrid.addValue(thisGridIndex, value)) {
					JOptionPane.showMessageDialog(null, "Grid " + cells[i].getGrid() + " contains multiple " + value + "s. This is not a valid Sudoku.");
					return;
				}
				
				cellsSolved++;
			}
			
			//cellField[cellIndex].setFont(new Font("Tahoma", Font.BOLD, 14));
			//cellField[cellIndex].setText(value+"");
		}
		
		if (cellsSolved==81) {
			JOptionPane.showMessageDialog(null, "Congratulations, the Sudoku you entered is correct!");
			return;
		}
		
		cellIndex=1;
		
		while (!stuck) {
			
			cellUpdated=false;
			
			if (cellIndex==82) {
				cellIndex=1;
			}
			
			//assign blocks and block indices for the cell
			thisCell = cells[cellIndex];
			thisRow=rows[thisCell.getRow()];
			thisColumn=columns[thisCell.getColumn()];
			thisGrid=grids[thisCell.getGrid()];
			thisRowIndex = thisCell.getRowIndex();
			thisColumnIndex = thisCell.getColumnIndex();
			thisGridIndex = thisCell.getGridIndex();
			
			//JOptionPane.showMessageDialog(null, "ROW " + cellIndex + "Before \n\n"+rows[cells[cellIndex].getRow()].toString());
			//JOptionPane.showMessageDialog(null, "COLUMN " + cellIndex + "Before \n\n"+columns[cells[cellIndex].getColumn()].toString());
			//JOptionPane.showMessageDialog(null, "GRID " + cellIndex + "Before \n\n"+grids[cells[cellIndex].getGrid()].toString());
	
			//cellUpdated = cells[cellIndex].doesntContain(thisRow, thisColumn, thisGrid);

			
			//JOptionPane.showMessageDialog(null, "ROW " + cellIndex + "After \n\n"+rows[cells[cellIndex].getRow()].toString());
			//JOptionPane.showMessageDialog(null, "COLUMN " + cellIndex + "After \n\n"+columns[cells[cellIndex].getColumn()].toString());
			//JOptionPane.showMessageDialog(null, "GRID " + cellIndex + "After \n\n"+grids[cells[cellIndex].getGrid()].toString());
	
			
			if (cellField[cellIndex].getText().isEmpty()) {
				
				//JOptionPane.showMessageDialog(null, thisCell.getRow() + ":" + thisCell.getColumn() + "\n" + thisCell.toStringCellContains());
				//JOptionPane.showMessageDialog(null, thisCell.getRow() + ":" + thisCell.getColumn() + "Row\n" + thisRow.toStringBlockCantContain());
				//JOptionPane.showMessageDialog(null, thisCell.getRow() + ":" + thisCell.getColumn() + "Column\n" + thisColumn.toStringBlockCantContain());
				//JOptionPane.showMessageDialog(null, thisCell.getRow() + ":" + thisCell.getColumn() + "Grid\n" + thisGrid.toStringBlockCantContain());
				
				thisRow.updateBlock();
				thisColumn.updateBlock();
				thisGrid.updateBlock();
				
				if (thisRow.checkCell(thisRowIndex) > 0) {
					value=thisRow.checkCell(thisRowIndex);
					thisCell.addValue(value);
					thisRow.addValue(thisRowIndex, value);
					thisColumn.addValue(thisColumnIndex, value);
					thisGrid.addValue(thisGridIndex, value);
					cellField[cellIndex].setText(value+"");
					cellField[cellIndex].select(0, 1);
					cellField[cellIndex].setSelectedTextColor(new Color(255,0,0));
					cellsStuck=0;
					cellsSolved++;
				}else if (thisColumn.checkCell(thisColumnIndex) > 0) {
					value=thisColumn.checkCell(thisColumnIndex);
					thisCell.addValue(value);
					thisRow.addValue(thisRowIndex, value);
					thisColumn.addValue(thisColumnIndex, value);
					thisGrid.addValue(thisGridIndex, value);
					cellField[cellIndex].setText(value+"");
					cellField[cellIndex].select(0, 1);
					cellField[cellIndex].setSelectedTextColor(new Color(255,0,0));
					cellsStuck=0;
					cellsSolved++;
				}else if (thisGrid.checkCell(thisGridIndex) > 0) {
					value=thisGrid.checkCell(thisGridIndex);
					thisCell.addValue(value);
					thisRow.addValue(thisRowIndex, value);
					thisColumn.addValue(thisColumnIndex, value);
					thisGrid.addValue(thisGridIndex, value);
					cellField[cellIndex].setText(value+"");
					cellField[cellIndex].select(0, 1);
					cellField[cellIndex].setSelectedTextColor(new Color(255,0,0));
					cellsStuck=0;
					cellsSolved++;
				}else {
					cellsStuck++;
				}
				
				cellUpdated = thisCell.wingNut(thisRow,thisColumn, cellIndex);
				if (cellUpdated) {
					JOptionPane.showMessageDialog(null, thisColumn.toStringBlockCantContain());
					JOptionPane.showMessageDialog(null, thisCell.toStringCellContains());
					cellsStuck=0;
				}
				
			}
			
			if (cellsSolved==81) {
				JOptionPane.showMessageDialog(null, "Sudoku has been solved!");
				return;
			}
			
			if (cellsStuck>=81) {
				JOptionPane.showMessageDialog(null, "On to step 3");
				for (int i=1; i<=9; i++) {
					squareSolveUpdated=squareSolveUpdated*rows[i].squareSolve("Row " + i);
					squareSolveUpdated=squareSolveUpdated*columns[i].squareSolve("Column " + i);
					squareSolveUpdated=squareSolveUpdated*grids[i].squareSolve("Grid " + i);
				}
				
				JOptionPane.showMessageDialog(null, "Cell 12 \n" + cells[12].toStringCellContains());
				
				if (squareSolveUpdated==0) {
					cellsStuck=0;
					squareSolveUpdated=1;
					for (int i=1; i<=9; i++) {
						if (rows[i].updateBlock())
							JOptionPane.showMessageDialog(null, "Updated Row " + i);
						if (columns[i].updateBlock())
							JOptionPane.showMessageDialog(null, "Updated Column " + i);
						if (grids[i].updateBlock())
							JOptionPane.showMessageDialog(null, "Updated Grid " + i);
					}
				}else {
					stuck=true;
					JOptionPane.showMessageDialog(null, "On to step 4");
					for (int i=1; i<=9;i++) {
						secondaryCell=rows[i].secondaryBlock("row",i);
						if (secondaryCell!=null) {
							if (grids[secondaryCell.getGrid()].updateSecondary(secondaryCell.getSecondaryBlockValue())) {
								cellsStuck=0;
								stuck=false;
							}
						}
						secondaryCell=columns[i].secondaryBlock("column",i);
						if (secondaryCell!=null) {
							if (grids[secondaryCell.getGrid()].updateSecondary(secondaryCell.getSecondaryBlockValue())) {
								cellsStuck=0;
								stuck=false;
							}
						}
						secondaryCell=grids[i].secondaryBlock("grid",i);
						if (secondaryCell!=null) {
							if (secondaryCell.getSecondaryBlockType().equals("row")) {
								if (rows[secondaryCell.getRow()].updateSecondary(secondaryCell.getSecondaryBlockValue())) {
								cellsStuck=0;
								stuck=false;
								}
							}else if (secondaryCell.getSecondaryBlockType().equals("column")) {
								if (columns[secondaryCell.getColumn()].updateSecondary(secondaryCell.getSecondaryBlockValue())) {
								cellsStuck=0;
								stuck=false;
								}
							}
						}
					}
					
					JOptionPane.showMessageDialog(null, "Cell 12 \n" + cells[12].toStringCellContains());
				}
			}
			
			
			cellIndex++;
		}
		
		JOptionPane.showMessageDialog(null, "I'm stumped. I cannot solve this sudoku.");
		
	}
	
}
	


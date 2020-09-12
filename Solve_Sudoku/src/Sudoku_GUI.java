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
	
	private Cell[] cells = new Cell[82];
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
			cells[i]=new Cell(i);
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
		int cellIndex=1;
		int value=0;
		int cellsSolved=0; //total cells solved. When this equals 81 puzzle has been solved
		int cellsStuck=0;
		int squareSolveUpdated=1;
		boolean stuck=false;
		boolean cellUpdated;
		
		for (int i=1; i<=9; i++) {
			rows[i]=new Block();
			columns[i]=new Block();
			grids[i]=new Block();
		}
		
		/*
		 * Initial Traversal through all 81 cells to read in all values available at the start.
		 * This loop will determine if any entries are illegal (not an integer from 1-9 or if any numbers
		 * are input twice from same block
		 */
		for (int i=1; i<=81; i++) {
				
			if (!cellField[cellIndex].getText().isEmpty()) {
					
				try {
					value=Integer.parseInt(cellField[cellIndex].getText());	
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, "The value in row " + cells[cellIndex].getRow() + " column " + cells[cellIndex].getColumn() + " is not valid. Please ensure all entries are integers between 1 and 9");
					return;
				}
					
				if (value<1 || value>9) {
					JOptionPane.showMessageDialog(null, "The value in row " + cells[cellIndex].getRow() + " column " + cells[cellIndex].getColumn() + " is not a valid number. Please ensure all entries are integers between 1 and 9");
					return;
				}
					
				if (!rows[cells[cellIndex].getRow()].addValue(cells[cellIndex].getRowIndex(), value)) {
					JOptionPane.showMessageDialog(null, "Row " + cells[cellIndex].getRow() + " contains multiple " + value + "s. This is not a valid Sudoku.");
					return;
				}
				if (!columns[cells[cellIndex].getColumn()].addValue(cells[cellIndex].getColumnIndex(), value)) {
					JOptionPane.showMessageDialog(null, "Column " + cells[cellIndex].getColumn() + " contains multiple " + value + "s. This is not a valid Sudoku.");
					return;
				}
				if (!grids[cells[cellIndex].getGrid()].addValue(cells[cellIndex].getGridIndex(), value)) {
					JOptionPane.showMessageDialog(null, "Grid " + cells[cellIndex].getGrid() + " contains multiple " + value + "s. This is not a valid Sudoku.");
					return;
				}
				
				cellsSolved++;
			}
			
			//cellField[cellIndex].setFont(new Font("Tahoma", Font.BOLD, 14));
			//cellField[cellIndex].setText(value+"");
			cellIndex++;
		}
		
		if (cellsSolved==81) {
			JOptionPane.showMessageDialog(null, "Congratulations, the Sudoku you entered is correct!");
			return;
		}
		
		cellIndex=1;
		
		while (!stuck) {
			
			System.out.println(cellIndex);
			System.out.println(cellsStuck);
			cellUpdated=false;
			
			if (cellIndex==82) {
				cellIndex=1;
			}
			
			//JOptionPane.showMessageDialog(null, "ROW " + cellIndex + "Before \n\n"+rows[cells[cellIndex].getRow()].toString());
			//JOptionPane.showMessageDialog(null, "COLUMN " + cellIndex + "Before \n\n"+columns[cells[cellIndex].getColumn()].toString());
			//JOptionPane.showMessageDialog(null, "GRID " + cellIndex + "Before \n\n"+grids[cells[cellIndex].getGrid()].toString());
	
			cellUpdated = cells[cellIndex].doesntContain(rows[cells[cellIndex].getRow()], columns[cells[cellIndex].getColumn()], grids[cells[cellIndex].getGrid()]);
			
			if (cellUpdated)
				cellsStuck=0;
			
			//JOptionPane.showMessageDialog(null, "ROW " + cellIndex + "After \n\n"+rows[cells[cellIndex].getRow()].toString());
			//JOptionPane.showMessageDialog(null, "COLUMN " + cellIndex + "After \n\n"+columns[cells[cellIndex].getColumn()].toString());
			//JOptionPane.showMessageDialog(null, "GRID " + cellIndex + "After \n\n"+grids[cells[cellIndex].getGrid()].toString());
	
			
			if (cellField[cellIndex].getText().isEmpty()) {
				if (rows[cells[cellIndex].getRow()].checkCell(cells[cellIndex].getRowIndex())!=0) {
					value=rows[cells[cellIndex].getRow()].checkCell(cells[cellIndex].getRowIndex());
					rows[cells[cellIndex].getRow()].addValue(cells[cellIndex].getRowIndex(), value);
					columns[cells[cellIndex].getColumn()].addValue(cells[cellIndex].getColumnIndex(), value);
					grids[cells[cellIndex].getGrid()].addValue(cells[cellIndex].getGridIndex(), value);
					cellField[cellIndex].setText(value+"");
					JOptionPane.showMessageDialog(null, "Cell " + cellIndex + " is " + value + " by row.");
					cellsSolved++;
					cellsStuck=0;
				}else if (columns[cells[cellIndex].getColumn()].checkCell(cells[cellIndex].getColumnIndex())!=0) {
					value=columns[cells[cellIndex].getColumn()].checkCell(cells[cellIndex].getColumnIndex());
					rows[cells[cellIndex].getRow()].addValue(cells[cellIndex].getRowIndex(), value);
					columns[cells[cellIndex].getColumn()].addValue(cells[cellIndex].getColumnIndex(), value);
					grids[cells[cellIndex].getGrid()].addValue(cells[cellIndex].getGridIndex(), value);
					cellField[cellIndex].setText(value+"");
					JOptionPane.showMessageDialog(null, "Cell " + cellIndex + " is " + value + " by column.");
					cellsSolved++;
					cellsStuck=0;
				}else if (grids[cells[cellIndex].getGrid()].checkCell(cells[cellIndex].getGridIndex())!=0) {
					value=grids[cells[cellIndex].getGrid()].checkCell(cells[cellIndex].getGridIndex());
					rows[cells[cellIndex].getRow()].addValue(cells[cellIndex].getRowIndex(), value);
					columns[cells[cellIndex].getColumn()].addValue(cells[cellIndex].getColumnIndex(), value);
					grids[cells[cellIndex].getGrid()].addValue(cells[cellIndex].getGridIndex(), value);
					cellField[cellIndex].setText(value+"");
					cellsSolved++;
					JOptionPane.showMessageDialog(null, "Cell " + cellIndex + " is " + value + " by grid.");
					cellsStuck=0;
				}else {
					if (!cellUpdated)
						cellsStuck++;
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
				
				if (squareSolveUpdated==0) {
					cellsStuck=0;
					squareSolveUpdated=1;
				}else {
					stuck=true;
				}
			}
				
			cellIndex++;
		}
		
		JOptionPane.showMessageDialog(null, "I'm stumped. I cannot solve this sudoku.");
		
	}
	
}
	


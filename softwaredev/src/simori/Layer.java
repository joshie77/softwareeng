package simori;

/**
 * Class to store the 16x16 grid of 1 layer.
 * <p> 
 *
 * @author  Adam
 * @version 1.0.1
 */
public class Layer {
	private boolean[][] grid; //Grid to store whether each button is on (True) or off (False).
	private int instrument; //int to store current instrument value.
	
	/**
	 * Constructor for an individual layer.
	 * Initialises the 2D array (grid) and sets a default instrument.
	 * @author  Adam
	 * @version 1.0.1
	 */
	public Layer() {
		this.grid = new boolean[16][16]; //Initialise the grid as a 16x16 2 dimensional array.
		this.instrument = 0; //Set a default instrument.
	}
	
	
	/**
	 * Method to collate the on and off values in a certain column.
	 * @author  Adam
	 * @version 1.0.1
	 * @param column  The integer value of the column to be returned.
	 * @return boolean array containing each value in the column as a True for on or False for off
	 */
	public boolean[] getCol(int column){
		boolean[] col = new boolean[16];
		for(int i = 0; i < 16; i++) { //For each row of the grid.
			col[i] = grid[i][column]; // Add the requested column value to the array col.
		}
		return col;
	}
	
	/**
	 * Method to return the layers grid. Only used for turning on/off lights when layer is changed.
	 * @author  Adam
	 * @version 1.0.1
	 * @return Two dimensional Int array containing the whole grid. 
	 */
	public boolean[][] getGrid(){
		return grid;
	}
	
	/**
	 * Method to update a button in the grid when it is turned on / off.
	 * @author  Adam
	 * @version 1.0.0
	 * @param column  The column containing the button to change 
	 * @param row     The row containing the button to change
	 */
	public void updateButton(int column, int row){
		grid[row][column] = !grid[row][column]; //Inverse the current value to swap.
	}
	
	
}

import java.util.Random;

public class Board {
	
	private int[][] board;
	private int[] queens; // location of queens in each row of board
	private int size;
	private int h; // number of pairs of queens that are attacking each other, either directly or indirectly
	
	/**
	 * Class constructor.
	 * 
	 * @param n	Dimensions of board (n * n) and number of queens 
	 */
	public Board(int n) {
		
		size = n;
		board = new int[size][size];
		queens = new int[size];
		
		initialize();
		h = evaluate(board);
	}
	
	public Board(int[][] initialState) {
		
		size = initialState.length;
		board = new int[size][size];
		queens = findQueens(initialState);

		for(int i = 0; i < size; i++) 
			board[i][queens[i]] =  1;
		
		h = evaluate(board);
	}
	
	public int[][] getBoard() {
		
		return board;
	}
	
	/**
	 * Get the h-value associated with this board. Lower number
	 * 	mean that the puzzle is close to a solution.
	 * 
	 * @return Total number of queens attacking each other
	 */
	public int getCost() {
		
		return h;
	}
	
	/**
	 * The total n x n size of this board
	 * 
	 * @return size	n
	 */
	public int getSize() {
		
		return size;
	}
	
	/**
	 * The locations of the queen of each row in this board.
	 * 
	 * @return	Array of column indexes of each queen.
	 */
	public int[] getQueens() {
		
		return queens;
	}
	
	/**
	 * Move a queen to a different location within the row
	 * 
	 * @param row	Row number of the board
	 * @param location	New column index within the row
	 * @return True if this move solves the problem
	 */
	public boolean move(int row, int location) {
		
		board[row][queens[row]] = 0;	// reset old queen location
		queens[row] = location;
		board[row][location] = 1;	// assign new location
		h = evaluate(board);
		if (h == 0) return true;
		else return false;
	}
	
	/**
	 * Utility function to find how many new conflicts would occur
	 * 	from moving a queen in the given row to each possible spot
	 * 
	 * @param row	Row of the board to test
	 * @return	Number of intersecting queen paths at each location
	 */
	public int[] conflicts(int row) {
		
		int[] conflicts = new int[size];
		for(int i = 0; i < size; i++) {
			if(board[row][i] == 1) {	// skip evaluation of current location if has queen
				conflicts[i] = Integer.MAX_VALUE;	// ensure won't mess up min
				continue;
			}
			Board temp = new Board(board);
			temp.move(row, i);
			conflicts[i] = temp.getCost();
		}
		return conflicts;
	}
	
	/**
	 * Human-readable representation of this board.
	 * 
	 * @return	Grid layout of the board, where queens are 
	 * 			represented by a '1'
	 */
	public String toString() {
		
		String result = "";
		
		for (int[] row : board) {
			for(int i : row) 
				result += i + " ";
			result += "\n";
		}
		result += "h-cost: " + h;
		return result;
	}
	
	/**
	 * Initialize this board with random queen positioning
	 */
	private void initialize() {
		
		Random rand = new Random();
		for(int i = 0; i < size; i++) {
			
			queens[i] = rand.nextInt(size);	// assign queen to random column number
			board[i][queens[i]] = 1;	// queen location on board represented by 1 
		}
	}
	
	/**
	 * Determine the heuristic value of this board based on
	 * 	the amount of queens attacking each other.
	 * 
	 * @see Board.checkHorizontal, Board.checkVertical, Board.checkDiagonals
	 */
	private int evaluate(int[][] state) {
		
			int hcost = 0;
//			hcost += checkHorizontal(state);	// since we ensure 1 queen every row, don't need to check horizontal?
			hcost += checkVertical(state); 
			hcost += checkDiagonals(state);
			return hcost;

	}
	
	/**
	 * Iterate through the board to find location of all queens (if not known)
	 * 
	 * @param layout	Unknown board to search
	 * @return 	Current queen locations of given layout
	 */
	private int[] findQueens(int[][] layout) {
		
		int newSize = layout.length;
		int[] newQueens = new int[newSize];
		for(int i = 0; i < newSize; i++) {
			for(int j = 0; j < newSize; j++) {
				if(layout[i][j] == 1)
					newQueens[i] = j;
			}
		}
		return newQueens;
	}
	
	/**
	 * Check each row of this board for any "side-to-side" attack conflicts.
	 */
//	private int checkHorizontal(int[][] state) {
//		
//		int row, col, cost  = 0;
//		
//		for(int i = 0; i < size; i++) {
//			row = i;
//			for(int j = 0; j < size; j++) {
//				col = j;
//				
//				if(state[row][col] == 1 && queens[row] != col) {
//					System.out.println("Queen in row " + (row + 1) + " attacked horizontally by Queen in position " + (col + 1));
//					cost++;
//				}
//			}
//		}
//		
//		return cost;
//	}
	
	/**
	 * Check each column of this board (that contains queens) for any 
	 * 	"up-and-down" attack conflicts. 
	 */
	private int checkVertical(int[][] state) {
		
		int row, col, cost  = 0;
		
		for(int i = 0; i < size; i++) {
			col = queens[i]; // only check columns that contain queens
			for(int j = 0; j < size; j++) {			
				row = j;

				if(state[row][col] == 1 && row != i) {
//					System.out.println("Queen in column " + (col + 1) + " attacked vertically by Queen in position " + (row + 1));
					cost++;
				}
			}
		}
		
		return cost;
	}
	/**
	 * Check the diagonal paths of each queen (forwards and backwards) for
	 * 	any diagonal attack conflicts.
	 */
	private int checkDiagonals(int[][] state) {
				
		int row, col, pivot, cost  = 0;	

		for (int i = 0; i < size; i++) { // saves time by only checking board diagonals containing queens
			
			pivot = queens[i];
			
			// row and col should be amount of diagonal steps it takes to get to edge of board from queen's location
			col = Math.max(0, pivot - i); 
			row = Math.max(0, i - pivot); 
			
			// Check forwards diagonal of current queen
			for(int j = 0; j < size; j++) {
				
				if (row >= size || col >= size) 
					break;			
				
				if (state[row][col] == 1 && col != pivot && row != i) {
//					System.out.println("Queen at position [" + (pivot + 1) + ", " + (i + 1) + "] attacked (backward) diagonally by Queen in position [" + (col + 1) + ", " + (row + 1) + "]");
					cost++;
				}
				row++; col++;
			}
			
			col = Math.min(size - 1, pivot + i);
			row = Math.max(0, i - (size - 1) + pivot); 

			// Check backwards diagonal of current queen
			for(int j = 0; j < size; j++) {
				
				if (col < 0 || row >= size) 
					break;	
				
				if (state[row][col] == 1 && col != pivot && row != i) {
//					System.out.println("Queen at position [" + (pivot + 1) + ", " + (i + 1) + "] attacked (forward) diagonally by Queen in position [" + (col + 1) + ", " + (row + 1) + "]");
					cost++;
				}
				row++; col--;
			}
		}
		
		return cost;
	}
}

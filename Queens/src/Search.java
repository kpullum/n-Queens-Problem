public class Search {
	
	
	/**
	 * Follow the steepest-ascent hill-climbing search method 
	 * 	to find a solution to an n-queen problem. If the algorithm
	 * 	cannot find a neighbor that is better than the current state,
	 * 	the algorithm will stop.
	 * 
	 * @param initial	The initial state of the puzzle
	 * 
	 * @return The best neighbor state of the initial given state
	 */
	public static Board hillClimbing(Board initial) {
		
		/*
		 * function Hill-Climbing(problem) returns a state that is a local maximum
		 * 	inputs: problem - a problem
		 * 	local variables: current - a node
		 * 					neighbor - a node
		 * 	current <-- Make-Node(Initial-State[problem])
		 * 	loop do
		 * 		neighbor <-- a highest-valued successor of current
		 * 		if Value[neighbor] <= Value[current] then return State[current]
		 * 		current <-- neighbor
		 */		
			
		int row = 0, location = 0;
		int size = initial.getSize();
		int[][] initialState = initial.getBoard();
		int[] queens = initial.getQueens();	// starting locations of each queen
		if(initial.getCost() == 0)
			return initial;
		Board best = new Board(initialState);	// best - highest value successor
		Board current;
		
		while(best.getCost() != 0 && row < size) {
			
			current = new Board(initialState);
			if(current.move(row, location))
				return current;
			else if(current.getCost() <= best.getCost()) 
				best = new Board(current.getBoard());

			if (queens[row] == location)
				location += 2;	// skip repeating the same queen locations as initial
			else
				location++; 
		    if (location >= size) 
				{ row++; location = 0; }
		    current = null;
		}
		
		return best;
	}
	
	/**
	 * Follow the Min-Conflicts algorithm to find a solution to
	 * 	an n-queen problem. If a solution is not found in the given
	 * 	amount of steps, the algorithm gives up.
	 * 
	 * @param initial	The initial state of the puzzle
	 * @param maxSteps
	 * 
	 * @return
	 */
	public static Board minConflicts(Board initial, int maxSteps) {
		
		/*
		 * function Min-Conflicts(csp, max_steps) returns: a solution or failure
		 * 	inputs:	csp - a constraint satisfaction problem
		 * 			max_steps - the number of steps allowed before giving up
		 * 
		 * 	current <-- an initial complete assignment for csp
		 * 	for i = 1 to max_steps do
		 * 		if current is a solution for csp then return current
		 * 		var <-- a randomly chosen, conflicted variable from Variables[csp]
		 * 		value <-- the value v for var that minimizes Conflicts(var, v, current, csp)
		 * 		set var = value in current
		 * 	return failure
		 */
		
		if(initial.getCost() == 0)
			return initial;
		
		int min, minCol, minRow;

		int[] conflicts;
		int[][] initialState = initial.getBoard();	
		Board current = new Board(initialState);
		int[][] currentState = current.getBoard();
		for (int i = 0; i < maxSteps; i++) {
			
			// search whole board to find move with lowest number of conflicts
			min = minCol =  minRow = Integer.MAX_VALUE;
			for (int j = 0; j < initial.getSize(); j++) {
				
				conflicts = current.conflicts(j);
				
				for (int k = 0; k < conflicts.length; k++) {
					
					if(conflicts[k] < min) {
						min = conflicts[k];
						minCol = k;
						minRow = j;
					}
				}
			}
			
			if(current.move(minRow, minCol)) {
				if(!Queens.multi) System.out.println("\nMin-conflict found solution in " + i + " steps.");
				return current;
			}
			else {
				currentState = current.getBoard();
				current = new Board(currentState);
			}		
		}
		return current;	
	}
}

import java.util.Scanner;

public class Queens {

	public static boolean multi;
	private static Board b, solution;
	private static boolean done;
	private static int algo, choice, size, steps, tests;
	
	public static void main(String[] args) {
		
		System.out.println("CS 4200 Project 2");
		done = multi = false;
		while(!inputLoop());
		if(!done) {
			
			if(multi) multiTest();
			else singleTest();
		}
//		Board b = new Board(8);
//		System.out.println(performMinConflicts(b, 10));
	}
	
	private static boolean inputLoop() {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Select:");
		System.out.println("[1] Single Test");
		System.out.println("[2] Multi-Test");
		System.out.println("[3] Exit");
		choice = Integer.parseInt(sc.nextLine());
		
		switch(choice) {
		
			case 1:
				multi = false;
				break;
				
			case 2:
				multi = true;
				break;
				
			case 3:
				done = true;
				sc.close();
				return true;
				
			default:
				System.out.println("Input not recognized, please try again.");
				sc.close();
				return false;
		}
		
		System.out.println("Which algorithm would you like to test?");
		System.out.println("[1] Steepest-Ascent Hill Climbing");
		System.out.println("[2] Min-Conflicts");
		algo = Integer.parseInt(sc.nextLine());
		
		if(multi) {
			
			System.out.println("How many puzzles would you like to test?");
			tests = Integer.parseInt(sc.nextLine());
		}
		
		System.out.println("Enter board size (default is 8):");
		size = Integer.parseInt(sc.nextLine());
		
		if(algo == 2) {
			System.out.println("Enter maximum steps for Min-Conflict:");
			steps = Integer.parseInt(sc.nextLine());
		}
		
		sc.close();
		
		return true;
	}
	
	private static void singleTest() {

		b = new Board(size);
		System.out.println("Initial board:\n" + b);
		
		switch(algo) {
		
			case 1:
				solution = performHillSearch(b);
				if(solution.getCost() > 0)
					System.out.println("\nHill-climbing search could not find a solution. Showing best attempt.");
				System.out.println("\nResult of search:\n" + solution);	
				break;
				
			case 2:
				solution = performMinConflicts(b, steps);
				if(solution.getCost() > 0)
					System.out.println("Min-conflicts could not find a solution in " + steps + " steps. Showing best attempt.");
				System.out.println("\nResult of search:\n" + solution);	
				break;
		}
	}
	
	private static void multiTest() {
		
		int solved = 0;
		
		for(int i = 0; i <= tests; i++) {
			
			b = new Board(size);
			
			switch(algo) {
				
				case 1:
					solution = performHillSearch(b);
					break;
					
				case 2:
					solution = performMinConflicts(b, steps);
					break;
			}
			
			if(solution.getCost() == 0)
				 solved++;  

		}
		solved = (int)(solved * 100.0f / tests);
		System.out.println("Percent of tests successfully solved: " + solved + "%");
	}
	
	private static Board performHillSearch(Board initial) {
		Board current, previous, solution;
		current = new Board(initial.getBoard());
		
		 do {
			 
			previous = new Board(current.getBoard());
			current = Search.hillClimbing(previous);
//			System.out.println("Step " + ++i + ":\n" + current);
		} 
		while(current.getCost() != 0 && previous.getCost() != current.getCost());
		 
		 solution = new Board(current.getBoard());
		 
		return solution;
	}
	
	private static Board performMinConflicts(Board initial, int maxSteps) {
		return Search.minConflicts(initial, maxSteps);
		
	}
}

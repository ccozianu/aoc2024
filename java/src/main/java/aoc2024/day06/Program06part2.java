package aoc2024.day06;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Program06part2 {
	
	private static class ProgramInput 
	{
		boolean [][] obstacles;
		int [] startPos;
		int ROWS;
		int COLS;
		
		public ProgramInput clone() {
			ProgramInput cloned = new ProgramInput();
			cloned.obstacles = new boolean[obstacles.length][];
			for(int i=0;i<ROWS;i++) {
				cloned.obstacles[i] = obstacles[i].clone();
			}
			cloned.ROWS = ROWS;
			cloned.COLS = COLS;
			cloned.startPos = startPos.clone();
			return cloned;
		}
	}
	
	
	private static final int directions[][] = {
			{-1,0},{0,1},{1,0},{0,-1}	
	};
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			ProgramInput input = readInput();
			long count = 0;
			for (int i=0;i<input.ROWS;i++) {
				for (int j=0;j<input.COLS; j++) {
					if ((i!= input.startPos[0] || j!= input.startPos[1]) && !input.obstacles[i][j]) {
						ProgramInput newInput = input.clone();
						newInput.obstacles[i][j] = true;
						int result = checkForLoop(newInput);
						if (result ==1 ) {
							System.err.println(""+i+"," + j);
						}
						count += result;
					}
				}
			}
			System.out.println("Found loops: "+count);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	private static int checkForLoop(ProgramInput input) throws Exception {
		class Vars {
			boolean [][][] visitedFlags = allocateVisitedFlags(input.obstacles.length, input.obstacles[0].length);
			
			// store at location i,j null if we haven't decided yet, True if it loops by putting an obstacle at i,j False otherwise
			int [] currPos = input.startPos.clone();
			int currDir= 0;
			{
				visitedFlags[currDir][currPos[0]][currPos[1]] = true;
			}
		}; final var __ = new Vars();
		
		class CheckOutOfBounds{
			boolean __(int i, int max) {
				return i<0 || i>=max;
			}
		}; final var checkOutOfBounds= new CheckOutOfBounds();
		class ExitException extends Exception { 
			int loopOrNot;
			public ExitException(int loopOrNot_) {
				this.loopOrNot = loopOrNot_;
			}
		};
		
		Callable<int[]> moveProcedure = () -> {
			var dir = __.currDir;
			do {
				int [] newPos = new int[] { __.currPos[0] + directions[dir][0], 
											__.currPos[1] + directions[dir][1] };
				if (checkOutOfBounds.__(newPos[0],input.ROWS) || checkOutOfBounds.__(newPos[1],input.COLS)) {
					throw new ExitException(0);
				}
				if (input.obstacles[newPos[0]][newPos[1]]) {
					dir = (dir+1) %4;
				} 
				else {
					// we found a legal move
					__.currDir = dir;
					return newPos;
				}
			} while ( dir !=__.currDir);
			throw new RuntimeException("Should not get here");
		};

		try {
			while (true) {
				__.currPos = moveProcedure.call();
				if ( ! __.visitedFlags[__.currDir][__.currPos[0]][__.currPos[1]]) {
					__.visitedFlags[__.currDir][__.currPos[0]][__.currPos[1]]= true;
				}
				else {
						return 1;
				}
			}
		}catch (ExitException ex) {
			return ex.loopOrNot;
		}
	}
	
	private static boolean[][][] allocateVisitedFlags(int rows, int cols) {
		boolean [][][] result = new boolean[4][][];
		for (int i =0; i<4; i++) {
			result[i] = new boolean[rows][];
			for (int j=0;j<rows;j++) {
				result[i][j] = new boolean[cols];
			}
		}
		return result;
	}

	private static ProgramInput readInput() {
		ProgramInput result = new ProgramInput();
		try {
			var obstacles = new ArrayList<boolean[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program06part2.class.getResourceAsStream("/input06.txt")))) {
				String currLine;
				int currRow=0;
				while ((currLine = reader.readLine())!= null ) {
					currLine = currLine.trim();
					boolean currObstacles[] = new boolean[currLine.length()];
					for( int i=0; i<currLine.length();i++) {
						switch(currLine.charAt(i)) {
							case '.' : 
										break;
							case '^' :
										result.startPos = new int[] {currRow,i};
										break;
							case '#' :
										currObstacles[i] = true;
										break;
							default:
								throw new IllegalArgumentException("Invalid character at position "+i);
						}
					}
					obstacles.add(currObstacles);
					currRow++;
				}
			}
			result.obstacles = obstacles.toArray(new boolean[][] {});
			result.ROWS = result.obstacles.length;
			result.COLS = result.obstacles[0].length;
			return result;
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

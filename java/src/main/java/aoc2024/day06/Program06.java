package aoc2024.day06;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Program06 {
	
	private static class ProgramInput 
	{
		boolean [][] obstacles;
		int [] startPos;
		int ROWS;
		int COLS;
	}
	
	
	private static final int directions[][] = {
			{-1,0},{0,1},{1,0},{0,-1}	
	};
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			ProgramInput input = readInput();
			
			class Vars {
				int countVisited = 1;
				boolean [][][] visitedFlags = allocateVisitedFlags(input.obstacles.length, input.obstacles[0].length);
				int [] currPos = input.startPos.clone();
				int currDir= 0;
				{
					visitedFlags[4][currPos[0]][currPos[1]] = true;
					visitedFlags[currDir][currPos[0]][currPos[1]] = true;
				}
			}; final var __ = new Vars();
			
			class CheckOutOfBounds{
				boolean __(int i, int max) {
					return i<0 || i>=max;
				}
			}; final var checkOutOfBounds= new CheckOutOfBounds();
			class ExitException extends Exception {};
			
			Callable<int[]> moveProcedure = () -> {
				var dir = __.currDir;
				do {
					int [] newPos = new int[] { __.currPos[0] + directions[dir][0], 
												__.currPos[1] + directions[dir][1] };
					if (checkOutOfBounds.__(newPos[0],input.ROWS) || checkOutOfBounds.__(newPos[1],input.COLS)) {
						throw new ExitException();
					}
					if (input.obstacles[newPos[0]][newPos[1]]) {
						dir = (dir+1) %4;
					} 
					else {
						__.currDir = dir;
						// let's pretend there was an obstacle on newPos, change direction more
						dir = (dir+1)%4;
						int[] newPosIfObstacle = new int[] { __.currPos[0] + directions[dir][0], 
															__.currPos[1] + directions[dir][1] };
						if (input.obstacles[newPos[0]][newPos[1]]) {
							dir = (dir+1) %4;
						} 
						return newPos;
					}
				} while ( dir !=__.currDir);
				throw new RuntimeException("Detected a complete loop, in one spot");
			};

			try {
				while (true) {
					__.currPos = moveProcedure.call();
					if ( ! __.visitedFlags[4][__.currPos[0]][__.currPos[1]]) {
						__.countVisited++;
						__.visitedFlags[4][__.currPos[0]][__.currPos[1]]= true;
						__.visitedFlags[__.currDir][__.currPos[0]][__.currPos[1]]= true;
					}
					else {
						// if we visited with the same direction, we detected a loop
						if (__.visitedFlags[__.currDir][__.currPos[0]][__.currPos[1]]) {
							throw new RuntimeException("loop detected");
						}
						else {
							__.visitedFlags[__.currDir][__.currPos[0]][__.currPos[1]] = true;
						}
					}
				}
			}catch (ExitException ex) {
				System.out.println("Count cells visited: " + __.countVisited );
			}
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private static boolean[][][] allocateVisitedFlags(int rows, int cols) {
		boolean [][][] result = new boolean[5][][];
		for (int i =0; i<5; i++) {
			result[i] = new boolean[rows][];
			for (int j=0;j<rows;j++) {
				result[i][j] = new boolean[cols];
			}
		}
		return result;
	}

	private static final Set<Long> EMPTY_SET_OF_LONG = Collections.emptySet();
	private static long checkInputLine(Long[] line, Map<Long, Set<Long>> successorsOf) {
		var numbersBefore = new HashSet<Long>();
		for( var l: line) {
			var successors = successorsOf.getOrDefault(l, EMPTY_SET_OF_LONG);
			for (var n: numbersBefore) {
				if (successors.contains(n)) {
					return 0; 
				}
			}
			numbersBefore.add(l);
		}
		if (line.length % 2 == 0) {
			return 0;
		}
		return line[line.length/2];
	}

	private static ProgramInput readInput() {
		ProgramInput result = new ProgramInput();
		try {
			var obstacles = new ArrayList<boolean[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program06.class.getResourceAsStream("/input06.txt")))) {
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

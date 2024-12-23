package aoc2024.day20;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import aoc2024.util.IntPair;

public class Program20Part2 {
	
	private static class ProgramInput 
	{
		char [][] grid;
		IntPair startPos ;
		int ROWS;
		int COLS;
		IntPair endPos ;
		public boolean isObstacle(IntPair point) {
			return this.grid[point.X][point.Y]=='#';
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return super.toString();
		}
	}
	
	
	private static final IntPair DIRECTIONS[] = {
			new IntPair(-1,0), new IntPair(0,1), new IntPair(1,0), new IntPair(0,-1)
	};
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			final ProgramInput input = readInput();
			
			class Vars {
				// we're walking backward
				IntPair currPos = input.endPos;
			}; final var vars = new Vars();
			
			class GridOps {
				IntPair[] neighBorsOf(IntPair p) {
					var result = new IntPair[DIRECTIONS.length];
					int i=0;
					for (var dir:DIRECTIONS) {
						result[i++] = move(p,dir);
					}
					return result;
				}

				private IntPair move(IntPair p, IntPair dir) {
					return new IntPair(p.X + dir.X, p.Y + dir.Y);
				}

				public int distance(IntPair p1, IntPair p2) {
					return Math.abs(p1.X-p2.X) + Math.abs(p1.Y-p2.Y);
				}
			} final var gridOps = new GridOps();
			
			
			// ops for cached distances
			class DistancesAt {
				final Integer [][] cachedDistances; {
					cachedDistances = new Integer[input.ROWS][];
					for(int i=0; i< input.ROWS; i++) cachedDistances[i] = new Integer[input.COLS];
				}
				public void set(IntPair pos, int i) {
					cachedDistances[pos.X][pos.Y] = i;
				}
				public Integer get(IntPair next) {
					return cachedDistances[next.X][next.Y];
				}
			} final var distancesAt = new DistancesAt();
			
			int moveCount=0;
			distancesAt.set(vars.currPos,0);
			ArrayList<IntPair> path = new ArrayList<>();
			path.add(vars.currPos);
			while(! vars.currPos.equals(input.startPos)) {
				var currWallNeighBors = new IntPair[3];
				var wallIdx = 0;
				IntPair foundNext=null;
				for(IntPair next: gridOps.neighBorsOf(vars.currPos)) {
					
					if(input.isObstacle(next)) {
						continue;
					}
					else { 
						if (distancesAt.get(next) != null) {
							// we already visited continue
							continue;
						}
						distancesAt.set(next, ++moveCount);
						if (foundNext != null) {
							throw new IllegalStateException("Found alternative continuations at: "+vars.currPos);
						}
						foundNext = next;
					}
				}
				path.add(foundNext);
				vars.currPos = foundNext;
			}
			distancesAt.set(input.startPos, moveCount);
			System.out.println(path.size());
			
			int countOfCheatsGT100 = 0;
			for (int i=0; i< path.size()-1; i++) {
				IntPair endOfCheat = path.get(i);
				int distanceOfEndOfCheat = distancesAt.get(endOfCheat);
				for (int j=i+100;j<path.size();j++) {
					IntPair startOfCheat = path.get(j);
					int cheatDuration = gridOps.distance(startOfCheat,endOfCheat);
					int distanceAtStartOfCheat = distancesAt.get(startOfCheat);
					if(  cheatDuration <= 20 
						&& distanceAtStartOfCheat >= distanceOfEndOfCheat + 100 + cheatDuration) {
						countOfCheatsGT100 ++;
					}
				}
			}
			System.out.println(countOfCheatsGT100);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	


	private static ProgramInput readInput() {
		ProgramInput result = new ProgramInput();
		try {
			var gridLines = new LinkedList<char[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program20Part2.class.getResourceAsStream("/input20.txt")))) {
				String currLine;
				int currRow=0;
				while ((currLine = reader.readLine())!= null ) {
					currLine = currLine.trim();
					char currGridLine[] = new char[currLine.length()+2];
					currGridLine[0] = currGridLine[currGridLine.length-1] = '#';
					for( int i=0; i<currLine.length();i++) {
						switch(currLine.charAt(i)) {
							case 'S' :
										result.startPos = new IntPair(currRow+1, i+1);
										currGridLine[i+1] = '.';
										break;
							case 'E' :  
										result.endPos = new IntPair(currRow+1, i+1);
										currGridLine[i+1] = '.';
										break;
							case '#':
							case '.' :
										currGridLine[i+1] = currLine.charAt(i);
										break;
							default:
								throw new IllegalArgumentException("Invalid character at position "+i);
						}
					}
					gridLines.add(currGridLine);
					currRow++;
				}
			}
			char [] firstRow = new char[gridLines.get(0).length];
			Arrays.fill(firstRow, '#');
			gridLines.addFirst(firstRow);
			gridLines.addLast(firstRow.clone());
			result.grid = gridLines.toArray(new char[][] {});
			result.ROWS = result.grid.length;
			result.COLS = result.grid[0].length;
			return result;
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

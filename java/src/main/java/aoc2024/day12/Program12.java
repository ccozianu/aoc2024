package aoc2024.day12;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import aoc2024.util.IntPair;
import aoc2024.util.UnionFind;

public class Program12 {
	
	private final int ROWS;
	private final int COLS;
	private Character[][] grid;
	private PointState[][] pointStates;

	public Program12(Character[][] input) {
		// TODO Auto-generated constructor stub
		this.ROWS = input.length;
		this.COLS = input[0].length;
		this.grid = input;
		PointState [][] pointStates = new PointState [ROWS][];
		for(int i=0;i<ROWS;i++) {
			pointStates[i] = new PointState[COLS];
		}		
		this.pointStates =  pointStates;
	}

	private static class PointState {
		int fenceCount=4;
	}



	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			Character [][] input = readInput();
			Program12 program = new Program12(input);
			
			System.out.println(program.run());
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private long run() {
		UnionFind.Builder ufBuilder = new UnionFind.Builder(COLS*ROWS);
		
		var currPoints = new LinkedList<IntPair>();
		currPoints.add(new IntPair(0, 0));
		this.pointStates[0][0] = new PointState();
		while(currPoints.size()>0) {
			var nextPoints = new LinkedList<IntPair>();
			for(var p: currPoints) {
				int pIdx =  pointToInt(p);
				var pState = getState(p);
				for(var nextP: nextPoints(p)) {
					if (checkWithinBounds(nextP) ) {
						PointState nextPState;
						if ((nextPState = getState(nextP)) == null) {
							nextPoints.add(nextP);
							setState(nextP, nextPState = new PointState());
						}
						if (charAt(p) == charAt(nextP)) {
							int nextPIdx = pointToInt(nextP);
							ufBuilder.unite(pIdx,nextPIdx);
							pState.fenceCount--;
							nextPState.fenceCount--;
						};
					}
				}
				currPoints = nextPoints;
			}
		}
		var unionFind = ufBuilder.build();
		Map<Integer, List<Integer>> ufClasses = unionFind.getClasses();
		long total = 0;
		for (var entry: ufClasses.values()) {
			long area = entry.size();
			long fencePerimeter=0;
			for (int i: entry) {
				fencePerimeter += getState(intToPoint(i)).fenceCount;
			}
			total += fencePerimeter*area;
		}
		return total;
	}



	private IntPair intToPoint(int i) {
		return new IntPair(i / COLS, i % COLS);
	}

	private void setState(IntPair p, PointState state) {
		this.pointStates[p.X][p.Y] = state;
	}

	private PointState getState(IntPair point) {
		return this.pointStates[point.X][point.Y];
	}

	private int pointToInt(IntPair p) {
		return p.X * COLS +  p.Y;
	}

	private char charAt(IntPair p) {
		return this.grid[p.X][p.Y];
	}

	private boolean checkWithinBounds(IntPair p) {
		return p.X < ROWS && p.Y < COLS;
	}

	// as we iterate from 0,0 upward in increwasing index order, breadth first
	// we return X+1,Y and X,Y+1, leaving for the calling code has to check for out of bounds
	private IntPair[] nextPoints(IntPair p) {
		return new IntPair[] {new IntPair(p.X,p.Y+1), new IntPair(p.X+1,p.Y)};
	}

	private static Character[][] readInput() {
		try {
			var result = new ArrayList<Character[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program12.class.getResourceAsStream("/input12.txt")))) {
				String currLine;
				while ((currLine = reader.readLine())!= null ) {
					Character charLine[] = new Character[currLine.length()];
					result.add(charLine);
					for (int i=0;i<charLine.length;i++) {
						charLine[i] = currLine.charAt(i);
					}
				}
			}
			return result.toArray(new Character[][] {});
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

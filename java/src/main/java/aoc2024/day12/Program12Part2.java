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

public class Program12Part2 {
	
	private final int ROWS;
	private final int COLS;
	private Character[][] grid;
	private PointState[][] pointStates;

	public Program12Part2(Character[][] input) {
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

	static final int WEST=0,EAST=1,SOUTH=2,NORTH=3;
	private static class PointState {
		boolean[] borders = {true,true,true,true};
		int borderCount() {
			return (borders[0] ? 1: 0) + (borders[1] ? 1: 0) +(borders[2] ? 1: 0) +(borders[2] ? 1: 0)  ;
		}
		
		public long horizontalDiscount(PointState hNeighbor) {
			// TODO Auto-generated method stub
			return ( borders[SOUTH] && hNeighbor.borders[SOUTH]? 1: 0) + 
					(borders[NORTH] && hNeighbor.borders[NORTH]? 1: 0 );
		}
		public long verticalDiscount(PointState vNeighbor) {
			// TODO Auto-generated method stub
			return ( borders[WEST] && vNeighbor.borders[WEST]? 1: 0) + 
					(borders[EAST] && vNeighbor.borders[EAST]? 1: 0 );
		}		
	}



	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			Character [][] input = readInput();
			Program12Part2 program = new Program12Part2(input);
			
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
				PointState pState = getState(p);
				IntPair[] currSuccessors = nextPoints(p);
				{
					IntPair nextPHorizontal = currSuccessors[0];
					if (checkWithinBounds(nextPHorizontal)) {
						PointState nextPState;
						if ((nextPState = getState(nextPHorizontal)) == null) {
							nextPoints.add(nextPHorizontal);
							setState(nextPHorizontal, nextPState = new PointState());
						}
						if (charAt(p) == charAt(nextPHorizontal)) {
							int nextPIdx = pointToInt(nextPHorizontal);
							ufBuilder.unite(pIdx,nextPIdx);
							pState.borders[EAST]=false;
							nextPState.borders[WEST]=false;
						};
					}
				}
				{
					IntPair nextPVertical = currSuccessors[1];
					if (checkWithinBounds(nextPVertical)) {
						PointState nextPState;
						if ((nextPState = getState(nextPVertical)) == null) {
							nextPoints.add(nextPVertical);
							setState(nextPVertical, nextPState = new PointState());
						}
						if (charAt(p) == charAt(nextPVertical)) {
							int nextPIdx = pointToInt(nextPVertical);
							ufBuilder.unite(pIdx,nextPIdx);
							pState.borders[NORTH]=false;
							nextPState.borders[SOUTH]=false;
						};
					}
				}
			}
			currPoints = nextPoints;
		}
		var unionFind = ufBuilder.build();
		Map<Integer, List<Integer>> ufClasses = unionFind.getClasses();
		long total = 0;
		for (var entry: ufClasses.entrySet()) {
			long area = entry.getValue().size();
			long fencePerimeter=0;
			for (int i: entry.getValue()) {
				IntPair p = intToPoint(i);
				PointState pState= getState(p);
				fencePerimeter += pState.borderCount();
				var nextPs = nextPoints(p);
				IntPair nextHorizontal = nextPs[0];
				IntPair nextVertical = nextPs[1];
				if (checkWithinBounds(nextHorizontal) && entry.getKey().equals(unionFind.classOf(pointToInt(nextHorizontal)))) {
					fencePerimeter -= pState.horizontalDiscount(getState(nextHorizontal));
				}
				if (checkWithinBounds(nextVertical) && entry.getKey().equals(unionFind.classOf(pointToInt(nextVertical)))) {
					fencePerimeter -= pState.verticalDiscount(getState(nextVertical));
				}
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
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program12Part2.class.getResourceAsStream("/input12.txt")))) {
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

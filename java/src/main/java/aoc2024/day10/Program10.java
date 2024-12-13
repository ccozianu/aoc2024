package aoc2024.day10;

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

import aoc29024.util.IntPair;


public class Program10 {
	
	
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			long [][] input = readInput();
			int ROWS = input.length;
			int COLS = input[0].length;
			long totalScore = 0;
			
			for (int i=0; i<ROWS;i++) {
				for (int j=0;j<COLS;j++) {
					if(input[i][j] == 9) {
						totalScore += scoreForTarget(input,i,j);
					}
				}
			}
			
			System.out.println(totalScore);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	


	private static long scoreForTarget(long[][] input, int i, int j) {
		int ROWS = input.length;
		int COLS = input[0].length;
		long result = 0;
		HashSet<IntPair> currentSet = new HashSet<IntPair>();
		currentSet.add(new IntPair(i, j));
		for (int step=0; step<9;step++) {
			HashSet<IntPair> nextSet = new HashSet<>();
			for(var p: currentSet) {
				for (var nextP: neighborsOf(p)) {
					if (checkWithinBounds(nextP, ROWS, COLS) && (input[p.X][p.Y] == input[nextP.X][nextP.Y] + 1)) {
						nextSet.add(nextP);
					}
				}
			}
			currentSet = nextSet;
			if (currentSet.isEmpty() ) {
				break;
			}
		}
		return currentSet.size();
	}




	private static boolean checkWithinBounds(IntPair nextPoint, int rows, int cols) {
		return nextPoint.X>=0 && nextPoint.X<rows && nextPoint.Y >=0 && nextPoint.Y < cols;
	}




	private static IntPair[] neighborsOf(IntPair p) {
		return new IntPair[] {
				new IntPair(p.X,p.Y+1),new IntPair(p.X,p.Y-1),
				new IntPair(p.X+1,p.Y),new IntPair(p.X-1,p.Y),
		};
	}




	private static long[][] readInput() {
		ArrayList<long[]> result = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program10.class.getResourceAsStream("/input10.txt")))) {
				String currLine;
				while ( (currLine= reader.readLine()) != null) {
					long[] longs= new long[currLine.length()];
					for (int i=0;i<longs.length;i++) {
						longs[i] = currLine.charAt(i) - '0';
						if (longs[i]<0 || longs[i]>9) {
							throw new IllegalArgumentException(""+currLine.charAt(i));
						}
					}
					result.add(longs);
				}
				return result.toArray(new long[][] {});
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

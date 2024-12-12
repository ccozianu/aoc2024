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

import aoc29024.util.ImmutablePoint;


public class Program10Part2 {
	
	
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			long [][] input = readInput();
			int ROWS = input.length;
			int COLS = input[0].length;
			long totalScore = 0;
			
			for (int i=0; i<ROWS;i++) {
				for (int j=0;j<COLS;j++) {
					if(input[i][j] == 0) {
						totalScore += scoreForSource(input,i,j);
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
	


	private static long scoreForSource(long[][] input, int i, int j) {
		int ROWS = input.length;
		int COLS = input[0].length;
		HashMap<ImmutablePoint,Long> currentSet = new HashMap<>();
		currentSet.put(new ImmutablePoint(i, j),1l);
		for (int step=0; step<9;step++) {
			HashMap<ImmutablePoint,Long> nextSet = new HashMap<>();
			for(Map.Entry<ImmutablePoint,Long> p: currentSet.entrySet()) {
				for (var nextP: neighborsOf(p.getKey())) {
					if (checkWithinBounds(nextP, ROWS, COLS) && (input[p.getKey().X][p.getKey().Y] + 1== input[nextP.X][nextP.Y])) {
						nextSet.merge(nextP, p.getValue(), (Long x,Long y)-> x+y);
					}
				}
			}
			currentSet = nextSet;
			if (currentSet.isEmpty() ) {
				break;
			}
		}
		long result =0 ;
		for( var l:currentSet.values()) {
			result += l;
		};
		return result;
	}




	private static boolean checkWithinBounds(ImmutablePoint nextPoint, int rows, int cols) {
		return nextPoint.X>=0 && nextPoint.X<rows && nextPoint.Y >=0 && nextPoint.Y < cols;
	}




	private static ImmutablePoint[] neighborsOf(ImmutablePoint p) {
		return new ImmutablePoint[] {
				new ImmutablePoint(p.X,p.Y+1),new ImmutablePoint(p.X,p.Y-1),
				new ImmutablePoint(p.X+1,p.Y),new ImmutablePoint(p.X-1,p.Y),
		};
	}




	private static long[][] readInput() {
		ArrayList<long[]> result = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program10Part2.class.getResourceAsStream("/input10.txt")))) {
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

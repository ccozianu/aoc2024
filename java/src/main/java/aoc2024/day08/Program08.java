package aoc2024.day08;

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

public class Program08 {
	
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			Character [][] input = readInput();
			long count = 0l ;
			final int ROWS = input.length;
			final int COLS = input[0].length;
			boolean [][] nextPoints = new boolean [ROWS][];
			for(int i=0;i<ROWS;i++) {
				nextPoints[i] = new boolean[COLS];
			}
					
			var points = new HashMap<Character,List<Point>>();
			for (int i=0; i<ROWS;i++) {
				for (int j=0;j<COLS;j++) {
					if(input[i][j] != null) {
						Point currPoint= new Point(i,j);
						var previousPoints = points.getOrDefault(input[i][j], new LinkedList<Point>());
						for(var point:previousPoints) {
							var p1 = nextPoint(point,currPoint);
							if (checkWithinBounds(p1, ROWS, COLS)) {
								if (! nextPoints[p1.x][p1.y]) {
									count++;
									nextPoints[p1.x][p1.y] = true;
								}
							}
							var p2 = nextPoint(currPoint,point);
							if (checkWithinBounds(p2, ROWS, COLS)) {
								if (! nextPoints[p2.x][p2.y]) {
									count++;
									nextPoints[p2.x][p2.y] = true;
								}
							}
						}
						previousPoints.add(currPoint);
						points.put(input[i][j], previousPoints);
					}
				}
			}
			
			System.out.println(count);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	



	private static boolean checkWithinBounds(Point nextPoint, int rows, int cols) {
		return nextPoint.x>=0 && nextPoint.x<rows && nextPoint.y >=0 && nextPoint.y < cols;
	}




	private static Point nextPoint(Point p1, Point p2) {
		return new Point(2*p2.x-p1.x, 2*p2.y-p1.y);
	}




	private static Character[][] readInput() {
		try {
			var result = new ArrayList<Character[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program08.class.getResourceAsStream("/input08.txt")))) {
				String currLine;
				while ((currLine = reader.readLine())!= null ) {
					Character charLine[] = new Character[currLine.length()];
					result.add(charLine);
					for (int i=0;i<charLine.length;i++) {
						char c= currLine.charAt(i);
						if (c != '.') {
							charLine[i] = c;
						}
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

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

public class Program08Part2 {
	
	
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
							for (Point p : nextPoint(point,currPoint, COLS, ROWS)) {
								if (! nextPoints[p.x][p.y]) {
									count++;
									nextPoints[p.x][p.y] = true;
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


	private static Iterable<Point> nextPoint(Point p1, Point p2, int rows, int cols) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		int gcd= modifiedGCD(dx,dy);
		int incrX = dx/gcd;
		int incrY = dy/gcd;
		var result = new LinkedList<Point>();
		
		Point p = p1;
		while (checkWithinBounds(p, rows, cols)) {
			result.add(p);
			p= new Point(p.x+incrX,p.y+incrY);
		}
		p=new Point(p1.x -incrX, p1.y-incrY);
		while (checkWithinBounds(p, rows, cols)) {
			result.add(p);
			p= new Point(p.x-incrX,p.y-incrY);
		}
		return result;
	}


	
	private static int modifiedGCD(int x, int y) {
		if (x==0&&y==0) throw new IllegalArgumentException("Can't do GCD of 0,0");
		if (y==0) {return x;}
		int toDivide = Math.abs(x);
		int divisor = Math.abs(y);
		int remainder = toDivide % divisor; 
		while (remainder !=0 ) {
			toDivide = divisor;
			divisor = remainder;
			remainder = toDivide % divisor; 
		}
		return divisor;
	}



	private static Character[][] readInput() {
		try {
			var result = new ArrayList<Character[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program08Part2.class.getResourceAsStream("/input08.txt")))) {
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

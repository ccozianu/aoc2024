package aoc2024.day04;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Program04 {
	
	final static int [][] DIRECTIONS= {
			{-1,-1},{-1,0},{-1,1},
			{0,-1},{0,1},
			{1,-1},{1,0},{1,1}
	};
	static final char[] XMAS= {'X','M','A','S' };
	
	static int checkDirection(char[][]arr, int x, int y, int dx, int dy) {
		char[] test = new char[4];
		for (int i=0, newx = x, newy = y; i<4; i++, newx +=dx, newy += dy) {
			if (arr[newx][newy]!=XMAS[i]) {
				return 0;
			}
			test[i]=arr[newx][newy];
		}
		return 1;
	}
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			char[][] data = readInput();
			// the algorithm is too banal to do a method
			long count=0;
			for (int i=3;i<data.length-3; i++) {
				for(int j=3;j<data[0].length-3;j++) {
					for (var dir:DIRECTIONS) {
						count += checkDirection(data, i, j, dir[0], dir[1]);
					}
				}
			}
			System.out.println("Count: "+count);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private static char[][] readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program04.class.getResourceAsStream("/input04.txt")))) {
				String currLine;
				ArrayList<char[]> result = new ArrayList<>();
				while ((currLine = reader.readLine())!= null) {
					result.add(currLine.toCharArray());
				}
				// add buffering so we don't do if else which is
				//					0000000000
				//					0000000000
				//					0000000000
				// xxxx				000xxxx000	
				// xxxx    => 		000xxxx000
				// xxxx				000xxxx000
				// 					0000000000
				//					0000000000
				//					0000000000
				int newLength = result.get(0).length + 6;
				int newSize = result.size() + 6;
				char[][] finalResult = new char[newSize][];
				char[] filling = new char[newLength];
				Arrays.fill(filling, '0');
				// fill six extra row
				finalResult[0]=finalResult[1]=finalResult[2]=finalResult[newSize-1]=finalResult[newSize-2]=finalResult[newSize-3]=filling;
				// each of the real row add chars in the beginning and end
				for (int i=0; i< result.size();i++) {
					var newLine = new char[newLength];
					newLine[0]=newLine[1]=newLine[2]=newLine[newLength-1]=newLine[newLength-2]=newLine[newLength-3]='0';
					System.arraycopy(result.get(i), 0, newLine, 3, newLength-6);
					finalResult[i+3] = newLine;
				}
				return finalResult;
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

package aoc2024.day15;

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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import aoc2024.util.IntPair;
import aoc2024.util.UnionFind;

public class Program15 {
	
	private final int ROWS;
	private final int COLS;
	private char[][] grid;
	private IntPair currentPoint;
	private Stream<Character> moves;

	public Program15(char[][] input, IntPair startPos, Character[] moves) {
		// TODO Auto-generated constructor stub
		this.ROWS = input.length;
		this.COLS = input[0].length;
		this.currentPoint = startPos;
		this.grid = input;
		this.moves = Arrays.asList(moves).stream();
	}




	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			Program15 program = readInput();
			
			System.out.println(program.run());
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	HashMap<Character,IntPair> directionMap = new HashMap<>(); {
		directionMap.put('v', new IntPair(1,0));
		directionMap.put('^', new IntPair(-1,0));
		directionMap.put('<', new IntPair(0,-1));
		directionMap.put('>', new IntPair(0, 1));
	}
	private long run() {
		moves.forEach(c->{
			Program15.this.move(directionMap.get(c));
		});
		System.out.println();
		return this.specialSum();
	}


	private void move(IntPair dir) {
		// need to determine, in direction dir
		// move in the direction, until we found a free point, or a wall
		IntPair p = new IntPair(currentPoint.X, currentPoint.Y);
		boolean foundWall = false;
		boolean foundSpace = false;
		do {
			p = p.moveBy(dir);
			Character c = grid[p.X][p.Y];
			foundSpace = c == '.';
			foundWall = c == '#';
		} while (!foundWall && !foundSpace);
		
		if (foundWall) {
			return;
		}
		// we found the first space, move the blocks along direction forward 
		// by going backward from the foundPoint to the currentPos
		IntPair minusDir = dir.opposite();
		IntPair backCursor ;
		while ( !(backCursor= p.moveBy(minusDir)).equals(currentPoint) ) {
			setAt(p,'O');
			p=backCursor;
		}
		setAt(p,'.');
		currentPoint=p;
	}

	private long specialSum() {
		long total=0;
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<COLS;j++) {
				if (grid[i][j]=='O') {
					total += 100*i+j;
				}
			}
		}
		return total;
	}


	private void setAt(IntPair p, char c) {
		grid[p.X][p.Y] = c;
	}

	private static Program15 readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program15.class.getResourceAsStream("/input15.txt")))) {
				String currLine;
				var gridLines = new ArrayList<char[]>();
				int currNo=0;
				IntPair startPos = null;
				while (!(currLine = reader.readLine()).isBlank()) {
					char charLine[] = new char[currLine.length()];
					gridLines.add(charLine);
					for (int i=0;i<charLine.length;i++) {
						charLine[i] = currLine.charAt(i);
						if (charLine[i]=='@') {
							assert startPos == null;
							startPos = new IntPair(currNo, i);
							charLine[i]='.';
						}
					}
					currNo++;
				}
				char[][] grid = gridLines.toArray(new char[][] {});
				ArrayList<Character> moves= new ArrayList<>();
				while((currLine = reader.readLine())!=null) {
					for (char c: currLine.toCharArray()) {
						moves.add(c);
					}
				}
				return new Program15(grid, startPos, moves.toArray(new Character[]{}));
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

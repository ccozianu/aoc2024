package aoc2024.day15;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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

import javax.naming.OperationNotSupportedException;

import aoc2024.util.IntPair;
import aoc2024.util.Pair;
import aoc2024.util.UnionFind;

public class Program15Part2 {
	
	private final int ROWS;
	private final int COLS;
	private char[][] grid;
	private IntPair currentPoint;
	private Stream<Character> moves;

	public Program15Part2(char[][] input, IntPair startPos, Character[] moves) {
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
			Program15Part2 program = readInput();
			
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
			Program15Part2.this.move(directionMap.get(c));
		});
		System.out.println();
		return this.specialSum();
	}


	private void move(IntPair dir) {
		if (isHorizontalDir(dir)) {
			moveHorizontally(dir);
		}
		else {
			moveVertically(dir);
		}
	}

	private void moveVertically(IntPair dir) {
		final BitSet ZEROS = new BitSet(COLS);
		var startingBlock= new Pair<Integer,BitSet>(currentPoint.X, (BitSet)ZEROS.clone());
		startingBlock._2.set(currentPoint.Y);
		
		// chain is the list of points that will be affected by the move, at a 
		boolean foundWall = false;
		boolean foundSpace = false;

		/**
		 * given a list of points where the previous points would like to move
		 * determine if it would hit a wall, or everything is empty (the happy case)
		 * or a list of block points that would need to further move
		 */
		class FindOneWallOrAllSpaceOrNextChain{ Object __(Pair<Integer,BitSet> points) {
			int i=-1;
			BitSet nextBlockBitSet = (BitSet) ZEROS.clone();
			var allSpaces = true;
			while ((i=points._2.nextSetBit(i+1)) != -1) {
				switch(grid[points._1][i]) {
					case '.': {
						break;
					}
					case '#': {
						return '#';						
					}
					case '[': {
						allSpaces=false;
						nextBlockBitSet.set(i);
						nextBlockBitSet.set(i+1);
						break;
					}
					case ']': {
						allSpaces=false;
						nextBlockBitSet.set(i);
						nextBlockBitSet.set(i-1);
						break;
					}
					default: {
						throw new IllegalStateException("The grid at "+points._1+","+i+"contains illegal character "+grid[points._1][i]);
					}
				}
			}
			if(allSpaces) {
				return '.';
			}
			return new Pair<Integer,BitSet>(points._1,nextBlockBitSet);
		}} var findOneWallOrAllSpaceOrNextChain = new FindOneWallOrAllSpaceOrNextChain();
		var nextBlock = startingBlock;
		LinkedList<Pair<Integer,BitSet>> blocksToMove= new LinkedList<>();
		do {
			nextBlock = new Pair<>(nextBlock._1 + dir.X,nextBlock._2);
			Object c = findOneWallOrAllSpaceOrNextChain.__(nextBlock);
			if (c instanceof Pair) {
				blocksToMove.add(nextBlock=(Pair)c);
			} else {
				foundWall = ((Character)c) == '#';
				foundSpace = ((Character)c) == '.';
			}
		} while (!foundWall && !foundSpace);
		
		if (foundWall) {
			return;
		}
		
		// We have a least where the last is the one before all spaces
		// let's move them up (or down respectively) with care
		var itr = blocksToMove.descendingIterator();
		while(itr.hasNext()){
			Pair<Integer,BitSet> block = itr.next();
			int xToMoveTo=block._1 + dir.X;
			int y= -1;
			while(( y = block._2.nextSetBit(y+1))!=-1) {
				grid[xToMoveTo][y] = grid[block._1][y];
				grid[block._1][y] = '.';
			}
		}
		currentPoint= currentPoint.moveBy(dir);
	}




	private void moveHorizontally(IntPair dir) {
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
		// by going backward from the foundPoint to the currentPos, 
		// so we do not overwwrite info we hadn't already saved by moving
		IntPair minusDir = dir.opposite();
		IntPair backCursor ;
		while ( !(backCursor= p.moveBy(minusDir)).equals(currentPoint) ) {
			setAt(p,getAt(backCursor));
			p=backCursor;
		}
		setAt(p,'.');
		currentPoint=p;
		
	}




	private char getAt(IntPair point) {
		return grid[point.X][point.Y];
	}




	private boolean isHorizontalDir(IntPair dir) {
		// TODO Auto-generated method stub
		return dir.X==0;
	}




	private long specialSum() {
		long total=0;
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<COLS;j++) {
				if (grid[i][j]=='[') {
					total += 100*i+j;
				}
			}
		}
		return total;
	}


	private void setAt(IntPair p, char c) {
		grid[p.X][p.Y] = c;
	}

	private static Program15Part2 readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program15Part2.class.getResourceAsStream("/input15.txt")))) {
				String currLine;
				var gridLines = new ArrayList<char[]>();
				int currNo=0;
				IntPair startPos = null;
				while (!(currLine = reader.readLine()).isBlank()) {
					char charLine[] = new char[2*currLine.length()];
					gridLines.add(charLine);
					for (int i=0;i<currLine.length();i++) {
						char c = currLine.charAt(i);
						switch (c) {
							case '@': {
										assert startPos == null;
										startPos = new IntPair(currNo, 2*i);
										charLine[2*i]=charLine[2*i+1]='.';
										break;
							}
							case 'O': {
								charLine[2*i]= '[';
								charLine[2*i+1]= ']';
								break;
							}
							case '#': {
								charLine[2*i]=charLine[2*i+1]='#';
								break;
							}
							case '.': {
								charLine[2*i]=charLine[2*i+1]='.';
								break;
							}
							default: throw new IllegalArgumentException("Char '"+ c+ "' on line no "+currNo+": "+currLine);
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
				return new Program15Part2(grid, startPos, moves.toArray(new Character[]{}));
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (char[] line:grid) {
			builder.append(line);
			builder.append('\n');
		}
		builder.setCharAt(currentPoint.X*(COLS+1)+ currentPoint.Y, '@');
		return builder.toString();
	}
}

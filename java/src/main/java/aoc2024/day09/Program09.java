package aoc2024.day09;

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

import aoc2024.day08.Program08;

public class Program09 {
	
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			long[] input = readInput();
			int leftIdx=0;
			int leftBlockId = 0;
			int rightIdx=input.length - 1;
			int rightBlockId = rightIdx/2;
			// this should be changed to long , but start with int because lazily trying to get away with int so List<Point(int,int)> can be the datastructure
			long startingBlockPos = 0;
			//LinkedList<Point> compactedStructure = new LinkedList<>();
			long checkSum =0;
			while(rightIdx >= leftIdx) {
				if (leftIdx %2 == 0) {
					// compactedStructure.addLast(new Point(startingBlockPos, leftBlockId));
					checkSum += computeForCheckSum(startingBlockPos,leftBlockId,input[leftIdx]);
					startingBlockPos += input[leftIdx];
					leftBlockId++;
					leftIdx++;
				}
				else {
					// fill in the empty space
					long currentEmptyCount = input[leftIdx];
					while(currentEmptyCount>0 && rightIdx>leftIdx) {
						if (input[rightIdx]>=currentEmptyCount ) {
							// later we may add to a linked list the way we organized the block
							checkSum +=computeForCheckSum(startingBlockPos, rightBlockId, currentEmptyCount);
							input[rightIdx] -= currentEmptyCount;
							startingBlockPos += currentEmptyCount;
							currentEmptyCount = 0;
						}
						else { // we empty the coming right block by moving
							checkSum += computeForCheckSum(startingBlockPos, rightBlockId, input[rightIdx]);
							currentEmptyCount -= input[rightIdx];
							startingBlockPos += input[rightIdx];
							input[rightIdx] = 0;
							rightIdx -= 2;
							rightBlockId --;
						}
					}
					leftIdx++;
				}
			}
			// if left meets right on a block that hsn't been left empty
			if (rightIdx == leftIdx) {
				checkSum += computeForCheckSum(startingBlockPos, leftBlockId, input[leftIdx]);
			}
			System.out.println(checkSum);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	








	private static long computeForCheckSum(long startingBlockPos, int blockId, long count) {
		return blockId * (count*startingBlockPos + count*(count-1)/2);
	}









	private static long[] readInput() {
		try {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program08.class.getResourceAsStream("/input09.txt")))) {
				String currLine = reader.readLine();
				long[] result= new long[currLine.length()];
				for (int i=0;i<result.length;i++) {
					result[i] = currLine.charAt(i) - '0';
					if (result[i]<0 || result[i]>9) {
						throw new IllegalArgumentException(""+currLine.charAt(i));
					}
				}
				return result;
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

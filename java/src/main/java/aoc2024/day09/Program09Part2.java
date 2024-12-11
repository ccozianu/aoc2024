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

public class Program09Part2 {
	
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			long[] input = readInput();
			long blockStarts[] = new long[input.length];
			for(int blockId=1;blockId < input.length/2+1; blockId++) {
				// add the empty space before
				blockStarts[2*blockId -1] = blockStarts[2*blockId-2] + input[2*blockId-2];
				blockStarts[2*blockId]=blockStarts[2*blockId -1] + input[2*blockId-1];
				
			}

			long checkSum = 0;
			for (int idx = input.length -1; idx > 0; idx -=2 ) {
				for (int emptyIdx = 1; emptyIdx < idx; emptyIdx +=2) {
					if (input[idx] <= input[emptyIdx]) {
						blockStarts[idx] = blockStarts[emptyIdx];
						input[emptyIdx] -= input[idx];
						blockStarts[emptyIdx]+= input[idx];
						break;
					}	
				}
				// once we finished the position of the current block is finalized, compoute its checksum
				long currentCheckSum = computeForCheckSum(blockStarts[idx], idx/2, input[idx]);
				checkSum += currentCheckSum;
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

package aoc2024.day11;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import aoc29024.util.IntPair;


public class Program11 {
	
	private static final long [] POW10 = {
			1l, 10l, 100l, 1000l, 10000l,
			100000l, 1000000l, 10000000l, 100000000l, 1000000000l,
			10000000000l, 100000000000l, 1000000000000l, 10000000000000l, 100000000000000l,
			1000000000000000l, 10000000000000000l, 100000000000000000l, 1000000000000000000l
	};
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			List<Long>currList = readInput();
			System.out.println(countFuturePebbles(currList,75));
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	


	private static long countFuturePebbles(List<Long> currList, int rounds) {
		HashMap<Long, Long> hashStack[] = new HashMap[rounds+1];
		for(int i=0; i<rounds+1;i++){
			hashStack[i] = new HashMap<>();
		}
		return countFuturePebbles(currList, rounds, hashStack);
	}
	
	private static long countFuturePebbles(List<Long> currList, int rounds, HashMap<Long, Long>[] computedCache) {
		if (rounds == 0) {
			return currList.size();
		}
		long result = 0;
		for (Long l: currList) {
				if (computedCache[rounds].containsKey(l)) {
					result += computedCache[rounds].get(l);
				}
				else {
					var nextList = transform(l);
					long count = countFuturePebbles(nextList, rounds - 1, computedCache);
					computedCache[rounds].put(l, count);
					result += count;
				}
		}
	
		return result;
	}



	private static List<Long> transform(Long l) {
		if (l < 0) // we've had overflow
			throw new IllegalArgumentException("l: "+l);
		if ( l == 0 ) {
			return Arrays.asList(1l);
		}
		int digitCount = digitCount(l); 
		if (digitCount%2 == 0) {
			long rightDigits = l % POW10[digitCount/2];
			long leftDigits = l / POW10[digitCount/2];
			return Arrays.asList(leftDigits, rightDigits);
		}
		return Arrays.asList(l*2024);
	}



	private static int digitCount(Long l) {
		if (l<=0) {
			throw new IllegalArgumentException("l:"+l);
		}
		for (int i=0;i<POW10.length-1;i++) {
			if (POW10[i]<=l && l<POW10[i+1]) {
				return i+1;
			}
		}
		return POW10.length;
	}


	private static List<Long> readInput() {
		try {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program08.class.getResourceAsStream("/input11.txt")))) {
				String [] currLine = reader.readLine().split("\\s");
				
				ArrayList<Long> result= new ArrayList<>(currLine.length);
				for (String s:currLine) {
					result.add(Long.valueOf(s));
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

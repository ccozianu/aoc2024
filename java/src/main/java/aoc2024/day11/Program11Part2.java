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
import aoc2024.util.IntPair;


public class Program11Part2 {
	
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
			HashSet<Long> allNums = new HashSet<>();
			for (int i=0; i<25;i++) {
				ArrayList<Long> nextList = new ArrayList<>(currList.size()*5/4);
				for (Long l: currList) {
//					if (allNums.contains(l)) {
//						continue;
//					}
//					else {
						allNums.add(l);
						nextList.addAll(transform(l));
//					}
				}
				currList =  nextList;
			}
			System.out.println(currList.size());
			System.out.println(allNums.size());
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	


	private static Collection<? extends Long> transform(Long l) {
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

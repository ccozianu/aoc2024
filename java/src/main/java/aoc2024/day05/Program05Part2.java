package aoc2024.day05;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Program05Part2 {
	
	private static class ProgramInput 
	{
		Map<Long,Set<Long>> successorsOf = new TreeMap<>();
		Long[][] updateLines;
	}
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			ProgramInput input = readInput();
			// the algorithm is too banal to do a method
			long sum=0;
			for (var line:input.updateLines) {
				sum += checkInputLine(line,input.successorsOf);
			}
			System.out.println("Sum of middle points of valid lines is: "+sum);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private static final Set<Long> EMPTY_SET_OF_LONG = Collections.emptySet();
	private static long checkInputLine(Long[] line, Map<Long, Set<Long>> successorsOf) {
		boolean swapEffected = false;
		for( int i=1; i<line.length; i++ ) {
			var successors = successorsOf.getOrDefault(line[i], EMPTY_SET_OF_LONG);
			for (int  j=0; j<i; j++) {
				if (successors.contains(line[j])) {
					swap(line,i,j);
					swapEffected = true;
				}
			}
		}
		if (line.length % 2 == 0) {
			return 0;
		}
		return swapEffected ? line[line.length/2] :0;
	}

	private static void swap(Long[] line, int i, int j) {
		Long tmp = line[j];
		line[j] = line[i];
		line[i] = tmp;
	}

	private static ProgramInput readInput() {
		ProgramInput result = new ProgramInput();
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program05Part2.class.getResourceAsStream("/input05.txt")))) {
				String currLine;
				while ((currLine = reader.readLine())!= null ) {
					if (currLine.isBlank()) {
						break;
					}
					String[] splits = currLine.split("\\|");
					Long val1 = Long.valueOf(splits[0]);
					Long val2 = Long.valueOf(splits[1]);
					var successors = result.successorsOf.getOrDefault(val1, new HashSet<Long>());
					successors.add(val2);
					result.successorsOf.put(val1,successors);
				}
				var updatedLines = new ArrayList<Long[]>();
				while ((currLine = reader.readLine())!= null ) {
					String [] splits = currLine.split(",");
					Long[] longs = new Long[splits.length];
					for(int i=0; i<splits.length;i++) {
						longs[i]= Long.valueOf(splits[i]);
					}
					updatedLines.add(longs);
				}
				result.updateLines = updatedLines.toArray(new Long[][] {});
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

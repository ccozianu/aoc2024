package aoc2024.day05;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Program05 {
	
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
		var numbersBefore = new HashSet<Long>();
		for( var l: line) {
			var successors = successorsOf.getOrDefault(l, EMPTY_SET_OF_LONG);
			for (var n: numbersBefore) {
				if (successors.contains(n)) {
					return 0; 
				}
			}
			numbersBefore.add(l);
		}
		if (line.length % 2 == 0) {
			return 0;
		}
		return line[line.length/2];
	}

	private static ProgramInput readInput() {
		ProgramInput result = new ProgramInput();
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program05.class.getResourceAsStream("/input05.txt")))) {
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

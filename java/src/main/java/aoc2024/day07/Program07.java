package aoc2024.day07;

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
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Program07 {
	
	
	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			long [][] input = readInput();
			long sum = 0l ;
			
			class Cut extends RuntimeException {};
			
			class CheckForGoal {
				void __(long goal, long accumulator, long [] operands, int startingFrom) {
					if (startingFrom == operands.length) {
						if (goal == accumulator) throw new Cut();
						return;
					}
					if (accumulator<=goal) {
						__(goal, accumulator + operands[startingFrom], operands, startingFrom +1);
						__(goal, accumulator * operands[startingFrom], operands, startingFrom +1);
						__(goal, concatenate(accumulator, operands[startingFrom]), operands, startingFrom +1);
					}
				}

				private long concatenate(long a, long b) {
					return Long.valueOf(""+a+b);
				}
			} var innerFunc = new CheckForGoal();
			
			for (var v: input) {
				try {
					innerFunc.__(v[0], v[1], v, 2);
				}
				catch(Cut cut) {
					sum +=v[0];
				}
			}
			System.out.println(sum);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	



	private static long[][] readInput() {
		try {
			var result = new ArrayList<long[]>();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program07.class.getResourceAsStream("/input07.txt")))) {
				String currLine;
				while ((currLine = reader.readLine())!= null ) {
					String splits[] = currLine.split(":\\s+|\\s+");
					long[] currLongs= new long[splits.length];
					result.add(currLongs);
					for (int i=0; i<splits.length; i++) {
						currLongs[i] = Long.valueOf(splits[i]);
					}
				}
			}
			return result.toArray(new long[][] {});
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

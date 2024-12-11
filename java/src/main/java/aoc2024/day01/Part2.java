package aoc2024.day01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Part2 {
	public static void main(String[] args) {
		try {
			// we read input data as two arrays
			Long[][] data = readInput();
			// the algorithm is too banal to do a method
			System.out.println("Answer is: " + computeSimilarityScore(data[0],data[1]));
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	private static long computeSimilarityScore(Long[] arr0, Long[] arr1) {
		HashMap<Long, Long> occurrences = new HashMap<>();
		for (long key:arr1) {
			occurrences.putIfAbsent(key, 0l);
			occurrences.put(key,occurrences.get(key)+1);
		}
		long sum = 0;
		for (long elm:arr0) {
			sum += elm * occurrences.getOrDefault(elm, 0l);
		}
		return sum;
	}

	private final static Long[] LONGS = {}; 

	private static Long[][] readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Part2.class.getResourceAsStream("/input01.txt")))) {
				String currLine;
				ArrayList<Long> arr0 = new ArrayList<>(), arr1= new ArrayList<>();
				Pattern pattern = Pattern.compile("\\s+");
				while ((currLine = reader.readLine())!= null) {
					String[] numbers = pattern.split(currLine);
					arr0.add(Long.parseLong(numbers[0]));
					arr1.add(Long.parseLong(numbers[1]));
				}
				return new Long[][]{arr0.toArray(LONGS), arr1.toArray(LONGS)};
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}

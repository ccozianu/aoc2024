package aoc2024.day01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Program {

	public static void main(String[] args) {
		try {
			// we read input data as two arrays
			Long[][] data = readInput();
			// the algorithm is too banal to do a method
			Arrays.sort(data[0]);
			Arrays.sort(data[1]);
			long sum = 0;
			for (int i = 0; i< data[0].length; i++) {
				sum += Math.abs(data[0][i]-data[1][i]);
			}
			System.out.println("Answer is: "+sum);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private final static Long[] LONGS = {}; 

	private static Long[][] readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program.class.getResourceAsStream("/input01.txt")))) {
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

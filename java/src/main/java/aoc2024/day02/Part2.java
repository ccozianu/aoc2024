package aoc2024.day02;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Part2 {

	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			Long[][] data = readInput();
			long count = 0;
			for(int i=0; i<data.length; i++) {
				if (! isDataSafe(data[i], true)) {
					System.err.println("unsafe data: " + i+ ": "+Arrays.asList(data[i]));
				}
				count += isDataSafe(data[i], true) ? 1 : 0;
			}
			System.out.println("Answer is: "+count);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private static boolean isDataSafe(Long[] lineData, boolean canTryRemoving) {
		if (lineData.length<2) {
			return true;
		}
		long firstDiff = lineData[1] - lineData[0];
		int sign = Long.signum(firstDiff);
		if (firstDiff*sign > 3 || sign == 0 ) {
			return canTryRemoving &&(isSafeRemoving(lineData,0) || isSafeRemoving(lineData, 1));
		}
		for(int i = 2; i<lineData.length;i++) {
			long currDiff = lineData[i] - lineData[i-1];
			long currSign = Long.signum(currDiff);
			if (currSign ==0 || currSign != sign || currDiff * currSign > 3) {
				return canTryRemoving && (isSafeRemoving(lineData,i) || isSafeRemoving(lineData,i-1) || isSafeRemoving(lineData,i-2));
			}
		}
		return true;
	}

	private static boolean isSafeRemoving(Long[] lineData, int i) {
		ArrayList<Long> newData= new ArrayList<>(lineData.length);
		newData.addAll(Arrays.asList(lineData));
		newData.remove(i);
		return isDataSafe(newData.toArray(LONGS[0]), false);
	}

	private final static Long[][] LONGS = {{}}; 

	private static Long[][] readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Part2.class.getResourceAsStream("/input02.txt")))) {
				String currLine;
				ArrayList<Long[]> result = new ArrayList<>();
				Pattern pattern = Pattern.compile("\\s+");
				while ((currLine = reader.readLine())!= null) {
					String[] numbers = pattern.split(currLine);
					Long[] lineData = new Long[numbers.length];
					for (int i=0; i< numbers.length; i++) {
						lineData[i] = Long.parseLong(numbers[i]);
					}
					result.add(lineData);
				}
				return result.toArray(LONGS);
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

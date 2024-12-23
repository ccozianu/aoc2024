package aoc2024.day19;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;


public class Program19Part2 {

	public static void main(String[] args) {
		String[][] input= readInput();
		String[] availablePatterns = input[0];
		String[] targets=input[1]; 
		long total=0;
		for (var target:targets) {
			total += canBeComposedOf(target, availablePatterns);
		}
		System.out.println(total);

	}

	private static long canBeComposedOf(final String target, final String[] availablePatterns) {
		final Long[] cache = new Long[target.length()+1];
		cache[target.length()]=1l;
		class Recurse {
			long __(int index) {
				if (cache[index] != null) {
					return cache[index];
				}
				String localTarget = target.substring(index);
				long total =0;
				for(var pattern:availablePatterns) {
					if (localTarget.startsWith(pattern)) {
						total += __(index + pattern.length());
					}
				}
				return cache[index] = total;
			}
		} var recurse =  new Recurse();
		
		return recurse.__(0);
	}

	private static String[][] readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program19Part2.class.getResourceAsStream("/input19test.txt")))) {
				String [] words = reader.readLine().split(", ");
				Arrays.sort(words);
				reader.readLine();
				String currLine;
				ArrayList<String> targets = new ArrayList<>();
				while ((currLine=reader.readLine()) != null ) {
					targets.add(currLine);
				}
				return new String[][] { words, targets.toArray(new String[] {})};
			}
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}

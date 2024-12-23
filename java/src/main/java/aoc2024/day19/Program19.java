package aoc2024.day19;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;


public class Program19 {

	public static void main(String[] args) {
		String[][] input= readInput();
		String[] availablePatterns = input[0];
		String[] targets=input[1]; 
		int total=0;
		for (var target:targets) {
			if (canBeComposedOf(target, availablePatterns)) {
				total++;
			}
		}
		System.out.println(total);

	}

	// both this and Part2 can be done much more efficiently, but since the first try runs under a second
	// simplest solution is always better these days
	private static boolean canBeComposedOf(final String target, final String[] availablePatterns) {
		final Boolean[] cache = new Boolean[target.length()+1];
		cache[target.length()]=true;
		class Recurse {
			boolean __(int index) {
				if (cache[index] != null) {
					return cache[index];
				}
				String localTarget = target.substring(index);
				for(var pattern:availablePatterns) {
					if (localTarget.startsWith(pattern)) {
						boolean success = __(index + pattern.length());
						if (success) {
							return cache[index] = success;
						}
					}
				}
				// if nwe reach here we are unsuccessful
				return cache[index] = false;
			}
		} var recurse =  new Recurse();
		
		return recurse.__(0);
	}

	private static String[][] readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program19.class.getResourceAsStream("/input19test.txt")))) {
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

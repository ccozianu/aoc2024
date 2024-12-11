package aoc2024.day03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class Program03Part2 {

	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			List<String> data = readInput();
			Pattern pat= Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)");
			long sum=0;
			boolean do_ = true;
			for(var lineData:data) {
				Matcher matcher = pat.matcher(lineData);
				while(matcher.find()) {
					var s = matcher.group(0);
					var s1=matcher.group(1);
					var s2=matcher.group(2);
					if(s.equals("do()")) {
						do_ = true;
					}
					else if (s.equals("don't()")) {
						do_ = false;
					}
					else if (do_) {
						sum+=Long.parseLong(s1)*Long.parseLong(s2);
					}
				}
			}
			System.out.println("Answer is: "+sum);
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private static List<String> readInput() {
		try {
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program03Part2.class.getResourceAsStream("/input03.txt")))) {
				String currLine;
				ArrayList<String> result = new ArrayList<>();
				while ((currLine = reader.readLine())!= null) {
					result.add(currLine);
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

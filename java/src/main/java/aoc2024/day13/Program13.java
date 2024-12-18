package aoc2024.day13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Program13 {
	
	private static final long PRICE_A = 3;
	private static final long PRICE_B = 1;
	private LongEq2x2[] equations;

	public Program13(LongEq2x2[] input) {
		this.equations = input;
	}

	/**
	 * A diophantine equation two by two
	 * Represents the equation , in the dimensions as presented above
	 * (x,y)(m00,m01) = ( target0 )
	 *      (m10,m11)   ( target1 )
	 */
	private static class LongEq2x2 {
		public LongEq2x2(long[][] matrix, long[] target) {
			this.matrix = matrix;
			this.target = target;
		}
		final long [][] matrix;
		final long [] target;
		
		public long[] solveIt() {
			long m10_reduced = matrix[1][0]*matrix[0][1] - matrix[1][1]*matrix[0][0];
			long t0_reduced = target[0]* matrix[0][1] - target[1]*matrix[0][0];
			if (m10_reduced==0) {
				throw new UnsupportedOperationException("Not implemented just yet");
			}
			if (t0_reduced % m10_reduced != 0) {
				// no integer solution
				return null; 
			}
			long y = t0_reduced / m10_reduced;
			long t0_x = (target[0] - y * matrix[1][0]);
			if (t0_x % matrix[0][0] != 0) {
				// x not integer, bad luck
				return null;
			}
			long x = t0_x / matrix[0][0]; 
			return new long[] {x,y};
		}
	}



	public static void main(String[] args) {
		try {
			// we read input data as an array of long arrays, each inner long array containing one data line
			var input = readInput();
			Program13 program = new Program13(input);
			
			System.out.println(program.run());
		}
		catch(Exception ex) {
			System.out.println("Unexpected exception caught in main(): " + ex);
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	
	private long run() {
		long total=0;
		for( var eq: this.equations) {
			long[] sol = eq.solveIt();
			if(sol != null) {
				total += PRICE_A * sol[0] + PRICE_B*sol[1];
			}
		}
		return total;
	}


	private static LongEq2x2[] readInput() {
		try {
			var result = new ArrayList<LongEq2x2>();
			Pattern patternA = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
			Pattern patternB = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
			Pattern patternX = Pattern.compile( "Prize: X=(\\d+), Y=(\\d+)"); 
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program13.class.getResourceAsStream("/input13.txt")))) {
				String currLine;
				while (true) {
					currLine = reader.readLine();
					var matcherA= patternA.matcher(currLine);
					if(!matcherA.matches()) {
						throw new IllegalArgumentException(currLine);
					}
					long[] rowA = {Long.parseLong(matcherA.group(1)), Long.parseLong(matcherA.group(2))};

					currLine = reader.readLine();
					var matcherB= patternB.matcher(currLine);
					if ( !matcherB.matches() ) {
						throw new IllegalArgumentException(currLine);
					}
					long[] rowB = {Long.parseLong(matcherB.group(1)), Long.parseLong(matcherB.group(2))};
					
					currLine = reader.readLine();
					var matcherX= patternX.matcher(currLine);
					if ( !matcherX.matches() ) {
						throw new IllegalArgumentException(currLine);
					}
					long[] rowX = {Long.parseLong(matcherX.group(1)), Long.parseLong(matcherX.group(2))};
					
					result.add(new LongEq2x2(new long[][]{rowA,rowB}, rowX));

					if (reader.readLine() == null) break;
				}
			}
			return result.toArray(new LongEq2x2[] {});
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}

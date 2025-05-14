package aoc2024.day24;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Program24 {

	public static void main(String[] args) {
		try {
			Circuit circuit = readInput();
			System.out.println(circuit.solve());
		}
		catch(Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	

	private static class Circuit {
		
		private final Map<String, Wire> allWires;
		private final Map<Integer, Wire> output;

		public Circuit(Map<String, Wire> allWires, Map<Integer, Wire> output) {
			// TODO Auto-generated constructor stub
			this.allWires = allWires;
			this.output = output;
		}

		public long solve() {
			Stack<Wire> solverStack = new Stack<>();
			Map<String, Boolean> solverCache = new HashMap<>();
			solverStack.addAll(this.output.values());
			while(! solverStack.isEmpty()) {
				Wire next = solverStack.peek();
				if (solverCache.containsKey(next.name())) {
					solverStack.pop();
					continue;
				}
				String [] dependsOn = next.dependsOn();
				boolean depsResolved = true;
				for(String toSolve: dependsOn) {
					if (! solverCache.containsKey(toSolve)) {
						depsResolved = false;
						solverStack.push(allWires.get(toSolve));
					}
				}
				if (depsResolved) {
					boolean input[] = new boolean[dependsOn.length];
					for(int i=0;i<input.length;i++) {
						input[i] = solverCache.get(dependsOn[i]);
					}
					solverCache.put(next.name(), next.eval(input));
					solverStack.pop();
				}
			}
			long result = 0;
			for(var e:output.entrySet()) {
				if (solverCache.get(e.getValue().name())) {
					result += (1l << e.getKey());
				}
			}
			return result;
		}

		private static class BuilderFromInput{
			
			Map<String,Wire> allWires = new HashMap<>();
			Map<Integer,Wire> output= new HashMap();
		
			final Pattern outputName= Pattern.compile("z(?<index>\\d\\d)");
			void addInputNode(String name, boolean value) {
				Wire result = new InputWire(name,value);
				if (allWires.putIfAbsent(name, result)!= null) {
					throw new IllegalStateException("name:" + name+ " was already assigned");
				}
			}

			public void addOpNode(java.lang.String name, java.lang.String op, java.lang.String input1,
					java.lang.String input2) {
				Wire result = new OpWire(name, op, input1, input2);
				if (allWires.putIfAbsent(name, result)!= null) {
					throw new IllegalStateException("name:" + name+ " was already assigned");
				}
				Matcher match;
				if ((match=outputName.matcher(name)).matches()) {
					output.put(Integer.parseInt(match.group("index")), result);
				}
			}

			Circuit build() {
				return new Circuit(allWires, output);
			}

		}
		
		
		// poor man's union type in java
		private static abstract class Wire {
			private final String name;

			abstract String[] dependsOn();
			
			private Wire(String name) {
				this.name = name;
			}

			protected abstract boolean eval(boolean[] input);

			protected final String name() {
				return this.name;
			}
		}
		
		private static class InputWire extends Wire {
			
			private final boolean val;
			
			InputWire(String name, boolean val_) {
				super(name);
				this.val = val_;
			}
			
			@Override
			protected boolean eval(boolean[] input) {
				return this.val;
			}
			
			@Override
			java.lang.String[] dependsOn() {
				return new String[]{};
			}
		}
			
		private static class OpWire extends Wire {
			
			private final java.lang.String[] input = {null,null};
			Function<boolean[], Boolean> op;

			OpWire(String name, String op, String input1, String input2) {
				super(name);
				switch(op) {
					case "OR" : this.op = (input -> input[0] || input[1]);
								break;
					case "AND" : this.op = (input -> input[0] && input[1]);
								 break;
					case "XOR" : this.op = (input -> input[0] ^ input[1]);
								 break;
				
				}
				this.input[0] = input1;
				this.input[1] = input2;
			}
			
			@Override
			String[] dependsOn() {
				return input;
			}
			
			@Override
			protected boolean eval(boolean[] input) {
				return op.apply(input);
			}
		}
		
	}
		
	private static Circuit readInput() {
		try {
			var resultBuilder = new Circuit.BuilderFromInput();
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program24.class.getResourceAsStream("/input24.txt")))) {
				String currLine;
				{
					Pattern pattern1 = Pattern.compile("([a-z]\\d\\d): ([01])");
					while (!(currLine = reader.readLine()).isBlank()) {
						var matcher= pattern1.matcher(currLine);
						matcher.matches();
						resultBuilder.addInputNode(matcher.group(1), matcher.group(2).equals("1"));
					}
				}
				
				{
					Pattern pattern2 = Pattern.compile("([a-z0-9]{3})\\s(XOR|AND|OR)\\s([a-z0-9]{3})\\s->\\s([a-z0-9]{3})");
					while ((currLine = reader.readLine()) != null) {
						var matcher= pattern2.matcher(currLine);
						matcher.matches();
						resultBuilder.addOpNode(matcher.group(4), matcher.group(2),matcher.group(1), matcher.group(3));
					}
				}
				
			}
			return resultBuilder.build();
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	

}

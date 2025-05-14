package aoc2024.day24;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Program24Part2Take1 {

	private static class AddressingWithRenaming implements Cloneable {
		final Map<String,Boolean> baseMap;
		final Map<String,String> flips = new HashMap<>();
		public AddressingWithRenaming(Map<String, Boolean> initialValues) {
			baseMap = new HashMap<>(initialValues);
		}

		private String realNameFor(String name) {
			return flips.getOrDefault(name, name);
		}

		public boolean containsKey(String name) {
			return baseMap.containsKey(realNameFor(name));
		}

		public boolean get(String name) {
			return baseMap.get(realNameFor(name));
		}

		public void put(String name, boolean val_) {
			baseMap.put(realNameFor(name),val_);
		}
		
		@Override
		protected AddressingWithRenaming clone() {
			var result = new AddressingWithRenaming(baseMap);
			result.flips.putAll(this.flips);
			return result;
		}

		public void flip(String name1, String name2) {
			if (flips.containsKey(name1)) {
				throw new IllegalArgumentException("Name: "+ name1 + "is already flipped to: " + flips.get(name1));
			}
			if (flips.containsKey(name2)) {
				throw new IllegalArgumentException("Name: "+ name2 + "is already flipped to: " + flips.get(name2));
			}
			flips.put(name1,name2);
			flips.put(name2, name1);
		}

		public String[] realDependsOn(String[] dependsOn) {
			var result = new String[dependsOn.length];
			int i=0;
			for (var s:dependsOn) {
				result[i++] = realNameFor(s);
			}
			return result;
		}

		public boolean canFlip(String name1, String name2) {
			return ! (flips.containsKey(name1) || flips.containsKey(name2));
		}
	}


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
	

	private static class Circuit implements Cloneable{
		
		private final Map<String, Wire> allWires;
		private final int WireCount;
		private final Wire[] wireArray;
		
		private final Map<Integer,InputWire> Xbits;
		private final Map<Integer,InputWire> Ybits;

		private final Map<Integer, Wire> output;
		private final AddressingWithRenaming initializedCache;

		public Circuit ( Map<String, Wire> allWires,
						Map<Integer, Wire> output,
						Map<Integer, InputWire> xwires,
						Map<Integer, InputWire> ywires)
		{
			this(allWires,output,xwires,ywires, new AddressingWithRenaming(Collections.EMPTY_MAP),true);
		}
		private Circuit( Map<String, Wire> allWires,
						Map<Integer, Wire> output,
						Map<Integer, InputWire> xwires,
						Map<Integer, InputWire> ywires,
						AddressingWithRenaming cache,
						boolean doBootStrapCache) {
			// TODO Auto-generated constructor stub
			this.allWires = allWires;
			this.output = output;
			this.Xbits = xwires;
			this.Ybits = ywires;
			int i=0;
			WireCount = allWires.size();
			wireArray = new Wire[WireCount];
			this.initializedCache = cache.clone();
			for(Wire w: allWires.values()) {
				wireArray[i++] = w;
			}
			if (doBootStrapCache) {
				for(var x:xwires.values()) {
					initializedCache.put(x.name(), x.val);
				}
				for(var y:ywires.values()) {
					initializedCache.put(y.name(), y.val);
				}
			}
		}

		@Override
		protected Circuit clone() {
			return new Circuit(allWires, output, Ybits, Xbits, initializedCache, false);
		}
		
		public String solve() {
			int originalDelta = this.computeDelta();
			System.out.println("Original delta: "+ originalDelta);
			this.initializedCache.flip("z06", "qsf");
			int maxDelta = this.computeDelta();
			System.out.println("Max delta: "+ maxDelta);
			
			final boolean [][]toAvoid=new boolean[WireCount][];
			for (int i=0;i<WireCount;i++) {
				toAvoid[i]=new boolean[WireCount];
			}
			loop0:
			for (int i0=0; i0< WireCount-1; i0++) {
				for (int j0=i0+1; j0<WireCount;j0++) {
					if (!this.canFlip(i0,j0)) {
						continue loop0;
					}
					Circuit newCircuit0 = this.cloneWithFlip(i0,j0);
					int newDelta = newCircuit0.computeDelta();
					if (newDelta > maxDelta)  {
						continue loop0;
					}
					if (newDelta>originalDelta) {
						//toAvoid[i0][j0] = true;
					}
					if (newDelta < maxDelta) {
						maxDelta= newDelta;
						System.out.println("newDelta, level 0: " + newDelta);
					}
					loop1:
					for(int i1 = i0+ 1; i1<WireCount; i1 ++ ) {
						for (int j1=i1+1; j1<WireCount;j1++) {
							if (!newCircuit0.canFlip(i1,j1)) {
									continue loop1;
							}
							Circuit newCircuit1 = newCircuit0.cloneWithFlip(i1,j1);
							int newDelta1 = newCircuit1.computeDelta();
							if (newDelta1 > maxDelta+4)  {
								continue loop1;
							}
							if (newDelta1 < maxDelta) {
								maxDelta= newDelta1;
								System.out.println("newDelta, level 1: " + newDelta1);
							}
							loop2:
							for(int i2 = i1+ 1; i2<WireCount; i2 ++ ) {
								for (int j2=i2+1; j2<WireCount;j2++) {
									if (!newCircuit1.canFlip(i1,j1)) {
											continue loop2;
									}
									Circuit newCircuit2 = newCircuit1.cloneWithFlip(i2,j2);
									int newDelta2 = newCircuit2.computeDelta();
									if (newDelta2 > maxDelta)  {
										continue loop2;
									}
									if (newDelta2 < maxDelta) {
										maxDelta= newDelta2;
										System.out.println("newDelta, level 2: " + newDelta2);
									}
								}
							}
							
						}
					}
				}
			}

			class Recursion {
				void apply(Circuit starter, int prevDelta, int level, int startIdx) {
					if (level == 0) {
						if (prevDelta == 0) {
							throw new RuntimeException("Found solution for: "+starter.initializedCache.flips);
						}
						return;
					}
					int levelMaxDelta  = prevDelta;
					for (int i0=startIdx; i0< WireCount-1; i0++) {
						if(level>=3) {
							System.out.println("level: " + level + " startIdx: "+i0);
						}
						for (int j0=i0+1; j0<WireCount;j0++) {
							if (starter.canFlip(i0,j0) && ! toAvoid[i0][j0]) {
								Circuit newCircuit0 = starter.cloneWithFlip(i0,j0);
								int newDelta = newCircuit0.computeDelta();
								if (newDelta<=levelMaxDelta) {
									if (newDelta < levelMaxDelta) {
										levelMaxDelta = newDelta;
										if (level>=2) {
											System.out.println("level: " + level + " newDelta: " + newDelta);
										}
									}
									apply(newCircuit0,newDelta, level-1, startIdx+1);
								}
								else {
									if (newDelta > prevDelta) {
										toAvoid[i0][j0] = true;
									}
								}
							}
						}
					}
				}
			} final var recursiveLoop = new Recursion();
			recursiveLoop.apply(this, originalDelta, 3, 0);
			return "End.";
		}
		
		private boolean canFlip(int i0, int j0) {
			return this.initializedCache.canFlip(wireArray[i0].name(),wireArray[j0].name());
		}

		private int computeDelta() {
			long x = toLong((Map)Xbits, initializedCache.baseMap);
			long y = toLong((Map)Ybits, initializedCache.baseMap);
			long z = x + y;
			long computed = compute();
			long diff = computed ^ z;
			BitSet bs = BitSet.valueOf(new long[] {diff});
			return bs.cardinality();
		}

		private Circuit cloneWithFlip(int i0, int j0) {
			Circuit result = this.clone();
			result.flip(i0,j0);
			return result;
		}

		private void flip(int i0, int j0) {
			this.initializedCache.flip(wireArray[i0].name(), wireArray[j0].name());
		}

		public long compute() {
			ArrayList<Wire> solverStack = new ArrayList<>(wireArray.length);
			AddressingWithRenaming  solverCache = this.initializedCache;
			solverStack.addAll(this.output.values());
			while(! solverStack.isEmpty()) {
				int top = solverStack.size()-1;
				Wire next = solverStack.get(top);
				if (solverCache.containsKey(next.name())) {
					solverStack.remove(top);
					continue;
				}
				String [] dependsOn = solverCache.realDependsOn(next.dependsOn());
				boolean depsResolved = true;
				for(String toSolve: dependsOn) {
					if (! solverCache.containsKey(toSolve)) {
						depsResolved = false;
						solverStack.add(allWires.get(toSolve));
					}
				}
				if (depsResolved) {
					boolean input[] = new boolean[dependsOn.length];
					for(int i=0;i<input.length;i++) {
						input[i] = solverCache.get(dependsOn[i]);
					}
					solverCache.put(next.name(), next.eval(input));
					solverStack.remove(top);
				}
			}
			long result = toLong(output,solverCache);
			return result;
		}

		private static long toLong(Map<Integer,Wire> wires, AddressingWithRenaming computedValues) {
			long result = 0;
			for(var e:wires.entrySet()) {
				if (computedValues.get(e.getValue().name())) {
					result += (1l << e.getKey());
				}
			}
			return result;
		}

		private static long toLong(Map<Integer,Wire> wires, Map<String,Boolean> computedValues) {
			long result = 0;
			for(var e:wires.entrySet()) {
				if (computedValues.get(e.getValue().name())) {
					result += (1l << e.getKey());
				}
			}
			return result;
		}

		private static class BuilderFromInput{
			
			Map<String,Wire> allWires = new HashMap<>();
			Map<Integer,Wire> output= new HashMap<>();
			
			Map<Integer,InputWire> Xwires = new HashMap<>();
			Map<Integer,InputWire> Ywires = new HashMap<>();
		
			final Pattern outputName= Pattern.compile("z(?<index>\\d\\d)");
			final Pattern inputName= Pattern.compile("(x|y)(?<index>\\d\\d)");
			void addInputNode(String name, boolean value) {
				var result = new InputWire(name,value);
				if (allWires.putIfAbsent(name, result)!= null) {
					throw new IllegalStateException("name:" + name+ " was already assigned");
				}
				Matcher match;
				if ((match=inputName.matcher(name)).matches()) {
					(match.group(1).equals("x")? Xwires:Ywires).put(Integer.parseInt(match.group("index")), result);
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
				return new Circuit(allWires, output, Xwires,Ywires);
			}

		}
		
		
		// poor man's union type in java
		private static abstract class Wire implements Cloneable {
			private final String name;

			abstract String[] dependsOn();
			
			private Wire(String name) {
				this.name = name;
			}

			protected abstract boolean eval(boolean[] input);

			protected final String name() {
				return this.name;
			}
			@Override
			public String toString() {
				return this.name();
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
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program24Part2Take1.class.getResourceAsStream("/input24.txt")))) {
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

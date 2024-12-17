package aoc2024.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Minimalistic UnionFind data structure
 */
public class UnionFind {
	
	// minimalistic union find
	public static class Builder {
		
		private final int[] backingArray;

		public Builder(int n) {
			this.backingArray = new int[n];
			for(int i=0; i<n; i++) {
				this.backingArray[i] = i;
			}
		}
		
		private int classOf(int i) {
			
			while (backingArray[i] != i) {
				int nxt = backingArray[backingArray[i]];
				backingArray[i] = nxt;
				i = nxt;
			}
			return i;
			
		}
		
		public void unite(int i, int j) {
			backingArray[classOf(i)] = classOf(j);
		}
		
		public UnionFind build() {
			return new UnionFind(this.backingArray);
		}
	}


	private final int[] backingArray;
	private Map<Integer,List<Integer>> cachedClasses;
	
	private UnionFind(int[] backingArray) {
		this.backingArray = backingArray.clone();
	}
	
	public int size() {
		return this.backingArray.length;
	}
	
	public int classOf(int i) {
		while (i!=backingArray[i]) {
			i=backingArray[i];
		}
		return i;
	}
	
	public boolean areEquivalent(int i, int j) {
		return classOf(i) == classOf(j);
	}

	public Map<Integer, List<Integer>> getClasses() {
		if (this.cachedClasses != null) {
			return cachedClasses;
		}
		else {
			HashMap<Integer, List<Integer>> result = new HashMap<>();
			for (int i=0; i<size();i++) {
				int cls = classOf(i);
				List<Integer> classList;
				if ((classList = result.get(cls))==null) {
					result.put(cls, classList = new LinkedList<>());
				}
				classList.add(i);
			}
			return cachedClasses=result;
		}
	}
}

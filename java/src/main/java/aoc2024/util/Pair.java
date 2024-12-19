package aoc2024.util;

public class Pair<X,Y> {
	public final X _1;
	public final Y _2;
	public Pair(X x, Y y) {
		this._1 = x;
		this._2 = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj instanceof Pair
				&& this._1.equals(((Pair) obj)._1) 
				&& this._2.equals(((Pair) obj)._2);
	}
	
	@Override
	public int hashCode() {
		return _1.hashCode()^ _2.hashCode();
	}
	
	@Override
	public String toString() {
		return "("+ _1 + ',' + _2 +")";
	}

}

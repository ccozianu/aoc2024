package aoc29024.util;

public class LongPair {
	public final long X;
	public final long Y;
	public LongPair(long x, long y) {
		this.X = x;
		this.Y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj instanceof LongPair
				&& this.X == ((LongPair) obj).X 
				&& this.Y == ((LongPair) obj).Y;
	}
	
	@Override
	public int hashCode() {
		return (int) (X ^ Y);
	}
	
	@Override
	public String toString() {
		return "("+X+','+Y+")";
	}
}

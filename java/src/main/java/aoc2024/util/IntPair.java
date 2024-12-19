package aoc2024.util;

public class IntPair {
	public final int X;
	public final int Y;
	public IntPair(int x, int y) {
		this.X = x;
		this.Y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj instanceof IntPair
				&& this.X == ((IntPair) obj).X 
				&& this.Y == ((IntPair) obj).Y;
	}
	
	@Override
	public int hashCode() {
		return X ^ Y;
	}
	
	@Override
	public String toString() {
		return "("+X+','+Y+")";
	}

	public IntPair moveBy(IntPair dir) {
		return new IntPair(X+dir.X, Y + dir.Y);
	}

	public IntPair opposite() {
		return new IntPair(-X,-Y);
	}

}

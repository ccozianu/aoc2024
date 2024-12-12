package aoc29024.util;

public class ImmutablePoint {
	public final int X;
	public final int Y;
	public ImmutablePoint(int x, int y) {
		this.X = x;
		this.Y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj instanceof ImmutablePoint
				&& this.X == ((ImmutablePoint) obj).X 
				&& this.Y == ((ImmutablePoint) obj).Y;
	}
	
	@Override
	public int hashCode() {
		return X ^ Y;
	}
	
	@Override
	public String toString() {
		return "("+X+','+Y+")";
	}
}

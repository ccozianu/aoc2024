package aoc2024.day14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2024.util.UnionFind;


public class Program14Part2 {

	public static void main(String[] args) {
		try {
//		Grid t5 = testGrid.moveInSeconds(5);
		Grid grid= readInput();
		grid.displayTo(System.out,4);
		grid.displayTo(System.out,1);
		
		for (int i=1; i< 100000;i++) {
			grid.moveInSeconds(1);
			if (grid.isLikelyToTarget()) {
				grid.displayTo(System.out,4);
				grid.displayTo(System.out,1);
				System.out.println(i);
				Thread.sleep(5000);
			}
			if (i%1000000 == 999999) {
				grid.displayTo(System.out,4);
				System.out.println("Step: "+i);
			}
		}
		}
		catch(Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static class Grid {
		final int XRANGE=101, YRANGE=103;
		private Robot[] robots;
		public Grid(Robot[] startingRobots){
			this.robots = startingRobots;
		}
		
		public void displayTo(PrintStream out, int scale) {
			char[][] display = new char[YRANGE/scale+1][];
			for(int i=0;i<YRANGE/scale+1;i++) {
				display[i]= new char[XRANGE/scale+1];
				Arrays.fill(display[i], ' ');
			}
			for(var r:robots) {
				display[r.yStart/scale][r.xStart/scale] = 'X';
			}
			System.out.println();
			for(var l: display) {
				System.out.println(l);
			}
		}

		int X_PLUS = XRANGE+1;
		int Y_PLUS = YRANGE+1;
		boolean scaledBitmap[]= new boolean[X_PLUS*Y_PLUS];
		
		public boolean isLikelyToTarget() {
			Arrays.fill(scaledBitmap, false);
			UnionFind.Builder ufBuilder = new UnionFind.Builder((XRANGE+1)*(YRANGE+1));
			for(var r:robots) {
				scaledBitmap[r.xStart*Y_PLUS+r.yStart] = true;
			}
			for(var r:robots) {
				if (scaledBitmap[(r.xStart+1)*Y_PLUS+r.yStart]) {
					ufBuilder.unite(r.xStart*Y_PLUS+r.yStart, (r.xStart+1)*Y_PLUS+r.yStart);
				}
				if (scaledBitmap[r.xStart+1*Y_PLUS+r.yStart+1]) {
					ufBuilder.unite(r.xStart*Y_PLUS+r.yStart, r.xStart*Y_PLUS+r.yStart+1);
				}
			}
			return ufBuilder.getMaxClassSize()>30;
		}
		
		public long safetyFactor() {
			long [][] accumulator = new long[][]{{0,0},{0,0}};
			int halfR=XRANGE/2 + 1;
			int halfC=YRANGE/2 + 1;
			for(var r: robots) {
				if (r.xStart == halfR - 1 || r.yStart == halfC - 1) {
					continue;
				}
				accumulator[r.xStart/halfR][r.yStart/halfC] ++;
			}
			return accumulator[0][0]*accumulator[0][1]*accumulator[1][0]*accumulator[1][1];
		}

		void moveInSeconds(int seconds) {
			for(int i=0;i<robots.length; i++) {
				robots[i].move(seconds, XRANGE,YRANGE);
			}
		}
	}
	private static class Robot {
		int xStart,yStart;
		final int speedX, speedY;
		Robot(int x, int y,int speedX, int speedY){
			this.xStart=x;
			this.yStart=y;
			this.speedX=speedX;
			this.speedY=speedY;
		}
		
		public void move(int seconds, int ROWS, int COLS) {
			xStart = modulo(xStart + seconds*speedX, ROWS);
			yStart = modulo(yStart + seconds*speedY, COLS);
		}
	}
	private static Grid readInput() {
		try {
			var result = new ArrayList<Robot>();
			Pattern pattern = Pattern.compile("p=(\\d+),(\\d+) v=([-]?\\d+),([-]?\\d+)");
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program14Part2.class.getResourceAsStream("/input14.txt")))) {
				String currLine;
				while ((currLine = reader.readLine()) != null) {
					
					var matcher= pattern.matcher(currLine);
					matcher.matches();
					Robot robot = new Robot (
							Integer.parseInt(matcher.group(1)),// startX 
							Integer.parseInt(matcher.group(2)), // startY
							Integer.parseInt(matcher.group(3)), // speedX
							Integer.parseInt(matcher.group(4)) // speedY							
					);
					result.add(robot);
				}
			}
			return new Grid(result.toArray(new Robot[] {}));
		} catch (RuntimeException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Because java's modulo return negative numbers
	 * and we want 0<=modulo<y
	 */
	public static int modulo(int x, int y) {
		int result = x %y;
		return result>=0? result: result+y;
	}
	

}

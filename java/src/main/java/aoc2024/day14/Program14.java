package aoc2024.day14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Program14 {

	public static void main(String[] args) {
		try {
		Grid testGrid = new Grid(new Robot[]{new Robot(2,4,2,-3)});
//		Initial state:
//			...........
//			...........
//			...........
//			...........
//			..1........
//			...........
//			...........

//			After 1 second:
//			...........
//			....1......
//			...........
//			...........
//			...........
//			...........
//			...........

//			After 2 seconds:
//			...........
//			...........
//			...........
//			...........
//			...........
//			......1....
//			...........

//			After 3 seconds:
//			...........
//			...........
//			........1..
//			...........
//			...........
//			...........
//			...........

//			After 4 seconds:
//			...........
//			...........
//			...........
//			...........
//			...........
//			...........
//			..........1

//			After 5 seconds:
//			...........
//			...........
//			...........
//			.1.........
//			...........
//			...........
//			...........
//		Grid t1 = testGrid.moveInSeconds(1);
//		Grid t2 = testGrid.moveInSeconds(2);
//		Grid t3 = testGrid.moveInSeconds(3);
//		Grid t4 = testGrid.moveInSeconds(4);
//		Grid t5 = testGrid.moveInSeconds(5);
		Grid grid= readInput();
		System.out.println(grid.moveInSeconds(100).safetyFactor());
		for (int i=0;i<1000000;i++) {
			Grid next = grid.moveInSeconds(i);
			if (next.isSymmetric()) {
				next.displayTo(System.out);
				System.out.println(i);
				Thread.sleep(5000);
			}
			if (i%1000 == 999) {
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
		
		public void displayTo(PrintStream out) {
			char[][] display = new char[YRANGE][];
			for(int i=0;i<YRANGE;i++) {
				display[i]= new char[XRANGE];
				Arrays.fill(display[i], '.');
			}
			for(var r:robots) {
				display[r.yStart][r.xStart] = '*';
			}
			System.out.println();
			for(var l: display) {
				System.out.println(l);
			}
		}

		public boolean isSymmetric() {
			long [][] accumulator = new long[][]{{0,0},{0,0}};
			int halfR=XRANGE/2 + 1;
			int halfC=YRANGE/2 + 1;
			for(var r: robots) {
				if (r.xStart == halfR - 1 || r.yStart == halfC - 1) {
					continue;
				}
				accumulator[r.xStart/halfR][r.yStart/halfC] ++;
			}		
			return accumulator[1][0] == accumulator[1][1];
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

		Grid moveInSeconds(int seconds) {
			Robot[] nextRobots = new Robot[robots.length];
			for(int i=0;i<robots.length; i++) {
				nextRobots[i] = robots[i].move(seconds, XRANGE,YRANGE);
			}
			return new Grid(nextRobots);
		}
	}
	private static class Robot {
		final int xStart,yStart;
		final int speedX, speedY;
		Robot(int x, int y,int speedX, int speedY){
			this.xStart=x;
			this.yStart=y;
			this.speedX=speedX;
			this.speedY=speedY;
		}
		
		public Robot move(int seconds, int ROWS, int COLS) {
			return new Robot(
						modulo(xStart + seconds*speedX, ROWS),
						modulo(yStart + seconds*speedY, COLS),
						speedX,
						speedY
					);
		}
	}
	private static Grid readInput() {
		try {
			var result = new ArrayList<Robot>();
			Pattern pattern = Pattern.compile("p=(\\d+),(\\d+) v=([-]?\\d+),([-]?\\d+)");
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(Program14.class.getResourceAsStream("/input14.txt")))) {
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

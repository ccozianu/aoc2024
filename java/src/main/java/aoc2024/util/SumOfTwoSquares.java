package aoc2024.util;

public class SumOfTwoSquares {
    public static void main(String[] args) {
        int target = 10429;
        System.out.println("Finding integer solutions for x^2 + y^2 = " + target);

        int limit = (int) Math.sqrt(target);
        for (int x = 1; x <= limit; x++) {
            int remaining = target - x * x;
            int y = (int) Math.round(Math.sqrt(remaining));

            if (y * y == remaining) {
                System.out.println("x = " + x + ", y = " + y);
            }
        }
    }
}

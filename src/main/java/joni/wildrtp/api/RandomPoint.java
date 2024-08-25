package joni.wildrtp.api;

import java.util.Random;

import joni.wildrtp.WildRTP;

public interface RandomPoint {

	public enum Algorithm {
		CIRCLE, SQUARE
	}

	public static int[] getRandomPoint(Algorithm algorithm, double startRadius, double endRadius, int originX,
			int originY) {

		int[] randomPoint = null;

		if (algorithm == null) {
			WildRTP.logger().fine("RandomPoint.getRandomPoint() caused an error:");
			WildRTP.logger().fine("algorithm is null");
			return null;
		}

		if (algorithm.equals(Algorithm.CIRCLE))
			randomPoint = generateRandomPointOnCircle(startRadius, endRadius, originX, originY);

		if (algorithm.equals(Algorithm.SQUARE))
			randomPoint = generateRandomPointOnSquare(startRadius, endRadius, originX, originY);

		if (randomPoint == null) {
			WildRTP.logger().fine("RandomPoint.getRandomPoint() caused an error:");
			WildRTP.logger().fine("randomPoint is null");
		}

		return randomPoint;

	}

	public static int[] generateRandomPointOnCircle(double startRadius, double endRadius, int originX, int originY) {
		Random random = new Random();

		double angle = random.nextDouble() * 2 * Math.PI;
		double radius = startRadius + random.nextDouble() * (endRadius - startRadius);

		int x = originX + (int) (radius * Math.cos(angle));
		int y = originY + (int) (radius * Math.sin(angle));

		return new int[] { x, y };
	}

	public static int[] generateRandomPointOnSquare(double startRadius, double endRadius, int originX, int originY) {
		Random random = new Random();

		double sideLength = endRadius - startRadius;

		int xDirection = random.nextBoolean() ? 1 : -1;
		int yDirection = random.nextBoolean() ? 1 : -1;

		int x = originX + xDirection * ((int) (random.nextDouble() * sideLength) + (int) startRadius);
		int y = originY + yDirection * ((int) (random.nextDouble() * sideLength) + (int) startRadius);

		return new int[] { x, y };
	}

}

package joni.wildrtp.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;

import joni.wildrtp.api.RandomPoint.Algorithm;

public interface GetLocation {

	public static Location getRandomLocation(World w, Algorithm a, double startRadius, double endRadius, int originX,
			int originY) {

		int[] point = RandomPoint.getRandomPoint(a, 0, 0, 0, 0);

		Location loc = w.getHighestBlockAt(point[0], point[1]).getLocation();

		return loc;
	}

	public static SafeLocation getRandomSafeLocationObject(World w, Algorithm a, double startRadius, double endRadius,
			int originX, int originY) {

		Location loc = null;
		int tries = 0;
		boolean safe = false;

		while (!safe) {

			loc = getRandomLocation(w, a, startRadius, endRadius, originX, originY);

			if (w.getEnvironment().equals(Environment.NORMAL) && !(loc.getBlock().isLiquid())) {
				return new SafeLocation(tries, loc);
			}

			if (w.getEnvironment().equals(Environment.THE_END)
					&& !(loc.getBlock().getType().equals(Material.END_STONE))) {
				return new SafeLocation(tries, loc);
			}

			tries++;

			if (tries >= 15) {
				System.err.println("It takes more than 15 tries to get a safelocation!");
				return null;
			}

		}

		return null;
	}

	public static Location getRandomSafeLocation(World w, Algorithm a, double startRadius, double endRadius,
			int originX, int originY) {

		Location loc = null;
		boolean safe = false;
		int tries = 0;

		while (!safe) {

			loc = getRandomLocation(w, a, startRadius, endRadius, originX, originY);

			if (w.getEnvironment().equals(Environment.NORMAL) && !(loc.getBlock().isLiquid())) {
				return loc;
			}

			if (w.getEnvironment().equals(Environment.THE_END)
					&& !(loc.getBlock().getType().equals(Material.END_STONE))) {
				return loc;
			}

			tries++;

			if (tries >= 15) {
				System.err.println("It takes more than 15 tries to get a safelocation!");
				return null;
			}

		}

		return null;
	}

	public class SafeLocation {
		public final int tries;
		public final Location location;

		public SafeLocation(int tries, Location location) {
			this.tries = tries;
			this.location = location;
		}
	}

}

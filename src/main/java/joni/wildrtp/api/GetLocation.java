package joni.wildrtp.api;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;

import io.papermc.lib.PaperLib;
import joni.wildrtp.api.RandomPoint.Algorithm;

public interface GetLocation {

	public static Location getRandomLocation(World w, Algorithm a, double startRadius, double endRadius, int originX,
			int originY) {

		int[] point = RandomPoint.getRandomPoint(a, startRadius, endRadius, originX, originY);

		Location loc = new Location(w, point[0], 0, point[1]);
		Chunk c = PaperLib.getChunkAtAsync(loc).join();
		loc = c.getWorld().getHighestBlockAt(loc.blockX(), loc.blockY()).getLocation();

		return loc;
	}

	public static SafeLocation getRandomSafeLocation(World w, Algorithm a, double startRadius, double endRadius,
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

			if (tries >= 20) {
				System.err.println(
						"It takes more than 20 tries to get a safelocation! Cancelled this to improve performance!");
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

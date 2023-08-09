package joni.wildrtp.api;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import io.papermc.lib.PaperLib;
import joni.wildrtp.api.RandomPoint.Algorithm;

public interface GetLocation {

	public static ChunkLocationSnapshot getRandomLocation(World w, Algorithm a, double startRadius, double endRadius,
			int originX, int originY) {

		try {
			int[] point = RandomPoint.getRandomPoint(a, startRadius, endRadius, originX, originY);

			Location loc = new Location(w, point[0], 0, point[1]);

			CompletableFuture<Chunk> cf = PaperLib.getChunkAtAsync(loc, true);

			Chunk c = cf.get();

			@NotNull
			ChunkSnapshot cs = c.getChunkSnapshot();

			int y = cs.getHighestBlockYAt(loc.getBlockX() & 0xF, loc.getBlockZ() & 0xF) - 1;
			loc.setY(y + 1);
			return new ChunkLocationSnapshot(cs, loc);

		} catch (Exception e) {
			System.out.println("There went something wrong!");
			System.out.println(e);
		}

		return null;

	}

	public static SafeLocation getRandomSafeLocation(World w, Algorithm a, double startRadius, double endRadius,
			int originX, int originY) {

		Location loc = null;
		ChunkSnapshot cs = null;
		int tries = 0;
		boolean safe = false;

		while (!safe) {

			tries++;

			ChunkLocationSnapshot cl = getRandomLocation(w, a, startRadius, endRadius, originX, originY);
			loc = cl.location;
			cs = cl.chunkSnapshot;

			if (w.getEnvironment().equals(Environment.NETHER) && !(loc.getBlock().isLiquid())) {
				for (int y = 0; y < 127; y++) {

					int chunkX = loc.getBlockX() & 15;
					int chunkZ = loc.getBlockZ() & 15;

					@NotNull
					BlockData b = cs.getBlockData(chunkX, y, chunkZ);

					if (b.getMaterial().isSolid()
							&& cs.getBlockData(chunkX, y + 1, chunkZ).getMaterial() == Material.AIR
							&& cs.getBlockData(chunkX, y + 2, chunkZ).getMaterial() == Material.AIR) {
						loc.setY(y + 1);
						return new SafeLocation(tries, loc);
					}
				}
			}

			if (w.getEnvironment().equals(Environment.NORMAL) && !(loc.getBlock().isLiquid())) {
				return new SafeLocation(tries, loc);
			}

			if (w.getEnvironment().equals(Environment.THE_END) && loc.getBlock().getType().equals(Material.END_STONE)) {
				return new SafeLocation(tries, loc);
			}

			if (w.getEnvironment().equals(Environment.CUSTOM) && !(loc.getBlock().isLiquid())) {
				return new SafeLocation(tries, loc);
			}

			if (tries >= 20) {
				System.err.println("");
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

	public class ChunkLocationSnapshot {
		public final ChunkSnapshot chunkSnapshot;
		public final Location location;

		public ChunkLocationSnapshot(ChunkSnapshot chunkSnapshot, Location location) {
			this.chunkSnapshot = chunkSnapshot;
			this.location = location;
		}
	}

}

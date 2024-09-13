package joni.wildrtp.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;

import io.papermc.lib.PaperLib;
import joni.wildrtp.WildRTP;
import joni.wildrtp.api.RandomPoint.Algorithm;

public interface GetLocation {

	public static ChunkLocationSnapshot getRandomLocation(World w, Algorithm a, double startRadius, double endRadius,
			int originX, int originY) {

		try {
			int[] point = RandomPoint.getRandomPoint(a, startRadius, endRadius, originX, originY);

			Location loc = new Location(w, point[0], 0, point[1]);

			CompletableFuture<Chunk> cf = PaperLib.getChunkAtAsync(loc, true);

			Chunk c = cf.get();

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
				if (loc.getBlock().getType().isSolid())
					return new SafeLocation(tries, loc);
			}

			if (w.getEnvironment().equals(Environment.THE_END) && loc.getBlock().getType().equals(Material.END_STONE)) {
				return new SafeLocation(tries, loc);
			}

			if (w.getEnvironment().equals(Environment.CUSTOM) && !(loc.getBlock().isLiquid())) {
				return new SafeLocation(tries, loc);
			}

			if (tries >= 20) {
				FileConfiguration conf = WildRTP.getPlugin().getConfig();
				if (!conf.getBoolean("enabled")) {
					return null;
				}

				loc.setY(conf.getDouble("y"));

				Location b1 = new Location(w, loc.getX() + 1, loc.getY(), loc.getZ());
				Location b2 = new Location(w, loc.getX() + 1, loc.getY(), loc.getZ() + 1);
				Location b3 = new Location(w, loc.getX() + 1, loc.getY(), loc.getZ() - 1);
				Location b4 = new Location(w, loc.getX(), loc.getY(), loc.getZ() + 1);
				Location b5 = new Location(w, loc.getX(), loc.getY(), loc.getZ() - 1);
				Location b6 = new Location(w, loc.getX() - 1, loc.getY(), loc.getZ());
				Location b7 = new Location(w, loc.getX() - 1, loc.getY(), loc.getZ() + 1);
				Location b8 = new Location(w, loc.getX() - 1, loc.getY(), loc.getZ() - 1);
				Location b9 = new Location(w, loc.getX(), loc.getY(), loc.getZ());

				List<Location> locs = new ArrayList<>();

				locs.add(b1);
				locs.add(b2);
				locs.add(b3);
				locs.add(b4);
				locs.add(b5);
				locs.add(b6);
				locs.add(b7);
				locs.add(b8);
				locs.add(b9);

				boolean alt_safe = true;

				for (Location l : locs) {
					if (!l.getBlock().getType().equals(Material.AIR)) {
						alt_safe = false;
					}
				}

				if (alt_safe) {
					Material m = null;
					try {
						m = Material.valueOf(conf.getString("block"));
					} catch (IllegalArgumentException e) {
						m = Material.COBBLESTONE;
					}

					final Material fm = m;

					Bukkit.getScheduler().runTask(WildRTP.getPlugin(), new Runnable() {

						@Override
						public void run() {
							for (Location l : locs) {
								l.getBlock().setType(fm);
							}
						}
					});

				}

				loc.add(0.5, 1, 0.5);
				return new SafeLocation(tries, loc);
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

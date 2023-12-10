package joni.wildrtp.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import io.papermc.lib.PaperLib;
import joni.wildrtp.WildRTP;
import joni.wildrtp.api.GetLocation.SafeLocation;
import joni.wildrtp.api.RandomPoint.Algorithm;

public interface TeleportToRandom {

	public static void teleport(World w, Algorithm a, double startRadius, double endRadius, int originX, int originY,
			Entity e) {

		if (PaperLib.isSpigot() && !PaperLib.isPaper()) {
			Bukkit.getScheduler().runTask(WildRTP.getPlugin(), new Runnable() {

				@Override
				public void run() {
					e.teleport(
							GetLocation.getRandomSafeLocation(w, a, startRadius, endRadius, originX, originY).location
									.add(0, 1, 0));
				}
			});

		}

		new Thread() {
			public void run() {

				PaperLib.teleportAsync(e,
						GetLocation.getRandomSafeLocation(w, a, startRadius, endRadius, originX, originY).location
								.add(0, 1, 0));

			}
		}.start();
	}

	public static void teleportWithInfo(World w, Algorithm a, double startRadius, double endRadius, int originX,
			int originY, Entity e) {

		new Thread() {
			public void run() {

				SafeLocation s = GetLocation.getRandomSafeLocation(w, a, startRadius, endRadius, originX, originY);

				if (PaperLib.isSpigot() && !PaperLib.isPaper()) {
					Bukkit.getScheduler().runTask(WildRTP.getPlugin(), new Runnable() {

						@Override
						public void run() {
							e.teleport(s.location.add(0, 1, 0));
						}
					});
				} else {
					PaperLib.teleportAsync(e, s.location.add(0, 1, 0));
				}

				SendInfo.sendEnd(e, s);
			}
		}.start();

	}

}

package joni.wildrtp.api;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import io.papermc.lib.PaperLib;
import joni.wildrtp.api.GetLocation.SafeLocation;
import joni.wildrtp.api.RandomPoint.Algorithm;

public interface TeleportToRandom {

	public static void teleport(World w, Algorithm a, double startRadius, double endRadius, int originX, int originY,
			Entity e) {
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

				PaperLib.teleportAsync(e, s.location.add(0, 1, 0));
				e.sendMessage("Tries: " + s.tries);
				SendInfo.sendFinished(e, s);
			}
		}.start();
	}

}

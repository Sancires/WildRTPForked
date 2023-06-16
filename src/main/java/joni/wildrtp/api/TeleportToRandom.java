package joni.wildrtp.api;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import joni.wildrtp.api.RandomPoint.Algorithm;

public interface TeleportToRandom {

	public static void teleport(World w, Algorithm a, double startRadius, double endRadius, int originX, int originY,
			Entity e) {
		e.teleport(GetLocation.getRandomSafeLocation(w, a, startRadius, endRadius, originX, originY).location);
	}

	public static void teleport(World w, Algorithm a, double startRadius, double endRadius, int originX, int originY,
			Player p) {
		p.teleport(GetLocation.getRandomSafeLocation(w, a, startRadius, endRadius, originX, originY).location);
	}

}

package joni.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import joni.wildrtp.WildRTP;
import joni.wildrtp.api.RandomPoint.Algorithm;
import joni.wildrtp.api.TeleportToRandom;

public class PlayerTeleportManager {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	public static void teleport(Player p) {

		int centerX = 0;
		int centerZ = 0;

		if (config.getBoolean("teleport.center.player")) {
			centerX = p.getLocation().getBlockX();
			centerZ = p.getLocation().getBlockZ();

		} else {
			centerX = config.getInt("teleport.center.x");
			centerZ = config.getInt("teleport.center.z");
		}

		TeleportToRandom.teleportWithInfo(p.getWorld(), Algorithm.valueOf(config.getString("teleport.algorithm")),
				config.getDouble("teleport.min"), config.getDouble("teleport.max"), centerX, centerZ, p);
	}

}

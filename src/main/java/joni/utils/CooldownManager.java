package joni.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import joni.wildrtp.WildRTP;

public class CooldownManager {

	private static HashMap<UUID, Long> lastRun = new HashMap<UUID, Long>();
	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	public static Boolean isOnCooldwon(Player p) {

		if (p.hasPermission("wildrtp.cooldown.bypass"))
			return false;

		long time = System.currentTimeMillis();
		@NotNull
		UUID uuid = p.getUniqueId();
		Long last = lastRun.get(uuid);
		long cooldownTime = config.getInt("cooldown.time") * 1000;

		if (!(lastRun.containsKey(uuid))) {
			lastRun.put(uuid, time);
			return false;
		}

		// including

		System.out.println(getCustomCooldown(p));

		if (config.getBoolean("cooldown.custom") && last <= (time - getCustomCooldown(p))) {
			lastRun.replace(uuid, time);
			return false;
		}

		if (last <= (time - cooldownTime)) {
			lastRun.replace(uuid, time);
			return false;
		}

		// optimized ?

		/*
		 * if (last <= time - (config.getBoolean("cooldown.custom") ?
		 * getCustomCooldown(p) : cooldownTime)) { lastRun.replace(uuid, time); return
		 * false; }
		 */

		// WIP CUSTOM PERMISSION COOLDOWNS
		p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.cooldown").replaceAll("%cooldown%",
				String.valueOf(formatTime(cooldownTime + last - time))));

		return true;

	}

	// WIP scheduled for tomorrow (today)

	private static long getCustomCooldown(Player pl) {
		for (String permission : pl.getEffectivePermissions().stream().map(p -> p.getPermission())
				.toArray(String[]::new)) {
			if (permission.startsWith("wildrtp.cooldown.custom.")) {
				System.out.println("CooldownManager.getCustomCooldown()");
				String cooldownString = permission.replace("wildrtp.cooldown.custom", "");
				try {
					return Long.parseLong(cooldownString) * 1000;
				} catch (NumberFormatException ignored) {
				}
			}
		}
		System.out.println("player = 0");
		return 0;
	}

	private static String formatTime(long ms) {
		if (ms < 120000) {
			return (ms / 1000) + "s";
		}
		if (ms < 3600000) {
			return (ms / 60000) + "m";
		}

		if (ms >= 3600000) {
			return (ms / 3600000) + "h";
		}

		return null;

	}

}

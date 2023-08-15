package joni.utils;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
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

		if (MoveTimer.awaitRTP.contains(uuid)) {
			return false;
		}

		Long last = lastRun.get(uuid);
		long cooldownTime = config.getInt("cooldown.time") * 1000;
		Boolean custom = config.getBoolean("cooldown.custom");
		long customTime = getCustomCooldown(p, cooldownTime);

		if (!(lastRun.containsKey(uuid))) {
			lastRun.put(uuid, time);
			return false;
		}

		if (custom && last <= (time - customTime)) {
			lastRun.replace(uuid, time);
			return false;
		}

		if (last <= (time - cooldownTime)) {
			lastRun.replace(uuid, time);
			return false;
		}

		if (custom) {
			p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.cooldown").replaceAll("%cooldown%",
					String.valueOf(formatTime(customTime + last - time))));
			return true;
		}

		p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.cooldown").replaceAll("%cooldown%",
				String.valueOf(formatTime(cooldownTime + last - time))));

		return true;
	}

	private static long getCustomCooldown(Player p, Long normal) {
		ConfigurationSection cs = config.getConfigurationSection("custom");
		Set<String> keys = cs.getKeys(false);
		for (String key : keys) {
			if (p.hasPermission("wildrtp.cooldown.custom." + key)) {
				return config.getLong("custom." + key + ".cooldown") * 1000;
			}
		}
		return normal;
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

	public static void reload() {
		config = WildRTP.getPlugin().getConfig();
	}

}

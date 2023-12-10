package joni.wildrtp.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import joni.utils.ConfigLoader;
import joni.wildrtp.WildRTP;
import joni.wildrtp.api.GetLocation.SafeLocation;

public interface SendInfo {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	public static void sendEnd(Entity e, SafeLocation s) {

		if (!(e instanceof Player))
			return;

		Player p = (Player) e;

		if (config.getBoolean("messages.end"))
			p.sendMessage(preparedEndMessage(s, p));

		if (config.getBoolean("titles.end.enabled"))
			p.sendTitle(ConfigLoader.loadMessage("titles.end.title").replaceAll("%tries%", String.valueOf(s.tries)),
					ConfigLoader.loadMessage("titles.end.sub").replaceAll("%tries%", String.valueOf(s.tries)),
					config.getInt("titles.end.fadeIn") * 20, config.getInt("titles.end.stay") * 20,
					config.getInt("titles.end.fadeOut") * 20);

	}

	private static String preparedEndMessage(SafeLocation s, Player p) {
		String msg = ConfigLoader.loadMessageWithPrefix("chat.end");
		msg = msg.replaceAll("%tries%", String.valueOf(s.tries));
		msg = msg.replaceAll("%player%", String.valueOf(p.getName()));

		return msg;
	}

	private static String preparedStartMessage(Player p) {
		String msg = ConfigLoader.loadMessageWithPrefix("chat.start");
		msg = msg.replaceAll("%player%", String.valueOf(p.getName()));

		return msg;
	}

	public static void sendStart(Entity e) {
		if (!(e instanceof Player))
			return;

		Player p = (Player) e;

		if (config.getBoolean("messages.start"))
			p.sendMessage(preparedStartMessage(p));

		if (config.getBoolean("titles.start.enabled"))
			p.sendTitle(ConfigLoader.loadMessage("titles.start.title"), ConfigLoader.loadMessage("titles.start.sub"),
					config.getInt("titles.start.fadeIn") * 20, config.getInt("titles.start.stay") * 20,
					config.getInt("titles.start.fadeOut") * 20);

	}

}

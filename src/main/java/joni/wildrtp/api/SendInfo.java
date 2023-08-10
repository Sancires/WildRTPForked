package joni.wildrtp.api;

import java.time.Duration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import joni.utils.ConfigLoader;
import joni.wildrtp.WildRTP;
import joni.wildrtp.api.GetLocation.SafeLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public interface SendInfo {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	public static void sendEnd(Entity e, SafeLocation s) {

		if (!(e instanceof Player))
			return;

		Player p = (Player) e;

		if (config.getBoolean("messages.end"))
			p.sendMessage(preparedEndMessage(s, p));

		if (config.getBoolean("titles.end.enabled"))
			p.showTitle(Title.title(
					Component.text(ConfigLoader.loadMessage("titles.end.title").replaceAll("%tries%",
							String.valueOf(s.tries))),
					Component.text(
							ConfigLoader.loadMessage("titles.end.sub").replaceAll("%tries%", String.valueOf(s.tries))),
					Title.Times.times(Duration.ofSeconds(config.getInt("titles.end.fadeIn")),
							Duration.ofSeconds(config.getInt("titles.end.stay")),
							Duration.ofSeconds(config.getInt("titles.end.fadeOut")))));

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
			p.showTitle(Title.title(Component.text(ConfigLoader.loadMessage("titles.start.title")),
					Component.text(ConfigLoader.loadMessage("titles.start.sub")),
					Title.Times.times(Duration.ofSeconds(config.getInt("titles.start.fadeIn")),
							Duration.ofSeconds(config.getInt("titles.start.stay")),
							Duration.ofSeconds(config.getInt("titles.start.fadeOut")))));

	}

}

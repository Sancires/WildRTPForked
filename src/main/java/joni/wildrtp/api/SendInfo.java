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

	public static void sendFinished(Entity e, SafeLocation s) {

		if (!(e instanceof Player))
			return;

		Player p = (Player) e;

		p.sendMessage(preparedFinishMessage(s, p));

		if (config.getBoolean("titles.after.enabled"))
			p.showTitle(Title.title(Component.text(ConfigLoader.loadMessage("titles.end.title")),
					Component.text(ConfigLoader.loadMessage("titles.end.sub")),
					Title.Times.times(Duration.ofSeconds(config.getInt("titles.after.fadeIn")),
							Duration.ofSeconds(config.getInt("titles.after.stay")),
							Duration.ofSeconds(config.getInt("titles.after.fadeOut")))));

	}

	private static String preparedFinishMessage(SafeLocation s, Player p) {
		String msg = ConfigLoader.loadMessageWithPrefix("chat.teleported");
		msg = msg.replaceAll("%tries%", s.tries + "");
		msg = msg.replaceAll("%player%", p.getName() + "");

		return msg;
	}

}

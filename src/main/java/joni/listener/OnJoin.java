package joni.listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import joni.utils.ColorTranslator;
import joni.utils.PlayerTeleportManager;
import joni.wildrtp.WildRTP;

public class OnJoin implements Listener {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (config.getBoolean("auto.onfirstjoin") && !p.hasPlayedBefore()) {
			PlayerTeleportManager.teleport(p);
			return;
		}
		if (config.getBoolean("auto.onjoin")) {
			PlayerTeleportManager.teleport(p);
			return;
		}
		if (WildRTP.update && WildRTP.notify && p.isOp()) {
			Bukkit.getScheduler().runTaskLater(WildRTP.getPlugin(), new Runnable() {

				@Override
				public void run() {
					p.sendMessage(
							ColorTranslator.translateColor("[&2WildRTP&f] &6There is an update avaible for WildRTP!"));
					p.sendMessage(
							ColorTranslator.translateColor("[&2WildRTP&f] &6https://modrinth.com/plugin/wildrtp"));
				}
			}, 20);

		}
	}

	public static void reload() {
		config = WildRTP.getPlugin().getConfig();
	}

}

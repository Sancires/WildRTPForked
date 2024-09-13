package joni.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import joni.utils.PlayerTeleportManager;
import joni.wildrtp.WildRTP;

public class OnJoin implements Listener {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (config.getBoolean("auto.onfirstjoin") && !p.hasPlayedBefore()) {
			PlayerTeleportManager.teleport(p);
			return;
		}
		if (config.getBoolean("auto.onjoin")) {
			PlayerTeleportManager.teleport(p);
			return;
		}
	}

	public static void reload() {
		config = WildRTP.getPlugin().getConfig();
	}

}

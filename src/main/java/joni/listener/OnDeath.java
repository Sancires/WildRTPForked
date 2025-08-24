package joni.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import joni.utils.PlayerTeleportManager;
import joni.wildrtp.WildRTP;

public class OnDeath implements Listener {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (config.getBoolean("auto.ondeath") &&
				(p.getBedSpawnLocation()!= null))
			new Thread() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ignored) {
					}
					PlayerTeleportManager.teleport(p);
				}
			}.start();
	}

	public static void reload() {
		config = WildRTP.getPlugin().getConfig();
	}

}
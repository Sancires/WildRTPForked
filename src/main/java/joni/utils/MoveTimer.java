package joni.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import joni.wildrtp.WildRTP;
import joni.wildrtp.api.SendInfo;

public class MoveTimer {

	public static ArrayList<UUID> awaitRTP = new ArrayList<UUID>();
	public static ArrayList<UUID> isCancelled = new ArrayList<UUID>();

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	public static void wait(Player p) {
		new Thread() {
			public void run() {
				UUID u = p.getUniqueId();
				if (p.hasPermission("wildrtp.movetimer.bypass")) {
					remove(u);
					SendInfo.sendStart(p);
					PlayerTeleportManager.teleport(p);
					return;
				}
				if (awaitRTP.contains(u)) {
					p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.movetimer.already"));
					return;
				}
				awaitRTP.add(u);
				for (int i = config.getInt("movetimer.time"); i > 0; i--) {
					if (isCancelled.contains(u)) {
						p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.movetimer.cancelled"));
						remove(u);
						CooldownManager.getLastRun().remove(u);
						return;
					}
					p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.movetimer.remaining").replaceAll("%time%",
							String.valueOf(i)));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				remove(u);
				SendInfo.sendStart(p);
				PlayerTeleportManager.teleport(p);
			}
		}.start();
	}

	private static void remove(UUID u) {
		awaitRTP.remove(u);
		isCancelled.remove(u);
	}

}

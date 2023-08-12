package joni.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public interface MoveTimer {

	public static ArrayList<UUID> awaitRTP = new ArrayList<UUID>();
	public static ArrayList<UUID> isCancelled = new ArrayList<UUID>();

	public static void wait(Player p) {
		new Thread() {
			public void run() {
				UUID u = p.getUniqueId();
				if (awaitRTP.contains(u)) {
					p.sendMessage("already await tp");
					return;
				}
				awaitRTP.add(u);
				for (int i = 10; i > 0; i--) {
					if (isCancelled.contains(u)) {
						p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.movetimer.cancelled"));
						remove(u);
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
				// Do something WIP for release!!!
				p.sendMessage("done");
				remove(u);
			}
		}.start();
	}

	private static void remove(UUID u) {
		awaitRTP.remove(u);
		isCancelled.remove(u);
	}

}

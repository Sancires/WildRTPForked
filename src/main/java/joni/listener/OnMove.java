package joni.listener;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import joni.utils.MoveTimer;

public class OnMove implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		final Location to = e.getTo();
		final Location from = e.getFrom();

		if (!(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()))
			return;

		@NotNull
		UUID uuid = p.getUniqueId();
		if (MoveTimer.awaitRTP.contains(uuid) && !MoveTimer.isCancelled.contains(uuid)) {
			MoveTimer.isCancelled.add(uuid);
		}
	}

}

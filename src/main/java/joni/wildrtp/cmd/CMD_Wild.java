package joni.wildrtp.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import joni.wildrtp.api.RandomPoint.Algorithm;
import joni.wildrtp.api.TeleportToRandom;

public class CMD_Wild implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label,
			@NotNull String[] args) {
		Player p = (Player) s;
		TeleportToRandom.teleport(p.getWorld(), Algorithm.CIRCLE, Double.parseDouble(args[0]),
				Double.parseDouble(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), p);
		return false;
	}

}

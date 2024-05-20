package joni.wildrtp.cmd;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import joni.wildrtp.api.RandomPoint.Algorithm;
import joni.wildrtp.api.TeleportToRandom;

public class CMD_CustomWild implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (!s.hasPermission("wildrtp.custom"))
			return false;

		if (args.length != 6) {
			s.sendMessage("Use /customrtp <player> <world> <startRadius> <endRadius> <originX> <originZ>");
			return false;
		}

		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
			s.sendMessage("The player is not online!");
			return false;
		}

		World w = Bukkit.getWorld(args[1]);
		if (w == null) {
			s.sendMessage("The world does not exists!");
			return false;
		}

		TeleportToRandom.teleport(w, Algorithm.CIRCLE, Double.parseDouble(args[2]), Double.parseDouble(args[3]),
				Integer.parseInt(args[4]), Integer.parseInt(args[5]), p);

		return false;
	}

}

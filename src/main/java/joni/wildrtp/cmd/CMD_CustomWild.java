package joni.wildrtp.cmd;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import joni.utils.ConfigLoader;
import joni.utils.CooldownManager;
import joni.wildrtp.WildRTP;
import joni.wildrtp.api.RandomPoint.Algorithm;
import joni.wildrtp.api.TeleportToRandom;

public class CMD_CustomWild implements CommandExecutor {

	private static final String prefix = ConfigLoader.loadPrefix();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (!s.hasPermission("wildrtp.custom"))
			return false;

		if (!(args.length >= 6 && args.length <= 8)) {
			s.sendMessage(prefix + "Arguments:");
			s.sendMessage(prefix + "<required arguments>");
			s.sendMessage(prefix + "(optional arguments)");
			s.sendMessage(prefix
					+ "Use /customrtp <player / %player%> <world> <startRadius> <endRadius> <originX> <originZ> (cooldown true / false) (messages true / false)");
			return false;
		}

		Player p;
		if (args[0].equals("%player%") && s instanceof Player) {
			p = (Player) s;
		} else {
			p = Bukkit.getPlayer(args[0]);
		}

		if (p == null) {
			s.sendMessage(prefix + "The player is not online!");
			return false;
		}

		World w = Bukkit.getWorld(args[1]);
		if (w == null) {
			s.sendMessage(prefix + "The world does not exists!");
			return false;
		}

		if (args.length >= 7) {
			if (Boolean.parseBoolean(args[6])) {
				FileConfiguration config = WildRTP.getPlugin().getConfig();
				Boolean cooldown = false;
				if (config.getBoolean("cooldown.enabled"))
					cooldown = CooldownManager.isOnCooldwon(p);

				if (cooldown)
					return false;
			}

			if (args.length >= 8) {
				if (Boolean.parseBoolean(args[7])) {
					TeleportToRandom.teleportWithInfo(w, Algorithm.CIRCLE, Double.parseDouble(args[2]),
							Double.parseDouble(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), p);
					return false;
				}
			}
		}

		TeleportToRandom.teleport(w, Algorithm.CIRCLE, Double.parseDouble(args[2]), Double.parseDouble(args[3]),
				Integer.parseInt(args[4]), Integer.parseInt(args[5]), p);

		return false;
	}

}

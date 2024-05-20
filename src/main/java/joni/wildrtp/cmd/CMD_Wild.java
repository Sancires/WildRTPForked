package joni.wildrtp.cmd;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import joni.utils.ConfigLoader;
import joni.utils.CooldownManager;
import joni.utils.MoveTimer;
import joni.utils.PlayerTeleportManager;
import joni.wildrtp.WildRTP;
import joni.wildrtp.api.SendInfo;

public class CMD_Wild implements CommandExecutor {

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (args.length == 1) {
			String arg = args[0];
			switch (arg) {
			case "info":
				info(s);
				return false;
			case "reload":
				if (s.hasPermission("wildrtp.reload")) {
					s.sendMessage("config reloaded!");
					WildRTP.reload();
					return false;
				}
			}
		}

		if (!(s instanceof Player))
			return false;

		Player p = (Player) s;

		if (!(p.hasPermission("wildrtp.rtp")))
			return false;

		List<String> blacklist = config.getStringList("blacklist");
		List<String> whitelist = config.getStringList("whitelist");

		if (config.getBoolean("whitelist-enabled")) {
			if (whitelist == null) {
				p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.blacklisted"));
				return false;
			}
			if (whitelist.isEmpty()) {
				p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.blacklisted"));
				return false;
			}
			for (String ww : whitelist) {
				if (p.getWorld().getName().equals(ww)) {
					System.out.println(ww);
					System.out.println(whitelist);
					System.out.println(p.getWorld().getName());
				} else {
					p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.blacklisted"));
					return false;
				}
			}
		} else {
			if (!blacklist.isEmpty())
				for (String bw : blacklist) {
					if (p.getWorld().getName().equals(bw)) {
						p.sendMessage(ConfigLoader.loadMessageWithPrefix("chat.blacklisted"));
						return false;
					}
				}
		}

		Boolean cooldown = false;
		if (config.getBoolean("cooldown.enabled"))
			cooldown = CooldownManager.isOnCooldwon(p);

		if (cooldown)
			return false;

		if (config.getBoolean("movetimer.enabled")) {
			MoveTimer.wait(p);
			return false;
		}

		SendInfo.sendStart(p);

		PlayerTeleportManager.teleport(p);

		return false;
	}

	private void info(CommandSender s) {
		s.sendMessage(ConfigLoader.loadPrefix() + "by " + WildRTP.author + " | Version: " + WildRTP.ver);
	}

	public static void reload() {
		config = WildRTP.getPlugin().getConfig();
	}

}

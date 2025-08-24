package joni.wildrtp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import joni.listener.OnDeath;
import joni.listener.OnJoin;
import joni.listener.OnMove;
import joni.utils.CooldownManager;
import joni.utils.MessageFile;
import joni.utils.PlayerTeleportManager;
import joni.wildrtp.cmd.CMD_CustomWild;
import joni.wildrtp.cmd.CMD_Wild;

public class WildRTP extends JavaPlugin {

	public static String name = "WildRTP";
	public static String author = "Joni";
	public static String ver = "2.6.1";
	public static Boolean update = false;
	public static Boolean notify = true;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		MessageFile.createConfig();
		updateConfig();
		initEvents();
		initCommands();
		metrics();

	}
	public static Plugin getPlugin() {
		return Bukkit.getPluginManager().getPlugin("WildRTP");
	}

	public static Logger logger() {
		return getPlugin().getLogger();
	}

	public static void reload() {
		getPlugin().reloadConfig();
		MessageFile.createConfig();
		getPlugin().saveDefaultConfig();
		OnDeath.reload();
		OnJoin.reload();
		CooldownManager.reload();
		PlayerTeleportManager.reload();
		CMD_Wild.reload();
		logger().info("The config and messages have been reloaded!");
	}

	private void initEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		if (getConfig().getBoolean("movetimer.enabled"))
			pm.registerEvents(new OnMove(), this);
		pm.registerEvents(new OnJoin(), this);
		if (getConfig().getBoolean("auto.ondeath"))
			pm.registerEvents(new OnDeath(), this);
	}

	private void initCommands() {
		getCommand("wild").setExecutor(new CMD_Wild());
		getCommand("customrtp").setExecutor(new CMD_CustomWild());
	}

	public void metrics() {
		if (!getConfig().getBoolean("metrics"))
			return;
		int pluginId = 17799;
		new Metrics(this, pluginId);
	}

	private void updateConfig() {
		File fc = new File(getDataFolder(), "config.yml");
		FileConfiguration c = new YamlConfiguration();
		try {
			c.load(fc);
		} catch (IOException | InvalidConfigurationException e) {
			logger().warning("Error updating config.");
		}

		if (c.getInt("config-version") == 2) {
			return;
		}

		if (c.getInt("config-version") == 1) {
			c.set("config-version", 2);
			c.set("notify-updates-on-join", true);
			try {
				c.save(fc);
			} catch (IOException e) {
				logger().warning("Error updating config.");
			}
			reloadConfig();
			logger().info("Updated to the new config!");
			return;
		}

		if (c.getInt("config-version") != 1) {
			c.set("config-version", 1);
			c.set("whitelist-enabled", false);
			c.set("whitelist", "");

			List<String> wc = new ArrayList<String>(Arrays.asList("Whitelist",
					"allow teleportation only in whitelisted worlds", "overrides blacklist", "use like blacklist"));
			c.setComments("whitelist", wc);

			try {
				c.save(fc);
			} catch (IOException e) {
				logger().warning("Error updating config.");
			}
			reloadConfig();
			logger().info("Updated to the new config!");
		}
	}
}



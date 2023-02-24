package joni.wildtp.main;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import joni.org.bstats.bukkit.Metrics;
import joni.wildtp.MessageFile;
import joni.wildtp.cmd.RTP;

public class WildRTP extends JavaPlugin {

	public static String name = "WildRTP";
	public static String author = "Joni";
	public static String ver = "1.3.1";

	@Override
	public void onEnable() {
		Information(getServer());
		saveDefaultConfig();
		if (getConfig().getInt("config-version") < 3) {
			Bukkit.getLogger().warning("[WildRTP] Please update your config file to avoid issues!");
		}
		getCommand("wild").setExecutor(new RTP());
		initBStats();
		MessageFile.createConfig();
		initEvents();
	}

	public void Information(Server s) {
		System.out.println("WildRTP by " + author);
		System.out.println("Your are running on version " + ver);
		System.out.println("Dedected following version " + s.getVersion());
	}

	public static Plugin getPlugin() {
		return Bukkit.getPluginManager().getPlugin("WildRTP");
	}

	private void initBStats() {
		if (!(getConfig().getBoolean("bStats.enabled"))) {
			// Disable bStats
			return;
		}
		int pluginId = 17799;
		Metrics metrics = new Metrics(this, pluginId);
	}

	private void initEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new RTP(), this);
	}
}

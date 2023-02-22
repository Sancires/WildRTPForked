package joni.wildtp;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import joni.wildtp.main.WildRTP;

public class MessageFile {

	private static File customConfigFile;
	private static FileConfiguration customConfig;
	private static Plugin plugin = WildRTP.getPlugin();

	public static FileConfiguration getConfig() {
		return customConfig;
	}

	public static void createConfig() {
		customConfigFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}

		customConfig = new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}

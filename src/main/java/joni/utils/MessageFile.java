package joni.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import joni.wildrtp.WildRTP;

public class MessageFile {

	private static File customConfigFile;
	private static FileConfiguration customConfig;
	private static Plugin plugin = WildRTP.getPlugin();

	static FileConfiguration config = WildRTP.getPlugin().getConfig();

	public static FileConfiguration getConfig() {
		return customConfig;
	}

	public static void createConfig() {
		File langFolder = new File(plugin.getDataFolder(), "lang");
		if (!langFolder.exists()) {
			langFolder.mkdirs();
		}

		String[] languages = { "en_EN", "de_DE", "fr_FR" };

		for (String l : languages) {
			File file = new File(langFolder, l + ".yml");
			if (!file.exists()) {
				file.getParentFile().mkdir();
				plugin.saveResource("lang/" + l + ".yml", false);
			}
		}

		customConfigFile = new File(langFolder, config.getString("lang") + ".yml");

		customConfig = new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}

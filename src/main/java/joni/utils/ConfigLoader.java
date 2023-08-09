package joni.utils;

import joni.wildrtp.WildRTP;

public interface ConfigLoader {

	public static String loadMessage(String path) {
		String msg = ColorTranslator.translateColor(MessageFile.getConfig().getString(path));
		return msg;
	}

	public static String loadMessageWithPrefix(String path) {
		String prefix = ColorTranslator.translateColor(WildRTP.getPlugin().getConfig().getString("prefix"));
		return prefix + loadMessage(path);
	}

	public static String loadPrefix() {
		String prefix = ColorTranslator.translateColor(WildRTP.getPlugin().getConfig().getString("prefix"));
		return prefix;
	}

}

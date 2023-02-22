package joni.wildtp;

public interface MessageLoader extends ColorTranslator {

	public default String message(String conf) {
		final String msg = MessageFile.getConfig().getString(conf);
		return translateColor(msg);
	}

}

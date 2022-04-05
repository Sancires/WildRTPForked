package lasajoni.main;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import lasajoni.cmd.RTP;

public class Main extends JavaPlugin {

	public String name = "WildRTP";
	public String author = "Joni";
	public String ver = "1.1";

	@Override
	public void onEnable() {
		Information(getServer());
		getCommand("wild").setExecutor(new RTP());
	}

	public void Information(Server s) {
		System.out.println("WildRTP by " + author);
		System.out.println("Your are running on version " + ver);
		System.out.println("Dedected following version " + s.getVersion());
	}
}

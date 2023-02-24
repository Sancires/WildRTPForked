package joni.wildtp.cmd;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import joni.wildtp.MessageLoader;
import joni.wildtp.main.WildRTP;

public class RTP implements CommandExecutor, MessageLoader, Listener {

	HashMap<Player, Long> cooldown = new HashMap<Player, Long>();
	static HashMap<UUID, BukkitTask> taskmap = new HashMap<UUID, BukkitTask>();
	static HashMap<UUID, Boolean> pvp = new HashMap<UUID, Boolean>();
	FileConfiguration config = WildRTP.getPlugin().getConfig();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (s.hasPermission(config.getString("permission.reload")) && args.length == 1 && args[0].equals("reload")) {
			s.sendMessage(config.getString("plugin-settings.prefix") + message("config.reload"));
			WildRTP.getPlugin().reloadConfig();
			return false;
		}
		if (args.length == 1 && args[0].equals("info")) {
			s.sendMessage(
					config.getString("plugin-settings.prefix") + "by " + WildRTP.author + " | Version: " + WildRTP.ver);
			return false;
		}
		if (!(s instanceof Player)) {
			s.sendMessage(config.getString("plugin-settings.prefix") + message("player.only"));
			return false;
		}
		if (config.getString("permission.teleport").equals("NONE")) {
			Player p = (Player) s;
			cooldown(p, s);
			return false;
		}
		if (!(s.hasPermission(config.getString("permission.teleport")))) {
			s.sendMessage(config.getString("plugin-settings.prefix") + message("no.permission"));
			return false;
		} else {
			Player p = (Player) s;
			cooldown(p, s);
		}
		return false;
	}

	private void cooldown(Player p, CommandSender s) {
		if (pvp.get(p.getUniqueId()) != null && pvp.get(p.getUniqueId()) && config.getInt("teleport.pvp-timer") != 0) {
			s.sendMessage(config.getString("plugin-settings.prefix") + message("pvp.timer.already"));
			return;
		}
		if (cooldown.get(p) == null) {
			if (config.getInt("teleport.pvp-timer") != 0) {
				String msg = config.getString("plugin-settings.prefix") + message("pvp.timer.teleport")
						.replace("{timer}", "" + config.getInt("teleport.pvp-timer") / 20);
				s.sendMessage(msg);
			}
			pvp.put(p.getUniqueId(), true);
			BukkitTask task = Bukkit.getScheduler().runTaskLater(WildRTP.getPlugin(), new Runnable() {

				@Override
				public void run() {
					tp(p, s);
				}

			}, config.getInt("teleport.pvp-timer"));
			taskmap.put(p.getUniqueId(), task);
			return;
		}
		if (!(cooldown.get(p) < System.currentTimeMillis() - config.getLong("teleport.cooldown"))) {
			long cooldwon = (cooldown.get(p) + config.getLong("teleport.cooldown") - System.currentTimeMillis()) / 1000;
			String msg = message("cooldown").replace("{cooldown}", cooldwon + "");
			final String fmsg = msg;
			s.sendMessage(config.getString("plugin-settings.prefix") + fmsg);
			return;
		}
		if (config.getInt("teleport.pvp-timer") != 0) {
			String msg = config.getString("plugin-settings.prefix")
					+ message("pvp.timer.teleport").replace("{timer}", "" + config.getInt("teleport.pvp-timer") / 20);
			s.sendMessage(msg);
		}
		pvp.put(p.getUniqueId(), true);
		BukkitTask task = Bukkit.getScheduler().runTaskLater(WildRTP.getPlugin(), new Runnable() {

			@Override
			public void run() {
				tp(p, s);
			}

		}, config.getInt("teleport.pvp-timer"));
		taskmap.put(p.getUniqueId(), task);
	}

	private void tp(Player p, CommandSender s) {
		pvp.remove(p.getUniqueId());
		taskmap.remove(p.getUniqueId());
		cooldown.put(p, System.currentTimeMillis());
		cooldown.replace(p, System.currentTimeMillis());
		s.sendMessage(config.getString("plugin-settings.prefix") + message("teleport.soon"));
		if (p.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
			s.sendMessage(config.getString("plugin-settings.prefix") + message("temp.nether.disabled"));
			return;
		}
		if (p.getWorld().getName().equals(config.getString("disabled-worlds." + p.getWorld().getName()))) {
			p.sendMessage(config.getString("plugin-settings.prefix") + message("world.disabled"));
			return;
		}
		Location loc = loc(p.getWorld());
		tp(p.getPlayer(), p.getWorld(), loc);
	}

	private Location loc(World world) {
		Random rand = new Random();
		int x = config.getInt("teleport.max-x");
		int z = config.getInt("teleport.max-z");
		int minx = config.getInt("teleport.min-x");
		int minz = config.getInt("teleport.min-z");
		int xRange = x - minx;
		int zRange = z - minz;
		int xPositive = (rand.nextBoolean() ? -1 : 1);
		int zPositive = (rand.nextBoolean() ? -1 : 1);
		int randomx = (int) (Math.random() * xRange * xPositive + (minx * xPositive));
		int randomz = (int) (Math.random() * zRange * zPositive + (minz * zPositive));
		Location loc = world.getHighestBlockAt(randomx, randomz).getLocation();
		return loc;
	}

	private void tp(Player p, World world, Location loc) {
		int count = 1;
		int antiCrash = 0;
		boolean safe = false;
		while (!(safe)) {
			if (world.getEnvironment().equals(World.Environment.THE_END)
					&& loc.getBlock().getType().equals(Material.END_STONE)) {
				safe = true;
				break;
			}
			if (!(loc.getBlock().isLiquid()) && !(world.getEnvironment().equals(World.Environment.THE_END))) {
				safe = true;
				break;
			}
			loc = loc(p.getWorld());
			count++;
			antiCrash++;
			if (antiCrash >= 50) {
				error(world, p, loc);
				break;
			}
		}
		if (antiCrash >= 50) {
			return;
		}
		p.teleport(loc.add(0, 2, 0));
		String msg = message("location.found").replace("{count}", count + "");
		final String fmsg = config.getString("plugin-settings.prefix") + msg;
		p.sendMessage(fmsg);
		new BukkitRunnable() {
			@Override
			public void run() {
				p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 255, 5);
			}
		}.runTaskLater(WildRTP.getPlugin(), 5);

	}

	private void error(World w, Player p, Location loc) {
		p.sendMessage(config.getString("plugin-settings.prefix") + "An error occurred while executing the command!");
		Bukkit.getLogger().warning("WildRTP prevented your server from crashing!");
		Bukkit.getLogger().warning("Please report this to WildRTP!");
		Bukkit.getLogger().warning("Debug Information:");
		Bukkit.getLogger().warning("Server version: " + Bukkit.getBukkitVersion());
		Bukkit.getLogger().warning("World: " + w.toString());
		Bukkit.getLogger().warning("World enviroment: " + w.getEnvironment().toString());
		Bukkit.getLogger().warning("Player who executed the command: " + p.getName());
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (pvp.get(p.getUniqueId()) != null && pvp.get(p.getUniqueId())) {
			p.sendMessage(config.getString("plugin-settings.prefix") + message("pvp.timer.cancel"));
			BukkitTask t = taskmap.get(p.getUniqueId());
			t.cancel();
			pvp.remove(p.getUniqueId());
			taskmap.remove(p.getUniqueId());
		}
	}

}

package joni.wildtp.cmd;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import joni.wildtp.main.WildRTP;

public class RTP implements CommandExecutor {
	
	HashMap<Player, Long> cooldown = new HashMap<Player, Long>();
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (s.hasPermission(WildRTP.getPlugin().getConfig().getString("permission.reload")) && args.length == 1 && args[0].equals("reload")) {s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "The config has been reloaded!"); WildRTP.getPlugin().reloadConfig(); return false;}
		if (args.length == 1 && args[0].equals("info")) {s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "by " + WildRTP.author + " | Version: " + WildRTP.ver); return false;}
		if (!(s instanceof Player)) {s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "You must be a player"); return false;}
		if (WildRTP.getPlugin().getConfig().getString("permission.teleport").equals("NONE")) {
			Player p = (Player) s;
			cooldown(p, s);
			return false;
		}
		if (!(s.hasPermission(WildRTP.getPlugin().getConfig().getString("permission.teleport")))) {s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "You don't have the permission to execute this command!"); return false;}
		return false;
	}
	
	private void cooldown(Player p, CommandSender s) {
		if (cooldown.get(p) == null) {tp(p, s); return;}
		if (!(cooldown.get(p) < System.currentTimeMillis() -  WildRTP.getPlugin().getConfig().getLong("teleport.cooldown"))) {s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "You are on cooldown for " + (System.currentTimeMillis() - cooldown.get(p))/1000 + "s!"); return;}
		tp(p, s);
	}
	
	private void tp(Player p, CommandSender s) {
		cooldown.put(p, System.currentTimeMillis());
		cooldown.replace(p, System.currentTimeMillis());
		s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "You will be teleported soon!");
		if (p.getWorld().getEnvironment().equals(World.Environment.NETHER)) {s.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "You can't use this command in the nether!"); return;}
		if (p.getWorld().getName().equals(WildRTP.getPlugin().getConfig().getString("disabled-worlds." + p.getWorld().getName()))) {p.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "This world is disabled!"); return;}
		Location loc = loc(p.getWorld());
		tp(p.getPlayer(), p.getWorld(), loc);
	}

	private Location loc(World world) {
		Random rand = new Random();
		int x = WildRTP.getPlugin().getConfig().getInt("teleport.max-x");
		int z = WildRTP.getPlugin().getConfig().getInt("teleport.max-y");
		int randomx = (int) (Math.random() * x * (rand.nextBoolean() ? -1 : 1));
		int randomz = (int) (Math.random() * z * (rand.nextBoolean() ? -1 : 1));
		Location loc = world.getHighestBlockAt(randomx, randomz).getLocation();
		return loc;
	}

	private void tp(Player p, World world, Location loc) {
		int count = 1;
		int antiCrash = 0;
		boolean safe = false;
		while (!(safe)){
			if (world.getEnvironment().equals(World.Environment.THE_END) && loc.getBlock().getType().equals(Material.END_STONE)) {safe = true; break;}
			if (!(loc.getBlock().isLiquid()) && !(world.getEnvironment().equals(World.Environment.THE_END))) {safe = true; break;}
			loc = loc(p.getWorld());
			count++;
			antiCrash++;
			if (antiCrash >= 50) {error(world, p, loc); break;}
		}
		p.teleport(loc.add(0, 2, 0));
		p.sendMessage(WildRTP.getPlugin().getConfig().getString("plugin-settings.prefix") + "Location found after " + count + " tries");
		new BukkitRunnable() {	
			@Override
			public void run() {
				p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 255, 5);
			}
		}.runTaskLater(WildRTP.getPlugin(), 5);
		
	}
	
	private void error(World w, Player p, Location loc) {
		 System.out.println("WildRTP prevented your server from crashing!");
		 System.out.println("Please report this to WildRTP!");
		 System.out.println("Debug Information:");
		 System.out.println("Server version: " + Bukkit.getBukkitVersion());
		 System.out.println("World: " + w.toString());
		 System.out.println("World enviroment: " + w.getEnvironment().toString());
		 System.out.println("Player who executed the command: " + p.getName());
 	}

}

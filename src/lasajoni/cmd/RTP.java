package lasajoni.cmd;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (s instanceof Player) {
			s.sendMessage("Du wirst teleportiert!");
			Player p = (Player) s;
			Location loc = loc(p.getWorld());
			tp(p.getPlayer(), p.getWorld(), loc);
		}
		return false;
	}

	private Location loc(World world) {
		int x = 2500;
		int z = 2500;
		int randomx = (int) (Math.random() * x);
		int randomz = (int) (Math.random() * z);
		Location loc = world.getHighestBlockAt(randomx, randomz).getLocation();
		return loc;
	}

	private void tp(Player p, World world, Location loc) {
		int count = 1;
		while (loc.getBlock().isLiquid()) {
			loc = loc(p.getWorld());
			count++;
		}
		p.teleport(loc.add(0, 2, 0));
		p.sendMessage("Location found after " + count + " tries");
	}

}

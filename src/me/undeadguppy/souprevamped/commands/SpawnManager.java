package me.undeadguppy.souprevamped.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;

public class SpawnManager implements CommandExecutor, Listener {
	private Set<UUID> teleporting = new HashSet<>();

	public void sendToSpawn(final Player player) {
		int delay = 5;
		teleporting.add(player.getUniqueId());
		new BukkitRunnable() {
			Location spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();

			@Override
			public void run() {
				if (teleporting.contains(player.getUniqueId())) {
					teleporting.remove(player.getUniqueId());
					player.teleport(spawn);
					player.sendMessage(ChatColor.GREEN + "Teleported!");
					PlayerInventory inv = player.getInventory();
					inv.setHelmet(new ItemStack(Material.IRON_HELMET));
					inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					inv.setBoots(new ItemStack(Material.IRON_BOOTS));
					inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
					for (int i = 0; i <= inv.getSize(); i++) {
						inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
					}
					cancel();
					return;
				} else {
					player.sendMessage(ChatColor.RED + "Teleport cancelled!");
					cancel();
					return;
				}
			}
		}.runTaskLater(Main.getInstance(), 20 * delay);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			Player p = (Player) sender;
			if (teleporting.contains(p.getUniqueId())) {
				p.sendMessage(ChatColor.GREEN + "You're already teleporting to spawn!");
				return true;
			}
			p.sendMessage(ChatColor.GREEN + "Teleporting to spawn in 5 seconds!");
			sendToSpawn(p);
			return true;
		}
		return true;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (teleporting.contains(p.getUniqueId())) {
				teleporting.remove(p.getUniqueId());
				p.sendMessage(ChatColor.RED + "Teleport cancelled!");
				return;
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (teleporting.contains(e.getPlayer().getUniqueId())) {
			teleporting.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Location from = e.getFrom();
		Location to = e.getTo();
		if ((from.getBlockX() != to.getBlockX()) || (from.getBlockY() != to.getBlockY())
				|| (from.getBlockZ() != to.getBlockZ()) || (from.getWorld() != to.getWorld())) {
			if (teleporting.contains(e.getPlayer().getUniqueId())) {
				teleporting.remove(e.getPlayer().getUniqueId());
				e.getPlayer().sendMessage(ChatColor.RED + "Teleport cancelled!");
				return;
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			teleporting.remove(p.getUniqueId());
		}
	}

}

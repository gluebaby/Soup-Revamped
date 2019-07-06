package me.undeadguppy.souprevamped.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class RefillCMD implements CommandExecutor, Listener {

	private HashSet<UUID> souping = new HashSet<UUID>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("refill")) {
			if (souping.contains(p.getUniqueId())) {
				p.sendMessage(ChatColor.RED + "You are already refilling your soup!");
				return true;
			}
			EconomyResponse soup = Main.econ.withdrawPlayer(p, 20);
			if (soup.transactionSuccess()) {
				souping.add(p.getUniqueId());
				p.sendMessage(ChatColor.GREEN + "You have purchased a refill of soup for 20 credits!");
				p.sendMessage(ChatColor.GREEN + "You will be given your soup in 5 seconds...");
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 5));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 5));
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 5));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 200));
				p.setHealth(1D);
				new BukkitRunnable() {
					@Override
					public void run() {
						souping.remove(p.getUniqueId());
						for (PotionEffect effect : p.getActivePotionEffects()) {
							if (p.hasPotionEffect(effect.getType())) {
								p.removePotionEffect(effect.getType());
							}
						}
						p.setHealth(20D);
						for (int i = 0; i <= p.getInventory().getSize(); i++) {
							p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
						}
						p.sendMessage(ChatColor.GREEN + "You have recieved your soup!");

					}

				}.runTaskLater(Main.getInstance(), 5L * 20);
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You need 20 credits to purchase a refill of soup!");
				return true;
			}
		}

		return true;

	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (souping.contains(e.getPlayer().getUniqueId())) {
			souping.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (souping.contains(e.getEntity())) {
			souping.remove(e.getEntity().getUniqueId());
		}
	}

}

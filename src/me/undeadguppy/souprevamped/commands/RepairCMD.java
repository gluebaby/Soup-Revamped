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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class RepairCMD implements CommandExecutor, Listener {

	private HashSet<UUID> repair = new HashSet<UUID>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("repair")) {
			if (repair.contains(p.getUniqueId())) {
				p.sendMessage(ChatColor.RED + "You are already repairing!");
				return true;
			}
			EconomyResponse diamond = Main.econ.withdrawPlayer(p, 100);
			if (diamond.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased a repair upgrade for 100 credits!");
				p.sendMessage(ChatColor.GREEN + "You will be given your upgrade in 5 seconds...");
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 5));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 5));
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 5));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 200));
				p.setHealth(1D);
				new BukkitRunnable() {
					@Override
					public void run() {
						repair.remove(p.getUniqueId());
						for (PotionEffect effect : p.getActivePotionEffects()) {
							if (p.hasPotionEffect(effect.getType())) {
								p.removePotionEffect(effect.getType());
							}
						}
						p.setHealth(20D);
						PlayerInventory inv = p.getInventory();
						inv.setHelmet(new ItemStack(Material.IRON_HELMET));
						inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
						inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
						inv.setBoots(new ItemStack(Material.IRON_BOOTS));
						inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
						for (int i = 0; i <= p.getInventory().getSize(); i++) {
							p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
						}
						p.sendMessage(ChatColor.GREEN + "You have recieved your upgrade!");

					}

				}.runTaskLater(Main.getInstance(), 5L * 20);
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You must have 100 credits to purchase this upgrade!");
				return true;
			}

		}

		return true;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (repair.contains(e.getPlayer().getUniqueId())) {
			repair.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (repair.contains(e.getEntity())) {
			repair.remove(e.getEntity().getUniqueId());
		}
	}
}

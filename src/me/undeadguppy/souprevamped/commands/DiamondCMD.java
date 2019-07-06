package me.undeadguppy.souprevamped.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class DiamondCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("diamond")) {
			for (ItemStack item : p.getInventory().getArmorContents()) {
				if (item != null && item.getType().toString().startsWith("DIAMOND")) {
					p.sendMessage(ChatColor.RED + "You already have full diamond armor!");
					return true;
				}

			}
			EconomyResponse diamond = Main.econ.withdrawPlayer(p, 750);
			if (diamond.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased full diamond armor for 750 credits!");
				p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
				p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
				p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
				p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You must have 750 credits to purchase this upgrade!");
				return true;
			}

		}

		return true;
	}

}

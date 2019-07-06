package me.undeadguppy.souprevamped.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class KnockbackCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("knockback")) {
			PlayerInventory inv = p.getInventory();
			if (inv.getItemInHand() != null && inv.getItemInHand().getType() == Material.DIAMOND_SWORD) {
				ItemStack oldsword = inv.getItemInHand();
				if (oldsword.containsEnchantment(Enchantment.KNOCKBACK)) {
					if (oldsword.getEnchantmentLevel(Enchantment.KNOCKBACK) == 2) {
						p.sendMessage(ChatColor.RED + "You cannot upgrade your sword anymore!");
						return true;
					}
					EconomyResponse sharp2 = Main.econ.withdrawPlayer(p, 200);
					if (sharp2.transactionSuccess()) {
						oldsword.addEnchantment(Enchantment.KNOCKBACK, 2);
						p.sendMessage(ChatColor.GREEN + "You have upgraded your sword to knockback 2 for 200 credits!");
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "You must have 200 credits to purchase that upgrade!");
						return true;
					}

				} else {
					EconomyResponse knock1 = Main.econ.withdrawPlayer(p, 100);
					if (knock1.transactionSuccess()) {
						oldsword.addEnchantment(Enchantment.KNOCKBACK, 1);
						p.sendMessage(ChatColor.GREEN + "You have upgraded your sword to knockback 1 for 100 credits!");
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "You must have 100 credits to purchase that upgrade!");
						return true;
					}
				}
			} else {
				p.sendMessage(ChatColor.RED + "You must have a sword in your hand to use this command!");
				return true;
			}
		}
		return true;
	}

}

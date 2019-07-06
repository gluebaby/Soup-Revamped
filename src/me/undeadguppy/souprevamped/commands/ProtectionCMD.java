package me.undeadguppy.souprevamped.commands;

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

public class ProtectionCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("protection")) {
			PlayerInventory inv = p.getInventory();
			for (ItemStack item : inv.getArmorContents()) {
				if (item != null && item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
					// Has protection
					p.sendMessage(ChatColor.RED + "You cannot upgrade your protection anymore!");
					return true;
				}
			}
			EconomyResponse prot = Main.econ.withdrawPlayer(p, 100);
			if (prot.transactionSuccess()) {
				for (ItemStack armor : inv.getArmorContents()) {
					if (armor != null && !armor.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
						armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
				}
				p.sendMessage(ChatColor.GREEN + "You have purchased protection 1 for 100 credits!");

				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You need 100 credits to purchase that upgrade!");
				return true;
			}
		}
		return true;

	}
}

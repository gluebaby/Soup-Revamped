package me.undeadguppy.souprevamped.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.nametagedit.plugin.NametagEdit;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class SharpCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("sharp")) {
			PlayerInventory inv = p.getInventory();
			if (inv.getItemInHand() != null && inv.getItemInHand().getType() == Material.DIAMOND_SWORD) {
				ItemStack oldsword = inv.getItemInHand();
				if (oldsword.containsEnchantment(Enchantment.DAMAGE_ALL)) {
					int level = oldsword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
					switch (level) {
					case 1:
						EconomyResponse sharp2 = Main.econ.withdrawPlayer(p, 40);
						if (sharp2.transactionSuccess()) {
							NametagEdit.getApi().clearNametag(p);
							NametagEdit.getApi().setNametag(p, ChatColor.RED + "", null);
							NametagEdit.getApi().reloadNametag(p);
							oldsword.addEnchantment(Enchantment.DAMAGE_ALL, level + 1);
							p.sendMessage(
									ChatColor.GREEN + "You have upgraded your sword to sharpness 2 for 40 credits!");
							return true;
						} else {
							p.sendMessage(ChatColor.RED + "You must have 40 credits to purchase that upgrade!");
							return true;
						}
					case 2:
						EconomyResponse sharp3 = Main.econ.withdrawPlayer(p, 200);
						if (sharp3.transactionSuccess()) {
							NametagEdit.getApi().clearNametag(p);
							NametagEdit.getApi().setNametag(p, ChatColor.DARK_RED + "", null);
							NametagEdit.getApi().reloadNametag(p);
							oldsword.addEnchantment(Enchantment.DAMAGE_ALL, level + 1);
							p.sendMessage(
									ChatColor.GREEN + "You have upgraded your sword to sharpness 3 for 200 credits!");
							return true;
						} else {
							p.sendMessage(ChatColor.RED + "You must have 200 credits to purchase that upgrade!");
							return true;
						}
					default:
						p.sendMessage(ChatColor.RED + "You cannot upgrade your sword anymore!");
						return true;
					}

				} else {
					EconomyResponse sharp1 = Main.econ.withdrawPlayer(p, 5);
					if (sharp1.transactionSuccess()) {
						NametagEdit.getApi().clearNametag(p);
						NametagEdit.getApi().setNametag(p, ChatColor.GOLD + "", null);
						NametagEdit.getApi().reloadNametag(p);
						oldsword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
						p.sendMessage(ChatColor.GREEN + "You have upgraded your sword to sharpness 1 for 5 credits!");
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "You must have 5 credits to purchase that upgrade!");
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

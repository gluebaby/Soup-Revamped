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

public class SoupCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("soup")) {
			EconomyResponse soup = Main.econ.withdrawPlayer(p, 10);
			if (soup.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased a hotbar of soup for 10 credits!");

				for (int i = 0; i <= 9; i++) {
					p.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
				}

				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You need 10 credits to purchase a hotbar of soup!");
				return true;
			}
		}

		return true;

	}

}

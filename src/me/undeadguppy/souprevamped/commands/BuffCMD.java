package me.undeadguppy.souprevamped.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;

public class BuffCMD implements CommandExecutor {

	public BuffCMD() {
		Main.getInstance().getCommand("poison").setExecutor(this);
		Main.getInstance().getCommand("speed").setExecutor(this);
		Main.getInstance().getCommand("regen").setExecutor(this);
		Main.getInstance().getCommand("strength").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("poison")) {
			EconomyResponse poison = Main.econ.withdrawPlayer(Bukkit.getServer().getOfflinePlayer(p.getUniqueId()),
					150);
			if (poison.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased a poison potion!");
				Potion pot1 = new Potion(PotionType.POISON);
				pot1.setLevel(1);
				pot1.setSplash(true);
				p.getInventory().addItem(pot1.toItemStack(1));
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You do not have enough credits for this buff!");
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("speed")) {
			if (p.getActivePotionEffects().contains(PotionEffectType.SPEED)) {
				p.sendMessage(ChatColor.RED + "You already have speed!");
				return true;
			}
			EconomyResponse speed = Main.econ.withdrawPlayer(Bukkit.getServer().getOfflinePlayer(p.getUniqueId()), 60);
			if (speed.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased speed for 60 seconds!");
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 0));
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You do not have enough credits for this buff!");
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("regen")) {
			if (p.getActivePotionEffects().contains(PotionEffectType.REGENERATION)) {
				p.sendMessage(ChatColor.RED + "You already have regeneration!");
				return true;
			}
			EconomyResponse regen = Main.econ.withdrawPlayer(Bukkit.getServer().getOfflinePlayer(p.getUniqueId()), 60);
			if (regen.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased regen for 60 seconds!");
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 0));
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You do not have enough credits for this buff!");
				return true;
			}
		} else if (cmd.getName().equalsIgnoreCase("strength")) {
			if (p.getActivePotionEffects().contains(PotionEffectType.INCREASE_DAMAGE)) {
				p.sendMessage(ChatColor.RED + "You already have strength!");
				return true;
			}
			EconomyResponse regen = Main.econ.withdrawPlayer(Bukkit.getServer().getOfflinePlayer(p.getUniqueId()), 80);
			if (regen.transactionSuccess()) {
				p.sendMessage(ChatColor.GREEN + "You have purchased strength for 60 seconds!");
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60 * 20, 0));
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You do not have enough credits for this buff!");
				return true;
			}
		}
		return true;
	}

}

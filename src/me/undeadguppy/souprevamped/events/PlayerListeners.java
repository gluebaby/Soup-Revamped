package me.undeadguppy.souprevamped.events;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.nametagedit.plugin.NametagEdit;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;

public class PlayerListeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		NametagEdit.getApi().clearNametag(e.getPlayer());
		NametagEdit.getApi().reloadNametag(e.getPlayer());
		PlayerInventory inv = e.getPlayer().getInventory();
		inv.setHelmet(new ItemStack(Material.IRON_HELMET));
		inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		inv.setBoots(new ItemStack(Material.IRON_BOOTS));
		inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
		for (int i = 0; i <= inv.getSize(); i++) {
			inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.getPlayer().getInventory().clear();
		e.getPlayer().getActivePotionEffects().clear();
		
	}

	@EventHandler
	public void soup(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
				event.setCancelled(true);
				Damageable damageable = (Damageable) player;
				if (damageable.getHealth() != 20) {
					double newHealth = damageable.getHealth() + 7;
					if (newHealth > 20D) {
						player.setHealth(20D);
					} else {
						player.setHealth(newHealth);
					}
					player.getInventory().setItem(player.getInventory().getHeldItemSlot(),
							new ItemStack(Material.BOWL));
				} else if (player.getFoodLevel() != 20) {
					int newHunger = player.getFoodLevel() + 7;
					if (newHunger > 20) {
						player.setFoodLevel(20);
					} else {
						player.setFoodLevel(newHunger);
					}
					player.getInventory().setItem(player.getInventory().getHeldItemSlot(),
							new ItemStack(Material.BOWL));
				}

			}
		}
	}

	@EventHandler
	public void pickup(PlayerPickupItemEvent e) {
		ItemStack item = e.getItem().getItemStack();
		if (!(item.getType() == Material.MUSHROOM_SOUP)) {
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (!(e.getItemDrop().getItemStack().getType() == Material.BOWL)) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.GREEN + "Hey, you might want to hold onto that!");
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getSlotType() == SlotType.ARMOR) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
				e.getPlayer().getInventory().clear();
				e.getPlayer().getInventory().setArmorContents(null);
				PlayerInventory inv = e.getPlayer().getInventory();
				inv.setHelmet(new ItemStack(Material.IRON_HELMET));
				inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				inv.setBoots(new ItemStack(Material.IRON_BOOTS));
				inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
				for (int i = 0; i <= inv.getSize(); i++) {
					inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
				}
			}

		}.runTaskLater(Main.getInstance(), 20L);
	}

}

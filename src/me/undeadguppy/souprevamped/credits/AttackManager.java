package me.undeadguppy.souprevamped.credits;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.undeadguppy.souprevamped.Main;
import net.md_5.bungee.api.ChatColor;

public class AttackManager implements Listener {

	// Stores, in order, a damaged player, and damagers and the amount of damage
	// they've done.
	private HashMap<UUID, HashMap<UUID, Double>> attackMap = new HashMap<UUID, HashMap<UUID, Double>>();

	private boolean hasBeenDamaged(Player p) {
		return attackMap.containsKey(p.getUniqueId());
	}

	private void setDamaged(Player damaged, Player attacker, double amount) {
		if (!hasBeenDamaged(damaged)) {
			attackMap.put(damaged.getUniqueId(), new HashMap<UUID, Double>());
			attackMap.get(damaged.getUniqueId()).put(attacker.getUniqueId(), amount);
		}

	}

	private void addDamage(Player damaged, Player attacker, double amount) {
		if (hasBeenDamaged(damaged)) {
			HashMap<UUID, Double> dmgMap = attackMap.get(damaged.getUniqueId());
			if (dmgMap.containsKey(attacker.getUniqueId())) {
				// Player has been attacked by this person
				double oldamount = dmgMap.get(attacker.getUniqueId());
				dmgMap.put(attacker.getUniqueId(), oldamount + amount);
			} else {
				dmgMap.put(attacker.getUniqueId(), amount);
			}

			attackMap.put(damaged.getUniqueId(), dmgMap);
		}
	}

	private boolean hasDamagedPlayer(Player damaged, Player attacker) {
		return hasBeenDamaged(damaged) && attackMap.get(damaged.getUniqueId()).containsKey(attacker.getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				double damage = event.getFinalDamage();
				if (hasBeenDamaged(damaged)) {
					addDamage(damaged, damager, damage);
				} else {
					setDamaged(damaged, damager, damage);
				}
			}
		}
	}

	public void onQuit(PlayerQuitEvent e) {
		if (hasBeenDamaged(e.getPlayer())) {
			attackMap.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player dead = e.getEntity();
		if (dead.getKiller() != null && dead.getKiller() instanceof Player) {
			if (hasBeenDamaged(dead)) {
				for (UUID id : attackMap.get(dead.getUniqueId()).keySet()) {
					Player damager = Bukkit.getPlayer(id);
					if (hasDamagedPlayer(dead, damager) && damager != null) {
						double amount = attackMap.get(dead.getUniqueId()).get(damager.getUniqueId());
						damager.sendMessage(ChatColor.GREEN + "You have recieved " + amount
								+ " credits for your damage dealt to " + dead.getName() + "!");
						Main.econ.depositPlayer(damager, amount);
						dead.spigot().respawn();
						new BukkitRunnable() {

							@Override
							public void run() {
								dead.teleport(dead.getWorld().getSpawnLocation());
								dead.getInventory().clear();
								dead.getInventory().setArmorContents(null);
								PlayerInventory inv = dead.getInventory();
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
			}
		}
	}
}

package me.undeadguppy.souprevamped;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.undeadguppy.souprevamped.commands.BuffCMD;
import me.undeadguppy.souprevamped.commands.DiamondCMD;
import me.undeadguppy.souprevamped.commands.KnockbackCMD;
import me.undeadguppy.souprevamped.commands.ProtectionCMD;
import me.undeadguppy.souprevamped.commands.RefillCMD;
import me.undeadguppy.souprevamped.commands.RepairCMD;
import me.undeadguppy.souprevamped.commands.SharpCMD;
import me.undeadguppy.souprevamped.commands.SoupCMD;
import me.undeadguppy.souprevamped.commands.SpawnManager;
import me.undeadguppy.souprevamped.credits.AttackManager;
import me.undeadguppy.souprevamped.events.PlayerListeners;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static Economy econ = null;
	private static Main inst;
	private RefillCMD rcmd;
	private RepairCMD repaircmd;
	private SpawnManager spawn;

	public static Main getInstance() {
		return inst;
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public void onEnable() {
		inst = this;
		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		repaircmd = new RepairCMD();
		rcmd = new RefillCMD();
		spawn = new SpawnManager();
		getCommand("sharp").setExecutor(new SharpCMD());
		getCommand("diamond").setExecutor(new DiamondCMD());
		getCommand("refill").setExecutor(rcmd);
		getCommand("repair").setExecutor(repaircmd);
		getCommand("soup").setExecutor(new SoupCMD());
		getCommand("knockback").setExecutor(new KnockbackCMD());
		getCommand("protection").setExecutor(new ProtectionCMD());
		new BuffCMD();
		getCommand("spawn").setExecutor(spawn);
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new AttackManager(), this);
		pm.registerEvents(spawn, this);
		pm.registerEvents(rcmd, this);
		pm.registerEvents(repaircmd, this);
		pm.registerEvents(new PlayerListeners(), this);
	}

}

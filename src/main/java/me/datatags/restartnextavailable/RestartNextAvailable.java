package me.datatags.restartnextavailable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class RestartNextAvailable extends JavaPlugin implements Listener {
	public boolean enabled = false;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (enabled) {
			enabled = false;
			sender.sendMessage(ChatColor.YELLOW + "Restart cancelled.");
			return true;
		}

		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			enabled = true;
			sender.sendMessage(ChatColor.YELLOW + "Server will restart when everyone leaves.");
			return true;
		}

		sender.sendMessage(ChatColor.GREEN + "Nobody is online, restarting now!");
		getServer().spigot().restart();
		return true;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (!enabled) return;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!Bukkit.getOnlinePlayers().isEmpty()) return;
				getLogger().info("Everyone has left, restarting now!");
				getServer().spigot().restart();
			}
		}.runTask(this);
	}
}

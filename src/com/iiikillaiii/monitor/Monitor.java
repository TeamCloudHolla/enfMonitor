package com.iiikillaiii.monitor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Monitor extends JavaPlugin
{
    private static Monitor instance;
    private Runtime runtime;
    private int delay;
    private static long uptime;
    
    static {
        Monitor.uptime = 0L;
    }
    
    public Monitor() {
        this.delay = 10;
    }
    
    public void onEnable() {
        (Monitor.instance = this).saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
        this.delay = this.getConfig().getInt("Delay");
        Monitor.uptime = System.currentTimeMillis();
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new TPS(), 100L, 1L);
        new BukkitRunnable() {
            public void run() {
                Monitor.access$0(Monitor.this, Runtime.getRuntime());
                Monitor.sendMessage(TPS.getTPS(), Monitor.this.runtime.freeMemory(), Monitor.this.runtime.totalMemory());
            }
        }.runTaskTimer((Plugin)this, 1200L * this.delay, 1200L * this.delay);
    }
    
    public static Monitor getInstance() {
        return Monitor.instance;
    }
    
    public static void sendMessage(double tps, final long ram_usage, final long ram_max) {
        if (!Bukkit.getOnlinePlayers().isEmpty() && Bukkit.getOnlinePlayers().size() >= 1) {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Monitor");
            out.writeUTF("A8sT7TJN6H8cc3a428gkYkMEt");
            tps *= 100.0;
            tps = Math.round(tps);
            tps /= 100.0;
            out.writeDouble(tps);
            out.writeLong(ram_usage);
            out.writeLong(ram_max);
            out.writeLong(Monitor.uptime);
            out.writeInt(Bukkit.getOnlinePlayers().size());
            out.writeInt(Bukkit.getMaxPlayers());
            final Player player = (Player)Iterables.getFirst((Iterable)Bukkit.getOnlinePlayers(), (Object)null);
            player.sendPluginMessage((Plugin)getInstance(), "BungeeCord", out.toByteArray());
        }
    }
    
    static /* synthetic */ void access$0(final Monitor monitor, final Runtime runtime) {
        monitor.runtime = runtime;
    }
}

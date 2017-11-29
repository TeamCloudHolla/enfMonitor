package com.iiikillaiii.monitor.bungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import org.bukkit.craftbukkit.Main;

import com.iiikillaiii.monitor.bungee.libs.YamlConfig;

public class BungeeMonitor extends Plugin implements Listener
{
    private static BungeeMonitor instance;
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    @EventHandler
    public void onPluginMessage(final PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("BungeeCord") && e.getSender() instanceof Server) {
            final DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            final Server server = (Server)e.getSender();
            try {
                final String subchannel = in.readUTF();
                if (subchannel.equals("Monitor")) {
                    final String code = in.readUTF();
                    if (code.equalsIgnoreCase("A8sT7TJN6H8cc3a428gkYkMEt")) {
                        final double tps = in.readDouble();
                        long ram_usage = in.readLong();
                        ram_usage /= 1048576L;
                        long ram_max = in.readLong();
                        ram_max /= 1048576L;
                        ram_usage = ram_max - ram_usage;
                        final long uptime = in.readLong();
                        final long time = System.currentTimeMillis() - uptime;
                        final int online = in.readInt();
                        final int max_on = in.readInt();
                        final ServerInfo si = server.getInfo();
                        for (final ProxiedPlayer pl : BungeeCord.getInstance().getPlayers()) {
                            if (pl.hasPermission("enf.staff")) {
                                pl.sendMessage((BaseComponent)new TextComponent("§8§l\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"));
                                pl.sendMessage((BaseComponent)new TextComponent("§7[§cEnforcedServers§7] §6Monitor Report: §7§o(" + si.getName().toUpperCase() + ")"));
                                pl.sendMessage((BaseComponent)new TextComponent(""));
                                pl.sendMessage((BaseComponent)new TextComponent("§7TPS: §a" + tps + "§7, Memory:§a " + ram_usage + "/" + ram_max));
                                pl.sendMessage((BaseComponent)new TextComponent(""));
                                pl.sendMessage((BaseComponent)new TextComponent("§7Uptime: §a" + getTimeleft(time)));
                                pl.sendMessage((BaseComponent)new TextComponent("§7Players Online: §a" + online + "/" + max_on));
                                pl.sendMessage((BaseComponent)new TextComponent("§8§l\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"));
                            }
                        }
                    }
                }
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public static String getTimeleft(final long timeleft) {
        final int days = (int)TimeUnit.MILLISECONDS.toDays(timeleft);
        final int hours = (int)(TimeUnit.MILLISECONDS.toHours(timeleft) - TimeUnit.DAYS.toHours(days));
        final int minute = (int)(TimeUnit.MILLISECONDS.toMinutes(timeleft) - (TimeUnit.DAYS.toMinutes(days) + TimeUnit.HOURS.toMinutes(hours)));
        final int second = (int)(TimeUnit.MILLISECONDS.toSeconds(timeleft) - (TimeUnit.DAYS.toSeconds(days) + TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minute)));
        String remaining = "";
        if (days > 0) {
            if (days == 1) {
                remaining = String.valueOf(remaining) + days + " Day ";
            }
            else {
                remaining = String.valueOf(remaining) + days + " Days ";
            }
        }
        if (hours > 0) {
            if (hours == 1) {
                remaining = String.valueOf(remaining) + hours + " Hour ";
            }
            else {
                remaining = String.valueOf(remaining) + hours + " Hours ";
            }
        }
        if (minute > 0) {
            if (minute == 1) {
                remaining = String.valueOf(remaining) + minute + " Minute ";
            }
            else {
                remaining = String.valueOf(remaining) + minute + " Minutes ";
            }
        }
        if (second > 0) {
            if (second == 1) {
                remaining = String.valueOf(remaining) + second + " Second ";
            }
            else {
                remaining = String.valueOf(remaining) + second + " Seconds ";
            }
        }
        if (!remaining.isEmpty()) {
            remaining = remaining.substring(0, remaining.length() - 1);
        }
        return remaining;
    }
}

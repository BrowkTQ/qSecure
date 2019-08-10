package me.Browk.qSecure;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.dv8tion.jda.core.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;

import java.awt.*;

public class ServerCommandListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        if(!(block.getState() instanceof CommandBlock)) {
            return;
        }
        CommandBlock cb = (CommandBlock)block.getState();
        if(getWorldGuard() == null) {
            sendDiscordEmbed(block.getLocation(), cb.getCommand(), e.getPlayer(), "*nu* ");
            e.setCancelled(true);
            return;
        }
        if(!getWorldGuard().getRegionManager(block.getWorld()).hasRegion("commandblocks")) {
            e.setCancelled(true);
            sendDiscordEmbed(block.getLocation(), cb.getCommand(), e.getPlayer(), "*nu* ");
            return;
        }
        if(!getWorldGuard().getRegionManager(block.getWorld()).getRegion("commandblocks").contains(block.getX(), block.getY(), block.getZ())) {
            e.setCancelled(true);
            sendDiscordEmbed(block.getLocation(), cb.getCommand(), e.getPlayer(), "*nu* ");
            return;
        }
        sendDiscordEmbed(block.getLocation(), cb.getCommand(), e.getPlayer(), "");
    }

    @EventHandler
    public void redstoneChanges(BlockRedstoneEvent e){
        Block block = e.getBlock();
        if(getWorldGuard() == null) {
            block.setType(Material.AIR);
            return;
        }
        if(e.getOldCurrent() == 0 && e.getNewCurrent() > 0){
            if (block != null){
                BlockState state = block.getState();
                if(state != null){
                    if (state instanceof CommandBlock){
                        CommandBlock cb = (CommandBlock)state;
                        Location loc = block.getLocation();
                        if(!getWorldGuard().getRegionManager(block.getWorld()).hasRegion("commandblocks")) {
                            block.setType(Material.AIR);
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setAuthor(Main.jda.getSelfUser().getName(), null, Main.jda.getSelfUser().getAvatarUrl() + "?size=256");
                            embed.setTitle("Un command_block NU a fost executat!");
                            embed.addField("Comanda executata:", "`" + cb.getCommand() + "`", false);
                            embed.addField("Sectiune:", "`" + Main.server + "`", true);
                            embed.addField("Locatia:","`" + loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "`", true);
                            embed.setFooter("play.minecraft-romania.ro", Main.jda.getSelfUser().getAvatarUrl());
                            embed.setColor(Color.RED);
                            Main.jda.getTextChannelById(494255063750148111L).sendMessage(embed.build()).queue();
                            return;
                        }
                        if (!getWorldGuard().getRegionManager(loc.getWorld()).getRegion("commandblocks").contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) || (cb.getCommand().startsWith("op")) || (cb.getCommand().startsWith("pp")) || (cb.getCommand().startsWith("powerfulpemrs")) || (cb.getCommand().startsWith("pop")) || cb.getCommand().startsWith("minecraft:op")) {
                            block.setType(Material.AIR);
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setAuthor(Main.jda.getSelfUser().getName(), null, Main.jda.getSelfUser().getAvatarUrl() + "?size=256");
                            embed.setTitle("Un command_block NU a fost executat!");
                            embed.addField("Comanda executata:", "`" + cb.getCommand() + "`", false);
                            embed.addField("Sectiune:", "`" + Main.server + "`", true);
                            embed.addField("Locatia:","`" + loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "`", true);
                            embed.setFooter("play.minecraft-romania.ro", Main.jda.getSelfUser().getAvatarUrl());
                            embed.setColor(Color.RED);
                            Main.jda.getTextChannelById(494255063750148111L).sendMessage(embed.build()).queue();
                            return;
                        }
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setAuthor(Main.jda.getSelfUser().getName(), null, Main.jda.getSelfUser().getAvatarUrl() + "?size=256");
                        embed.setTitle("Un command_block a fost executat!");
                        embed.addField("Comanda executata:", "`" + cb.getCommand() + "`", false);
                        embed.addField("Sectiune:", "`" + Main.server + "`", true);
                        embed.addField("Locatia:","`" + loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "`", true);
                        embed.setFooter("play.minecraft-romania.ro", Main.jda.getSelfUser().getAvatarUrl());
                        embed.setColor(Color.GREEN);
                        Main.jda.getTextChannelById(494255063750148111L).sendMessage(embed.build()).queue();
                    }
                }
            }
        }
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    private void sendDiscordEmbed(Location loc, String command, Player p, String danu) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(Main.jda.getSelfUser().getName(), null, Main.jda.getSelfUser().getAvatarUrl() + "?size=256");
        embed.setTitle("Un command_block "+ danu +"a fost plasat!");
        embed.addField("De catre:", "`" + p.getName() + "`", true);
        embed.addField("IP jucator:", "`" + p.getAddress().getHostString() + "`", true);
        embed.addField("Are OP:", "`" + (p.isOp() ? "Da" : "Nu") + "`", true);
        embed.addField("Sectiune:", "`" + Main.server + "`", true);
        embed.addField("Locatia:","`" + loc.getWorld().getName() + ", " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "`", true);
        embed.setFooter("play.minecraft-romania.ro", Main.jda.getSelfUser().getAvatarUrl());
        if(danu.equalsIgnoreCase("")) {
            embed.setColor(Color.GREEN);
        } else {
            embed.setColor(Color.RED);
        }
        Main.jda.getTextChannelById(494255063750148111L).sendMessage(embed.build()).queue();
    }

}

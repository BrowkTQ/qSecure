package me.Browk.qSecure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            try {
                PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.UPDATE1);
                preparedStatement.setString(1, "neconectat");
                preparedStatement.setString(2, p.getName());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if(Main.discordID.containsValue(Main.sesiune.get(p))) {
            for(String msgid : Main.discordID.keySet()) {
                if(Main.discordID.get(msgid).equalsIgnoreCase(Main.sesiune.get(p))) {
                    try {
                        Main.jda.getTextChannelById(494255505192255501L).deleteMessageById(msgid).queue();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.UPDATE1);
                        preparedStatement.setString(1, "neconectat");
                        preparedStatement.setString(2, p.getName());
                        preparedStatement.execute();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    sendDiscordEmbedE(Main.sesiune.get(p), p.getName());
                }
            }
        }
        Main.sesiune.remove(p);
        Main.discordID.remove(p);
        return;
    }

    private void sendDiscordEmbedE(String id, String name) {
        User user = Main.jda.getUserById(id);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256");
        embed.setTitle("Nu a fost conectat contul `" + name + "`!");
        embed.setDescription("» Nu a fost confirmata conectarea pe server a contului " + name + "!\n» Motiv: *Jucatorul s-a deconectat inainte de confirmare!*");
        embed.setFooter("play.minecraft-romania.ro | Browk_", Main.jda.getSelfUser().getAvatarUrl());
        embed.setColor(Color.RED);
        Main.jda.getGuildById(369226969583321088L).getTextChannelById(494255505192255501L).sendMessage(embed.build()).queue();
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(p)) {
            return;
        }
        if(Main.discordID.containsValue(Main.sesiune.get(p))) {
            for(String msgid : Main.discordID.keySet()) {
                if(Main.discordID.get(msgid).equalsIgnoreCase(Main.sesiune.get(p))) {
                    try {
                        Main.jda.getTextChannelById(494255505192255501L).deleteMessageById(msgid).queue();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    sendDiscordEmbedE(Main.sesiune.get(p), p.getName());
                }
            }
        }
        Main.sesiune.remove(p);
        Main.discordID.remove(p);
        return;
    }

}

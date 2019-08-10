package me.Browk.qSecure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiscordReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getTextChannel().getIdLong() != 494255505192255501L) {
            return;
        }
        if(event.getUser().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
            return;
        }
        if(!Main.discordID.containsKey(event.getMessageId())) {
            event.getReaction().removeReaction(event.getUser()).queue();
            return;
        }
        if(!Main.sesiune.containsValue(Main.discordID.get(event.getMessageId()))) {
            event.getReaction().removeReaction(event.getUser()).queue();
            return;
        }
        if(!event.getUser().getId().equalsIgnoreCase(Main.discordID.get(event.getMessageId()))) {
            event.getReaction().removeReaction(event.getUser()).queue();
            return;
        }
        Main.jda.getTextChannelById(494255505192255501L).deleteMessageById(event.getMessageId()).queue();
        for(Player p : Main.sesiune.keySet()) {
            if(Main.sesiune.get(p).equalsIgnoreCase(event.getUser().getId())) {
                if (event.getReactionEmote().getName().equalsIgnoreCase("⛔")) {
                    sendDiscordEmbedE(Main.sesiune.get(p), p.getName(), p.getAddress().getHostString());
                    Main.sesiune.remove(p);
                    Main.discordID.remove(event.getMessageId());
                    try {
                        PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.UPDATE1);
                        preparedStatement.setString(1, "neconectat");
                        preparedStatement.setString(2, p.getName());
                        preparedStatement.execute();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instace, new Runnable() {
                        public void run() {
                            p.kickPlayer("§ePlay-Secure §8- §cConexiunea a fost refuzata!");
                        }
                    }, 1L);
                    return;
                } else if (event.getReactionEmote().getName().equalsIgnoreCase("✅")) {
                    sendDiscordEmbedC(Main.sesiune.get(p), p.getName(), p.getAddress().getHostString());
                    Main.discordID.remove(event.getMessageId());
                    Main.sesiune.replace(p, "conectat");
                    sendToBungeeCord(p, p.getName());
                    try {
                        PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.UPDATE1);
                        preparedStatement.setString(1, "conectat");
                        preparedStatement.setString(2, p.getName());
                        preparedStatement.execute();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    p.sendMessage("§ePlay-Secure §8- §aConexiunea a fost confirmata!");
                    return;
                } else {
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }
        }
    }

    private void sendDiscordEmbedC(String id, String name, String ip) {
        User user = Main.jda.getUserById(id);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256");
        embed.setTitle("Ai confirmat contul `" + name + "`!");
        embed.setDescription("» <@" + id + "> a confirmat conectarea pe server a contului " + name + "!");
        embed.addField("IP jucator:", ip, true);
        embed.addField("Sectiune:", Main.server, true);
        embed.setFooter("play.minecraft-romania.ro | Browk_", Main.jda.getSelfUser().getAvatarUrl());
        embed.setColor(Color.GREEN);
        Main.jda.getGuildById(369226969583321088L).getTextChannelById(494255505192255501L).sendMessage(embed.build()).queue();
    }

    public void sendToBungeeCord(Player p, String sub) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("qSecure");
            out.writeUTF(p.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());
    }

    private void sendDiscordEmbedE(String id, String name, String ip) {
        User user = Main.jda.getUserById(id);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256");
        embed.setTitle("Ai respins conectarea contului `" + name + "`!");
        embed.setDescription("» <@" + id + "> a respins conectarea pe server a contului " + name + "!");
        embed.addField("IP jucator:", ip, true);
        embed.addField("Sectiune:", Main.server, true);
        embed.setFooter("play.minecraft-romania.ro | Browk_", Main.jda.getSelfUser().getAvatarUrl());
        embed.setColor(Color.RED);
        Main.jda.getGuildById(369226969583321088L).getTextChannelById(494255505192255501L).sendMessage(embed.build()).queue();
    }

}

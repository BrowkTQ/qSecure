package me.Browk.qSecure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscordMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getTextChannel().getId().equalsIgnoreCase("494255505192255501")) {
            return;
        }
        if(event.getMessage().getAuthor() == event.getJDA().getSelfUser()) {
            return;
        }
        event.getMessage().delete().queue();
        String[] code = event.getMessage().getContentRaw().split(" ");
        if(!event.getMessage().getContentRaw().startsWith("!confirm ") || (code.length != 2) || (code[1].length() != 10)) {
            return;
        }
        try {
            PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.SELECT3);
            preparedStatement.setString(1, code[1]);
            ResultSet localResultSet = preparedStatement.executeQuery();
            if(localResultSet.next()) {
                Player player = Bukkit.getServer().getPlayer(localResultSet.getString(1));
                if(player == null || !player.isOnline()) {
                    return;
                }
                PreparedStatement preparedStatement2 = Main.mysql.getConnection().prepareStatement(Main.mysql.UPDATE3);
                preparedStatement2.setString(1, "neconectat");
                preparedStatement2.setString(2, "");
                preparedStatement2.setString(3, event.getAuthor().getId());
                preparedStatement2.setString(4, localResultSet.getString(1));
                preparedStatement2.execute();
                preparedStatement2.close();
                sendDiscordEmbedC(event.getAuthor().getId(), localResultSet.getString(1));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instace, new Runnable() {
                    public void run() {
                        player.kickPlayer("§ePlay-Secure §8- §cTrebuie sa te reconectezi!");
                    }
                }, 1L);
            }
            localResultSet.close();
            preparedStatement.close();
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void sendDiscordEmbedE(String id) {
        User user = Main.jda.getUserById(id);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256");
        embed.setTitle("Eroare!");
        embed.setDescription("» Pentru a confirma contul de discord, te rugam sa parcurgi urmatorii pasi:\n① Foloseste in joc comanda **/confirmarediscord**.\n② Introdu pe acest channel pin-ul primit: **!confirm <pin>**");
        embed.setFooter("play.minecraft-romania.ro | Browk_", Main.jda.getSelfUser().getAvatarUrl());
        embed.setColor(Color.RED);
        Main.jda.getGuildById(369226969583321088L).getTextChannelById(494255505192255501L).sendMessage(embed.build()).queue();
    }

    private void sendDiscordEmbedC(String id, String name) {
        User user = Main.jda.getUserById(id);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256");
        embed.setTitle("Ai confirmat contul `" + name + "`!");
        embed.setDescription("» De acum, va trebui sa confirmi pe Discord conectarea pe server!");
        embed.setFooter("play.minecraft-romania.ro | Browk_", Main.jda.getSelfUser().getAvatarUrl());
        embed.setColor(Color.BLUE);
        Main.jda.getGuildById(369226969583321088L).getTextChannelById(494255505192255501L).sendMessage(embed.build()).queue();
    }

}

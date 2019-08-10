package me.Browk.qSecure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        try {
            PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.SELECT1);
            preparedStatement.setString(1, p.getName());
            ResultSet localResultSet = preparedStatement.executeQuery();
            if(localResultSet.next()) {
                if(localResultSet.getString("statut_sesiune").equalsIgnoreCase("conectat")) {
                    Main.sesiune.put(p, "conectat");
                    return;
                } else if(localResultSet.getString("statut_sesiune").equalsIgnoreCase("neconectat")) {
                    String id = localResultSet.getString("discordID");
                    Main.sesiune.put(p, id);
                    String msgid = sendDiscordEmbedC(id, p.getName());
                    Main.discordID.put(msgid, id);
                    Main.jda.getTextChannelById(494255505192255501L).addReactionById(msgid, "✅").queue();
                    Main.jda.getTextChannelById(494255505192255501L).addReactionById(msgid, "⛔").queue();
                    p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
                } else {
                    try {
                        PreparedStatement preparedStatement2 = Main.mysql.getConnection().prepareStatement(Main.mysql.UPDATE1);
                        preparedStatement2.setString(1, "neconectat");
                        preparedStatement2.setString(2, p.getName());
                        preparedStatement2.execute();
                        preparedStatement2.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    String id = localResultSet.getString("discordID");
                    Main.sesiune.put(p, id);
                    String msgid = sendDiscordEmbedC(id, p.getName());
                    Main.discordID.put(msgid, id);
                    Main.jda.getTextChannelById(494255505192255501L).addReactionById(msgid, "✅").queue();
                    Main.jda.getTextChannelById(494255505192255501L).addReactionById(msgid, "⛔").queue();
                    p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
                }
            } else {
                if(!p.hasPermission("qsecure.use") && !p.isOp()) {
                    localResultSet.close();
                    preparedStatement.close();
                    return;
                }
                Main.sesiune.put(p, "conectat");
                localResultSet.close();
                preparedStatement.close();
                return;
            }
            localResultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String sendDiscordEmbedC(String id, String name) {
        User user = Main.jda.getUserById(id);
        EmbedBuilder embed = new EmbedBuilder();
        String userName = user.getName();
        String userAvatar = user.getAvatarUrl() + "?size=256";
        embed.setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256");
        embed.setTitle("S-a cerut confirmarea unei conexiuni!");
        embed.addField("Discord User:", "<@" + id + ">", true);
        embed.addField("Minecraft User:", name, true);
        embed.setFooter("play.minecraft-romania.ro", Main.jda.getSelfUser().getAvatarUrl());
        embed.setColor(Color.BLUE);
        String msgid = Main.jda.getGuildById(369226969583321088L).getTextChannelById(494255505192255501L).sendMessage(embed.build()).complete().getId();
        return msgid;
    }

}

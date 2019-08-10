package me.Browk.qSecure;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfirmDiscCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command command, String string, String[] args) {
        if(!(s instanceof Player) || !(s.hasPermission("qsecure.use"))) {
            s.sendMessage("§ePlay-Secure §8- §fAceasta este o facilitate disponibila doar pentru operatori!");
            return true;
        }
        try {
            PreparedStatement preparedStatement = Main.mysql.getConnection().prepareStatement(Main.mysql.SELECT1);
            preparedStatement.setString(1, s.getName());
            ResultSet localResultSet = preparedStatement.executeQuery();
            if(localResultSet.next()) {
                if(localResultSet.getString(3).equalsIgnoreCase("")) {
                    s.sendMessage("§ePlay-Secure §8- §fAi confirmat deja contul de discord!");
                    return true;
                } else {
                    String pin = RandomStringUtils.randomAlphanumeric(10);
                    try {
                        PreparedStatement preparedStatement2 = Main.mysql.getConnection().prepareStatement(Main.mysql.INSERT);
                        preparedStatement2.setString(1, s.getName());
                        preparedStatement2.setString(2, "neconectat");
                        preparedStatement2.setString(3, pin);
                        preparedStatement2.execute();
                        preparedStatement2.close();
                        deleteDupa(pin);
                    } catch (SQLException ex) {
                        s.sendMessage("§ePlay-Secure §8- §cA avut loc o eroare in timpul conectarii la baza de date!");
                        ex.printStackTrace();
                        return true;
                    }
                    s.sendMessage("§ePlay-Secure §8- §fTe rugam sa introduci codul de confirmare: §a" + pin + "\n§ePlay-Secure §8- §fCodul se introduce pe conferinta Staff, pe channel-ul §a#confirm-account§f!");
                    return true;
                }
            }
            localResultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            s.sendMessage("§ePlay-Secure §8- §cA avut loc o eroare in timpul conectarii la baza de date!");
            ex.printStackTrace();
            return true;
        }
        String pin = RandomStringUtils.randomAlphanumeric(10);
        try {
            PreparedStatement preparedStatement2 = Main.mysql.getConnection().prepareStatement(Main.mysql.INSERT);
            preparedStatement2.setString(1, s.getName());
            preparedStatement2.setString(2, "neconectat");
            preparedStatement2.setString(3, pin);
            preparedStatement2.execute();
            preparedStatement2.close();
        } catch (SQLException ex) {
            s.sendMessage("§ePlay-Secure §8- §cA avut loc o eroare in timpul conectarii la baza de date!");
            ex.printStackTrace();
            return true;
        }
        s.sendMessage("§ePlay-Secure §8- §fTe rugam sa introduci codul de confirmare: §a" + pin + "\n§ePlay-Secure §8- §fCodul se introduce pe conferinta Staff, pe channel-ul §a#confirm-account§f!");
        return true;
    }

    private void deleteDupa(String pin) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instace, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement preparedStatement3 = Main.mysql.getConnection().prepareStatement(Main.mysql.SELECT3);
                    preparedStatement3.setString(1, pin);
                    ResultSet localResultSet = preparedStatement3.executeQuery();
                    if(localResultSet.next()) {
                        PreparedStatement preparedStatement4 = Main.mysql.getConnection().prepareStatement(Main.mysql.DELETE2);
                        preparedStatement4.setString(1, pin);
                        preparedStatement4.execute();
                        preparedStatement4.close();
                    }
                    preparedStatement3.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }, 120L*20L);
    }

}

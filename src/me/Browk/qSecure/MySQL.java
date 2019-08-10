package me.Browk.qSecure;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private Connection connection;
    private String url;
    private String username;
    private String password;
    private String TABLE;
    public String INSERT;
    public String UPDATE1;
    public String UPDATE2;
    public String UPDATE3;
    public String SELECT1;
    public String SELECT2;
    public String SELECT3;
    public String DELETE;
    public String DELETE2;

    public MySQL(final String tableprefix, final String host, final String port, final String database, final String username, final String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
        this.TABLE = "CREATE TABLE IF NOT EXISTS " + tableprefix + "_sesiune (nume VARCHAR(255), statut_sesiune VARCHAR(255), pin VARCHAR(255), discordID VARCHAR(255))";
        this.INSERT = "INSERT INTO " + tableprefix + "_sesiune (nume, statut_sesiune, pin) VALUES(?, ?, ?)";
        this.UPDATE1 = "UPDATE " + tableprefix + "_sesiune SET statut_sesiune=? WHERE nume=?";
        this.UPDATE2 = "UPDATE " + tableprefix + "_sesiune SET pin=?, discordID=? WHERE nume=?";
        this.UPDATE3 = "UPDATE " + tableprefix + "_sesiune SET statut_sesiune=?, pin=?, discordID=? WHERE nume=?";
        this.SELECT1 = "SELECT * FROM " + tableprefix + "_sesiune WHERE nume=?";
        this.SELECT2 = "SELECT * FROM " + tableprefix + "_sesiune WHERE discordID=?";
        this.SELECT3 = "SELECT nume FROM " + tableprefix + "_sesiune WHERE pin=?";
        this.DELETE = "DELETE FROM " + tableprefix + "_sesiune WHERE nume=?";
        this.DELETE2 = "DELETE FROM " + tableprefix + "_sesiune WHERE nume=?";
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(this.url, this.username, this.password);
    }

    public Connection getConnection() throws SQLException {
        if (this.connection == null || !this.connection.isValid(5)) {
            this.connect();
        }
        return this.connection;
    }

    public void setupTable() {
        try {
            this.getConnection().createStatement().executeUpdate(this.TABLE);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Nu s-a putut realiza conexiunea la baza de date! Se va reincerca realizarea conexiunii in 30 de secunde!");
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instace, (Runnable)new Runnable() {
                @Override
                public void run() {
                    setupTable();
                }
            }, 20L, 300L);
            return;
        }
        Bukkit.getLogger().info("Baza de date a fost incarcata cu succes!");
    }
}

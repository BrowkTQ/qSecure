package me.Browk.qSecure;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigHandler {

    static ConfigHandler instance = new ConfigHandler();
    FileConfiguration config;
    File cfile;

    public static ConfigHandler getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        config = p.getConfig();
        cfile = new File(p.getDataFolder(), "config.yml");

        config.options().header("qSecure by Browk_.\n");
        config.addDefault("mysql.host", "0.0.0.0");
        config.addDefault("mysql.port", "3306");
        config.addDefault("mysql.database", "numeBazaDeDate");
        config.addDefault("mysql.user", "userBazaDeDate");
        config.addDefault("mysql.password", "parolaUser");
        config.addDefault("nume server", "numeSectiune");

        config.options().copyDefaults(true);
        saveConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "Nu a fost salvat fisierul pentru configuratii!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }
}
package me.Browk.qSecure;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Main extends JavaPlugin {

    ConfigHandler settings = ConfigHandler.getInstance();
    public static JDA jda;
    public static MySQL mysql;
    public static HashMap<Player, String> sesiune = new HashMap<>();
    public static HashMap<String, String> discordID = new HashMap<>();
    public static Main instace;
    public static String server;

    public void onEnable() {
        instace = this;
        settings.setup(this);
        loadDatabase();
        startMessage();
        getCommand("confirmarediscord").setExecutor(new ConfirmDiscCommand());
        loadEvents();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        loadDiscordBot();
    }

    private void startMessage() {
        ConsoleCommandSender c = getServer().getConsoleSender();
        c.sendMessage(ChatColor.YELLOW + "");
        c.sendMessage(ChatColor.YELLOW + "         _____");
        c.sendMessage(ChatColor.YELLOW + "        /  ___|");
        c.sendMessage(ChatColor.YELLOW + "   __ _ \\ `--.   ___   ___  _   _  _ __  ___");
        c.sendMessage(ChatColor.YELLOW + "  / _` | `--. \\ / _ \\ / __|| | | || '__|/ _ \\");
        c.sendMessage(ChatColor.YELLOW + " | (_| |/\\__/ /|  __/| (__ | |_| || |  |  __/");
        c.sendMessage(ChatColor.YELLOW + "  \\__, |\\____/  \\___| \\___| \\__,_||_|   \\___|");
        c.sendMessage(ChatColor.YELLOW + "     | |");
        c.sendMessage(ChatColor.YELLOW + "     |_|");
        c.sendMessage(ChatColor.YELLOW + "");
        server = settings.getConfig().getString("nume server");
    }

    private void loadDatabase() {
        ConsoleCommandSender c = getServer().getConsoleSender();if (this.mysql != null) {
            this.mysql = null;
        }
        System.out.println("Se realizeaza conexiunea la baza de date!");
        (this.mysql = new MySQL("qSecure", settings.getConfig().getString("mysql.host"), settings.getConfig().getString("mysql.port"), settings.getConfig().getString("mysql.database"), settings.getConfig().getString("mysql.user"), settings.getConfig().getString("mysql.password"))).setupTable();
    }

    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new OtherListeners(), this);
        getServer().getPluginManager().registerEvents(new PreCommandListener(), this);
        getServer().getPluginManager().registerEvents(new ServerCommandListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
    }

    private void loadListeners(JDABuilder builder) {
        builder.addEventListener(new DiscordMessageListener());
        builder.addEventListener(new DiscordReactionListener());
    }

    private void loadDiscordBot() {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("NDk1Njk1ODgxOTcxMTcxMzM4.DqDULg.mMhpDx1jT04Cit84tc9BwUSBpfM");
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setGame(Game.playing("play.mc-ro.ro"));
        loadListeners(builder);
        try {
            jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bukkit.getLogger().info("Bot-ul a fost conectat cu succes!");
    }

    public void onDisable() {
        sesiune.clear();
        discordID.clear();
    }

}

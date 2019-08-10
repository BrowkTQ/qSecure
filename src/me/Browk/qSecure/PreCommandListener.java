package me.Browk.qSecure;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreCommandListener implements Listener {

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if(!Main.sesiune.containsKey(e.getPlayer())) {
            return;
        }
        if(!Main.sesiune.get(p).equalsIgnoreCase("conectat")) {
            p.sendMessage("§ePlay-Secure §8- §cTrebuie sa confirmi pe Discord pentru a finaliza conexiunea!");
            e.setCancelled(true);
        }
        return;
    }

}
